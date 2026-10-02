package com.extraadvancements.handler;

import com.extraadvancements.Grant;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.BlockPos;

/**
 * 除虫服务：用带精准采集的工具挖掉一个虫蚀方块。
 *
 * <p>原版没有「破坏方块」触发器，而精准采集挖虫蚀方块掉的是普通对应方块，
 * 掉落物上没有任何可辨识特征 —— 所以「当时手里拿的是什么」只能由模组看。
 *
 * <p>注意 Fabric 的 PlayerBlockBreakEvents.AFTER 只给到 (level, player, pos, state, blockEntity)，
 * 没有工具参数，主手物品要自己取（此刻方块已破坏，但主手栈依然有效）。
 */
public final class BlockBreakHandler {

    private BlockBreakHandler() {
    }

    public static void afterBlockBreak(Level level, Player player, BlockPos pos,
                                       BlockState state, BlockEntity blockEntity) {
        if (!(level instanceof ServerLevel serverLevel)
                || !(player instanceof ServerPlayer serverPlayer)) {
            return;
        }
        if (!isInfested(state) || !hasSilkTouch(serverPlayer.getMainHandItem())) {
            return;
        }
        MinecraftServer server = serverLevel.getServer();
        if (server == null) {
            return;
        }
        Grant.to(server, serverPlayer, Grant.NS + ":interactions/pest_control");
    }

    /** 七种被虫蚀的方块。 */
    private static boolean isInfested(BlockState state) {
        Block block = state.getBlock();
        return block == Blocks.INFESTED_STONE
                || block == Blocks.INFESTED_COBBLESTONE
                || block == Blocks.INFESTED_STONE_BRICKS
                || block == Blocks.INFESTED_MOSSY_STONE_BRICKS
                || block == Blocks.INFESTED_CRACKED_STONE_BRICKS
                || block == Blocks.INFESTED_CHISELED_STONE_BRICKS
                || block == Blocks.INFESTED_DEEPSLATE;
    }

    private static boolean hasSilkTouch(ItemStack stack) {
        ItemEnchantments enchantments = stack.get(DataComponents.ENCHANTMENTS);
        if (enchantments == null) {
            return false;
        }
        for (Holder<Enchantment> enchantment : enchantments.keySet()) {
            if (enchantment.is(Enchantments.SILK_TOUCH)) {
                return true;
            }
        }
        return false;
    }
}
