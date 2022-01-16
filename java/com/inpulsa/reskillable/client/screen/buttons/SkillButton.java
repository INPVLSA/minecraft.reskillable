package com.inpulsa.reskillable.client.screen.buttons;

import com.inpulsa.reskillable.Configuration;
import com.inpulsa.reskillable.client.screen.SkillScreen;
import com.inpulsa.reskillable.common.capabilities.SkillModel;
import com.inpulsa.reskillable.common.network.RequestLevelUp;
import com.inpulsa.reskillable.common.skills.Skill;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.TranslatableComponent;
import org.jetbrains.annotations.NotNull;

public class SkillButton extends Button {
    private final Skill skill;

    public SkillButton(int x, int y, Skill skill) {
        super(x, y, 79, 32, new TranslatableComponent(""), null);
        this.skill = skill;
    }

    public void renderButton(@NotNull PoseStack stack, int mouseX, int mouseY, float partialTicks) {
        Minecraft minecraft = Minecraft.getInstance();
        minecraft.textureManager.bindForSetup(SkillScreen.RESOURCES);

        int level = SkillModel.get().getSkillLevel(this.skill);
        int maxLevel = Configuration.getMaxLevel();
        int u = ((int)Math.ceil((double)level * 4.0D / (double)maxLevel) - 1) * 16 + 176;
        int v = this.skill.index * 16 + 128;

        this.blit(stack, this.x, this.y, 176, (level == maxLevel ? 64 : 0) + (this.isMouseOver(mouseX, mouseY) ? 32 : 0), this.width, this.height);
        this.blit(stack, this.x + 6, this.y + 8, u, v, 16, 16);
        minecraft.font.draw(stack, new TranslatableComponent(this.skill.displayName), (float)(this.x + 25), (float)(this.y + 7), 16777215);
        minecraft.font.draw(stack, level + "/" + maxLevel, (float)(this.x + 25), (float)(this.y + 18), 12500670);

        if (this.isMouseOver(mouseX, mouseY) && level < maxLevel) {
            int cost = Configuration.getStartCost() + (level - 1) * Configuration.getCostIncrease();

            if (minecraft.player != null) {
                int colour = minecraft.player.experienceLevel >= cost ? 8322080 : 16536660;
                String text = Integer.toString(cost);
                minecraft.font.draw(stack, text, (float)(this.x + 73 - minecraft.font.width(text)), (float)(this.y + 18), colour);
            }
        }
    }

    public static void onButtonPress(Button button) {
        RequestLevelUp.send(((SkillButton)button).skill);
    }
}
