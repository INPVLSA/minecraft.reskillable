package com.inpulsa.reskillable.client;

import com.inpulsa.reskillable.Configuration;
import com.inpulsa.reskillable.client.screen.SkillScreen;
import com.inpulsa.reskillable.common.capabilities.SkillCapability;
import com.inpulsa.reskillable.common.capabilities.SkillModel;
import com.inpulsa.reskillable.common.skills.Requirement;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.client.event.RenderGameOverlayEvent.Post;
import net.minecraftforge.event.TickEvent.ClientTickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.lwjgl.opengl.GL11;

import java.util.Arrays;
import java.util.List;

public class Overlay extends GuiComponent {
    protected static List<Requirement> requirements = null;
    private static int showTicks = 0;

    protected static int SHOW_TICKS_DEFAULT = 60;

    protected interface OverlayPositions {
        int ICON_ITEM_WIDTH = 20;
        int ICONS_OFFSET_Y = 15;
    }

    public Overlay() {
    }

    @SubscribeEvent
    public void onRenderOverlay(Post event) {
        if (event.getType() == ElementType.LAYER && showTicks > 0) {
            Minecraft minecraft = Minecraft.getInstance();

            if (minecraft.player != null) {
                if (minecraft.player.getCapability(SkillCapability.INSTANCE).isPresent()) {
                    PoseStack stack = event.getMatrixStack();
                    GL11.glEnable(3042);

                    int centerPosX = event.getWindow().getGuiScaledWidth() / 2;
                    int centerPosY = event.getWindow().getGuiScaledHeight() / 4;
                    this.renderMessageTitle(stack, minecraft.font, centerPosX, centerPosY);
                    this.renderSkillRequirementsIcons(stack, centerPosX, centerPosY, minecraft.font);
                }
            }
        }
    }

    protected void renderMessageTitle(PoseStack stack, Font font, int centerPosX, int centerPosY) {
        Component message = new TranslatableComponent("overlay.message");
        int textPosX = centerPosX - (font.width(message.getString()) / 2);

        font.draw(stack, message.getString(), textPosX, centerPosY, 16733525);
    }

    protected void renderSkillRequirementsIcons(PoseStack stack, int centerPosX, int centerPosY, Font font) {
        int requirementsCount = requirements.size();
        int posY = centerPosY + OverlayPositions.ICONS_OFFSET_Y;

        for (int requirementIndex = 0; requirementIndex < requirementsCount; requirementIndex++) {
            Requirement requirement = requirements.get(requirementIndex);
            int posX = this.calculateIconPosXInOverlay(centerPosX, requirementsCount, requirementIndex);

            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            RenderSystem.setShaderTexture(0, SkillScreen.RESOURCES);

            this.blit(
                stack,
                posX,
                posY,
                SkillScreen.SkillIconTextureResolver.getTexturePosX(requirement.level),
                SkillScreen.SkillIconTextureResolver.getTexturePosY(requirement.skill),
                SkillScreen.IconTextureParams.WIDTH,
                SkillScreen.IconTextureParams.HEIGHT
            );
            this.renderSkillRequirementNumber(stack, requirement, posX, posY, font);
        }
    }

    protected void renderSkillRequirementNumber(
        PoseStack stack,
        Requirement requirement,
        int iconPosX,
        int iconPosY,
        Font font
    ) {
        boolean isSkillLevelAchieved = SkillModel.get().getSkillLevel(requirement.skill) >= requirement.level;
        int color = isSkillLevelAchieved ? 5635925 : 16733525;

        font.draw(stack, Integer.toString(requirement.level), iconPosX + 17, iconPosY + 9, color);
    }

    protected int calculateIconPosXInOverlay(int centerX, int requirementCount, int index) {
        return centerX + (index * OverlayPositions.ICON_ITEM_WIDTH) - (requirementCount * 10) + 2;
    }

    @SubscribeEvent
    public void onClientTick(ClientTickEvent event) {
        if (showTicks > 0) {
            --showTicks;
        }
    }

    public static void showWarning(ResourceLocation resource) {
        requirements = Arrays.asList(Configuration.getRequirements(resource));
        Overlay.showTicks = Overlay.SHOW_TICKS_DEFAULT;
    }
}
