package com.inpulsa.reskillable.client.screen.buttons;

import com.inpulsa.reskillable.client.screen.SkillScreen;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.network.chat.TranslatableComponent;
import org.jetbrains.annotations.NotNull;

public class TabButton extends Button {
    private final boolean selected;
    private final TabButton.TabType type;

    public interface BackgroundTextureCoordinates {
        int START_X = 0;
        int START_X_SELECTED = 31;
        int START_Y = 166;
        int WIDTH = 27;
        int WIDTH_SELECTED = 31;
        int HEIGHT = 27;
    }

    public interface IconTextureCoordinates {
        int START_X = 240;
        int INVENTORY_Y = 130;
        int SKILL_Y = 145;
        int WIDTH = 14;
        int HEIGHT = 13;
    }

    public TabButton(int posX, int posY, TabButton.TabType type, boolean selected) {
        super(
            posX,
            posY,
            selected ? BackgroundTextureCoordinates.WIDTH_SELECTED : BackgroundTextureCoordinates.WIDTH,
            BackgroundTextureCoordinates.HEIGHT,
            new TranslatableComponent(""),
            TabButton::onButtonPress
        );
        this.type = type;
        this.selected = selected;
    }

    public void render(@NotNull PoseStack stack) {
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, SkillScreen.RESOURCES);

        this.renderBg(stack);
        this.renderIcon(stack);
    }

    @Override
    public void render(@NotNull PoseStack stack, int p_93658_, int p_93659_, float p_93660_) {
        this.render(stack);
    }

    protected void renderBg(PoseStack stack) {
        this.blit(
                stack,
                this.x + (this.selected ? 0 : 3),
                this.y,
                this.selected ? BackgroundTextureCoordinates.START_X_SELECTED : BackgroundTextureCoordinates.START_X,
                BackgroundTextureCoordinates.START_Y,
                this.width,
                this.height
        );
    }

    protected void renderIcon(PoseStack stack) {
        this.blit(
                stack,
                this.x + 7,
                this.y + 7,
                IconTextureCoordinates.START_X,
                this.type == TabType.INVENTORY ? IconTextureCoordinates.INVENTORY_Y : IconTextureCoordinates.SKILL_Y,
                IconTextureCoordinates.WIDTH,
                IconTextureCoordinates.HEIGHT
        );
    }

    @SuppressWarnings("ConstantConditions")
    public static void onButtonPress(Button button) {
        Minecraft minecraft = Minecraft.getInstance();

        switch (((TabButton)button).type) {
            case INVENTORY -> {
                if (!(minecraft.screen instanceof InventoryScreen)) {
                    minecraft.setScreen(new InventoryScreen(minecraft.player));
                }
            }
            case SKILLS -> {
                if (!(minecraft.screen instanceof SkillScreen)) {
                    minecraft.setScreen(new SkillScreen());
                }
            }
        }
    }

    public enum TabType {
        INVENTORY(0),
        SKILLS(1);

        public final int iconIndex;

        TabType(int index) {
            this.iconIndex = index;
        }
    }
}