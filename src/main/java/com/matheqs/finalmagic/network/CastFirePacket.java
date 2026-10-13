package com.matheqs.finalmagic.network;

import com.matheqs.finalmagic.entity.VisualFireballEntity;
import com.matheqs.finalmagic.entity.CastBurstEntity;
import com.matheqs.finalmagic.spell.CastOrigin;
import com.matheqs.finalmagic.registry.ModEntities;
import com.matheqs.finalmagic.spell.SpellAvailability;
import com.matheqs.finalmagic.spell.SpellCatalog;
import com.matheqs.finalmagic.spell.WandSpellData;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.network.CustomPayloadEvent;

import java.util.UUID;

public record CastFirePacket(InteractionHand hand, UUID wandId) {
    public static void encode(CastFirePacket packet, FriendlyByteBuf buffer) {
        buffer.writeEnum(packet.hand());
        buffer.writeUUID(packet.wandId());
    }

    public static CastFirePacket decode(FriendlyByteBuf buffer) {
        return new CastFirePacket(buffer.readEnum(InteractionHand.class), buffer.readUUID());
    }

    public static void handle(CastFirePacket packet, CustomPayloadEvent.Context context) {
        ServerPlayer player = context.getSender();
        if (player == null) return;
        ItemStack wand = player.getItemInHand(packet.hand());
        if (!packet.wandId().equals(WandSpellData.getWandId(wand))
                || !WandSpellData.isActiveFor(player, wand)) {
            player.sendSystemMessage(Component.translatable(
                    "message.finalmagic.selection.invalid_wand"));
            return;
        }
        if (!SpellCatalog.FIRE.id().equals(WandSpellData.getCurrentSpell(wand))) {
            player.sendSystemMessage(Component.translatable("message.finalmagic.cast.no_spell"));
            return;
        }
        if (!SpellAvailability.isFireAvailable(player)) {
            player.sendSystemMessage(Component.translatable("message.finalmagic.fire_scroll_missing"));
            return;
        }
        if (player.getCooldowns().isOnCooldown(wand.getItem())) return;
        VisualFireballEntity fireball = ModEntities.VISUAL_FIREBALL.get()
                .create(player.serverLevel());
        if (fireball == null) return;
        Vec3 direction = player.getLookAngle().normalize();
        Vec3 origin = CastOrigin.position(player, packet.hand(), 1.0F);
        fireball.setPos(origin.x, origin.y - fireball.getBbHeight() / 2, origin.z);
        fireball.setDeltaMovement(direction.scale(1.0));
        if (player.serverLevel().addFreshEntity(fireball)) {
            CastBurstEntity burst = ModEntities.CAST_BURST.get().create(player.serverLevel());
            if (burst != null) {
                burst.attachTo(player, packet.hand());
                player.serverLevel().addFreshEntity(burst);
            }
            player.getCooldowns().addCooldown(wand.getItem(), 20);
            player.swing(packet.hand(), true);
        }
    }
}