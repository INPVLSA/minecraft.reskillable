package com.inpulsa.reskillable.client.screen.buttons;

import com.inpulsa.reskillable.Configuration;
import com.inpulsa.reskillable.client.screen.SkillScreen;
import com.inpulsa.reskillable.common.capabilities.SkillModel;
import com.inpulsa.reskillable.common.network.RequestLevelUp;
import com.inpulsa.reskillable.common.skills.Skill;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.TranslatableComponent;
import org.jetbrains.annotations.NotNull;
import net.minecraft.client.gui.Font;

public class SkillButton extends Button {
    private final Skill skill;

    private interface BackgroundPositions {
        int RESOURCE_OFFSET_X = 176;
        int RESOURCE_OFFSET_Y = 0;
        int RESOURCE_MAX_LEVEL_OFFSET_X = 176;
        int RESOURCE_MAX_LEVEL_OFFSET_Y = 64;
        int WIDTH = 78;
        int HEIGHT = 32;
    }

    public SkillButton(int x, int y, Skill skill) {
        super(x, y, 79, 32, new TranslatableComponent(""), SkillButton::onButtonPress);
        this.skill = skill;
    }

    @Override
    public void render(@NotNull PoseStack stack, int mouseX, int mouseY, float partialTicks) {
        Minecraft minecraft = Minecraft.getInstance();

        int level = SkillModel.get().getSkillLevel(this.skill);
        int maxLevel = Configuration.getMaxLevel();

        if (level == maxLevel) {
            this.renderMaxLevelBg(stack);
        } else {
            this.renderDefaultBg(stack);
        }
        this.drawButtonTitle(stack, minecraft.font);
        this.drawButtonLevels(stack, minecraft.font, level, maxLevel);

//        if (this.isMouseOver(mouseX, mouseY) && level < maxLevel) {
//            int upCost = RequestLevelUp.calculateLevelUpCost(level);
//
//            if (minecraft.player != null) {
//                // ? GREEN : RED
//
//            }
//        }
//        int u = ((int)Math.ceil((double)level * 4.0D / (double)maxLevel) - 1) * 16 + 176;
//        int v = this.skill.index * 16 + 128;

//        this.blit(stack, this.x, this.y, 176, (level == maxLevel ? 64 : 0) + (this.isMouseOver(mouseX, mouseY) ? 32 : 0), this.width, this.height);
//        this.blit(stack, this.x + 6, this.y + 8, u, v, 16, 16);
//        minecraft.font.draw(stack, new TranslatableComponent(this.skill.displayName), (float)(this.x + 25), (float)(this.y + 7), 16777215);
//        minecraft.font.draw(stack, level + "/" + maxLevel, (float)(this.x + 25), (float)(this.y + 18), 12500670);

        if (this.isMouseOver(mouseX, mouseY) && level < maxLevel) {
            this.drawIncreaseCost(stack, minecraft, level);
        }
    }

    protected void drawButtonTitle(PoseStack stack, Font font) {
        int posX = this.x + 30;
        int posY = this.y + 14;

        // White
        font.draw(stack, new TranslatableComponent(this.skill.displayName), posX, posY, 16777215);
    }

    protected void drawButtonLevels(PoseStack stack, Font font, int level, int maxLevel) {
        int posX = this.x + 30;
        int posY = this.y + 25;

        String levelsString = level + "/" + maxLevel;
        // ?
        font.draw(stack, levelsString, posX, posY, 12500670);
    }

    protected void drawIncreaseCost(PoseStack stack, Minecraft minecraft, int level) {
        int cost = RequestLevelUp.calculateLevelUpCost(level);

        if (minecraft.player != null) {
            int colour = minecraft.player.experienceLevel >= cost ? 8322080 : 16536660;
            String text = Integer.toString(cost);

            int posX = this.x + 73 - minecraft.font.width(text);
            int posY = this.y + 18;
            minecraft.font.draw(stack, text, posX, posY, colour);
        }
    }

    protected void renderDefaultBg(PoseStack stack) {
        this.renderBg(
            stack,
            BackgroundPositions.RESOURCE_OFFSET_X,
            BackgroundPositions.RESOURCE_OFFSET_Y
        );
    }

    protected void renderMaxLevelBg(PoseStack stack) {
        this.renderBg(
            stack,
            BackgroundPositions.RESOURCE_MAX_LEVEL_OFFSET_X,
            BackgroundPositions.RESOURCE_MAX_LEVEL_OFFSET_Y
        );
    }

    protected void renderBg(PoseStack stack, int offsetX, int offsetY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, SkillScreen.RESOURCES);

        this.blit(
            stack,
            this.x + 6,
            this.y + 8,
            offsetX,
            offsetY,
            BackgroundPositions.WIDTH,
            BackgroundPositions.HEIGHT
        );
    }

    public static void onButtonPress(Button button) {
        RequestLevelUp.send(((SkillButton)button).skill);
    }
}
