package com.extraadvancements.mixin;

import com.extraadvancements.Grant;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.fox.Fox;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * 狐狸怎么叫？今天不叫 —— 狐狸用掉不死图腾。
 *
 * <p>狐狸叼着图腾受到致命伤时会消耗图腾复活（而不是把它掉出来），但这个过程没有任何触发器。
 * 这里在 {@code LivingEntity#checkTotemDeathProtection} 的 TAIL 插一个只读回调：
 * 返回 true 表示确实消耗了图腾、死亡被挡下。这个方法是 private，mixin 可以照常注入。
 *
 * <p>纯数据包侧还有一条近似判定（轮询带吸收效果的狐狸），见数据包的 check/fox_totem.mcfunction。
 */
@Mixin(LivingEntity.class)
public abstract class LivingEntityTotemMixin {

    @Inject(
            method = "checkTotemDeathProtection(Lnet/minecraft/world/damagesource/DamageSource;)Z",
            at = @At("TAIL")
    )
    private void extraadv$afterTotem(DamageSource source, CallbackInfoReturnable<Boolean> cir) {
        if (!Boolean.TRUE.equals(cir.getReturnValue())) {
            return;
        }
        if (!((Object) this instanceof Fox fox)) {
            return;
        }
        if (!(fox.level() instanceof ServerLevel level)) {
            return;
        }
        MinecraftServer server = level.getServer();
        if (server == null) {
            return;
        }
        Grant.nearby(server, level, fox.position(), 16,
                Grant.NS + ":interactions/what_does_the_fox_say");
    }
}
