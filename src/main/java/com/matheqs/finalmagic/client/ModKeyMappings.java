package com.matheqs.finalmagic.client;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.settings.KeyConflictContext;
import org.lwjgl.glfw.GLFW;

public final class ModKeyMappings {
    private ModKeyMappings() {}

    public static final KeyMapping SPELL_CATALOG = new KeyMapping(
            "key.finalmagic.spell_catalog", KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_G,
            "key.categories.finalmagic");
}