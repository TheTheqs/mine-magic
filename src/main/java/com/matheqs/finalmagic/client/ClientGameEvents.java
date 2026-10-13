package com.matheqs.finalmagic.client;

import com.matheqs.finalmagic.FinalMagic;
import com.matheqs.finalmagic.client.screen.SpellCatalogScreen;
import com.matheqs.finalmagic.item.WoodenWandItem;
import com.matheqs.finalmagic.network.CastFirePacket;
import com.matheqs.finalmagic.network.ModNetwork;
import com.matheqs.finalmagic.spell.WandSpellData;
import net.minecraftforge.client.event.InputEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.network.chat.Component;
import java.util.UUID;
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
    @SubscribeEvent
    public static void onInteraction(InputEvent.InteractionKeyMappingTriggered event) {
        Minecraft minecraft = Minecraft.getInstance();
        if (!event.isAttack() || minecraft.screen != null || minecraft.player == null
                || !(minecraft.player.getMainHandItem().getItem() instanceof WoodenWandItem)) return;
        event.setCanceled(true);
        event.setSwingHand(false);
        UUID wandId = WandSpellData.getWandId(minecraft.player.getMainHandItem());
        if (wandId == null) {
            minecraft.player.displayClientMessage(Component.translatable(
                    "message.finalmagic.selection.invalid_wand"), false);
            return;
        }
        ModNetwork.castFire(new CastFirePacket(InteractionHand.MAIN_HAND, wandId));
    }
}