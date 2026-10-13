package com.matheqs.finalmagic.registry;

import com.matheqs.finalmagic.FinalMagic;
import com.matheqs.finalmagic.entity.VisualFireballEntity;
import com.matheqs.finalmagic.entity.CastBurstEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITIES =
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, FinalMagic.MODID);

    public static final RegistryObject<EntityType<VisualFireballEntity>> VISUAL_FIREBALL =
            ENTITIES.register("visual_fireball", () ->
                    EntityType.Builder.<VisualFireballEntity>of(
                            VisualFireballEntity::new, MobCategory.MISC)
                            .sized(0.35F, 0.35F)
                            .clientTrackingRange(8)
                            .updateInterval(1)
                            .build("finalmagic:visual_fireball"));
    public static final RegistryObject<EntityType<CastBurstEntity>> CAST_BURST =
            ENTITIES.register("cast_burst", () ->
                    EntityType.Builder.<CastBurstEntity>of(CastBurstEntity::new, MobCategory.MISC)
                            .sized(0.1F, 0.1F).clientTrackingRange(8).updateInterval(1)
                            .build("finalmagic:cast_burst"));
}