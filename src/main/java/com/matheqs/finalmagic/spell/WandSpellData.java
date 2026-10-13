package com.matheqs.finalmagic.spell;

import com.matheqs.finalmagic.binding.WandBindingSavedData;
import com.matheqs.finalmagic.item.WoodenWandItem;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;

import java.util.UUID;

public final class WandSpellData {
    private static final String CURRENT_SPELL = "finalmagic_current_spell";
    private WandSpellData() {}

    private static CompoundTag data(ItemStack wand) {
        return wand.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
    }

    public static UUID getWandId(ItemStack wand) {
        CompoundTag data = data(wand);
        return data.hasUUID("finalmagic_wand_uuid")
                ? data.getUUID("finalmagic_wand_uuid") : null;
    }

    public static ResourceLocation getCurrentSpell(ItemStack wand) {
        return ResourceLocation.tryParse(data(wand).getString(CURRENT_SPELL));
    }

    public static boolean isActiveFor(ServerPlayer player, ItemStack wand) {
        if (!(wand.getItem() instanceof WoodenWandItem)) return false;
        CompoundTag data = data(wand);
        UUID wandId = getWandId(wand);
        return wandId != null && data.hasUUID("finalmagic_owner_uuid")
                && player.getUUID().equals(data.getUUID("finalmagic_owner_uuid"))
                && WandBindingSavedData.get(player.serverLevel().getServer())
                .isActiveWand(player.getUUID(), wandId);
    }

    public static void setCurrentSpell(ItemStack wand, ResourceLocation spellId) {
        CustomData.update(DataComponents.CUSTOM_DATA, wand,
                tag -> tag.putString(CURRENT_SPELL, spellId.toString()));
    }
}