package com.example.handler;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.network.chat.Component;
import net.minecraft.ChatFormatting;

public class SpearHelper {
	public static void checkSpearLungeConditions(Player player) {
		ItemStack mainHand = player.getMainHandItem();
		String itemName = mainHand.getItem().toString();
		if (!itemName.contains("spear")) return;

		boolean isMoving = player.getDeltaMovement().horizontalDistance() > 0.1;
		boolean isAttacking = player.getAttackStrengthScale(0) > 0.9;
		boolean isFalling = player.getDeltaMovement().y < -0.1;

		String status = "";
		ChatFormatting color = ChatFormatting.RED;

		if (!isAttacking) {
			status = "⚔ Charge spear...";
		} else if (!isMoving && !isFalling) {
			status = "⚔ Move forward to lunge!";
		} else if (isMoving && isAttacking) {
			status = "✓ READY TO LUNGE";
			color = ChatFormatting.GREEN;
		}

		if (!status.isEmpty()) {
			player.displayClientMessage(
				Component.literal(status).withStyle(color),
				true
			);
		}
	}
}
