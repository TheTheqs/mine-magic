package com.matheqs.finalmagic.client.screen;

import com.matheqs.finalmagic.client.ModKeyMappings;
import com.matheqs.finalmagic.registry.ModItems;
import com.matheqs.finalmagic.spell.SpellCatalog;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class SpellCatalogScreen extends Screen {
    private static final int PANEL_WIDTH = 280;
    private int panelHeight;
    private int visibleRows;
    private static final int COLUMNS = 4;
    private static final int CARD_WIDTH = 60;
    private static final int CARD_HEIGHT = 62;
    private final Map<SpellCatalog.School, Button> tabs =
            new EnumMap<>(SpellCatalog.School.class);
    private SpellCatalog.School school = SpellCatalog.School.CONJURATION;
    private int firstRow;
    private int left;
    private int top;

    public SpellCatalogScreen() {
        super(Component.translatable("gui.finalmagic.spells.title"));
    }

    @Override
    protected void init() {
        visibleRows = Math.max(1, Math.min(2, (entries().size() + COLUMNS - 1) / COLUMNS));
        panelHeight = 104 + visibleRows * 68;
        left = (width - PANEL_WIDTH) / 2;
        top = (height - panelHeight) / 2;
        tabs.clear();
        SpellCatalog.School[] schools = SpellCatalog.School.values();
        for (int i = 0; i < schools.length; i++) {
            SpellCatalog.School tabSchool = schools[i];
            Button tab = addRenderableWidget(Button.builder(
                    Component.translatable(tabSchool.translationKey()), button -> {
                        school = tabSchool;
                        firstRow = 0;
                        rebuildWidgets();
                    }).bounds(left + 10 + (i % 2) * 132, top + 28 + (i / 2) * 22, 128, 20).build());
            tabs.put(tabSchool, tab);
        }
        updateTabs();
    }

    private void updateTabs() {
        tabs.forEach((tabSchool, button) -> button.active = tabSchool != school);
    }

    private List<SpellCatalog.Entry> entries() {
        if (minecraft == null || minecraft.player == null) return List.of();
        List<SpellCatalog.Entry> result = new ArrayList<>();
        for (SpellCatalog.Entry entry : SpellCatalog.availableFor(minecraft.player)) {
            if (entry.school() == school) result.add(entry);
        }
        result.sort(Comparator.comparing(
                entry -> Component.translatable(entry.nameKey()).getString()));
        return result;
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        // Screen.render draws the blurred background and widgets before our foreground.
        super.render(graphics, mouseX, mouseY, partialTick);
        graphics.fill(left - 2, top - 2, left + PANEL_WIDTH + 2,
                top + panelHeight + 2, 0xFFB58B57);
        graphics.fill(left, top, left + PANEL_WIDTH, top + panelHeight, 0xFF30251E);
        graphics.drawCenteredString(font, title, left + PANEL_WIDTH / 2,
                top + 12, 0xFFF4DFC1);

        List<SpellCatalog.Entry> available = entries();
        int rows = (available.size() + COLUMNS - 1) / COLUMNS;
        firstRow = Math.min(firstRow, Math.max(0, rows - visibleRows));
        SpellCatalog.Entry hovered = null;
        for (int visible = 0; visible < COLUMNS * visibleRows; visible++) {
            int index = firstRow * COLUMNS + visible;
            if (index >= available.size()) break;
            SpellCatalog.Entry entry = available.get(index);
            int x = left + 12 + (visible % COLUMNS) * 64;
            int y = top + 76 + (visible / COLUMNS) * 68;
            boolean over = mouseX >= x && mouseX < x + CARD_WIDTH
                    && mouseY >= y && mouseY < y + CARD_HEIGHT;
            graphics.fill(x, y, x + CARD_WIDTH, y + CARD_HEIGHT,
                    over ? 0xFF725139 : 0xFF4B382B);
            if (minecraft != null
                    && minecraft.getResourceManager().getResource(entry.icon()).isPresent()) {
                graphics.blit(entry.icon(), x + 14, y + 4, 0, 0, 32, 32, 32, 32);
            } else {
                graphics.renderItem(new ItemStack(ModItems.FIRE_SCROLL.get()),
                        x + 22, y + 12);
            }
            graphics.drawCenteredString(font, Component.translatable(entry.nameKey()),
                    x + CARD_WIDTH / 2, y + 40, 0xFFFFFFFF);
            graphics.drawCenteredString(font,
                    Component.translatable("gui.finalmagic.spells.tier", entry.tier()),
                    x + CARD_WIDTH / 2, y + 51, 0xFFD6BB94);
            if (over) hovered = entry;
        }
        if (available.isEmpty()) {
            graphics.drawCenteredString(font,
                    Component.translatable("gui.finalmagic.spells.empty"),
                    left + PANEL_WIDTH / 2, top + 102, 0xFFD6BB94);
        }
        graphics.drawCenteredString(font,
                Component.translatable("gui.finalmagic.spells.count", available.size()),
                left + PANEL_WIDTH / 2, top + panelHeight - 24, 0xFFD6BB94);
        graphics.drawCenteredString(font,
                Component.translatable("gui.finalmagic.spells.close"),
                left + PANEL_WIDTH / 2, top + panelHeight - 11, 0xFFAA937B);
        for (Button tab : tabs.values()) {
            tab.render(graphics, mouseX, mouseY, partialTick);
        }
        if (hovered != null) {
            graphics.renderTooltip(font,
                    Component.translatable("gui.finalmagic.spells.available"), mouseX, mouseY);
        }
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY,
                                 double horizontal, double vertical) {
        int rows = (entries().size() + COLUMNS - 1) / COLUMNS;
        firstRow = Math.max(0, Math.min(Math.max(0, rows - visibleRows),
                firstRow + (vertical < 0 ? 1 : vertical > 0 ? -1 : 0)));
        return true;
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (ModKeyMappings.SPELL_CATALOG.matches(keyCode, scanCode)) {
            onClose();
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean isPauseScreen() { return false; }
}