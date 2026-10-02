# 死过的玩家：LastDeathLocation 与当前位置逐分量比对（±1 格）
execute as @a if data entity @s LastDeathLocation run function extra_advancements:travel/memento_mori_check
# 没死过的玩家：世界出生点
execute as @a unless data entity @s LastDeathLocation run function extra_advancements:travel/memento_mori_spawn
