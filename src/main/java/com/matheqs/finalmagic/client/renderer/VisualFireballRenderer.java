package com.matheqs.finalmagic.client.renderer;

import com.matheqs.finalmagic.FinalMagic;
import com.matheqs.finalmagic.entity.VisualFireballEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class VisualFireballRenderer extends AnimatedSpriteRenderer<VisualFireballEntity> {
    public VisualFireballRenderer(EntityRendererProvider.Context context) {
        super(context, ResourceLocation.fromNamespaceAndPath(
                FinalMagic.MODID, "textures/entity/fire_ball.png"), 32, 16, true, 0.7F);
    }
}