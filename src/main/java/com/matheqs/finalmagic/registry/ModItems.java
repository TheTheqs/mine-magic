package com.matheqs.finalmagic.registry;

import com.matheqs.finalmagic.FinalMagic;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {

    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, FinalMagic.MODID);

    public static final RegistryObject<Item> WOODEN_WAND =
            ITEMS.register("wooden_wand",
                    () -> new Item(new Item.Properties().stacksTo(1)));
}