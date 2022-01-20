package com.inpulsa.reskillable.client.screen;

import com.inpulsa.reskillable.client.screen.buttons.TabButton;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.components.Widget;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.client.event.ScreenEvent.PotionShiftEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.List;

public class InventoryTabs {
    public InventoryTabs() {
    }

    @SubscribeEvent
    public void onGuiInit(ScreenEvent.InitScreenEvent.Post event) {
        Screen screen = event.getScreen();

        if (this.isAffectedScreen(screen)) {
            boolean isCreativeOpen = screen instanceof CreativeModeInventoryScreen;
            boolean skillsOpen = screen instanceof SkillScreen;
            int posX = (screen.width - (isCreativeOpen ? 195 : 176)) / 2 - 28;
            int posY = (screen.height - (isCreativeOpen ? 136 : 166)) / 2;
            int buttonsPaddingY = 7;

            TabButton inventoryButton = new TabButton(
                    posX,
                    posY + buttonsPaddingY,
                    TabButton.TabType.INVENTORY,
                    !skillsOpen
            );
            TabButton skillsButton = new TabButton(
                    posX,
                    posY + TabButton.BackgroundTextureCoordinates.HEIGHT + buttonsPaddingY,
                    TabButton.TabType.SKILLS,
                    skillsOpen
            );

            if (screen instanceof InventoryScreen inventoryScreen
                && inventoryScreen.getRecipeBookComponent().isVisible()
            ) {
                return;
            }
            screen.renderables.add(inventoryButton);
            screen.renderables.add(skillsButton);

            if (screen instanceof InventoryScreen) {
                for (TabButton button: List.of(inventoryButton, skillsButton)) {
                    PoseStack poseStack = new PoseStack();
                    button.render(poseStack);
                }
            }
        }
    }

    private boolean isAffectedScreen(Screen screen) {
        return screen instanceof InventoryScreen
                || screen instanceof CreativeModeInventoryScreen
                || screen instanceof SkillScreen;
    }

    @SubscribeEvent
    public void onPotionShift(PotionShiftEvent event) {
        event.setCanceled(true);
    }

    @SubscribeEvent
    public void mouseClicked(ScreenEvent.MouseClickedEvent.Post event) {
        Screen screen = event.getScreen();

        if (screen instanceof InventoryScreen inventoryScreen) {
            // Hide tabs on RecipeBookComponent opening
            if (inventoryScreen.getRecipeBookComponent().isVisible()) {
                for (Widget button: screen.renderables) {
                    if (button instanceof TabButton tabButton) {
                        tabButton.hide();
                    }
                }
            } else {
                // Restoring tabs after closing RecipeBookComponent
                for (Widget button: screen.renderables) {
                    if (button instanceof TabButton tabButton) {
                        tabButton.show();
                    }
                }
            }
        }

        // TabButton::onPress doesn't work. Bad way to trigger it on click
        if (this.isAffectedScreen(screen)) {
            for (Widget button: screen.renderables) {
                if (button instanceof TabButton tabButton) {
                    tabButton.mouseClicked(event.getMouseX(), event.getMouseY(), 0);
                }
            }
        }
    }
}
