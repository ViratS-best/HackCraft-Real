package com.soulswap;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SoulSwapMod implements ModInitializer {
    public static final String MOD_ID = "soulswap";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        LOGGER.info("[Soul Swap] Mod initialized! Ender pearls now swap positions with mobs.");
        LOGGER.info("[Soul Swap] Feature 1: Pearl + Mob = Position Swap");
        LOGGER.info("[Soul Swap] Feature 2: Hostile mobs take 2 hearts of confusion damage");
        LOGGER.info("[Soul Swap] Feature 3: Passive animals grant 3s Invisibility");
    }
}
