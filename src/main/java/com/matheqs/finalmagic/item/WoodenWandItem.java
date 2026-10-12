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
import java.util.UUID;
import org.jetbrains.annotations.NotNull;

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

        CustomData customData = stack.getOrDefault(
                DataComponents.CUSTOM_DATA, CustomData.EMPTY);

        CompoundTag data = customData.copyTag();

        if (!data.hasUUID(OWNER_UUID)) {
            CustomData.update(DataComponents.CUSTOM_DATA, stack, tag -> {
                if (!tag.hasUUID(WAND_UUID)) {
                    tag.putUUID(WAND_UUID, UUID.randomUUID());
                }

                tag.putUUID(OWNER_UUID, player.getUUID());
                tag.putString(OWNER_NAME, player.getGameProfile().getName());
            });

            player.sendSystemMessage(
                    Component.translatable("message.finalmagic.wand_bound"));

            return InteractionResultHolder.sidedSuccess(stack, false);
        }

        if (!data.getUUID(OWNER_UUID).equals(player.getUUID())) {
            player.sendSystemMessage(
                    Component.translatable("message.finalmagic.wand_other_owner"));

            return InteractionResultHolder.fail(stack);
        }

        player.sendSystemMessage(
                Component.translatable("message.finalmagic.wand_already_bound"));

        return InteractionResultHolder.sidedSuccess(stack, false);
    }
}
