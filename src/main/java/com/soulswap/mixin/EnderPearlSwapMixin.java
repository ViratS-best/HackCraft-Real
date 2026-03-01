package com.soulswap.mixin;

import com.soulswap.SoulSwapMod;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.projectile.throwableitemprojectile.ThrownEnderpearl;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Soul Swap Mixin — intercepts Ender Pearl collisions.
 *
 * Feature 1: Throwing an Ender Pearl at a mob SWAPS your position with
 * that mob instead of teleporting you to the impact point.
 *
 * Feature 2: If the target is a hostile mob (Monster), they take 2 hearts
 * (4 HP) of "confusion" magic damage + brief Nausea effect.
 *
 * Feature 3: If the target is a passive animal (Animal), the player gains
 * a 3-second Invisibility buff to escape combat.
 */
@Mixin(ThrownEnderpearl.class)
public abstract class EnderPearlSwapMixin {

        @Inject(method = "onHit", at = @At("HEAD"), cancellable = true)
        private void soulSwap_onHit(HitResult hitResult, CallbackInfo ci) {
                // Only process entity hits — block hits use vanilla behavior
                if (hitResult.getType() != HitResult.Type.ENTITY) {
                        return;
                }

                ThrownEnderpearl self = (ThrownEnderpearl) (Object) this;

                // Server-side only
                if (self.level().isClientSide()) {
                        return;
                }

                EntityHitResult entityHit = (EntityHitResult) hitResult;
                Entity target = entityHit.getEntity();
                Entity owner = self.getOwner();

                // Must be a player hitting a living entity
                if (!(owner instanceof ServerPlayer player))
                        return;
                if (!(target instanceof LivingEntity livingTarget))
                        return;

                // ═══════════════════════════════════════════════
                // FEATURE 1: Position Swap
                // ═══════════════════════════════════════════════
                double playerX = player.getX();
                double playerY = player.getY();
                double playerZ = player.getZ();
                float playerYaw = player.getYRot();
                float playerPitch = player.getXRot();

                double targetX = target.getX();
                double targetY = target.getY();
                double targetZ = target.getZ();
                float targetYaw = target.getYRot();
                float targetPitch = target.getXRot();

                // Teleport player to mob's position
                player.teleportTo(targetX, targetY, targetZ);
                player.setYRot(playerYaw); // Keep original look direction
                player.setXRot(playerPitch);

                // Teleport mob to player's old position
                target.teleportTo(playerX, playerY, playerZ);
                target.setYRot(targetYaw);
                target.setXRot(targetPitch);

                ServerLevel serverLevel = (ServerLevel) self.level();

                // Visual effects — portal particles at BOTH locations
                serverLevel.sendParticles(ParticleTypes.PORTAL,
                                playerX, playerY + 1.0, playerZ,
                                40, 0.5, 1.0, 0.5, 0.2);
                serverLevel.sendParticles(ParticleTypes.PORTAL,
                                targetX, targetY + 1.0, targetZ,
                                40, 0.5, 1.0, 0.5, 0.2);

                // Soul fire flames for the "soul swap" theme
                serverLevel.sendParticles(ParticleTypes.SOUL_FIRE_FLAME,
                                playerX, playerY + 0.5, playerZ,
                                20, 0.3, 0.5, 0.3, 0.05);
                serverLevel.sendParticles(ParticleTypes.SOUL_FIRE_FLAME,
                                targetX, targetY + 0.5, targetZ,
                                20, 0.3, 0.5, 0.3, 0.05);

                // Sound effects — enderman teleport at both locations
                serverLevel.playSound(null, playerX, playerY, playerZ,
                                SoundEvents.ENDERMAN_TELEPORT, SoundSource.PLAYERS, 1.0F, 1.2F);
                serverLevel.playSound(null, targetX, targetY, targetZ,
                                SoundEvents.ENDERMAN_TELEPORT, SoundSource.PLAYERS, 1.0F, 0.8F);

                // ═══════════════════════════════════════════════
                // FEATURE 2: Hostile Mob → Confusion Damage
                // ═══════════════════════════════════════════════
                if (livingTarget instanceof Monster) {
                        // 2 hearts = 4 HP of magic ("confusion") damage
                        livingTarget.hurt(player.damageSources().magic(), 4.0F);

                        // Brief Nausea to sell the "confusion" flavor (2 seconds = 40 ticks)
                        livingTarget.addEffect(new MobEffectInstance(
                                        MobEffects.NAUSEA, 40, 0, false, true, true));

                        SoulSwapMod.LOGGER.debug("[Soul Swap] Hostile swap! {} took confusion damage",
                                        livingTarget.getName().getString());
                }

                // ═══════════════════════════════════════════════
                // FEATURE 3: Passive Animal → Player Invisibility
                // ═══════════════════════════════════════════════
                if (livingTarget instanceof Animal) {
                        // 3 seconds = 60 ticks of Invisibility
                        player.addEffect(new MobEffectInstance(
                                        MobEffects.INVISIBILITY, 60, 0, false, true, true));

                        // Extra flair — a shimmer particle burst on the player
                        serverLevel.sendParticles(ParticleTypes.ENCHANT,
                                        targetX, targetY + 1.0, targetZ,
                                        30, 0.5, 1.0, 0.5, 0.5);

                        SoulSwapMod.LOGGER.debug("[Soul Swap] Passive swap! {} granted invisibility",
                                        player.getName().getString());
                }

                // Discard the pearl and cancel vanilla teleport behavior
                self.discard();
                ci.cancel();
        }
}
