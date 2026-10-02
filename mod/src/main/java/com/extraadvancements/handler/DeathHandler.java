package com.extraadvancements.handler;

import com.extraadvancements.Grant;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Endermite;
import net.minecraft.world.phys.Vec3;

/**
 * 死亡上下文。原版没有任何触发器能判定「非玩家造成的击杀」或「某生物打死了某生物」，
 * 而死亡事件回调同时给到死者与伤害来源，一次覆盖两条进度。
 */
public final class DeathHandler {

    private DeathHandler() {
    }

    public static void afterDeath(LivingEntity entity, DamageSource source) {
        if (!(entity.level() instanceof ServerLevel level)) {
            return;
        }
        MinecraftServer server = level.getServer();
        if (server == null) {
            return;
        }
        Vec3 pos = entity.position();

        // 大卫与歌利亚：末影螨打死末影人，16 格内的玩家获得
        if (entity.getType() == EntityType.ENDERMAN && source.getEntity() instanceof Endermite) {
            Grant.nearby(server, level, pos, 16, Grant.NS + ":interactions/david_and_goliath");
        }

        // 烈焰飞雪：烈焰人的致死伤害必须是冰冻伤害（细雪）
        if (entity.getType() == EntityType.BLAZE && source.is(DamageTypeTags.IS_FREEZING)) {
            Grant.nearby(server, level, pos, 16, Grant.NS + ":interactions/hell_freezes_over");
        }
    }
}
