package com.inpulsa.reskillable.common.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class Commands {
    public Commands() {
    }

    @SubscribeEvent
    public void onRegisterCommands(RegisterCommandsEvent event) {
        CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();

        LiteralArgumentBuilder<CommandSourceStack> commands = LiteralArgumentBuilder.literal("skill");

        commands.then(SetCommand.register());
        commands.then(GetCommand.register());

        dispatcher.register(commands);
    }

    private static boolean registerRequirement(Object source) {
        return true;
//        return source.hasPermissionLevel(2);
    }
}
