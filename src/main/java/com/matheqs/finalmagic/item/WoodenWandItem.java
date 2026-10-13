package com.matheqs.finalmagic.item;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;
import com.matheqs.finalmagic.binding.WandBindingSavedData;
import com.matheqs.finalmagic.spell.SpellAvailability;
import net.minecraft.server.level.ServerLevel;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class WoodenWandItem extends Item {

    private static final String WAND_UUID = "finalmagic_wand_uuid";
    private static final String OWNER_UUID = "finalmagic_owner_uuid";
    private static final String OWNER_NAME = "finalmagic_owner_name";

    public WoodenWandItem(Item.Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(
            Level level,
            Player player,
            @NotNull InteractionHand hand) {

        ItemStack stack = player.getItemInHand(hand);

        if (level.isClientSide()) {
            return InteractionResultHolder.sidedSuccess(stack, true);
        }

        ServerLevel serverLevel = (ServerLevel) level;

        WandBindingSavedData bindings =
                WandBindingSavedData.get(serverLevel.getServer());

        CompoundTag data = stack.getOrDefault(
                DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();

        // Verifica se o proprietário registrado ainda tem este vínculo ativo.
        if (data.hasUUID(OWNER_UUID)) {
            UUID ownerId = data.getUUID(OWNER_UUID);

            boolean active = data.hasUUID(WAND_UUID)
                    && bindings.isActiveWand(
                    ownerId, data.getUUID(WAND_UUID));

            if (!active) {
                CustomData.update(DataComponents.CUSTOM_DATA, stack, tag -> {
                    tag.remove(OWNER_UUID);
                    tag.remove(OWNER_NAME);
                });

                data = stack.getOrDefault(
                        DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
            }
        }

        // Um vínculo ativo de outro jogador impede o uso.
        if (data.hasUUID(OWNER_UUID)) {
            if (!data.getUUID(OWNER_UUID).equals(player.getUUID())) {
                player.sendSystemMessage(Component.translatable(
                        "message.finalmagic.wand_other_owner"));

                return InteractionResultHolder.fail(stack);
            }

            player.sendSystemMessage(Component.translatable(
                    "message.finalmagic.wand_already_bound"));
            reportFireAvailability(player);

            return InteractionResultHolder.sidedSuccess(stack, false);
        }

        UUID wandId = data.hasUUID(WAND_UUID)
                ? data.getUUID(WAND_UUID)
                : UUID.randomUUID();

        if (!bindings.canBind(player.getUUID(), wandId)) {
            player.sendSystemMessage(Component.translatable(
                    "message.finalmagic.binding_locked"));

            return InteractionResultHolder.fail(stack);
        }

        bindings.bind(player.getUUID(), wandId);

        CustomData.update(DataComponents.CUSTOM_DATA, stack, tag -> {
            tag.putUUID(WAND_UUID, wandId);
            tag.putUUID(OWNER_UUID, player.getUUID());
            tag.putString(OWNER_NAME, player.getGameProfile().getName());
        });

        player.sendSystemMessage(Component.translatable(
                "message.finalmagic.wand_bound"));
        reportFireAvailability(player);

        return InteractionResultHolder.sidedSuccess(stack, false);
    }
    private void reportFireAvailability(Player player) {
        String messageKey = SpellAvailability.isFireAvailable(player)
                ? "message.finalmagic.fire_available"
                : "message.finalmagic.fire_scroll_missing";
        player.sendSystemMessage(Component.translatable(messageKey));
    }
}
