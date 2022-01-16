package com.inpulsa.reskillable.client;

import com.inpulsa.reskillable.Configuration;
import com.inpulsa.reskillable.common.capabilities.SkillModel;
import com.inpulsa.reskillable.common.skills.Requirement;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.List;

public class Tooltip {

    protected static String componentName = "tooltip.requirements";

    public Tooltip() {
    }

    @SubscribeEvent
    public void onTooltipDisplay(ItemTooltipEvent event) {
        if (Minecraft.getInstance().player != null) {
            ResourceLocation registryName = event.getItemStack().getItem().getRegistryName();

            if (registryName != null) {
                Requirement[] requirements = Configuration.getRequirements(registryName);

                if (requirements != null) {
                    List<Component> tooltips = event.getToolTip();
                    tooltips.add(TextComponent.EMPTY);
                    tooltips.add(this.getRequirementsTextComponentToAdd());

                    for (Requirement requirement : requirements) {
                        tooltips.add(this.getSkillNameTextComponentToAdd(requirement));
                    }
                }
            }

        }
    }

    protected MutableComponent getRequirementsTextComponentToAdd() {
        MutableComponent componentToAdd = new TranslatableComponent(Tooltip.componentName);
        componentToAdd.append(":");
        componentToAdd.withStyle(ChatFormatting.GRAY);

        return componentToAdd;
    }

    protected MutableComponent getSkillNameTextComponentToAdd(Requirement requirement) {
        MutableComponent componentToAdd = new TranslatableComponent(requirement.skill.displayName);
        componentToAdd.append(" " + requirement.level);
        componentToAdd.withStyle(this.getColorByRequirement(requirement));

        return componentToAdd;
    }

    protected ChatFormatting getColorByRequirement(Requirement requirement) {

        return SkillModel.get().getSkillLevel(requirement.skill) >= requirement.level ? ChatFormatting.GREEN : ChatFormatting.RED;
    }
}
