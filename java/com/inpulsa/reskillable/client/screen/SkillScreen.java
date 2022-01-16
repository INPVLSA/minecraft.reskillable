package com.inpulsa.reskillable.client.screen;

import com.inpulsa.reskillable.Reskillable;
import com.inpulsa.reskillable.client.screen.buttons.SkillButton;
import com.inpulsa.reskillable.common.skills.Skill;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
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

    protected void init() {
        int left = (this.width - 162) / 2;
        int top = (this.height - 128) / 2;
        int counter = 0;

        for (Skill skill: Skill.values()) {
            int x = left + counter % 2 * 83;
            int y = top + counter / 2 * 36;
            this.addWidget(new SkillButton(x, y, skill));
            ++counter;
        }
    }

    public void render(@NotNull PoseStack stack, int mouseX, int mouseY, float partialTicks) {
        Minecraft.getInstance().textureManager.bindForSetup(RESOURCES);
        int left = (this.width - 176) / 2;
        int top = (this.height - 166) / 2;
        this.renderBackground(stack);
        this.blit(stack, left, top, 0, 0, 176, 166);
        this.font.draw(stack, this.title, this.calculateRenderWidth(), (float)(top + 6), 4144959);
        super.render(stack, mouseX, mouseY, partialTicks);
    }

    protected float calculateRenderWidth() {
        return (float)(this.width / 2 - this.font.width(this.title) / 2);
    }

    public boolean isPauseScreen() {
        return false;
    }
}
