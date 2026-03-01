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

@Mixin(ThrownEnderpearl.class)
public abstract class EnderPearlSwapMixin {

        @Inject(method = "onHit", at = @At("HEAD"), cancellable = true)
        private void soulSwap_onHit(HitResult hitResult, CallbackInfo ci) {
                if (hitResult.getType() != HitResult.Type.ENTITY) {
                        return;
                }

                ThrownEnderpearl self = (ThrownEnderpearl) (Object) this;

                if (self.level().isClientSide()) {
                        return;
                }

                EntityHitResult entityHit = (EntityHitResult) hitResult;
                Entity target = entityHit.getEntity();
                Entity owner = self.getOwner();

                if (!(owner instanceof ServerPlayer player))
                        return;
                if (!(target instanceof LivingEntity livingTarget))
                        return;

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

                player.teleportTo(targetX, targetY, targetZ);
                player.setYRot(playerYaw);
                player.setXRot(playerPitch);

                target.teleportTo(playerX, playerY, playerZ);
                target.setYRot(targetYaw);
                target.setXRot(targetPitch);

                ServerLevel serverLevel = (ServerLevel) self.level();

                serverLevel.sendParticles(ParticleTypes.PORTAL,
                                playerX, playerY + 1.0, playerZ,
                                40, 0.5, 1.0, 0.5, 0.2);
                serverLevel.sendParticles(ParticleTypes.PORTAL,
                                targetX, targetY + 1.0, targetZ,
                                40, 0.5, 1.0, 0.5, 0.2);

                serverLevel.sendParticles(ParticleTypes.SOUL_FIRE_FLAME,
                                playerX, playerY + 0.5, playerZ,
                                20, 0.3, 0.5, 0.3, 0.05);
                serverLevel.sendParticles(ParticleTypes.SOUL_FIRE_FLAME,
                                targetX, targetY + 0.5, targetZ,
                                20, 0.3, 0.5, 0.3, 0.05);

                serverLevel.playSound(null, playerX, playerY, playerZ,
                                SoundEvents.ENDERMAN_TELEPORT, SoundSource.PLAYERS, 1.0F, 1.2F);
                serverLevel.playSound(null, targetX, targetY, targetZ,
                                SoundEvents.ENDERMAN_TELEPORT, SoundSource.PLAYERS, 1.0F, 0.8F);

                if (livingTarget instanceof Monster) {
                        livingTarget.hurt(player.damageSources().magic(), 4.0F);

                        livingTarget.addEffect(new MobEffectInstance(
                                        MobEffects.NAUSEA, 40, 0, false, true, true));

                        SoulSwapMod.LOGGER.debug("[Soul Swap] Hostile swap! {} took confusion damage",
                                        livingTarget.getName().getString());
                }

                if (livingTarget instanceof Animal) {
                        player.addEffect(new MobEffectInstance(
                                        MobEffects.INVISIBILITY, 60, 0, false, true, true));

                        serverLevel.sendParticles(ParticleTypes.ENCHANT,
                                        targetX, targetY + 1.0, targetZ,
                                        30, 0.5, 1.0, 0.5, 0.5);

                        SoulSwapMod.LOGGER.debug("[Soul Swap] Passive swap! {} granted invisibility",
                                        player.getName().getString());
                }

                self.discard();
                ci.cancel();
        }
}
