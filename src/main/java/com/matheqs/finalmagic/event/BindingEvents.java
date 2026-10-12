package com.matheqs.finalmagic.event;

import com.matheqs.finalmagic.FinalMagic;
import com.matheqs.finalmagic.binding.WandBindingSavedData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.level.SleepFinishedTimeEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(
        modid = FinalMagic.MODID,
        bus = Mod.EventBusSubscriber.Bus.FORGE)
public class BindingEvents {

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onPlayerDeath(LivingDeathEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            WandBindingSavedData.get(player.serverLevel().getServer())
                    .unlockBinding(player.getUUID());
        }
    }

    @SubscribeEvent
    public static void onSleepFinished(SleepFinishedTimeEvent event) {
        if (event.getLevel() instanceof ServerLevel level) {
            WandBindingSavedData bindings =
                    WandBindingSavedData.get(level.getServer());

            for (ServerPlayer player : level.players()) {
                if (player.isSleeping()) {
                    bindings.unlockBinding(player.getUUID());
                }
            }
        }
    }
}