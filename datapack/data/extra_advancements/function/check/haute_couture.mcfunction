# 四件皮革甲同时在身上，且每件都既有 dyed_color 又有 trim 组件。
# 命令的物品参数里单独写组件名 = 只检查该组件是否存在。
execute as @a if items entity @s armor.head minecraft:leather_helmet[minecraft:dyed_color,minecraft:trim] if items entity @s armor.chest minecraft:leather_chestplate[minecraft:dyed_color,minecraft:trim] if items entity @s armor.legs minecraft:leather_leggings[minecraft:dyed_color,minecraft:trim] if items entity @s armor.feet minecraft:leather_boots[minecraft:dyed_color,minecraft:trim] run advancement grant @s only extra_advancements:silly/haute_couture
