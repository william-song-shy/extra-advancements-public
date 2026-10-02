package com.extraadvancements.mixin;

import com.extraadvancements.handler.NoteBlockHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.NoteBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * 一人乐队 —— 在音符盒真正发声的那一刻记一笔。
 *
 * <p>注入 {@code NoteBlock#playNote(Entity, BlockState, Level, BlockPos)}：它是 private 的
 * 实例方法，而且**只在确实会出声时才被调用**（方法内部先判"上方不是空气且音色不吃上方方块"
 * 就直接 return），三个调用者分别是
 * {@code neighborChanged}（红石上升沿，实体参数为 null）、{@code useWithoutItem}（右键调音）
 * 与 {@code attack}（左键）—— 所以红石驱动的"同时响起"也能覆盖到，这正是纯数据包抓不到的那条路。
 *
 * <p>参数里直接带着 {@code BlockState}，音色就是 {@code NoteBlock.INSTRUMENT}；位置也从
 * {@code BlockPos} 拿到，可听半径的判断留给数据包侧的 {@link NoteBlockHandler}。
 *
 * <p>与 {@code ShulkerDuplicateMixin} 一样，这是单点只读注入：不改方块、不改状态，
 * 只把"哪个音色在哪里响了一声"转交给数据包。
 */
@Mixin(NoteBlock.class)
public abstract class NoteBlockMixin {

    @Inject(
            method = "playNote(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;)V",
            at = @At("HEAD")
    )
    private void extraadv$onPlayNote(Entity source, BlockState state, Level level, BlockPos pos,
                                     CallbackInfo ci) {
        if (!(level instanceof net.minecraft.server.level.ServerLevel serverLevel)) {
            return;
        }
        NoteBlockHandler.onPlayNote(serverLevel, state, pos);
    }
}
