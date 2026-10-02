# 由 #minecraft:tick 调用。只做计数与分发，逻辑都在 check/ 里。

# 每 tick
function extra_advancements:tick/every_tick

# 每 10 tick
scoreboard players add #t10 ea.clock 1
execute if score #t10 ea.clock matches 10.. run function extra_advancements:tick/every_10
execute if score #t10 ea.clock matches 10.. run scoreboard players set #t10 ea.clock 0

# 每 20 tick
scoreboard players add #t20 ea.clock 1
execute if score #t20 ea.clock matches 20.. run function extra_advancements:tick/every_20
execute if score #t20 ea.clock matches 20.. run scoreboard players set #t20 ea.clock 0
