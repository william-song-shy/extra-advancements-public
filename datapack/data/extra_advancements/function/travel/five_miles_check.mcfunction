execute store result score @s ea.fmx run data get entity @s Pos[0]
execute store result score @s ea.fmz run data get entity @s Pos[2]
# ① 粗筛：任一轴单独就越界
execute if score @s ea.fmx matches 32768.. run advancement grant @s only extra_advancements:travel/five_miles
execute if score @s ea.fmx matches ..-32768 run advancement grant @s only extra_advancements:travel/five_miles
execute if score @s ea.fmz matches 32768.. run advancement grant @s only extra_advancements:travel/five_miles
execute if score @s ea.fmz matches ..-32768 run advancement grant @s only extra_advancements:travel/five_miles
# ② 细判：除以 4 再平方相加，与阈值比
scoreboard players operation @s ea.fmx /= #4 ea.const
scoreboard players operation @s ea.fmz /= #4 ea.const
scoreboard players operation @s ea.fmx *= @s ea.fmx
scoreboard players operation @s ea.fmz *= @s ea.fmz
scoreboard players operation @s ea.fmx += @s ea.fmz
execute if score @s ea.fmx matches 4047139.. run advancement grant @s only extra_advancements:travel/five_miles
