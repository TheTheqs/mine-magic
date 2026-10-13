package com.matheqs.finalmagic.registry;

import com.matheqs.finalmagic.FinalMagic;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModSounds {

    public static final DeferredRegister<SoundEvent> SOUNDS =
            DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, FinalMagic.MODID);

    public static final RegistryObject<SoundEvent> INSCRIPTION_TABLE_DRAW =
            SOUNDS.register("block.inscription_table.draw",
                    () -> SoundEvent.createVariableRangeEvent(
                            ResourceLocation.fromNamespaceAndPath(
                                    FinalMagic.MODID, "block.inscription_table.draw")));
}