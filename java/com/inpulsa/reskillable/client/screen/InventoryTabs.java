package com.inpulsa.reskillable.client.screen;

import com.inpulsa.reskillable.client.screen.buttons.TabButton;
import net.minecraft.client.gui.components.Widget;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.client.event.ScreenEvent.PotionShiftEvent;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class InventoryTabs {
    public InventoryTabs() {
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void init(ScreenEvent.@NotNull DrawScreenEvent event) {
        Screen screen = event.getScreen();

        if (screen instanceof InventoryScreen || screen instanceof CreativeModeInventoryScreen || screen instanceof SkillScreen) {
            boolean isCreativeOpen = screen instanceof CreativeModeInventoryScreen;
            boolean skillsOpen = screen instanceof SkillScreen;
            int x = (screen.width - (isCreativeOpen ? 195 : 176)) / 2 - 28;
            int y = (screen.height - (isCreativeOpen ? 136 : 166)) / 2;

            TabButton inventoryButton = new TabButton(x, y + 7, TabButton.TabType.INVENTORY, !skillsOpen);
            TabButton skillsButton = new TabButton(x, y + 43, TabButton.TabType.SKILLS, skillsOpen);

            for (TabButton button: List.of(inventoryButton, skillsButton)) {
                button.renderButton(event.getPoseStack(), event.getMouseX(), event.getMouseY(), event.getPartialTicks());
            }
        }
    }

    @SubscribeEvent
    public void onPotionShift(PotionShiftEvent event) {
        event.setCanceled(true);
    }
}
