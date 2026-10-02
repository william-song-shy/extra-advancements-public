# 本 tick 饿到 0 → 重开 100 tick（5.0s）窗口
execute as @a if predicate extra_advancements:food/starving run scoreboard players set @s ea.dinner 101
# 窗口递减
execute as @a[scores={ea.dinner=1..}] run scoreboard players remove @s ea.dinner 1
# 窗口内吃满饥饿值与饱和度 → 达成
execute as @a[scores={ea.dinner=1..}] if predicate extra_advancements:food/stuffed run function extra_advancements:check/dinner_rush_win
# 窗口关闭
execute as @a[scores={ea.dinner=0}] run scoreboard players reset @s ea.dinner
