package com.matheqs.finalmagic;

import com.mojang.logging.LogUtils;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;
import com.matheqs.finalmagic.registry.ModItems;
import com.matheqs.finalmagic.registry.ModBlocks;
import com.matheqs.finalmagic.registry.ModBlockEntities;
import com.matheqs.finalmagic.registry.ModMenus;
import com.matheqs.finalmagic.registry.ModSounds;

@Mod(FinalMagic.MODID)
public class FinalMagic {

    public static final String MODID = "finalmagic";

    private static final Logger LOGGER = LogUtils.getLogger();

    public FinalMagic(FMLJavaModLoadingContext context) {
        ModBlocks.BLOCKS.register(context.getModEventBus());
        ModBlockEntities.BLOCK_ENTITIES.register(context.getModEventBus());
        ModItems.ITEMS.register(context.getModEventBus());
        ModMenus.MENUS.register(context.getModEventBus());
        ModSounds.SOUNDS.register(context.getModEventBus());

        LOGGER.info("FinalMagic inicializado.");
    }
}