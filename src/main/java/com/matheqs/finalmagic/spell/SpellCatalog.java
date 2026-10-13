package com.matheqs.finalmagic.spell;

import com.matheqs.finalmagic.FinalMagic;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

import java.util.List;

public final class SpellCatalog {
    private SpellCatalog() {}

    public enum School {
        CONJURATION("conjuration"), TRANSMUTATION("transmutation"),
        SUMMONING("summoning"), MANIPULATION("manipulation");

        private final String key;
        School(String key) { this.key = key; }
        public String translationKey() { return "school.finalmagic." + key; }
    }

    public record Entry(ResourceLocation id, String nameKey, School school,
                        int tier, ResourceLocation icon) {}

    public static final Entry FIRE = new Entry(
            ResourceLocation.fromNamespaceAndPath(FinalMagic.MODID, "fire"),
            "spell.finalmagic.fire", School.CONJURATION, 1,
            ResourceLocation.fromNamespaceAndPath(FinalMagic.MODID,
                    "textures/gui/spells/fire.png"));

    public static List<Entry> availableFor(Player player) {
        return SpellAvailability.isFireAvailable(player) ? List.of(FIRE) : List.of();
    }
}