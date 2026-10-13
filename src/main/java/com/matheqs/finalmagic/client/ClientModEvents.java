package com.matheqs.finalmagic.client;

import com.matheqs.finalmagic.FinalMagic;
import com.matheqs.finalmagic.client.screen.InscriptionTableScreen;
import com.matheqs.finalmagic.registry.ModMenus;
import com.matheqs.finalmagic.registry.ModItems;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(
        modid = FinalMagic.MODID,
        bus = Mod.EventBusSubscriber.Bus.MOD,
        value = Dist.CLIENT)
public class ClientModEvents {

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() ->
                MenuScreens.register(
                        ModMenus.INSCRIPTION_TABLE.get(),
                        InscriptionTableScreen::new));
    }
    @SubscribeEvent
    public static void onRegisterItemColors(RegisterColorHandlersEvent.Item event) {
        event.register((stack, tintIndex) -> tintIndex == 0 ? 0xFFFFDADA : -1,
                ModItems.FIRE_SCROLL.get());
    }
    @SubscribeEvent
    public static void onRegisterKeyMappings(RegisterKeyMappingsEvent event) {
        event.register(ModKeyMappings.SPELL_CATALOG);
    }
}