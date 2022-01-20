package com.inpulsa.reskillable;

import com.inpulsa.reskillable.client.Keybind;
import com.inpulsa.reskillable.client.Overlay;
import com.inpulsa.reskillable.client.Tooltip;
import com.inpulsa.reskillable.client.screen.InventoryTabs;
import com.inpulsa.reskillable.common.EventHandler;
import com.inpulsa.reskillable.common.commands.Commands;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod("reskillable")
public class Reskillable {
    public static final String MOD_ID = "reskillable";

    public Reskillable() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::commonSetup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::clientSetup);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Configuration.getConfig());
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        Configuration.load();

        MinecraftForge.EVENT_BUS.register(new EventHandler());
        MinecraftForge.EVENT_BUS.register(new Commands());

        new Network().registerNetwork();
    }

    private void clientSetup(FMLClientSetupEvent event) {
        MinecraftForge.EVENT_BUS.register(new InventoryTabs());
        MinecraftForge.EVENT_BUS.register(new Tooltip());
        MinecraftForge.EVENT_BUS.register(new Keybind());
        MinecraftForge.EVENT_BUS.register(new Overlay());
    }
}
