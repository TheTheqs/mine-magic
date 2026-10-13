package com.matheqs.finalmagic.client;

import com.matheqs.finalmagic.FinalMagic;
import com.matheqs.finalmagic.client.screen.SpellCatalogScreen;
import com.matheqs.finalmagic.item.WoodenWandItem;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = FinalMagic.MODID,
        bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class ClientGameEvents {
    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent.Post event) {
        Minecraft minecraft = Minecraft.getInstance();
        while (ModKeyMappings.SPELL_CATALOG.consumeClick()) {
            if (minecraft.player != null && minecraft.screen == null
                    && (minecraft.player.getMainHandItem().getItem() instanceof WoodenWandItem
                    || minecraft.player.getOffhandItem().getItem() instanceof WoodenWandItem)) {
                minecraft.setScreen(new SpellCatalogScreen());
            }
        }
    }
}