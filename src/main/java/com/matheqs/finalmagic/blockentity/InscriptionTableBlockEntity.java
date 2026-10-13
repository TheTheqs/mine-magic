package com.matheqs.finalmagic.blockentity;

import com.matheqs.finalmagic.registry.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class InscriptionTableBlockEntity extends BlockEntity {

    public InscriptionTableBlockEntity(
            BlockPos position,
            BlockState state) {

        super(
                ModBlockEntities.INSCRIPTION_TABLE.get(),
                position,
                state);
    }
}