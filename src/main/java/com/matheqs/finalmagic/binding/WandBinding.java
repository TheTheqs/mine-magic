package com.matheqs.finalmagic.binding;

import java.util.UUID;

public class WandBinding {

    private UUID activeWandId;
    private boolean bindingLocked;

    public WandBinding() {
        this.activeWandId = null;
        this.bindingLocked = false;
    }

    public WandBinding(UUID activeWandId, boolean bindingLocked) {
        this.activeWandId = activeWandId;
        this.bindingLocked = bindingLocked;
    }

    public UUID getActiveWandId() {
        return activeWandId;
    }

    public boolean isBindingLocked() {
        return bindingLocked;
    }

    public boolean isActiveWand(UUID wandId) {
        return wandId != null && wandId.equals(activeWandId);
    }

    public boolean canBind(UUID wandId) {
        return wandId != null
                && (isActiveWand(wandId) || !bindingLocked);
    }

    public void bind(UUID wandId) {
        if (!canBind(wandId)) {
            throw new IllegalStateException(
                    "Cannot establish this wand binding.");
        }

        if (isActiveWand(wandId)) {
            return;
        }

        activeWandId = wandId;
        bindingLocked = true;
    }

    public void unlockBinding() {
        bindingLocked = false;
    }
}