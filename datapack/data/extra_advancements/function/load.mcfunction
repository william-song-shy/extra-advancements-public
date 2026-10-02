# Extra Advancements —— 初始化（由 #minecraft:load 调用）

# ---- 内部计时器 / 常量 ----
scoreboard objectives add ea.clock dummy
scoreboard objectives add ea.const dummy
scoreboard players set #4 ea.const 4
scoreboard players set #8 ea.const 8

# ---- 5 Miles ----
scoreboard objectives add ea.fmx dummy
scoreboard objectives add ea.fmz dummy

# ---- 狼吞虎咽 ----
scoreboard objectives add ea.dinner dummy

# ---- 雅各天梯 ----
scoreboard objectives add ea.lstate dummy
scoreboard objectives add ea.lstart dummy
scoreboard objectives add ea.lnow dummy
scoreboard objectives add ea.ldx dummy
scoreboard objectives add ea.ldz dummy
scoreboard objectives add ea.lpx dummy
scoreboard objectives add ea.lpz dummy

# ---- 世界尽头？ ----
scoreboard objectives add ea.ringx dummy
scoreboard objectives add ea.ringz dummy

# ---- 向死而生 ----
# 世界出生点（由配套模组每 5 秒写入 #spawnx / #spawny / #spawnz）
scoreboard objectives add ea.spawn dummy
scoreboard objectives add ea.morix dummy
scoreboard objectives add ea.morix2 dummy
scoreboard objectives add ea.moriy dummy
scoreboard objectives add ea.moriy2 dummy
scoreboard objectives add ea.moriz dummy
scoreboard objectives add ea.moriz2 dummy

# ---- 全球变暖 ----
scoreboard objectives add ea.snow dummy

# ---- 模组标记（配套模组启动时置 1）----
scoreboard objectives add ea.mod dummy

# ---- 金石为开：每种矿石一条挖掘统计 ----
scoreboard objectives add ea.m0 minecraft.mined:minecraft.coal_ore
scoreboard objectives add ea.m1 minecraft.mined:minecraft.deepslate_coal_ore
scoreboard objectives add ea.m2 minecraft.mined:minecraft.iron_ore
scoreboard objectives add ea.m3 minecraft.mined:minecraft.deepslate_iron_ore
scoreboard objectives add ea.m4 minecraft.mined:minecraft.copper_ore
scoreboard objectives add ea.m5 minecraft.mined:minecraft.deepslate_copper_ore
scoreboard objectives add ea.m6 minecraft.mined:minecraft.gold_ore
scoreboard objectives add ea.m7 minecraft.mined:minecraft.deepslate_gold_ore
scoreboard objectives add ea.m8 minecraft.mined:minecraft.redstone_ore
scoreboard objectives add ea.m9 minecraft.mined:minecraft.deepslate_redstone_ore
scoreboard objectives add ea.m10 minecraft.mined:minecraft.lapis_ore
scoreboard objectives add ea.m11 minecraft.mined:minecraft.deepslate_lapis_ore
scoreboard objectives add ea.m12 minecraft.mined:minecraft.diamond_ore
scoreboard objectives add ea.m13 minecraft.mined:minecraft.deepslate_diamond_ore
scoreboard objectives add ea.m14 minecraft.mined:minecraft.emerald_ore
scoreboard objectives add ea.m15 minecraft.mined:minecraft.deepslate_emerald_ore
scoreboard objectives add ea.m16 minecraft.mined:minecraft.nether_gold_ore
scoreboard objectives add ea.m17 minecraft.mined:minecraft.nether_quartz_ore
scoreboard objectives add ea.m18 minecraft.mined:minecraft.ancient_debris

# ---- 除虫服务：被虫蚀方块的挖掘统计（每 tick 与上一 tick 比较）----
scoreboard objectives add ea.i0 minecraft.mined:minecraft.infested_stone
scoreboard objectives add ea.i1 minecraft.mined:minecraft.infested_cobblestone
scoreboard objectives add ea.i2 minecraft.mined:minecraft.infested_stone_bricks
scoreboard objectives add ea.i3 minecraft.mined:minecraft.infested_mossy_stone_bricks
scoreboard objectives add ea.i4 minecraft.mined:minecraft.infested_cracked_stone_bricks
scoreboard objectives add ea.i5 minecraft.mined:minecraft.infested_chiseled_stone_bricks
scoreboard objectives add ea.i6 minecraft.mined:minecraft.infested_deepslate
# 上一 tick / 本 tick 的挖掘总数
scoreboard objectives add ea.inow dummy
scoreboard objectives add ea.iprev dummy
