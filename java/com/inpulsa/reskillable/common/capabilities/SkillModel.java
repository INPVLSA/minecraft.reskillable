package com.inpulsa.reskillable.common.capabilities;

import com.inpulsa.reskillable.Configuration;
import com.inpulsa.reskillable.common.network.NotifyWarning;
import com.inpulsa.reskillable.common.skills.Requirement;
import com.inpulsa.reskillable.common.skills.Skill;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.util.INBTSerializable;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class SkillModel implements INBTSerializable<CompoundTag> {
    public int[] skillLevels = new int[]{1, 1, 1, 1, 1, 1, 1, 1};

    public SkillModel() {
    }

    public int getSkillLevel(@NotNull Skill skill) {
        return this.skillLevels[skill.index];
    }

    public void setSkillLevel(@NotNull Skill skill, int level) {
        this.skillLevels[skill.index] = level;
    }

    public void increaseSkillLevel(@NotNull Skill skill) {
        this.skillLevels[skill.index]++;
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public boolean canUseItem(Player player, @NotNull ItemStack item) {
        return this.canUse(player, item.getItem().getRegistryName());
    }

    public boolean canUseBlock(Player player, @NotNull Block block) {
        return this.canUse(player, block.getRegistryName());
    }

    public boolean canUseEntity(Player player, @NotNull Entity entity) {
        return this.canUse(player, entity.getType().getRegistryName());
    }

    private boolean canUse(Player player, ResourceLocation resource) {
        Requirement[] requirements = Configuration.getRequirements(resource);

        if (requirements != null) {
            for (Requirement currentRequirement : requirements) {
                if (this.getSkillLevel(currentRequirement.skill) < currentRequirement.level) {
                    if (player instanceof ServerPlayer) {
                        NotifyWarning.send(player, resource);
                    }

                    return false;
                }
            }
        }

        return true;
    }

    public static @NotNull SkillModel get() {
        var player = Objects.requireNonNull(Minecraft.getInstance().player);

        return SkillModel.get(player);
    }

    public static @NotNull SkillModel get(@NotNull Player player) {
        return player.getCapability(SkillCapability.INSTANCE).orElseThrow(() -> {
            String name = player.getName().getString();

            throw new IllegalArgumentException("Player " + name + " does not have a Skill Model!");
        });
    }

    public CompoundTag serializeNBT() {
        CompoundTag compound = new CompoundTag();
        compound.putInt("mining", this.skillLevels[0]);
        compound.putInt("gathering", this.skillLevels[1]);
        compound.putInt("attack", this.skillLevels[2]);
        compound.putInt("defense", this.skillLevels[3]);
        compound.putInt("building", this.skillLevels[4]);
        compound.putInt("farming", this.skillLevels[5]);
        compound.putInt("agility", this.skillLevels[6]);
        compound.putInt("magic", this.skillLevels[7]);
        
        return compound;
    }

    public void deserializeNBT(@NotNull CompoundTag compoundTag) {
        this.skillLevels[0] = compoundTag.getInt("mining");
        this.skillLevels[1] = compoundTag.getInt("gathering");
        this.skillLevels[2] = compoundTag.getInt("attack");
        this.skillLevels[3] = compoundTag.getInt("defense");
        this.skillLevels[4] = compoundTag.getInt("building");
        this.skillLevels[5] = compoundTag.getInt("farming");
        this.skillLevels[6] = compoundTag.getInt("agility");
        this.skillLevels[7] = compoundTag.getInt("magic");
    }
}
