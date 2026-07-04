package com.ltcg.afkchatspammer.forge;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.api.distmarker.Dist;

@Mod(AfkChatSpammerForge.MOD_ID)
public class AfkChatSpammerForge {
    public static final String MOD_ID = "afkchatspammer";

    public AfkChatSpammerForge(FMLJavaModLoadingContext context) {
        if (FMLEnvironment.dist == Dist.CLIENT) {
            AfkChatSpammerForgeClient.init(context);
        }
    }
}
