# 近似：狐狸能获得吸收效果的途径基本只有不死图腾。
# 精确判定由配套模组在图腾生效点完成。
execute as @e[type=minecraft:fox] at @s if predicate extra_advancements:effects/absorption run advancement grant @a[distance=..16] only extra_advancements:interactions/what_does_the_fox_say
