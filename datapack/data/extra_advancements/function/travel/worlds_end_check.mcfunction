# 复现 MC-159283 的 32 位整数溢出：X = x/8，Z = z/8，X*X + Z*Z < 0 即为空洞环内。
# 记分板本身就是 32 位 int，除法向零截断、乘法与加法静默回绕，与 Java 侧逐位一致。
execute store result score @s ea.ringx run data get entity @s Pos[0]
execute store result score @s ea.ringz run data get entity @s Pos[2]
scoreboard players operation @s ea.ringx /= #8 ea.const
scoreboard players operation @s ea.ringz /= #8 ea.const
scoreboard players operation @s ea.ringx *= @s ea.ringx
scoreboard players operation @s ea.ringz *= @s ea.ringz
scoreboard players operation @s ea.ringx += @s ea.ringz
execute if score @s ea.ringx matches ..-1 run advancement grant @s only extra_advancements:travel/worlds_end
