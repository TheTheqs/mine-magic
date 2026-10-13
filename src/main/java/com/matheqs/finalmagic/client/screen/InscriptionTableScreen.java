package com.matheqs.finalmagic.client.screen;

import com.matheqs.finalmagic.FinalMagic;
import com.matheqs.finalmagic.menu.InscriptionTableMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;

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
}