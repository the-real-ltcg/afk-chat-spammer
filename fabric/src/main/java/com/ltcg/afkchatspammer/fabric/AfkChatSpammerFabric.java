package com.ltcg.afkchatspammer.fabric;

import com.ltcg.afkchatspammer.AfkChatSpammerCore;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommands.argument;
import static net.fabricmc.fabric.api.client.command.v2.ClientCommands.literal;

public class AfkChatSpammerFabric implements ClientModInitializer {
    public static AfkChatSpammerCore CORE;

    @Override
    public void onInitializeClient() {
        var configPath = FabricLoader.getInstance().getConfigDir().resolve("afkchatspammer.json");
        CORE = new AfkChatSpammerCore(configPath, message -> {
            var mc = Minecraft.getInstance();
            if (mc.player != null && mc.getConnection() != null) {
                mc.getConnection().sendChat(message);
            }
        });

        ClientTickEvents.END_CLIENT_TICK.register(client -> CORE.clientTick());

        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> dispatcher.register(literal("afkspam")
            .then(literal("on").executes(ctx -> feedback(ctx, CORE.setEnabled(true))))
            .then(literal("off").executes(ctx -> feedback(ctx, CORE.setEnabled(false))))
            .then(literal("toggle").executes(ctx -> feedback(ctx, CORE.toggle())))
            .then(literal("status").executes(ctx -> feedback(ctx, CORE.status())))
            .then(literal("message")
                .then(argument("text", StringArgumentType.greedyString())
                    .executes(ctx -> feedback(ctx, CORE.setMessage(StringArgumentType.getString(ctx, "text"))))))
            .then(literal("interval")
                .then(argument("seconds", IntegerArgumentType.integer(1))
                    .executes(ctx -> feedback(ctx, CORE.setInterval(IntegerArgumentType.getInteger(ctx, "seconds"))))))
            .executes(ctx -> feedback(ctx, CORE.status()))));
    }

    private static int feedback(CommandContext<FabricClientCommandSource> ctx, String message) {
        ctx.getSource().sendFeedback(Component.literal(message));
        return 1;
    }
}
