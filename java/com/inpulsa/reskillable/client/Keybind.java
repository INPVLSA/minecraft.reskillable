package com.inpulsa.reskillable.client;

import com.inpulsa.reskillable.client.screen.SkillScreen;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.ClientRegistry;
import net.minecraftforge.client.event.InputEvent.KeyInputEvent;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class Keybind {
    protected static String ID = "key.skills";
    protected static String CATEGORY = "Reskillable";

    private final KeyMapping openKey;

    public Keybind() {
        this.openKey = new KeyMapping(ID, KeyConflictContext.IN_GAME, InputConstants.Type.KEYSYM, 71, CATEGORY);
        ClientRegistry.registerKeyBinding(this.openKey);
    }

    @SubscribeEvent
    public void onKeyInput(KeyInputEvent event) {
        Minecraft minecraft = Minecraft.getInstance();

        if (this.openKey.isDown() && minecraft.screen == null) {
            minecraft.screen = new SkillScreen();
        }
    }
}
