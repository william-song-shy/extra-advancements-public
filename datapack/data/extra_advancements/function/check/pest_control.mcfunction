# 新玩家先把分数建出来
execute as @a[tag=!ea.pest_ready] run function extra_advancements:check/pest_control_init
tag @a add ea.pest_ready
# 逐玩家汇总本 tick 的虫蚀方块挖掘总数并与上一 tick 比较
execute as @a run function extra_advancements:check/pest_control_player
