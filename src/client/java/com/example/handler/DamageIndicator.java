package com.example.handler;

import net.minecraft.network.chat.Component;
import net.minecraft.ChatFormatting;
import net.minecraft.world.entity.player.Player;

/**
 * Tracks and displays recent damage dealt to targets
 */
public class DamageIndicator {
	private static float lastDamageDealt = 0;
	private static long lastDamageTime = 0;
	private static final long DAMAGE_DISPLAY_DURATION = 3000; // 3 seconds

	public static void recordDamage(float damage) {
		lastDamageDealt = damage;
		lastDamageTime = System.currentTimeMillis();
	}

	public static void tick(Player player) {
		long now = System.currentTimeMillis();
		
		if (lastDamageDealt > 0 && now - lastDamageTime < DAMAGE_DISPLAY_DURATION) {
			// Display damage as floating text in actionbar
			String damageStr = String.format("%.1f", lastDamageDealt);
			
			Component damageMsg = Component.literal("⚔ Damage: ")
				.withStyle(ChatFormatting.RED)
				.append(Component.literal(damageStr).withStyle(ChatFormatting.YELLOW))
				.append(Component.literal(" ❤").withStyle(ChatFormatting.DARK_RED));
			
			player.displayClientMessage(damageMsg, true);
		} else if (now - lastDamageTime >= DAMAGE_DISPLAY_DURATION) {
			lastDamageDealt = 0;
		}
	}

	public static float getLastDamage() {
		return lastDamageDealt;
	}
}
