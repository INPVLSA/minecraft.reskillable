package com.inpulsa.reskillable.common.commands;

import com.inpulsa.reskillable.Configuration;
import com.inpulsa.reskillable.common.capabilities.SkillModel;
import com.inpulsa.reskillable.common.network.SyncToClient;
import com.inpulsa.reskillable.common.skills.Skill;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.server.command.EnumArgument;

public class SetCommand {
    public SetCommand() {
    }

    static ArgumentBuilder<CommandSourceStack, ?> register() {
        return Commands.literal("set")
                .then(
                    Commands.argument("player", EntityArgument.player())
                        .then(Commands.argument("skill", EnumArgument.enumArgument(Skill.class)))
                        .then(Commands.argument("level", IntegerArgumentType.integer(1, Configuration.getMaxLevel())))
                ).executes(SetCommand::execute);
    }

    private static int execute(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        ServerPlayer player = EntityArgument.getPlayer(context, "player");
        Skill skill = context.getArgument("skill", Skill.class);
        int level = IntegerArgumentType.getInteger(context, "level");
        SkillModel.get(player).setSkillLevel(skill, level);
        SyncToClient.send(player);

        return 1;
    }
}
