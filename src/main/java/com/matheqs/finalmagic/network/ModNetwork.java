package com.matheqs.finalmagic.network;

import com.matheqs.finalmagic.FinalMagic;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.ChannelBuilder;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.SimpleChannel;

public final class ModNetwork {
    private static final SimpleChannel CHANNEL = ChannelBuilder.named(
            ResourceLocation.fromNamespaceAndPath(FinalMagic.MODID, "main"))
            .networkProtocolVersion(1).simpleChannel();
    private ModNetwork() {}

    public static void register() {
        CHANNEL.messageBuilder(SelectSpellPacket.class, NetworkDirection.PLAY_TO_SERVER)
                .encoder(SelectSpellPacket::encode)
                .decoder(SelectSpellPacket::decode)
                .consumerMainThread(SelectSpellPacket::handle)
                .add();
        CHANNEL.messageBuilder(CastFirePacket.class, NetworkDirection.PLAY_TO_SERVER)
                .encoder(CastFirePacket::encode)
                .decoder(CastFirePacket::decode)
                .consumerMainThread(CastFirePacket::handle)
                .add();
        CHANNEL.build();
    }

    public static void selectSpell(SelectSpellPacket packet) {
        CHANNEL.send(packet, PacketDistributor.SERVER.noArg());
    }
    public static void castFire(CastFirePacket packet) {
        CHANNEL.send(packet, PacketDistributor.SERVER.noArg());
    }
}