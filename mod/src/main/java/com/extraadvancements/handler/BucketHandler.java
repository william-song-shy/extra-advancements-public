package com.extraadvancements.handler;

import com.extraadvancements.Grant;
import net.minecraft.core.BlockPos;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;

/**
 * 尼莫点：站在「以你为中心 65×129×65 的立方体中唯一的非空气方块」上放下热带鱼桶。
 *
 * <p>54 万方块远超函数 65536 条命令的硬上限，纯数据包做不了；但在 Java 侧扫一遍只要几毫秒，
 * 而且只在放桶那一刻跑一次。扫描带提前退出：正常世界里碰到第二个方块就返回，不会真的走满 54 万次。
 *
 * <p>用 {@code UseBlockCallback}（放置之前）而不是之后：判定要求的是「放下桶那一刻，
 * 你站的方块是唯一的非空气方块」，此时水还没生成。
 */
public final class BucketHandler {

    /** 立方体半径：x/z 各 ±32（合计 65），y ±64（合计 129）。 */
    private static final int RX = 32;
    private static final int RY = 64;
    private static final int RZ = 32;

    private BucketHandler() {
    }

    public static InteractionResult useBlock(Player player, Level level, InteractionHand hand,
                                             BlockHitResult hit) {
        // 永远返回 PASS：这个模组不接管任何交互，只旁听
        if (!(level instanceof ServerLevel serverLevel)
                || !(player instanceof ServerPlayer serverPlayer)) {
            return InteractionResult.PASS;
        }
        // 只看触发这次交互的那只手 —— Fabric 会对主手、副手各回调一次
        if (!serverPlayer.getItemInHand(hand).is(Items.TROPICAL_FISH_BUCKET)) {
            return InteractionResult.PASS;
        }
        if (!isOnlyBlockInCube(serverLevel, serverPlayer)) {
            return InteractionResult.PASS;
        }
        MinecraftServer server = serverLevel.getServer();
        if (server != null) {
            Grant.to(server, serverPlayer, Grant.NS + ":travel/point_nemo");
        }
        return InteractionResult.PASS;
    }

    /** 立方体里恰好只有一块非空气方块。 */
    private static boolean isOnlyBlockInCube(ServerLevel level, ServerPlayer player) {
        BlockPos center = player.blockPosition();
        int found = 0;
        for (int dx = -RX; dx <= RX; dx++) {
            for (int dy = -RY; dy <= RY; dy++) {
                for (int dz = -RZ; dz <= RZ; dz++) {
                    if (!level.getBlockState(center.offset(dx, dy, dz)).isAir()) {
                        if (++found > 1) {
                            return false;   // 提前退出
                        }
                    }
                }
            }
        }
        return found == 1;
    }
}
