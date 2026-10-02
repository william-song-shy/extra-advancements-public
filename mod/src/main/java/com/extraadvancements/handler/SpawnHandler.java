package com.extraadvancements.handler;

import com.extraadvancements.Grant;
import net.minecraft.core.BlockPos;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;

/**
 * 向死而生（「从未死亡」那一支）：把世界出生点的坐标交给数据包。
 *
 * <p>追溯指针只在「死过、且当前维度与殒命维度相同」时才指；从没死过的玩家，它只会乱转。
 * 而「向死而生」的要求是走回你该回到的地方 —— 死过的回殒命点，没死过的回世界出生点。
 * 可这个坐标数据包自己拿不到 —— 原版没有读世界出生点的命令（{@code /setworldspawn}
 * 只写不读），指针的朝向又是客户端算的、不进物品 NBT。所以由模组把主世界的出生点
 * 写进计分板 {@code ea.spawn} 的三个假玩家（{@code #spawnx} / {@code #spawny} /
 * {@code #spawnz}），数据包的 {@code travel/memento_mori_spawn_mod} 再拿它和玩家位置比。
 *
 * <p>没装模组时数据包有一条纯数据包近似（靠 tick 函数的命令源落在出生点上），
 * 装了模组就换成这里的精确值。
 *
 * <p>每 100 tick 无条件重写一次。这样 {@code /setworldspawn} 改过出生点、或者数据包的
 * {@code uninstall} 把计分板清掉之后，最多 5 秒就能自己恢复。
 */
public final class SpawnHandler {

    /** 刷新间隔：100 tick = 5 秒。 */
    private static final int REFRESH_INTERVAL = 100;

    private static int counter;

    private SpawnHandler() {
    }

    public static void onTick(MinecraftServer server) {
        if (++counter < REFRESH_INTERVAL) {
            return;
        }
        counter = 0;

        ServerLevel overworld = server.overworld();
        BlockPos spawn = overworld.getRespawnData().pos();

        Grant.quietly(server, "scoreboard objectives add ea.spawn dummy");
        Grant.quietly(server, "scoreboard players set #spawnx ea.spawn " + spawn.getX());
        Grant.quietly(server, "scoreboard players set #spawny ea.spawn " + spawn.getY());
        Grant.quietly(server, "scoreboard players set #spawnz ea.spawn " + spawn.getZ());
    }
}
