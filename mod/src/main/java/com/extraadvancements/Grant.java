package com.extraadvancements;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.Vec3;

/**
 * 发放进度。
 *
 * <p>一律走命令而不是直接操作 {@code PlayerAdvancements}：{@code advancement grant … only <id>}
 * 的语义（只授予该节点、不连带父进度、奖励函数只跑一次）由原版保证，
 * 手写 award 调用很难复刻。
 */
public final class Grant {

    /** 数据包命名空间。 */
    public static final String NS = "extra_advancements";

    private Grant() {
    }

    /**
     * 发放给事件位置 {@code radius} 格内的所有玩家。
     *
     * <p>注意必须把命令源定位到事件位置：{@code MinecraftServer#createCommandSourceStack()}
     * 默认把命令源放在主世界原点，不设位置的话 {@code distance} 是相对原点算的。
     */
    public static void nearby(MinecraftServer server, ServerLevel level, Vec3 pos,
                              double radius, String advancement) {
        CommandSourceStack source = server.createCommandSourceStack()
                .withLevel(level)
                .withPosition(pos)
                .withSuppressedOutput();
        server.getCommands().performPrefixedCommand(source,
                "advancement grant @a[distance=.." + number(radius) + "] only " + advancement);
    }

    /** 只发给某一个玩家（按名字选，Minecraft 用户名只含 [A-Za-z0-9_]，不必转义）。 */
    public static void to(MinecraftServer server, ServerPlayer player, String advancement) {
        CommandSourceStack source = server.createCommandSourceStack().withSuppressedOutput();
        server.getCommands().performPrefixedCommand(source,
                "advancement grant @a[name=\"" + player.getGameProfile().name() + "\"] only " + advancement);
    }

    /** 让数据包以该玩家为执行者跑一个函数（判定留给数据包时用）。 */
    public static void runFunctionAs(MinecraftServer server, ServerPlayer player, String function) {
        CommandSourceStack source = server.createCommandSourceStack().withSuppressedOutput();
        server.getCommands().performPrefixedCommand(source,
                "execute as @a[name=\"" + player.getGameProfile().name() + "\"] at @s run function " + function);
    }

    /** 在服务端命令源上跑一条命令（失败只写日志，不影响游戏）。 */
    public static void quietly(MinecraftServer server, String command) {
        server.getCommands().performPrefixedCommand(
                server.createCommandSourceStack().withSuppressedOutput(), command);
    }

    private static String number(double value) {
        return value == Math.rint(value) ? Long.toString((long) value) : Double.toString(value);
    }
}
