package com.matheqs.finalmagic;

import com.mojang.logging.LogUtils;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;
import com.matheqs.finalmagic.registry.ModItems;

@Mod(FinalMagic.MODID)
public class FinalMagic {

    public static final String MODID = "finalmagic";

    private static final Logger LOGGER = LogUtils.getLogger();

    public FinalMagic(FMLJavaModLoadingContext context) {
        ModItems.ITEMS.register(context.getModEventBus());

        LOGGER.info("FinalMagic inicializado.");
    }
}