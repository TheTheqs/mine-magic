package com.matheqs.finalmagic.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

public class AnimatedSpriteRenderer<T extends Entity> extends EntityRenderer<T> {
    private final ResourceLocation texture;
    private final int frameCount;
    private final int frameSize;
    private final boolean loop;
    private final float size;

    public AnimatedSpriteRenderer(EntityRendererProvider.Context context,
                                  ResourceLocation texture, int frameCount,
                                  int frameSize, boolean loop, float size) {
        super(context);
        this.texture = texture;
        this.frameCount = frameCount;
        this.frameSize = frameSize;
        this.loop = loop;
        this.size = size;
        shadowRadius = 0;
    }

    @Override
    public void render(T entity, float yaw, float partialTick,
                       PoseStack poses, MultiBufferSource buffers, int packedLight) {
        poses.pushPose();
        poses.translate(0, entity.getBbHeight() / 2, 0);
        poses.mulPose(entityRenderDispatcher.cameraOrientation());
        float scale = loop ? size * (1 + 0.06F * (float)
                Math.sin((entity.tickCount + partialTick) * 0.45F)) : size;
        poses.scale(scale, scale, scale);
        int frame = loop ? entity.tickCount % frameCount
                : Math.min(entity.tickCount, frameCount - 1);
        float inset = 0.5F / (frameCount * frameSize);
        float u0 = frame / (float) frameCount + inset;
        float u1 = (frame + 1) / (float) frameCount - inset;
        float vInset = 0.5F / frameSize;
        VertexConsumer vertices = buffers.getBuffer(RenderType.entityTranslucent(texture));
        PoseStack.Pose pose = poses.last();
        vertex(vertices, pose, -0.5F, -0.5F, u0, 1 - vInset);
        vertex(vertices, pose, 0.5F, -0.5F, u1, 1 - vInset);
        vertex(vertices, pose, 0.5F, 0.5F, u1, vInset);
        vertex(vertices, pose, -0.5F, 0.5F, u0, vInset);
        poses.popPose();
        super.render(entity, yaw, partialTick, poses, buffers, packedLight);
    }

    private static void vertex(VertexConsumer vertices, PoseStack.Pose pose,
                               float x, float y, float u, float v) {
        vertices.addVertex(pose.pose(), x, y, 0)
                .setColor(255, 255, 255, 255).setUv(u, v)
                .setOverlay(OverlayTexture.NO_OVERLAY).setLight(0xF000F0)
                .setNormal(pose, 0, 0, 1);
    }

    @Override
    public ResourceLocation getTextureLocation(T entity) { return texture; }
}