package com.inpulsa.reskillable;

import com.inpulsa.reskillable.common.network.NotifyWarning;
import com.inpulsa.reskillable.common.network.RequestLevelUp;
import com.inpulsa.reskillable.common.network.SyncToClient;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.Optional;

public class Network {
    public static SimpleChannel NETWORK;

    public void registerNetwork() {
        var resourceLocation = new ResourceLocation(Reskillable.MOD_ID, "main_channel");

        NETWORK = NetworkRegistry.newSimpleChannel(
                resourceLocation,
                () -> "1.0",
                (s) -> true,
                (s) -> true
        );
        NETWORK.registerMessage(
                1,
                SyncToClient.class,
                SyncToClient::encode,
                SyncToClient::new,
                SyncToClient::handle,
                Optional.of(NetworkDirection.PLAY_TO_CLIENT)
        );
        NETWORK.registerMessage(
                2,
                RequestLevelUp.class,
                RequestLevelUp::encode,
                RequestLevelUp::new,
                RequestLevelUp::handle,
                Optional.of(NetworkDirection.PLAY_TO_SERVER)
        );
        NETWORK.registerMessage(
                3,
                NotifyWarning.class,
                NotifyWarning::encode,
                NotifyWarning::new,
                NotifyWarning::handle,
                Optional.of(NetworkDirection.PLAY_TO_CLIENT)
        );
    }
}
