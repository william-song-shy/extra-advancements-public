# 先把船上的标记清掉，再按乘客类型重新打标
tag @e[type=#minecraft:boat] remove ea.blaze
tag @e[type=#minecraft:boat] remove ea.breeze
execute as @e[type=minecraft:blaze] on vehicle run tag @s add ea.blaze
execute as @e[type=minecraft:breeze] on vehicle run tag @s add ea.breeze
# 两种乘客都有的船，看牵引者是不是玩家
execute as @e[type=#minecraft:boat, tag=ea.blaze, tag=ea.breeze] on leasher if entity @s[type=minecraft:player] run advancement grant @s only extra_advancements:interactions/fire_and_wind
