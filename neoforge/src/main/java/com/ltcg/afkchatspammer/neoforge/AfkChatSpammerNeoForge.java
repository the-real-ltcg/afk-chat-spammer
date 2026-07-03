package com.ltcg.afkchatspammer.neoforge;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLEnvironment;

@Mod(AfkChatSpammerNeoForge.MOD_ID)
public class AfkChatSpammerNeoForge {
    public static final String MOD_ID = "afkchatspammer";

    public AfkChatSpammerNeoForge(IEventBus modEventBus, ModContainer modContainer) {
        // This mod is purely client-side (auto-typing chat). Client-only classes are kept in
        // AfkChatSpammerClient and only ever touched when running on the client physical side.
        if (FMLEnvironment.getDist() == Dist.CLIENT) {
            AfkChatSpammerClient.init(modContainer);
        }
    }
}
