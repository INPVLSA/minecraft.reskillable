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
        int RESOURCE_MAX_LEVEL_OFFSET_X = 175;
        int RESOURCE_MAX_LEVEL_OFFSET_Y = 64;
        int WIDTH = 79;
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
        this.renderIcon(stack, level);
        this.drawButtonTitle(stack, minecraft.font);
        this.drawButtonLevels(stack, minecraft.font, level, maxLevel);

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

            int posX = this.x + 78 - minecraft.font.width(text);
            int posY = this.y + 25;
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
            BackgroundPositions.RESOURCE_MAX_LEVEL_OFFSET_X + 1,
            BackgroundPositions.RESOURCE_MAX_LEVEL_OFFSET_Y
        );
    }

    protected void renderBg(PoseStack stack, int offsetX, int offsetY) {
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

    protected void renderIcon(PoseStack stack, int level) {
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, SkillScreen.RESOURCES);

        this.blit(
            stack,
            this.x + 6 + 5,
            this.y + 8 + 8,
            SkillScreen.SkillIconTextureResolver.getTexturePosX(level),
            SkillScreen.SkillIconTextureResolver.getTexturePosY(this.skill),
            SkillScreen.IconTextureParams.WIDTH,
            SkillScreen.IconTextureParams.HEIGHT
        );
    }

    public static void onButtonPress(Button button) {
        RequestLevelUp.send(((SkillButton)button).skill);
    }
}
