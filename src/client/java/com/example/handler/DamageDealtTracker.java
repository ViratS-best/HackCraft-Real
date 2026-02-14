package com.example.handler;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.network.chat.Component;
import net.minecraft.ChatFormatting;
import java.util.HashMap;
import java.util.Map;

public class DamageDealtTracker {
	private static final Map<Integer, Float> entityHealthMap = new HashMap<>();
	private static float lastDamageDealt = 0;
	private static long lastDamageTime = 0;
	private static final long DISPLAY_DURATION = 3000;

	public static void tick(Minecraft client, Player player) {
		if (player == null || player.level() == null) return;

		var entities = player.level().getEntities(player,
			player.getBoundingBox().inflate(30.0));

		for (var entity : entities) {
			if (!(entity instanceof LivingEntity) || entity == player) continue;

			LivingEntity living = (LivingEntity) entity;
			int entityId = entity.getId();
			float currentHealth = living.getHealth();

			if (entityHealthMap.containsKey(entityId)) {
				float lastHealth = entityHealthMap.get(entityId);
				if (currentHealth < lastHealth) {
					float damageDealt = lastHealth - currentHealth;
					lastDamageDealt = damageDealt;
					lastDamageTime = System.currentTimeMillis();
				}
			}

			entityHealthMap.put(entityId, currentHealth);
		}

		entityHealthMap.keySet().removeIf(id -> {
			for (var e : entities) {
				if (e.getId() == id) return false;
			}
			return true;
		});

		displayDamageDealt(player);
	}

	private static void displayDamageDealt(Player player) {
		long now = System.currentTimeMillis();
		if (lastDamageDealt > 0 && now - lastDamageTime < DISPLAY_DURATION) {
			String damageStr = String.format("%.1f", lastDamageDealt);
			Component damageMsg = Component.literal("⚔ Damage: ")
				.withStyle(ChatFormatting.RED)
				.append(Component.literal(damageStr).withStyle(ChatFormatting.YELLOW))
				.append(Component.literal(" ❤").withStyle(ChatFormatting.DARK_RED));
			player.displayClientMessage(damageMsg, true);
		}
	}
}
