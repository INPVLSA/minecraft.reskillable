package com.inpulsa.reskillable.client;

import com.inpulsa.reskillable.Configuration;
import com.inpulsa.reskillable.client.screen.SkillScreen;
import com.inpulsa.reskillable.common.capabilities.SkillCapability;
import com.inpulsa.reskillable.common.capabilities.SkillModel;
import com.inpulsa.reskillable.common.skills.Requirement;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
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
    private static List<Requirement> requirements = null;
    private static int showTicks = 0;

    public Overlay() {
    }

    @SubscribeEvent
    public void onRenderOverlay(Post event) {
        if (event.getType() == ElementType.LAYER && showTicks > 0) {
            Minecraft minecraft = Minecraft.getInstance();

            if (minecraft.player.getCapability(SkillCapability.INSTANCE).isPresent()) {
                PoseStack stack = event.getMatrixStack();
                minecraft.textureManager.bindForSetup(SkillScreen.RESOURCES);
                GL11.glEnable(3042);
                int cx = event.getWindow().getGuiScaledWidth() / 2;
                int cy = event.getWindow().getGuiScaledHeight() / 4;
                this.blit(stack, cx - 71, cy - 4, 0, 194, 142, 40);
                String message = (new TranslatableComponent("overlay.message")).getString();
                minecraft.font.draw(stack, message, cx - minecraft.font.getSplitter().stringWidth(message) / 2, (float)cy, 16733525);

                for(int i = 0; i < requirements.size(); ++i) {
                    Requirement requirement = requirements.get(i);
                    int maxLevel = Configuration.getMaxLevel();
                    int x = cx + i * 20 - requirements.size() * 10 + 2;
                    int y = cy + 15;
                    int u = Math.min(requirement.level, maxLevel - 1) / (maxLevel / 4) * 16 + 176;
                    int v = requirement.skill.index * 16 + 128;
                    minecraft.textureManager.bindForSetup(SkillScreen.RESOURCES);
                    this.blit(stack, x, y, u, v, 16, 16);
                    String level = Integer.toString(requirement.level);

                    boolean met = SkillModel.get().getSkillLevel(requirement.skill) >= requirement.level;
                    minecraft.font.draw(stack, level, (float)(x + 17 - minecraft.font.width(level)), (float)(y + 9), met ? 5635925 : 16733525);
                }
            }
        }

    }

    @SubscribeEvent
    public void onClientTick(ClientTickEvent event) {
        if (showTicks > 0) {
            --showTicks;
        }
    }

    public static void showWarning(ResourceLocation resource) {
        requirements = Arrays.asList(Configuration.getRequirements(resource));
        showTicks = 60;
    }
}
