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
import com.matheqs.finalmagic.registry.ModItems;
import net.minecraft.world.item.Items;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;
import com.matheqs.finalmagic.blockentity.InscriptionTableBlockEntity;
import com.matheqs.finalmagic.blockentity.InscriptionPatterns;
import net.minecraft.world.inventory.DataSlot;
import org.jetbrains.annotations.NotNull;

public class InscriptionTableMenu extends AbstractContainerMenu {

    public static final int CRAFT_BUTTON_ID = 9;
    private final ContainerLevelAccess access;
    private final InscriptionTableBlockEntity table;
    private final int[] syncedSymbols = new int[9];

    public InscriptionTableMenu(
            int containerId,
            Inventory playerInventory,
            BlockPos position) {

        this(
                containerId,
                playerInventory,
                position,
                null);
    }

    public InscriptionTableMenu(
            int containerId,
            Inventory playerInventory,
            BlockPos position,
            InscriptionTableBlockEntity table) {

        super(ModMenus.INSCRIPTION_TABLE.get(), containerId);

        this.table = table;

        ItemStackHandler tableInventory = table == null
                ? new ItemStackHandler(2)
                : table.getInventory();

        access = ContainerLevelAccess.create(
                playerInventory.player.level(), position);

        addSlot(new SlotItemHandler(tableInventory, 0, 80, 5) {
            @Override
            public boolean mayPlace(@NotNull ItemStack stack) {
                return stack.is(Items.PAPER);
            }
        });

        addSlot(new SlotItemHandler(tableInventory, 1, 80, 103) {
            @Override
            public boolean mayPlace(@NotNull ItemStack stack) {
                return stack.is(ModItems.CIPHER_COAL.get());
            }

            @Override
            public int getMaxStackSize() {
                return 1;
            }
        });

        addPlayerInventory(playerInventory);

        for (int i = 0; i < 9; i++) {
            final int cellIndex = i;

            addDataSlot(new DataSlot() {
                @Override
                public int get() {
                    return table == null
                            ? syncedSymbols[cellIndex]
                            : table.getSymbol(cellIndex);
                }

                @Override
                public void set(int value) {
                    syncedSymbols[cellIndex] = value;
                }
            });
        }
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

    public int getSymbol(int cellIndex) {
        return table == null
                ? syncedSymbols[cellIndex]
                : table.getSymbol(cellIndex);
    }

    @Override
    public boolean clickMenuButton(@NotNull Player player, int buttonId) {
        if (table == null || !stillValid(player) || !getCarried().isEmpty()) {
            return false;
        }

        if (player.level().getBlockEntity(table.getBlockPos()) != table) {
            return false;
        }

        if (buttonId == CRAFT_BUTTON_ID) {
            boolean crafted = table.craftScroll(player);
            if (crafted) broadcastChanges();
            return crafted;
        }
        return table.toggleSymbol(buttonId);
    }
    public boolean hasFirePreview() {
        return getSlot(0).getItem().is(Items.PAPER)
                && InscriptionPatterns.matchesFire(this::getSymbol);
    }

    public ItemStack getPaperPreview() {
        ItemStack paper = getSlot(0).getItem();
        return hasFirePreview()
                ? new ItemStack(ModItems.FIRE_SCROLL.get(), paper.getCount())
                : paper.copy();
    }
}