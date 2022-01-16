package com.inpulsa.reskillable.client.screen.buttons;

import com.inpulsa.reskillable.client.screen.SkillScreen;
import com.inpulsa.reskillable.common.skills.Skill;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class TabButton extends ImageButton {
    private final boolean selected;
    private final TabButton.TabType type;
    public static final ResourceLocation RESOURCE_LOCATION = SkillScreen.RESOURCES;

    public TabButton(int x, int y, TabButton.TabType type, boolean selected) {
        super(x, y, 31, 28 + 7, 0, 0, SkillScreen.RESOURCES, TabButton::onPress);
//        this.onPress = TabButton::onButtonPress;
        this.type = type;
        this.selected = selected;
    }

    @Override
    public void renderButton(@NotNull PoseStack stack, int mouseX, int mouseY, float partialTicks) {
        Minecraft minecraft = Minecraft.getInstance();
//        this.active = !(minecraft.screen instanceof InventoryScreen);

        if (this.active) {
//            minecraft.textureManager.bindForSetup(SkillScreen.RESOURCES);
//            this.renderButton(stack, );
            super.renderButton(stack, this.x, this.y, this.selected ? 31 : 0);

//            this.blit(stack, this.x, this.y, this.selected ? 31 : 0, 166, this.width, this.height);
//            this.blit(stack, this.x + (this.selected ? 8 : 10), this.y + 6, 240, 128 + this.type.iconIndex * 16, 16, 16);
        }
    }

    @SuppressWarnings("ConstantConditions")
    public static void onPress(Button button) {
        Minecraft minecraft = Minecraft.getInstance();

        switch (((TabButton)button).type) {
            case INVENTORY -> minecraft.screen = new InventoryScreen(minecraft.player);
            case SKILLS -> minecraft.screen = new SkillScreen();
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