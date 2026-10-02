# 由内部进度 internal/stew_consumed 的奖励函数调用。
# 直接查玩家身上此刻的药水效果，避开「按花枚举 duration」的脆弱做法。
execute if predicate extra_advancements:effects/fire_resistance run advancement grant @s only extra_advancements:interactions/todays_specials effect_fire_resistance
execute if predicate extra_advancements:effects/blindness run advancement grant @s only extra_advancements:interactions/todays_specials effect_blindness
execute if predicate extra_advancements:effects/saturation run advancement grant @s only extra_advancements:interactions/todays_specials effect_saturation
execute if predicate extra_advancements:effects/nausea run advancement grant @s only extra_advancements:interactions/todays_specials effect_nausea
execute if predicate extra_advancements:effects/jump_boost run advancement grant @s only extra_advancements:interactions/todays_specials effect_jump_boost
execute if predicate extra_advancements:effects/poison run advancement grant @s only extra_advancements:interactions/todays_specials effect_poison
execute if predicate extra_advancements:effects/regeneration run advancement grant @s only extra_advancements:interactions/todays_specials effect_regeneration
execute if predicate extra_advancements:effects/night_vision run advancement grant @s only extra_advancements:interactions/todays_specials effect_night_vision
execute if predicate extra_advancements:effects/weakness run advancement grant @s only extra_advancements:interactions/todays_specials effect_weakness
execute if predicate extra_advancements:effects/wither run advancement grant @s only extra_advancements:interactions/todays_specials effect_wither

# 撤销自身，让它下次喝炖菜还能再触发
advancement revoke @s only extra_advancements:internal/stew_consumed
