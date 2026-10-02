# 三个分量各自与模组写下的世界出生点相差不超过 1 格
execute store result score @s ea.morix run data get entity @s Pos[0]
scoreboard players operation @s ea.morix -= #spawnx ea.spawn
execute store result score @s ea.moriy run data get entity @s Pos[1]
scoreboard players operation @s ea.moriy -= #spawny ea.spawn
execute store result score @s ea.moriz run data get entity @s Pos[2]
scoreboard players operation @s ea.moriz -= #spawnz ea.spawn
execute if score @s ea.morix matches -1..1 if score @s ea.moriy matches -1..1 if score @s ea.moriz matches -1..1 run function extra_advancements:travel/memento_mori_spawn_win
