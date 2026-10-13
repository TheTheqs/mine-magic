package com.matheqs.finalmagic.network;

import com.matheqs.finalmagic.spell.SpellAvailability;
import com.matheqs.finalmagic.spell.SpellCatalog;
import com.matheqs.finalmagic.spell.WandSpellData;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.network.CustomPayloadEvent;

import java.util.UUID;

public record SelectSpellPacket(InteractionHand hand, UUID wandId,
                                ResourceLocation spellId) {
    public static void encode(SelectSpellPacket packet, FriendlyByteBuf buffer) {
        buffer.writeEnum(packet.hand());
        buffer.writeUUID(packet.wandId());
        buffer.writeResourceLocation(packet.spellId());
    }

    public static SelectSpellPacket decode(FriendlyByteBuf buffer) {
        return new SelectSpellPacket(buffer.readEnum(InteractionHand.class),
                buffer.readUUID(), buffer.readResourceLocation());
    }

    public static void handle(SelectSpellPacket packet, CustomPayloadEvent.Context context) {
        ServerPlayer player = context.getSender();
        if (player == null) return;
        ItemStack wand = player.getItemInHand(packet.hand());
        if (!packet.wandId().equals(WandSpellData.getWandId(wand))
                || !WandSpellData.isActiveFor(player, wand)) {
            player.sendSystemMessage(Component.translatable(
                    "message.finalmagic.selection.invalid_wand"));
            return;
        }
        // Only recognized, currently available spells can be selected.
        if (!packet.spellId().equals(SpellCatalog.FIRE.id())
                || !SpellAvailability.isFireAvailable(player)) {
            player.sendSystemMessage(Component.translatable(
                    "message.finalmagic.selection.unavailable"));
            return;
        }
        WandSpellData.setCurrentSpell(wand, packet.spellId());
        player.inventoryMenu.broadcastChanges();
        if (player.containerMenu != player.inventoryMenu) {
            player.containerMenu.broadcastChanges();
        }
        player.sendSystemMessage(Component.translatable(
                "message.finalmagic.selection.selected",
                Component.translatable(SpellCatalog.FIRE.nameKey())));
    }
}