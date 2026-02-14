# Combat Assistant Mod

Advanced PvP combat helper for Minecraft 1.21.11 (Fabric)

## Features

### 🎯 Core Combat Indicators

**Damage Dealt Tracker**
- Shows real-time damage dealt to enemies
- Format: `⚔ Damage: 5.2 ❤`
- Tracks up to 30 blocks away
- Displays for 3 seconds

**Damage Taken Indicator**
- Shows damage you receive
- Format: `❤ Damage Taken: 3.5`
- Displays for 3 seconds

### ⚔ Spear Combat Helper

**Lunge Condition Tracker**
- `⚔ Charge spear...` - Charge your attack
- `⚔ Move forward to lunge!` - Need forward momentum
- `✓ READY TO LUNGE` (green) - Optimal conditions

**Lunge Requirements:**
- Fully charged attack (90%+)
- Moving forward
- Airborne or falling (jump helps)
- Not in water

### ⚔ Weapon Swap Detection

**Attribute Swap Notification**
- Green checkmark on swap: `✓ Swapped: Iron Sword → Diamond Sword`
- Confirms successful weapon switch
- Perfect for PvP combos

### 🗡️ Mace Range Helper

**Range Indicator**
- `⚔ Mace Range: 4.2 blocks`
- Shows nearest entity distance
- Red warning when out of range

### 🚀 Server Optimization

**Network Packet Batching**
- Faster attribute swaps on servers
- Reduced latency for combat actions
- Better attack registration

## Installation

1. Download `modid-1.0.0.jar`
2. Place in `.minecraft/mods/` folder
3. Run Minecraft with Fabric Loader
4. All features enabled automatically

## Usage

**Testing:**
```bash
gradlew runClient
```

**Building:**
```bash
gradlew build
```

Output JAR: `build/libs/modid-1.0.0.jar`

## PvP Combat Tips

1. **Attribute Swapping:**
   - Use hotbar keys (1-9) for quick swaps
   - Watch confirmation message
   - No cooldown between swaps

2. **Spear Lunge:**
   - Jump first for guaranteed airborne
   - Charge attack while moving forward
   - Look for green "READY TO LUNGE" message

3. **Damage Tracking:**
   - Monitor damage dealt vs taken
   - Adjust strategy based on feedback
   - 3-second display duration

4. **Mace Combat:**
   - Check range indicator before attacking
   - AOE sweep hits multiple targets
   - Best at close range

## Technical Details

**Language:** Java 21  
**Minecraft:** 1.21.11  
**Loader:** Fabric 0.18.2+  
**API:** Fabric API 0.139.4+

**Components:**
- CombatAssistant.java - Main tick handler
- SpearHelper.java - Spear lunge detection
- DamageDealtTracker.java - Damage tracking
- SpearOptimizationMixin.java - Server optimization

## Compatibility

✓ Multiplayer servers  
✓ Single player  
✓ Server-side compatible (no server mod needed)  
✓ Works with other mods

## License

CC0 - Public Domain
