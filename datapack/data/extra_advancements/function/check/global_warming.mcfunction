execute in minecraft:overworld positioned 0 0 0 as @e[type=minecraft:snow_golem,distance=..30000000] run scoreboard players set @s ea.snow 0
execute in minecraft:the_end positioned 0 0 0 as @e[type=minecraft:snow_golem,distance=..30000000] run scoreboard players set @s ea.snow 0
execute in minecraft:the_nether positioned 0 0 0 as @e[type=minecraft:snow_golem,distance=..30000000] run scoreboard players add @s ea.snow 1
execute in minecraft:the_nether positioned 0 0 0 as @e[type=minecraft:snow_golem,distance=..30000000,scores={ea.snow=60..}] at @s run advancement grant @a[distance=..64] only extra_advancements:travel/global_warming
