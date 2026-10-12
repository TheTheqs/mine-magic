package com.matheqs.finalmagic.binding;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.saveddata.SavedData;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class WandBindingSavedData extends SavedData {

    private static final String DATA_NAME = "finalmagic_wand_bindings";

    private static final SavedData.Factory<WandBindingSavedData> FACTORY =
            new SavedData.Factory<>(
                    WandBindingSavedData::new,
                    WandBindingSavedData::load,
                    null);

    private final Map<UUID, WandBinding> bindings = new HashMap<>();

    public static WandBindingSavedData get(MinecraftServer server) {
        return server.overworld().getDataStorage()
                .computeIfAbsent(FACTORY, DATA_NAME);
    }

    public boolean canBind(UUID playerId, UUID wandId) {
        WandBinding binding = bindings.get(playerId);

        return binding == null
                ? wandId != null
                : binding.canBind(wandId);
    }

    public boolean isActiveWand(UUID playerId, UUID wandId) {
        WandBinding binding = bindings.get(playerId);

        return binding != null && binding.isActiveWand(wandId);
    }

    public void bind(UUID playerId, UUID wandId) {
        if (!canBind(playerId, wandId)) {
            throw new IllegalStateException(
                    "Cannot establish this wand binding.");
        }

        WandBinding binding = bindings.computeIfAbsent(
                playerId, id -> new WandBinding());

        if (!binding.isActiveWand(wandId)) {
            binding.bind(wandId);
            setDirty();
        }
    }

    public void unlockBinding(UUID playerId) {
        WandBinding binding = bindings.get(playerId);

        if (binding != null && binding.isBindingLocked()) {
            binding.unlockBinding();
            setDirty();
        }
    }

    private static WandBindingSavedData load(
            CompoundTag tag,
            HolderLookup.Provider registries) {

        WandBindingSavedData savedData = new WandBindingSavedData();
        ListTag entries = tag.getList("bindings", Tag.TAG_COMPOUND);

        for (int i = 0; i < entries.size(); i++) {
            CompoundTag entry = entries.getCompound(i);

            if (!entry.hasUUID("player_uuid")) {
                continue;
            }

            UUID playerId = entry.getUUID("player_uuid");

            UUID wandId = entry.hasUUID("active_wand_uuid")
                    ? entry.getUUID("active_wand_uuid")
                    : null;

            boolean locked = entry.getBoolean("binding_locked");

            savedData.bindings.put(
                    playerId, new WandBinding(wandId, locked));
        }

        return savedData;
    }

    @Override
    public CompoundTag save(
            CompoundTag tag,
            HolderLookup.Provider registries) {

        ListTag entries = new ListTag();

        for (Map.Entry<UUID, WandBinding> mapEntry : bindings.entrySet()) {
            CompoundTag entry = new CompoundTag();
            WandBinding binding = mapEntry.getValue();

            entry.putUUID("player_uuid", mapEntry.getKey());

            if (binding.getActiveWandId() != null) {
                entry.putUUID(
                        "active_wand_uuid", binding.getActiveWandId());
            }

            entry.putBoolean(
                    "binding_locked", binding.isBindingLocked());

            entries.add(entry);
        }

        tag.put("bindings", entries);
        return tag;
    }
}