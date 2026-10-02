# 传送检测（启发式）：单 tick 水平位移超过 10 格即判失败。
# 精确判定由配套模组负责（模组在传送入口把 ea.lstate 清零）。
execute store result score @s ea.ldx run data get entity @s Pos[0]
scoreboard players operation @s ea.ldx -= @s ea.lpx
execute store result score @s ea.ldz run data get entity @s Pos[2]
scoreboard players operation @s ea.ldz -= @s ea.lpz
execute unless score @s ea.ldx matches -10..10 run scoreboard players set @s ea.lstate 0
execute unless score @s ea.ldz matches -10..10 run scoreboard players set @s ea.lstate 0
# 记录本 tick 位置
execute store result score @s ea.lpx run data get entity @s Pos[0]
execute store result score @s ea.lpz run data get entity @s Pos[2]
# 高度差 ≥ 100 → 达成
execute store result score @s ea.lnow run data get entity @s Pos[1]
scoreboard players operation @s ea.lnow -= @s ea.lstart
execute if score @s ea.lstate matches 1.. if score @s ea.lnow matches 100.. run function extra_advancements:travel/jacobs_ladder_win
