package com.extraadvancements.handler;

import com.extraadvancements.Grant;
import net.minecraft.core.BlockPos;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * 一人乐队：在能听到的范围内，让所有音色的音符盒**同时**响起。
 *
 * <p>这件事纯数据包做不到，而且是从两头都做不到：
 * 一是**没有触发器**——右键音符盒走的是 {@code default_block_use}，可红石驱动的音符盒
 * （也就是真正能"同时"的那条路）不触发任何触发器；
 * 二是**扫不动**——可听半径 48 格是个 45 万方块量级的球，函数枚举不了自由搭建的现场。
 * 所以由模组在发声的源头记一笔：{@link NoteBlockMixin} 注入 {@code NoteBlock#playNote}
 * （private，红石 / 右键 / 左键三条路径都经过它），这里按 tick 收集每个玩家听到的音色，
 * 集齐 20 种就发奖。
 *
 * <p>几个照 26.1.2 字节码核实的数字：音符盒音量恒为 3.0、与音色无关，所以可听半径是
 * {@code 3 × 16 = 48} 格；{@code NoteBlockInstrument} 一共 27 个取值，这里要求其中
 * 26 个（16 个方块音色 + 铜的 4 个氧化态小号 + 6 个头颅音色），只排除生存里拿不到的
 * {@code custom_head}，理由见数据包 {@code tools/data_lists.py}。
 *
 * <p>数据包那边有一条无模组时的近似（亲手把每种音色敲一遍即可，不要求同一 tick），
 * 装了模组才是字面意义上的"同时响起"。
 */
public final class NoteBlockHandler {

    /** 可听半径：音量 3.0 × 16 格。 */
    private static final double HEARING_RANGE_SQR = 48.0 * 48.0;

    /** 要求的 26 种音色（只排除生存拿不到的 custom_head）。 */
    private static final Set<NoteBlockInstrument> REQUIRED = EnumSet.of(
            NoteBlockInstrument.HARP,
            NoteBlockInstrument.BASEDRUM,
            NoteBlockInstrument.SNARE,
            NoteBlockInstrument.HAT,
            NoteBlockInstrument.BASS,
            NoteBlockInstrument.FLUTE,
            NoteBlockInstrument.BELL,
            NoteBlockInstrument.GUITAR,
            NoteBlockInstrument.CHIME,
            NoteBlockInstrument.XYLOPHONE,
            NoteBlockInstrument.IRON_XYLOPHONE,
            NoteBlockInstrument.COW_BELL,
            NoteBlockInstrument.DIDGERIDOO,
            NoteBlockInstrument.BIT,
            NoteBlockInstrument.BANJO,
            NoteBlockInstrument.PLING,
            NoteBlockInstrument.TRUMPET,
            NoteBlockInstrument.TRUMPET_EXPOSED,
            NoteBlockInstrument.TRUMPET_WEATHERED,
            NoteBlockInstrument.TRUMPET_OXIDIZED,
            NoteBlockInstrument.ZOMBIE,
            NoteBlockInstrument.SKELETON,
            NoteBlockInstrument.CREEPER,
            NoteBlockInstrument.DRAGON,
            NoteBlockInstrument.WITHER_SKELETON,
            NoteBlockInstrument.PIGLIN
    );

    /** 本 tick 每个玩家听到的音色。tick 结束时清空——"同时"就是以 tick 为界的。 */
    private static final Map<UUID, Set<NoteBlockInstrument>> HEARD = new HashMap<>();

    private NoteBlockHandler() {
    }

    /** 由 {@link com.extraadvancements.mixin.NoteBlockMixin} 在音符盒真正发声时调用。 */
    public static void onPlayNote(ServerLevel level, BlockState state, BlockPos pos) {
        NoteBlockInstrument instrument;
        try {
            instrument = state.getValue(net.minecraft.world.level.block.NoteBlock.INSTRUMENT);
        } catch (IllegalArgumentException notANoteBlock) {
            return;
        }
        if (!REQUIRED.contains(instrument)) {
            return;
        }
        MinecraftServer server = level.getServer();
        if (server == null) {
            return;
        }
        for (ServerPlayer player : level.players()) {
            if (player.distanceToSqr(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5)
                    > HEARING_RANGE_SQR) {
                continue;       // 听不见就不算
            }
            Set<NoteBlockInstrument> heard = HEARD.computeIfAbsent(
                    player.getUUID(), key -> EnumSet.noneOf(NoteBlockInstrument.class));
            heard.add(instrument);
            if (heard.containsAll(REQUIRED)) {
                Grant.to(server, player, Grant.NS + ":silly/one_man_band");
            }
        }
    }

    /** 每个 tick 结束时清空——下一 tick 要重新集齐。 */
    public static void onEndTick(MinecraftServer server) {
        HEARD.clear();
    }
}
