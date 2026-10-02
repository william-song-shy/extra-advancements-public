# ea.lstate：0 = 未开始 / 已重置，1 = 本次滞空进行中
# 先给尚未记分的玩家把分数建出来（add 0 对已有分数无影响），
# 否则新玩家不会匹配下面的 scores={ea.lstate=0}
execute as @a run scoreboard players add @s ea.lstate 0
# 落地或骑乘 → 重置
execute as @a[scores={ea.lstate=1..}] if predicate extra_advancements:flags/on_ground run scoreboard players set @s ea.lstate 0
execute as @a[scores={ea.lstate=1..}] if predicate extra_advancements:flags/in_vehicle run scoreboard players set @s ea.lstate 0
# 在空中且未开始 → 记录起点
execute as @a[scores={ea.lstate=0}] unless predicate extra_advancements:flags/on_ground unless predicate extra_advancements:flags/in_vehicle run function extra_advancements:travel/jacobs_ladder_begin
# 进行中 → 更新高度差
execute as @a[scores={ea.lstate=1..}] run function extra_advancements:travel/jacobs_ladder_check
