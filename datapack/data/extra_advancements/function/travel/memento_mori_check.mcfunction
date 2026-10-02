# 维度必须一致：LastDeathLocation.dimension 与玩家当前维度
execute if predicate extra_advancements:dimension/overworld if data entity @s LastDeathLocation{dimension:"minecraft:overworld"} run function extra_advancements:travel/memento_mori_pos
execute if predicate extra_advancements:dimension/the_nether if data entity @s LastDeathLocation{dimension:"minecraft:the_nether"} run function extra_advancements:travel/memento_mori_pos
execute if predicate extra_advancements:dimension/the_end if data entity @s LastDeathLocation{dimension:"minecraft:the_end"} run function extra_advancements:travel/memento_mori_pos
