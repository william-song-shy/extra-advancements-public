# 执行者 = 玩家。把 7 种虫蚀方块的挖掘数加起来。
scoreboard players operation @s ea.inow = @s ea.i0
scoreboard players operation @s ea.inow += @s ea.i1
scoreboard players operation @s ea.inow += @s ea.i2
scoreboard players operation @s ea.inow += @s ea.i3
scoreboard players operation @s ea.inow += @s ea.i4
scoreboard players operation @s ea.inow += @s ea.i5
scoreboard players operation @s ea.inow += @s ea.i6
# 只在总数刚刚增加的这一 tick 判定：此刻主手拿的就是挖它的那把工具
execute unless score @s ea.inow = @s ea.iprev if predicate extra_advancements:equipment/silk_touch_mainhand run advancement grant @s only extra_advancements:interactions/pest_control
# 记下本 tick 的总数
scoreboard players operation @s ea.iprev = @s ea.inow
