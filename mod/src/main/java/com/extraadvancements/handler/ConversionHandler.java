package com.extraadvancements.handler;

import com.extraadvancements.Grant;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ConversionParams;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;

/**
 * 蛮兵转职：猪灵蛮兵离开下界 15 秒后僵尸化。
 *
 * <p>「僵尸化完成」这个瞬间原版没有触发器，而且新生成的僵尸猪灵不继承原实体 UUID，
 * 所以纯数据包只能做会误判的状态机近似。这里用 Fabric API 的 MOB_CONVERSION 事件精确命中 ——
 * 它正好覆盖 {@code AbstractPiglin#finishConversion} 走的那条「换类型、换实例」的路径，
 * 不需要写 mixin。
 */
public final class ConversionHandler {

    private ConversionHandler() {
    }

    public static void onConversion(Mob previous, Mob converted, ConversionParams params) {
        if (previous.getType() != EntityType.PIGLIN_BRUTE
                || converted.getType() != EntityType.ZOMBIFIED_PIGLIN) {
            return;
        }
        if (!(converted.level() instanceof ServerLevel level)) {
            return;
        }
        MinecraftServer server = level.getServer();
        if (server == null) {
            return;
        }
        Grant.nearby(server, level, converted.position(), 16,
                Grant.NS + ":interactions/brute_conversion");
    }
}
