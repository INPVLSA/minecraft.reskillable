package com.inpulsa.reskillable.common;

import com.inpulsa.reskillable.Configuration;
import com.inpulsa.reskillable.Reskillable;
import com.inpulsa.reskillable.common.capabilities.SkillModel;
import com.inpulsa.reskillable.common.capabilities.SkillProvider;
import com.inpulsa.reskillable.common.network.SyncToClient;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingEquipmentChangeEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.Clone;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerChangedDimensionEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerRespawnEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.EntityInteract;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.LeftClickBlock;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickBlock;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickItem;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.Collection;

public class EventHandler {
    public EventHandler() {
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onLeftClickBlock(LeftClickBlock event) {
        if (this.shouldCancelBlockInteraction(event)) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onRightClickBlock(RightClickBlock event) {
        if (this.shouldCancelBlockInteraction(event)) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onRightClickItem(RightClickItem event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItemStack();

        if (!player.isCreative() && !SkillModel.get(player).canUseItem(player, item)) {
            event.setCanceled(true);
        }
    }

    private boolean shouldCancelBlockInteraction(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        if (!player.isCreative()) {
            ItemStack item = event.getItemStack();
            // .getBlockEntity(event.getPos()) return null. Why?
            BlockState blockState = event.getWorld().getBlockState(event.getPos());
            Block block = blockState.getBlock();
            SkillModel skillModel = SkillModel.get(player);

            return !skillModel.canUseItem(player, item) || !skillModel.canUseBlock(player, block);
        }

        return false;
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onRightClickEntity(EntityInteract event) {
        if (this.shouldCancelEntityInteraction(event)) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onAttackEntity(AttackEntityEvent event) {
        if (this.shouldCancelEntityInteraction(event)) {
            event.setCanceled(true);
        }
    }

    private boolean shouldCancelEntityInteraction(PlayerEvent event) {
        boolean result = false;
        Player player = event.getPlayer();

        if (!player.isCreative()) {
            ItemStack item = player.getMainHandItem();
            result = !SkillModel.get(player).canUseItem(player, item);

            if (!result && event instanceof EntityInteract) {
                Entity entity = ((EntityInteract) event).getTarget();
                result = !SkillModel.get(player).canUseEntity(player, entity);
            }
        }

        return result;
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onChangeEquipment(LivingEquipmentChangeEvent event) {
        if (event.getEntity() instanceof Player player) {
            if (!player.isCreative()) {
                if (event.getSlot().getType() == EquipmentSlot.Type.ARMOR) {
                    ItemStack item = event.getTo();

                    if (!SkillModel.get(player).canUseItem(player, item)) {
                        ItemStack previousItem = event.getFrom();
                        int slotIndex = event.getSlot().getIndex();
                        // Unable to cancel event using .setCanceled(). Getting java.lang.UnsupportedOperationException
                        player.getInventory().armor.set(slotIndex, previousItem);
                        player.getInventory().add(item);
                    }
                }
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onEntityDrops(LivingDropsEvent event) {
        if (Configuration.getDisableWool() && event.getEntity() instanceof Sheep) {
            Collection<ItemEntity> drops = event.getDrops();

            drops.removeIf(drop -> drop.getItem().getItem().getRegistryName().toString().equals("wool"));
        }
    }

    @SubscribeEvent
    public void onPlayerDeath(LivingDeathEvent event) {
        if (Configuration.getDeathReset() && event.getEntity() instanceof Player) {
            SkillModel.get((Player)event.getEntity()).skillLevels = new int[]{1, 1, 1, 1, 1, 1, 1, 1};
        }
    }

    @SubscribeEvent
    public void onAttachCapabilities(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof Player) {
            SkillModel skillModel = new SkillModel();
            SkillProvider provider = new SkillProvider(skillModel);
            event.addCapability(new ResourceLocation(Reskillable.MOD_ID, "cap_skills"), provider);
            event.addListener(provider::invalidate);
        }

    }

    @SubscribeEvent
    public void onPlayerClone(Clone event) {
        SkillModel.get(event.getPlayer()).skillLevels = SkillModel.get(event.getOriginal()).skillLevels;
    }

    @SubscribeEvent
    public void onPlayerLogin(PlayerLoggedInEvent e) {
        SyncToClient.send(e.getPlayer());
    }

    @SubscribeEvent
    public void onPlayerRespawn(PlayerRespawnEvent e) {
        SyncToClient.send(e.getPlayer());
    }

    @SubscribeEvent
    public void onChangeDimension(PlayerChangedDimensionEvent e) {
        SyncToClient.send(e.getPlayer());
    }
}
