package com.matheqs.finalmagic.entity;

import com.matheqs.finalmagic.spell.CastOrigin;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.Optional;
import java.util.UUID;

public class CastBurstEntity extends Entity {
    private static final EntityDataAccessor<Optional<UUID>> CASTER =
            SynchedEntityData.defineId(CastBurstEntity.class, EntityDataSerializers.OPTIONAL_UUID);
    private static final EntityDataAccessor<Boolean> OFF_HAND =
            SynchedEntityData.defineId(CastBurstEntity.class, EntityDataSerializers.BOOLEAN);
    private int age;

    public CastBurstEntity(EntityType<? extends CastBurstEntity> type, Level level) {
        super(type, level);
        noPhysics = true;
        setNoGravity(true);
    }

    public void attachTo(Player player, InteractionHand hand) {
        entityData.set(CASTER, Optional.of(player.getUUID()));
        entityData.set(OFF_HAND, hand == InteractionHand.OFF_HAND);
        followCaster();
    }

    public Vec3 attachmentPosition(float partialTick) {
        Optional<UUID> casterId = entityData.get(CASTER);
        Player caster = casterId.map(id -> level().getPlayerByUUID(id)).orElse(null);
        if (caster == null || !caster.isAlive()) return null;
        InteractionHand hand = entityData.get(OFF_HAND)
                ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND;
        return CastOrigin.position(caster, hand, partialTick);
    }

    private void followCaster() {
        Vec3 position = attachmentPosition(1.0F);
        if (position != null) {
            setPos(position.x, position.y - getBbHeight() / 2, position.z);
        } else if (!level().isClientSide()) {
            discard();
        }
    }

    @Override
    public void tick() {
        super.tick();
        followCaster();
        if (++age >= 10 && !level().isClientSide()) discard();
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(CASTER, Optional.empty());
        builder.define(OFF_HAND, false);
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        age = tag.getInt("age");
        entityData.set(CASTER, tag.hasUUID("caster")
                ? Optional.of(tag.getUUID("caster")) : Optional.empty());
        entityData.set(OFF_HAND, tag.getBoolean("off_hand"));
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        tag.putInt("age", age);
        entityData.get(CASTER).ifPresent(id -> tag.putUUID("caster", id));
        tag.putBoolean("off_hand", entityData.get(OFF_HAND));
    }
}