package com.matheqs.finalmagic.block;

import com.matheqs.finalmagic.blockentity.InscriptionTableBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import com.matheqs.finalmagic.menu.InscriptionTableMenu;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;

public class InscriptionTableBlock extends Block implements EntityBlock {

    public InscriptionTableBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    public BlockEntity newBlockEntity(
            @NotNull BlockPos position,
            @NotNull BlockState state) {

        return new InscriptionTableBlockEntity(position, state);
    }

    @Override
    protected @NotNull InteractionResult useWithoutItem(
            @NotNull BlockState state,
            @NotNull Level level,
            @NotNull BlockPos position,
            @NotNull Player player,
            @NotNull BlockHitResult hitResult) {

        if (player instanceof ServerPlayer serverPlayer) {
            SimpleMenuProvider provider = new SimpleMenuProvider(
                    (containerId, inventory, menuPlayer) ->
                            new InscriptionTableMenu(
                                    containerId,
                                    inventory,
                                    position),
                    Component.translatable(
                            "block.finalmagic.inscription_table"));

            serverPlayer.openMenu(provider, position);
        }

        return InteractionResult.sidedSuccess(level.isClientSide());
    }
}