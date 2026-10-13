package com.matheqs.finalmagic.blockentity;

import com.matheqs.finalmagic.registry.ModBlockEntities;
import com.matheqs.finalmagic.registry.ModSounds;
import net.minecraft.sounds.SoundSource;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.items.ItemStackHandler;
import java.util.Arrays;
import com.matheqs.finalmagic.registry.ModItems;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

public class InscriptionTableBlockEntity extends BlockEntity {

    public static final int SYMBOL_EMPTY = 0;
    public static final int SYMBOL_FIRE = 1;

    private final int[] symbols = new int[9];
    private int previousPaperCount;

    private final ItemStackHandler inventory = new ItemStackHandler(2) {
        @Override
        protected void onContentsChanged(int slot) {
            if (slot == 0) {
                int paperCount = getStackInSlot(0).is(Items.PAPER)
                        ? getStackInSlot(0).getCount() : 0;

                // Any actual withdrawal resets the drawing, even from a stack.
                if (paperCount < previousPaperCount
                        && level != null && !level.isClientSide()) {
                    clearSymbols();
                }
                previousPaperCount = paperCount;
            }
            InscriptionTableBlockEntity.this.setChanged();
        }

        @Override
        protected void onLoad() {
            // Loading saved inventory must not erase the saved drawing.
            previousPaperCount = getStackInSlot(0).is(Items.PAPER)
                    ? getStackInSlot(0).getCount() : 0;
        }
    };

    public InscriptionTableBlockEntity(
            BlockPos position,
            BlockState state) {

        super(
                ModBlockEntities.INSCRIPTION_TABLE.get(),
                position,
                state);
    }

    public ItemStackHandler getInventory() {
        return inventory;
    }

    @Override
    protected void saveAdditional(
            @NotNull CompoundTag tag,
            HolderLookup.@NotNull Provider registries) {

        super.saveAdditional(tag, registries);
        tag.put("inventory", inventory.serializeNBT(registries));
        tag.putIntArray("symbols", symbols);
    }

    @Override
    protected void loadAdditional(
            @NotNull CompoundTag tag,
            HolderLookup.@NotNull Provider registries) {

        super.loadAdditional(tag, registries);

        inventory.deserializeNBT(
                registries, tag.getCompound("inventory"));

        Arrays.fill(symbols, SYMBOL_EMPTY);

        int[] savedSymbols = tag.getIntArray("symbols");

        for (int i = 0; i < Math.min(savedSymbols.length, symbols.length); i++) {
            if (savedSymbols[i] == SYMBOL_FIRE) {
                symbols[i] = SYMBOL_FIRE;
            }
        }
    }

    public int getSymbol(int cellIndex) {
        checkCellIndex(cellIndex);
        return symbols[cellIndex];
    }

    public void setSymbol(int cellIndex, int symbol) {
        checkCellIndex(cellIndex);

        if (symbol != SYMBOL_EMPTY && symbol != SYMBOL_FIRE) {
            throw new IllegalArgumentException("Unknown symbol: " + symbol);
        }

        if (symbols[cellIndex] != symbol) {
            symbols[cellIndex] = symbol;
            setChanged();
        }
    }

    private void checkCellIndex(int cellIndex) {
        if (cellIndex < 0 || cellIndex >= symbols.length) {
            throw new IllegalArgumentException(
                    "Invalid cell index: " + cellIndex);
        }
    }

    public void clearSymbols() {
        Arrays.fill(symbols, SYMBOL_EMPTY);
        setChanged();
    }

    private void playDrawingSound() {
        if (level != null && !level.isClientSide()) {
            level.playSound(
                    null,
                    worldPosition,
                    ModSounds.INSCRIPTION_TABLE_DRAW.get(),
                    SoundSource.BLOCKS,
                    0.6F,
                    1.0F);
        }
    }

    public boolean toggleSymbol(int cellIndex) {
        if (level == null || level.isClientSide()) {
            return false;
        }

        if (cellIndex < 0 || cellIndex >= symbols.length) {
            return false;
        }

        if (!inventory.getStackInSlot(0).is(Items.PAPER)) {
            return false;
        }

        if (getSymbol(cellIndex) == SYMBOL_FIRE) {
            setSymbol(cellIndex, SYMBOL_EMPTY);
            playDrawingSound();
            return true;
        }

        ItemStack coal = inventory.getStackInSlot(1);

        if (!coal.is(ModItems.CIPHER_COAL.get())
                || coal.getDamageValue() >= coal.getMaxDamage()) {
            return false;
        }

        int newDamage = coal.getDamageValue() + 1;

        if (newDamage >= coal.getMaxDamage()) {
            inventory.setStackInSlot(1, ItemStack.EMPTY);
        } else {
            coal.setDamageValue(newDamage);
        }

        setSymbol(cellIndex, SYMBOL_FIRE);
        playDrawingSound();
        return true;
    }
    public boolean hasFirePattern() {
        return InscriptionPatterns.matchesFire(this::getSymbol);
    }

    public boolean craftScroll(Player player) {
        if (level == null || level.isClientSide()
                || !inventory.getStackInSlot(0).is(Items.PAPER)) {
            return false;
        }

        ItemStack result = new ItemStack(
                hasFirePattern() ? ModItems.FIRE_SCROLL.get() : Items.PAPER);
        Inventory playerInventory = player.getInventory();
        int destination = -1;

        // Only the main inventory and hotbar are delivery destinations.
        for (int slot = 0; slot < playerInventory.items.size(); slot++) {
            ItemStack current = playerInventory.getItem(slot);
            if (current.isEmpty()
                    || (ItemStack.isSameItemSameComponents(current, result)
                    && current.getCount() < Math.min(
                            current.getMaxStackSize(), playerInventory.getMaxStackSize()))) {
                destination = slot;
                break;
            }
        }

        if (destination == -1) {
            player.sendSystemMessage(Component.translatable(
                    "message.finalmagic.inscription.inventory_full"));
            return false;
        }

        // Validate space before consuming; withdrawal also clears the pattern.
        ItemStack consumed = inventory.extractItem(0, 1, false);
        if (consumed.isEmpty()) {
            return false;
        }

        ItemStack current = playerInventory.getItem(destination);
        if (current.isEmpty()) {
            playerInventory.setItem(destination, result);
        } else {
            current.grow(1);
        }
        playerInventory.setChanged();
        clearSymbols();
        return true;
    }
}