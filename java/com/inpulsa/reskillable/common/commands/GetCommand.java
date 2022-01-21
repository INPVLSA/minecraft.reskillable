package com.inpulsa.reskillable.common.commands;

import com.inpulsa.reskillable.common.capabilities.SkillModel;
import com.inpulsa.reskillable.common.skills.Skill;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GetCommand {
    public GetCommand() {
    }

    static LiteralArgumentBuilder<CommandSourceStack> register() {
        RequiredArgumentBuilder<CommandSourceStack, EntitySelector> playerArg =
                Commands.argument("player", EntityArgument.player());
        RequiredArgumentBuilder<CommandSourceStack, Skill> skillArg =
                Commands.argument("skill", EnumArgument.enumArgument(Skill.class));

        return Commands.literal("get").then(
                playerArg.then(
                        skillArg.executes(GetCommand::execute)
                ).executes(GetCommand::executeWithoutSkill)
        );
    }

    private static int execute(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        ServerPlayer player = EntityArgument.getPlayer(context, "player");
        Skill skill = context.getArgument("skill", Skill.class);
        int level = SkillModel.get(player).getSkillLevel(skill);
        TranslatableComponent component = new TranslatableComponent(skill.displayName);
        context.getSource().sendSuccess(
            component.append(": " + level),
            false
        );

        return level;
    }

    private static int executeWithoutSkill(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        ServerPlayer player = EntityArgument.getPlayer(context, "player");
        List<Skill> skills = new ArrayList<>(List.of());
        skills.addAll(Arrays.asList(Skill.values()));

        for (Skill skill: skills) {
            int level = SkillModel.get(player).getSkillLevel(skill);
            TranslatableComponent component = new TranslatableComponent(skill.displayName);
            context.getSource().sendSuccess(
                    component.append(": " + level),
                    false
            );
        }

        return 0;
    }
}
