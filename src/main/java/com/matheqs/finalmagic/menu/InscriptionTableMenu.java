package com.matheqs.finalmagic.menu;

import com.matheqs.finalmagic.registry.ModBlocks;
import com.matheqs.finalmagic.registry.ModMenus;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.inventory.Slot;
import org.jetbrains.annotations.NotNull;

public class InscriptionTableMenu extends AbstractContainerMenu {

    private final ContainerLevelAccess access;

    public InscriptionTableMenu(
            int containerId,
            Inventory playerInventory,
            BlockPos position) {

        super(ModMenus.INSCRIPTION_TABLE.get(), containerId);

        access = ContainerLevelAccess.create(
                playerInventory.player.level(), position);

        addPlayerInventory(playerInventory);
    }

    @Override
    public boolean stillValid(@NotNull Player player) {
        return stillValid(
                access,
                player,
                ModBlocks.INSCRIPTION_TABLE.get());
    }

    @Override
    public @NotNull ItemStack quickMoveStack(@NotNull Player player, int slotIndex) {
        return ItemStack.EMPTY;
    }

    private void addPlayerInventory(Inventory inventory) {
        int inventoryTop = 131;

        // Inventário principal: índices 9 a 35.
        for (int row = 0; row < 3; row++) {
            for (int column = 0; column < 9; column++) {
                int inventoryIndex = 9 + row * 9 + column;

                addSlot(new Slot(
                        inventory,
                        inventoryIndex,
                        8 + column * 18,
                        inventoryTop + 1 + row * 18));
            }
        }

        // Barra rápida: índices 0 a 8.
        for (int column = 0; column < 9; column++) {
            addSlot(new Slot(
                    inventory,
                    column,
                    8 + column * 18,
                    inventoryTop + 59));
        }
    }
}