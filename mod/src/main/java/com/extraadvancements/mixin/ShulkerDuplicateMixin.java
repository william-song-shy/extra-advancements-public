package com.extraadvancements.mixin;

import com.extraadvancements.Grant;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.monster.Shulker;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * 出口贸易 —— 在下界复制一只潜影贝。
 *
 * <p>潜影贝复制没有任何触发器，而且 26.1.2 里也没有独立的复制方法：
 * 复制逻辑是**内联在 {@code Shulker#hurtServer} 里**的（该方法里能看到 getHealth /
 * nextInt / teleportSomewhere 的调用，但没有单独 spawnDuplicate 之类的方法）。
 * 所以只能在 hurtServer 前后各数一次附近的潜影贝数量，增加了就认为复制成功。
 *
 * <p>这是本模组里最不精确的一处：同一 tick 内如果有别的潜影贝生成，会误判。
 * 但它只在下界生效，误差可以接受。
 *
 * <p>顺带一个 26.1 的坑：伤害方法已经统一改名为 {@code hurtServer}，
 * 老的 {@code hurt(DamageSource, float)} 在 26.1.2 里根本不存在。
 */
@Mixin(Shulker.class)
public abstract class ShulkerDuplicateMixin {

    @Unique
    private int extraadv$shulkersBefore;

    @Inject(
            method = "hurtServer(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/damagesource/DamageSource;F)Z",
            at = @At("HEAD")
    )
    private void extraadv$beforeHurt(ServerLevel level, DamageSource source, float amount,
                                     CallbackInfoReturnable<Boolean> cir) {
        extraadv$shulkersBefore = extraadv$countNearby();
    }

    @Inject(
            method = "hurtServer(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/damagesource/DamageSource;F)Z",
            at = @At("TAIL")
    )
    private void extraadv$afterHurt(ServerLevel level, DamageSource source, float amount,
                                    CallbackInfoReturnable<Boolean> cir) {
        if (extraadv$countNearby() <= extraadv$shulkersBefore) {
            return;
        }
        if (level.dimension() != Level.NETHER) {
            return;
        }
        Shulker self = (Shulker) (Object) this;
        MinecraftServer server = level.getServer();
        if (server == null) {
            return;
        }
        Grant.nearby(server, level, self.position(), 16,
                Grant.NS + ":travel/export_business");
    }

    @Unique
    private int extraadv$countNearby() {
        Shulker self = (Shulker) (Object) this;
        return self.level()
                .getEntitiesOfClass(Shulker.class, self.getBoundingBox().inflate(4.0))
                .size();
    }
}
