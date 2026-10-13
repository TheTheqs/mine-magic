package com.matheqs.finalmagic.entity;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class VisualFireballEntity extends Entity {
    private static final int LIFETIME_TICKS = 60;
    private int age;

    public VisualFireballEntity(EntityType<? extends VisualFireballEntity> type, Level level) {
        super(type, level);
        noPhysics = true;
        setNoGravity(true);
    }

    @Override
    public void tick() {
        super.tick();
        Vec3 velocity = getDeltaMovement();
        if (level().isClientSide() && age % 2 == 0) {
            // A sparse trail keeps the animated core readable.
            level().addParticle(ParticleTypes.SMALL_FLAME,
                    getX(), getY() + getBbHeight() / 2, getZ(),
                    0, 0.003, 0);
        }
        setPos(getX() + velocity.x, getY() + velocity.y, getZ() + velocity.z);
        age++;
        if (!level().isClientSide() && age >= LIFETIME_TICKS) discard();
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {}

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) { age = tag.getInt("age"); }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) { tag.putInt("age", age); }
}