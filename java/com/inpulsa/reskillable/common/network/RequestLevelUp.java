package com.inpulsa.reskillable.common.network;

import com.inpulsa.reskillable.Configuration;
import com.inpulsa.reskillable.Network;
import com.inpulsa.reskillable.common.capabilities.SkillModel;
import com.inpulsa.reskillable.common.skills.Skill;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent.Context;

import java.util.function.Supplier;

public class RequestLevelUp {
    private final int skill;

    public RequestLevelUp(Skill skill) {
        this.skill = skill.index;
    }

    public RequestLevelUp(FriendlyByteBuf buffer) {
        this.skill = buffer.readInt();
    }

    public void encode(FriendlyByteBuf buffer) {
        buffer.writeInt(this.skill);
    }

    public void handle(Supplier<Context> context) {
        context.get().enqueueWork(() -> {
            ServerPlayer player = context.get().getSender();

            if (player != null) {
                SkillModel skillModel = SkillModel.get(player);
                Skill skill = Skill.values()[this.skill];
                int skillLevel = skillModel.getSkillLevel(skill);
                int cost = this.calculateLevelUpCost(skillLevel);

                if (this.isLevelCanBeUpped(player, skillLevel, cost)) {
                    if (!player.isCreative()) {
                        player.setExperienceLevels(-cost);
                    }

                    skillModel.increaseSkillLevel(skill);
                    SyncToClient.send(player);
                }
            }


        });
        (context.get()).setPacketHandled(true);
    }

    private int calculateLevelUpCost(int skillLevel) {
        return Configuration.getStartCost() + (skillLevel - 1) * Configuration.getCostIncrease();
    }

    private boolean isLevelCanBeUpped(ServerPlayer player, int skillLevel, int cost) {
        return skillLevel < Configuration.getMaxLevel() && (player.isCreative() || player.experienceLevel >= cost);
    }

    public static void send(Skill skill) {
        Network.NETWORK.sendToServer(new RequestLevelUp(skill));
    }
}
