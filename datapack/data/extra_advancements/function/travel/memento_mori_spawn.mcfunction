execute if score #spawnx ea.spawn matches -30000000..30000000 run function extra_advancements:travel/memento_mori_spawn_mod
execute unless score #spawnx ea.spawn matches -30000000..30000000 if entity @s[distance=..1] run function extra_advancements:travel/memento_mori_spawn_win
