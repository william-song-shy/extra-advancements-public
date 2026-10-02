# 世界出生点写在主世界，只有主世界才算数。
# （这条维度检查对两支都必要：模组那一支比的是计分板数字，计分板不认维度。）
execute if predicate extra_advancements:dimension/overworld if items entity @s weapon.mainhand minecraft:recovery_compass run advancement grant @s only extra_advancements:travel/memento_mori
