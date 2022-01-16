package com.inpulsa.reskillable.common.commands;

import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class Commands {
    public Commands() {
    }

    @SuppressWarnings("rawtypes")
    @SubscribeEvent
    public void onRegisterCommands(RegisterCommandsEvent event) {
        LiteralArgumentBuilder<Object> argumentBuilder = LiteralArgumentBuilder.literal("skills");
//        argumentBuilder.requires(Commands::registerCallback);
//        argumentBuilder.then((ArgumentBuilder)SetCommand.register());
//        argumentBuilder.then((ArgumentBuilder)GetCommand.register());
    }

//    private static boolean registerCallback(Object source) {
//        return source.hasPermissionLevel(2);
//    }
}
