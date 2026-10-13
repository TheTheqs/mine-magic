package com.matheqs.finalmagic.registry;

import com.matheqs.finalmagic.FinalMagic;
import com.matheqs.finalmagic.menu.InscriptionTableMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModMenus {

    public static final DeferredRegister<MenuType<?>> MENUS =
            DeferredRegister.create(
                    ForgeRegistries.MENU_TYPES,
                    FinalMagic.MODID);

    public static final RegistryObject<
            MenuType<InscriptionTableMenu>> INSCRIPTION_TABLE =
            MENUS.register("inscription_table",
                    () -> IForgeMenuType.create(
                            (containerId, inventory, buffer) ->
                                    new InscriptionTableMenu(
                                            containerId,
                                            inventory,
                                            buffer.readBlockPos())));
}