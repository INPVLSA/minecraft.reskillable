package com.inpulsa.reskillable.common.network;

import com.inpulsa.reskillable.Network;
import com.inpulsa.reskillable.common.capabilities.SkillModel;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent.Context;
import net.minecraftforge.network.PacketDistributor;

import java.util.function.Supplier;

public class SyncToClient {
    private final SkillModel skillModel;

    public SyncToClient(SkillModel skillModel) {
        this.skillModel = skillModel;
    }

    public SyncToClient(FriendlyByteBuf buffer) {
        this.skillModel = SkillModel.get();
        CompoundTag nbt = buffer.readNbt();

        if (nbt != null) {
            this.skillModel.deserializeNBT(nbt);
        }
    }

    public void encode(FriendlyByteBuf buffer) {
        buffer.writeNbt(this.skillModel.serializeNBT());
    }

    public void handle(Supplier<Context> context) {
        context.get().enqueueWork(() -> SkillModel.get().deserializeNBT(this.skillModel.serializeNBT()));
        context.get().setPacketHandled(true);
    }

    public static void send(Player player) {
        Network.NETWORK.send(PacketDistributor.PLAYER.with(() -> {

            return (ServerPlayer)player;
        }), new SyncToClient(SkillModel.get(player)));
    }
}