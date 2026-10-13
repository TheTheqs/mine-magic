package com.matheqs.finalmagic.client.screen;

import com.matheqs.finalmagic.FinalMagic;
import com.matheqs.finalmagic.blockentity.InscriptionTableBlockEntity;
import com.matheqs.finalmagic.menu.InscriptionTableMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import java.util.List;
import java.util.Optional;

public class InscriptionTableScreen
        extends AbstractContainerScreen<InscriptionTableMenu> {

    private static final ResourceLocation PANEL_TEXTURE =
            ResourceLocation.fromNamespaceAndPath(
                    FinalMagic.MODID,
                    "textures/gui/inscription_table.png");

    private static final ResourceLocation INVENTORY_TEXTURE =
            ResourceLocation.fromNamespaceAndPath(
                    "minecraft",
                    "textures/gui/container/crafting_table.png");

    private static final ResourceLocation FIRE_TEXTURE =
            ResourceLocation.fromNamespaceAndPath(
                    FinalMagic.MODID,
                    "textures/gui/symbols/fire_cipher.png");

    private static final int[] CELL_POSITIONS = {30, 53, 75};
    private static final int CELL_SIZE = 18;

    private static final int PANEL_SIZE = 123;
    private static final int INVENTORY_TOP = 131;

    public InscriptionTableScreen(
            InscriptionTableMenu menu,
            Inventory playerInventory,
            Component title) {

        super(menu, playerInventory, title);

        imageWidth = 176;
        imageHeight = 214;
    }

    @Override
    protected void renderBg(
            GuiGraphics graphics,
            float partialTick,
            int mouseX,
            int mouseY) {

        int panelLeft = leftPos + (imageWidth - PANEL_SIZE) / 2;

        graphics.pose().pushPose();
        graphics.pose().translate(panelLeft, topPos, 0);
        graphics.pose().scale(0.5F, 0.5F, 1.0F);

        graphics.blit(
                PANEL_TEXTURE,
                0,
                0,
                0,
                0,
                246,
                246,
                246,
                246);

        graphics.pose().popPose();

        graphics.blit(
                INVENTORY_TEXTURE,
                leftPos,
                topPos + INVENTORY_TOP,
                0,
                83,
                176,
                83,
                256,
                256);

        for (int cell = 0; cell < 9; cell++) {
            if (menu.getSymbol(cell) == InscriptionTableBlockEntity.SYMBOL_FIRE) {
                int column = cell % 3;
                int row = cell / 3;

                graphics.blit(
                        FIRE_TEXTURE,
                        panelLeft + CELL_POSITIONS[column] + 1,
                        topPos + CELL_POSITIONS[row] + 1,
                        0, 0, 16, 16, 16, 16);
            }
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        boolean paperHovered = mouseX >= leftPos + 80 && mouseX < leftPos + 96
                && mouseY >= topPos + 5 && mouseY < topPos + 21;
        if ((button == 0 || button == 1) && paperHovered && menu.hasFirePreview()) {
            if (menu.getCarried().isEmpty()) {
                requestCraft();
            }
            return true;
        }

        if (button == 0 && menu.getCarried().isEmpty()) {
            int panelLeft = leftPos + (imageWidth - PANEL_SIZE) / 2;

            for (int cell = 0; cell < 9; cell++) {
                int column = cell % 3;
                int row = cell / 3;
                int x = panelLeft + CELL_POSITIONS[column];
                int y = topPos + CELL_POSITIONS[row];

                if (mouseX >= x && mouseX < x + CELL_SIZE
                        && mouseY >= y && mouseY < y + CELL_SIZE) {
                    if (minecraft != null && minecraft.gameMode != null) {
                        minecraft.gameMode.handleInventoryButtonClick(
                                menu.containerId, cell);
                    }
                    return true;
                }
            }
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    protected void renderLabels(
            @NotNull GuiGraphics graphics,
            int mouseX,
            int mouseY) {
    }

    @Override
    public void render(
            @NotNull GuiGraphics graphics,
            int mouseX,
            int mouseY,
            float partialTick) {

        renderBackground(graphics, mouseX, mouseY, partialTick);
        super.render(graphics, mouseX, mouseY, partialTick);
        renderTooltip(graphics, mouseX, mouseY);
    }
private void requestCraft() {
        if (menu.getCarried().isEmpty()
                && minecraft != null && minecraft.gameMode != null) {
            minecraft.gameMode.handleInventoryButtonClick(
                    menu.containerId, InscriptionTableMenu.CRAFT_BUTTON_ID);
        }
    }

    @Override
    protected void renderSlot(GuiGraphics graphics, Slot slot) {
        if (slot.index == 0 && menu.hasFirePreview()) {
            ItemStack preview = menu.getPaperPreview();
            graphics.renderItem(preview, slot.x, slot.y);
            graphics.renderItemDecorations(font, preview, slot.x, slot.y);
        } else {
            super.renderSlot(graphics, slot);
        }
    }

    @Override
    protected void renderTooltip(GuiGraphics graphics, int mouseX, int mouseY) {
        if (menu.getCarried().isEmpty() && hoveredSlot != null
                && hoveredSlot.index == 0 && hoveredSlot.hasItem()) {
            ItemStack preview = menu.getPaperPreview();
            Component status = Component.translatable(menu.hasFirePreview()
                    ? "gui.finalmagic.inscription.valid"
                    : "gui.finalmagic.inscription.invalid");
            graphics.renderTooltip(font,
                    List.of(preview.getHoverName(), status),
                    Optional.empty(), mouseX, mouseY);
        } else {
            super.renderTooltip(graphics, mouseX, mouseY);
        }
    }
}