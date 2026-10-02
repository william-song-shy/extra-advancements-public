# 三个分量各自与上次死亡点相差不超过 1 格
execute store result score @s ea.morix run data get entity @s Pos[0]
execute store result score @s ea.morix2 run data get entity @s LastDeathLocation.pos[0]
scoreboard players operation @s ea.morix -= @s ea.morix2
execute store result score @s ea.moriy run data get entity @s Pos[1]
execute store result score @s ea.moriy2 run data get entity @s LastDeathLocation.pos[1]
scoreboard players operation @s ea.moriy -= @s ea.moriy2
execute store result score @s ea.moriz run data get entity @s Pos[2]
execute store result score @s ea.moriz2 run data get entity @s LastDeathLocation.pos[2]
scoreboard players operation @s ea.moriz -= @s ea.moriz2
execute if score @s ea.morix matches -1..1 if score @s ea.moriy matches -1..1 if score @s ea.moriz matches -1..1 if items entity @s weapon.mainhand minecraft:recovery_compass run advancement grant @s only extra_advancements:travel/memento_mori
