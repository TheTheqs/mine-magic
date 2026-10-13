package com.matheqs.finalmagic.registry;

import com.matheqs.finalmagic.FinalMagic;
import com.matheqs.finalmagic.item.WoodenWandItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.minecraft.world.item.BlockItem;

public class ModItems {

    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, FinalMagic.MODID);

    public static final RegistryObject<Item> WOODEN_WAND =
            ITEMS.register("wooden_wand",
                    () -> new WoodenWandItem(new Item.Properties().stacksTo(1)));

    public static final RegistryObject<Item> INSCRIPTION_TABLE =
            ITEMS.register("inscription_table",
                    () -> new BlockItem(
                            ModBlocks.INSCRIPTION_TABLE.get(),
                            new Item.Properties()));

    public static final RegistryObject<Item> CIPHER_COAL =
            ITEMS.register("cipher_coal",
                    () -> new Item(new Item.Properties().durability(20)));
}