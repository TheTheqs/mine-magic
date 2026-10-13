package com.matheqs.finalmagic.spell;

import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

public final class CastOrigin {
    private CastOrigin() {}

    public static Vec3 position(Player player, InteractionHand hand, float partialTick) {
        double yaw = Math.toRadians(Mth.rotLerp(partialTick, player.yRotO, player.getYRot()));
        Vec3 right = new Vec3(-Math.cos(yaw), 0, -Math.sin(yaw));
        double side = player.getMainArm() == HumanoidArm.RIGHT ? 1 : -1;
        if (hand == InteractionHand.OFF_HAND) side *= -1;
        return player.getEyePosition(partialTick).add(player.getViewVector(partialTick).scale(0.8))
                .add(right.scale(side * 0.22)).add(0, -0.18, 0);
    }
}