package com.inpulsa.reskillable.client.screen;

import com.inpulsa.reskillable.Reskillable;
import com.inpulsa.reskillable.client.screen.buttons.SkillButton;
import com.inpulsa.reskillable.common.skills.Skill;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class SkillScreen extends Screen {
    public static final ResourceLocation RESOURCES = new ResourceLocation(
            Reskillable.MOD_ID,
            "textures/gui/skills.png"
    );

    public SkillScreen() {
        super(new TranslatableComponent("container.skills"));
    }

    private interface BackgroundPositions {
        int RESOURCE_OFFSET_X = 0;
        int RESOURCE_OFFSET_Y = 0;
        int WIDTH = 176;
        int HEIGHT = 166;
    }

    @Override
    protected void init() {
        int left = (this.width - 174) / 2;
        int top = (this.height - 146) / 2;
        int counter = 0;

        for (Skill skill: Skill.values()) {
            int x = left + counter % 2 * 83;
            int y = top + counter / 2 * 36;
            this.addRenderableWidget(new SkillButton(x, y, skill));
            ++counter;
        }
    }

    public void render(@NotNull PoseStack stack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(stack);
        this.renderBg(stack);
        this.renderTitle(stack);
        super.render(stack, mouseX, mouseY, partialTicks);
    }

    protected float calculateRenderWidth() {
        return (float)(this.width / 2 - this.font.width(this.title) / 2);
    }

    public boolean isPauseScreen() {
        return false;
    }

    protected void renderTitle(PoseStack stack) {
        this.font.draw(
            stack,
            this.title,
            this.calculateRenderWidth(),
            this.getTopOffset() + 5,
            4144959
        );
    }

    protected void renderBg(PoseStack stack) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, RESOURCES);

        this.blit(
            stack,
            this.getLeftOffset(),
            this.getTopOffset(),
            BackgroundPositions.RESOURCE_OFFSET_X,
            BackgroundPositions.RESOURCE_OFFSET_Y,
            BackgroundPositions.WIDTH,
            BackgroundPositions.HEIGHT
        );
    }

    protected int getLeftOffset() {
        return (this.width - 176) / 2;
    }

    protected int getTopOffset() {
        return (this.height - 166) / 2;
    }
}
