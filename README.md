# Fabric Example Mod

## Setup

For setup instructions please see the [fabric documentation page](https://docs.fabricmc.net/develop/getting-started/setting-up) that relates to the IDE that you are using.

## License

This template is available under the CC0 license. Feel free to learn from it and incorporate it in your own projects.
## Features

### Damage Dealt Tracker

Shows real-time damage dealt to enemies in the action bar. When you hit an entity, you'll see the exact amount of damage dealt displayed in red and yellow.

- Format: `Damage: 5.2`
- Tracks up to 30 blocks away
- Displays for 3 seconds after each hit

### Damage Taken Indicator

Displays damage you receive when hit by enemies. Helps you understand how much damage you're taking in combat.

- Format: `Damage Taken: 3.5`
- Shows for 3 seconds after each hit
- Useful for assessing threat levels

### Spear Combat Helper

Provides real-time feedback on spear lunge conditions. The mod shows you exactly what conditions are needed to perform a successful lunge attack.

Status messages:
- `Charge spear...` - Charge your attack fully
- `Move forward to lunge!` - You need forward momentum
- `READY TO LUNGE` (green) - Optimal conditions met

Lunge requirements:
- Fully charged attack (90% or higher)
- Moving forward
- Airborne or falling (jumping helps)
- Not in water

### Weapon Swap Detection

Confirms successful attribute swaps with a notification message. Shows what weapon you swapped from and to.

- Format: `Swapped: Iron Sword to Diamond Sword`
- Displays in green when swap is detected
- Confirms equipment changes instantly

### Mace Range Helper

Displays the distance to the nearest entity when holding a mace. Helps you know if targets are within effective range.

- Format: `Mace Range: 4.2 blocks`
- Shows nearest entity distance
- Updates in real-time

### Server Optimization

Network packet batching optimization for faster attribute swaps on multiplayer servers. Reduces latency and improves responsiveness during combat actions.

## Installation

1. Download modid-1.0.0.jar from the releases
2. Place it in your .minecraft/mods/ folder
3. Launch Minecraft with Fabric Loader installed
4. All features will be enabled automatically

## Usage

Testing the mod in single player:

```
gradlew runClient
```

Building the mod from source:

```
gradlew build
```

The compiled JAR will be created at: build/libs/modid-1.0.0.jar

## PvP Combat Guide

### Attribute Swapping

Use your hotbar number keys (1-9) to quickly swap between weapons. The mod will confirm each swap with a notification message showing which weapon you switched to.

- Press hotbar keys for instant swaps
- Watch for the swap confirmation message
- No cooldown between swaps
- Swap while attacking for combo chains

### Spear Lunge Technique

To land consistent spear lunges:

1. Jump first to guarantee you're airborne
2. Charge your attack by holding the attack button
3. Move forward while the attack is charging
4. Watch for the green "READY TO LUNGE" message
5. Release to execute the lunge

### Reading Damage Numbers

Monitor both damage dealt and damage taken to adjust your strategy:

- High damage dealt: Your attacks are landing effectively
- High damage taken: You may need better armor or healing
- Track patterns to understand matchups

### Mace Combat

When using a mace:

- Check the range indicator before attacking
- Mace has good reach compared to swords
- Sweep attacks hit multiple targets
- Most effective at close to medium range

## Technical Details

Language: Java 21
Minecraft Version: 1.21.11
Fabric Loader: 0.18.2 or higher
Fabric API: 0.139.4 or higher

Main Components:
- CombatAssistant.java - Main tick handler and event listener
- SpearHelper.java - Spear lunge condition detection
- DamageDealtTracker.java - Entity damage tracking
- DamageIndicator.java - Damage display system
- SpearOptimizationMixin.java - Server-side optimization mixin

## Compatibility

Works on multiplayer servers (no server mod required)
Works in single player worlds
Compatible with other Fabric mods
No known conflicts

## Building from Source

Requirements:
- Java 21 or higher
- Gradle

Build:
```
gradlew build
```

Run client:
```
gradlew runClient
```

The mod uses Fabric Loom for development and Mixin for bytecode modifications.

## License

This mod is released under the CC0 license (public domain).
