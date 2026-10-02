package com.extraadvancements.mixin;

import com.extraadvancements.Grant;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Relative;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Set;

/**
 * 雅各天梯 —— 传送判定失败。
 *
 * <p>没有「上升高度」触发器，所以整条进度由数据包的状态机负责（记录起点 Y、落地即重置、
 * 上升 100 格即发放）。数据包唯一判不准的是**传送**：命令传送与末影珍珠在实现上
 * 无法与合法位移严格区分。模组在这里只做一件事 —— 把该玩家的 {@code ea.lstate} 清零，
 * 其余判定全部留在数据包。
 *
 * <p>Fabric API 没有通用传送事件（ServerEntityLevelChangeEvents 只管跨维度），
 * 而 Yarn 时代的 {@code ServerEntity#teleport} 在 26.1.2 里也不存在，
 * 所以注入 {@code Entity#teleportTo(ServerLevel, double, double, double, Set, float, float, boolean)} ——
 * /tp、末影珍珠和绝大多数模组传送都走这个重载。
 *
 * <p>加了一道距离门槛：位移不超过 8 格的小传送不算数，免得把正常的载具/换位也判成失败。
 */
@Mixin(Entity.class)
public abstract class EntityTeleportMixin {

    private static final double MIN_DISTANCE_SQR = 64.0;

    @Inject(
            method = "teleportTo(Lnet/minecraft/server/level/ServerLevel;DDDLjava/util/Set;FFZ)Z",
            at = @At("HEAD")
    )
    private void extraadv$beforeTeleport(ServerLevel level, double x, double y, double z,
                                         Set<Relative> relatives, float yRot, float xRot,
                                         boolean setCamera, CallbackInfoReturnable<Boolean> cir) {
        if (!((Object) this instanceof ServerPlayer player)) {
            return;
        }
        boolean crossDimension = player.level() != level;
        double dx = x - player.getX();
        double dy = y - player.getY();
        double dz = z - player.getZ();
        if (!crossDimension && dx * dx + dy * dy + dz * dz <= MIN_DISTANCE_SQR) {
            return;
        }
        MinecraftServer server = player.level().getServer();
        if (server == null) {
            return;
        }
        Grant.quietly(server, "scoreboard players set "
                + player.getGameProfile().name() + " ea.lstate 0");
    }
}
