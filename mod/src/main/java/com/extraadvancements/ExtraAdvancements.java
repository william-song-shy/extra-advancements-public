package com.extraadvancements;

import com.extraadvancements.handler.BlockBreakHandler;
import com.extraadvancements.handler.BucketHandler;
import com.extraadvancements.handler.ConversionHandler;
import com.extraadvancements.handler.DeathHandler;
import com.extraadvancements.handler.NoteBlockHandler;
import com.extraadvancements.handler.SpawnHandler;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Extra Advancements 的配套模组。
 *
 * <p>它只做一件事：把原版触发器判定不了的那几个事件认出来，然后交给数据包发进度。
 * 不注册任何方块 / 物品 / 实体 / 附魔，不取消任何事件，不改写任何返回值 ——
 * 唯一的写操作是 {@code /advancement grant}。
 *
 * <p>模组是**可选**的：没装时数据包照常加载，只是那几条进度拿不到（其中一部分有
 * 纯数据包近似判定，见数据包里的 check/*.mcfunction）。
 */
public class ExtraAdvancements implements ModInitializer {

    public static final String MOD_ID = "extra_advancements";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        ServerLifecycleEvents.SERVER_STARTED.register(server -> {
            // 给数据包留一个「模组在岗」的标记：scoreboard players set #mod ea.mod 1。
            // 先建目标再设值，这样即使数据包的 load 还没跑过也不会报错。
            Grant.quietly(server, "scoreboard objectives add ea.mod dummy");
            Grant.quietly(server, "scoreboard players set #mod ea.mod 1");
            LOGGER.info("Extra Advancements companion 已就绪");
        });

        // 死亡上下文：大卫与歌利亚（末影螨杀末影人）、烈焰飞雪（烈焰人死于冰冻）
        ServerLivingEntityEvents.AFTER_DEATH.register(DeathHandler::afterDeath);

        // 蛮兵僵尸化：原版没有任何触发器，Fabric API 直接给了这个事件
        ServerLivingEntityEvents.MOB_CONVERSION.register(ConversionHandler::onConversion);

        // 除虫服务：需要「破坏方块当时手里拿的是什么」
        PlayerBlockBreakEvents.AFTER.register(BlockBreakHandler::afterBlockBreak);

        // 向死而生：把世界出生点写进计分板（数据包看不到这个坐标）
        ServerTickEvents.END_SERVER_TICK.register(SpawnHandler::onTick);

        // 一人乐队：「同时」以 tick 为界，每 tick 结束清空已听到的音色集合
        ServerTickEvents.END_SERVER_TICK.register(NoteBlockHandler::onEndTick);

        // 尼莫点：放热带鱼桶时在 Java 侧扫一次体积
        UseBlockCallback.EVENT.register(BucketHandler::useBlock);
    }
}
