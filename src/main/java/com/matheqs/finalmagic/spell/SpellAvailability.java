package com.matheqs.finalmagic.spell;

import com.matheqs.finalmagic.registry.ModItems;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;

public final class SpellAvailability {
    private SpellAvailability() {}

    public static boolean isFireAvailable(Player player) {
        Inventory inventory = player.getInventory();
        for (int slot = 0; slot < inventory.getContainerSize(); slot++) {
            if (inventory.getItem(slot).is(ModItems.FIRE_SCROLL.get())) {
                return true;
            }
        }
        return false;
    }
}