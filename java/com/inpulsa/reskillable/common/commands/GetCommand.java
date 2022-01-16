package com.inpulsa.reskillable.common.commands;

import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.inpulsa.reskillable.common.capabilities.SkillModel;
import com.inpulsa.reskillable.common.skills.Skill;
import net.minecraft.Util;
import net.minecraft.commands.CommandSource;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;

import net.minecraftforge.server.command.EnumArgument;

public class GetCommand {
    public GetCommand() {
    }

    static ArgumentBuilder<CommandSourceStack, ?> register() {
        return Commands.literal("get").then(
                Commands.argument("player", EntityArgument.player())
                        .then(Commands.argument("skill", EnumArgument.enumArgument(Skill.class))
        ).executes(GetCommand::execute));
    }

    private static int execute(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        ServerPlayer player = EntityArgument.getPlayer(context, "player");
        Skill skill = context.getArgument("skill", Skill.class);
        int level = SkillModel.get(player).getSkillLevel(skill);
        TranslatableComponent component = new TranslatableComponent(skill.displayName);

        ((CommandSource)context.getSource()).sendMessage(component, Util.NIL_UUID);
        return level;
    }
}
