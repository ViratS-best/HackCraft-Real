package com.example.mixin.client;

import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import com.example.handler.DamageIndicator;

/**
 * Client-side: Track damage dealt when attacking entities
 */
@Mixin(LivingEntity.class)
public class DamageTrackerMixin {
	
	@Inject(at = @At("HEAD"), method = "hurt")
	private void trackDamage(CallbackInfo info) {
		LivingEntity self = (LivingEntity) (Object) this;
		// Record the damage being dealt
		if (self instanceof LivingEntity && self.getHealth() < self.getMaxHealth()) {
			float damageTaken = self.getMaxHealth() - self.getHealth();
			DamageIndicator.recordDamage(damageTaken);
		}
	}
}
