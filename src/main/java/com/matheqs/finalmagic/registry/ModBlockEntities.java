package com.matheqs.finalmagic.registry;

import com.matheqs.finalmagic.FinalMagic;
import com.matheqs.finalmagic.blockentity.InscriptionTableBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockEntities {

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(
                    ForgeRegistries.BLOCK_ENTITY_TYPES,
                    FinalMagic.MODID);

    public static final RegistryObject<
            BlockEntityType<InscriptionTableBlockEntity>> INSCRIPTION_TABLE =
            BLOCK_ENTITIES.register("inscription_table",
                    () -> BlockEntityType.Builder.of(
                            InscriptionTableBlockEntity::new,
                            ModBlocks.INSCRIPTION_TABLE.get()
                    ).build(null));
}