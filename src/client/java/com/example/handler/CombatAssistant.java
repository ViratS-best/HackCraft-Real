package com.example.handler;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.network.chat.Component;
import net.minecraft.ChatFormatting;

public class CombatAssistant {
	private static ItemStack lastHeldItem = ItemStack.EMPTY;
	private static long lastSwapTime = 0;
	private static float lastPlayerHealth = 20f;
	private static long lastDamageTime = 0;
	private static float lastDamageAmount = 0;
	private static final long DAMAGE_DISPLAY_DURATION = 3000;

	public static void tick(Minecraft client) {
		if (client.player == null || client.level == null) return;

		Player player = client.player;
		ItemStack currentItem = player.getMainHandItem();

		if (!canEqual(currentItem, lastHeldItem)) {
			if (!lastHeldItem.isEmpty() && !currentItem.isEmpty()) {
				detectAndNotifyAttributeSwap(player, lastHeldItem, currentItem);
			}
			lastHeldItem = currentItem.copy();
			lastSwapTime = System.currentTimeMillis();
		}

		if (currentItem.getItem() == Items.MACE) {
			checkMaceRange(player);
		}

		SpearHelper.checkSpearLungeConditions(player);
		trackAndDisplayDamage(player);
		DamageDealtTracker.tick(client, player);
	}

	private static void trackAndDisplayDamage(Player player) {
		float currentHealth = player.getHealth();
		if (currentHealth < lastPlayerHealth) {
			float damageTaken = lastPlayerHealth - currentHealth;
			lastDamageAmount = damageTaken;
			lastDamageTime = System.currentTimeMillis();
		}
		lastPlayerHealth = currentHealth;

		long now = System.currentTimeMillis();
		if (lastDamageAmount > 0 && now - lastDamageTime < DAMAGE_DISPLAY_DURATION) {
			String damageStr = String.format("%.1f", lastDamageAmount);
			Component damageMsg = Component.literal("❤ Damage Taken: ")
				.withStyle(ChatFormatting.RED)
				.append(Component.literal(damageStr).withStyle(ChatFormatting.YELLOW));
			player.displayClientMessage(damageMsg, true);
		}
	}

	private static void detectAndNotifyAttributeSwap(Player player, ItemStack from, ItemStack to) {
		String fromName = from.getHoverName().getString();
		String toName = to.getHoverName().getString();

		if (!fromName.equals(toName)) {
			Component feedback = Component.literal("✓ Swapped: ")
				.withStyle(ChatFormatting.GREEN)
				.append(Component.literal(fromName).withStyle(ChatFormatting.GOLD))
				.append(Component.literal(" → ").withStyle(ChatFormatting.WHITE))
				.append(Component.literal(toName).withStyle(ChatFormatting.GOLD));

			player.displayClientMessage(feedback, true);
		}
	}

	private static boolean canEqual(ItemStack a, ItemStack b) {
		if (a.isEmpty() && b.isEmpty()) return true;
		if (a.isEmpty() || b.isEmpty()) return false;
		return a.getItem() == b.getItem();
	}

	private static void checkMaceRange(Player player) {
		double MACE_RANGE = 10.0;

		var allEntities = player.level().getEntities(player, 
			player.getBoundingBox().inflate(MACE_RANGE));

		if (!allEntities.isEmpty()) {
			double closestDistance = Double.MAX_VALUE;
			for (var entity : allEntities) {
				if (entity == player) continue;
				double dist = player.distanceTo(entity);
				if (dist < closestDistance && dist <= MACE_RANGE) {
					closestDistance = dist;
				}
			}

			if (closestDistance != Double.MAX_VALUE && closestDistance <= MACE_RANGE) {
				double displayDist = Math.round(closestDistance * 10.0) / 10.0;
				Component rangeInfo = Component.literal("⚔ Mace Range: ")
					.withStyle(ChatFormatting.RED)
					.append(Component.literal(displayDist + " blocks").withStyle(ChatFormatting.YELLOW));
				player.displayClientMessage(rangeInfo, true);
			}
		}
	}
}
