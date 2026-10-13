package com.matheqs.finalmagic.client.renderer;

import com.matheqs.finalmagic.FinalMagic;
import com.matheqs.finalmagic.entity.CastBurstEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

public class CastBurstRenderer extends AnimatedSpriteRenderer<CastBurstEntity> {
    public CastBurstRenderer(EntityRendererProvider.Context context) {
        super(context, ResourceLocation.fromNamespaceAndPath(
                FinalMagic.MODID, "textures/entity/cast_burst.png"), 10, 72, false, 1.0F);
    }

    @Override
    public void render(CastBurstEntity entity, float yaw, float partialTick,
                       PoseStack poses, MultiBufferSource buffers, int packedLight) {
        poses.pushPose();
        Vec3 attachment = entity.attachmentPosition(partialTick);
        if (attachment != null) {
            // Follow the interpolated player pose instead of lagging behind by a tick.
            double x = Mth.lerp(partialTick, entity.xo, entity.getX());
            double y = Mth.lerp(partialTick, entity.yo, entity.getY());
            double z = Mth.lerp(partialTick, entity.zo, entity.getZ());
            poses.translate(attachment.x - x,
                    attachment.y - entity.getBbHeight() / 2 - y, attachment.z - z);
        }
        super.render(entity, yaw, partialTick, poses, buffers, packedLight);
        poses.popPose();
    }
}