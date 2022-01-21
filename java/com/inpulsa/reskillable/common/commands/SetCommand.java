package com.inpulsa.reskillable.common.commands;

import com.inpulsa.reskillable.Configuration;
import com.inpulsa.reskillable.common.capabilities.SkillModel;
import com.inpulsa.reskillable.common.network.SyncToClient;
import com.inpulsa.reskillable.common.skills.Skill;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.selector.EntitySelector;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.server.command.EnumArgument;

public class SetCommand {
    public SetCommand() {
    }

    static LiteralArgumentBuilder<CommandSourceStack> register() {
        RequiredArgumentBuilder<CommandSourceStack, EntitySelector> playerArg =
                Commands.argument("player", EntityArgument.player());
        RequiredArgumentBuilder<CommandSourceStack, Skill> skillArg =
                Commands.argument("skill", EnumArgument.enumArgument(Skill.class));
        RequiredArgumentBuilder<CommandSourceStack, Integer> levelArg =
                Commands.argument("level", IntegerArgumentType.integer(1, Configuration.getMaxLevel()));

        return Commands.literal("set").then(
                playerArg.then(
                        skillArg.then(
                                levelArg.executes(SetCommand::execute)
                        )
                )
        );
    }

    private static int execute(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        ServerPlayer player = EntityArgument.getPlayer(context, "player");
        Skill skill = context.getArgument("skill", Skill.class);

        int level = IntegerArgumentType.getInteger(context, "level");
        SkillModel.get(player).setSkillLevel(skill, level);
        SyncToClient.send(player);

        TranslatableComponent message = new TranslatableComponent("Skill level was updated");
        context.getSource().sendSuccess(message, false);

        return 1;
    }
}
