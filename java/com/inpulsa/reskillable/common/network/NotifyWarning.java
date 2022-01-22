package com.inpulsa.reskillable.common.network;

import com.inpulsa.reskillable.Network;
import com.inpulsa.reskillable.client.Overlay;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent.Context;
import net.minecraftforge.network.PacketDistributor;

import java.util.function.Supplier;

public class NotifyWarning {
    private final ResourceLocation resource;

    public NotifyWarning(ResourceLocation resource) {
        this.resource = resource;
    }

    public NotifyWarning(FriendlyByteBuf buffer) {
        this.resource = buffer.readResourceLocation();
    }

    public void encode(FriendlyByteBuf buffer) {
        buffer.writeResourceLocation(resource);
    }

    public void handle(Supplier<Context> context) {
        context.get().enqueueWork(() -> {
            Overlay.showWarning(this.resource);
        });
        context.get().setPacketHandled(true);
    }

    public static void send(Player player, ResourceLocation resource) {
        Network.NETWORK.send(
                PacketDistributor.PLAYER.with(() -> (ServerPlayer)player),
                new NotifyWarning(resource)
        );
    }
}
