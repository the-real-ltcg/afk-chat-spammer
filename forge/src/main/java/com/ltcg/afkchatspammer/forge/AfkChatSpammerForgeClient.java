package com.ltcg.afkchatspammer.forge;

import com.ltcg.afkchatspammer.AfkChatSpammerCore;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.client.Minecraft;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraftforge.client.ConfigScreenHandler;
import net.minecraftforge.client.event.RegisterClientCommandsEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;

final class AfkChatSpammerForgeClient {
    static AfkChatSpammerCore CORE;

    private AfkChatSpammerForgeClient() {
    }

    static void init(FMLJavaModLoadingContext context) {
        var configPath = FMLPaths.CONFIGDIR.get().resolve("afkchatspammer.json");
        CORE = new AfkChatSpammerCore(configPath, message -> {
            var mc = Minecraft.getInstance();
            if (mc.player != null && mc.getConnection() != null) {
                mc.getConnection().sendChat(message);
            }
        });

        TickEvent.ClientTickEvent.Post.BUS.addListener(AfkChatSpammerForgeClient::onClientTick);
        RegisterClientCommandsEvent.BUS.addListener(AfkChatSpammerForgeClient::registerCommands);

        context.getContainer().registerExtensionPoint(ConfigScreenHandler.ConfigScreenFactory.class,
            () -> new ConfigScreenHandler.ConfigScreenFactory((minecraft, modListScreen) -> new AfkConfigScreen(modListScreen, CORE)));
    }

    private static void onClientTick(TickEvent.ClientTickEvent.Post event) {
        CORE.clientTick();
    }

    private static void registerCommands(RegisterClientCommandsEvent event) {
        event.getDispatcher().register(literal("afkspam")
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
            .executes(ctx -> feedback(ctx, CORE.status())));
    }

    private static LiteralArgumentBuilder<CommandSourceStack> literal(String name) {
        return LiteralArgumentBuilder.literal(name);
    }

    private static <T> RequiredArgumentBuilder<CommandSourceStack, T> argument(String name, ArgumentType<T> type) {
        return RequiredArgumentBuilder.argument(name, type);
    }

    private static int feedback(CommandContext<CommandSourceStack> ctx, String message) {
        ctx.getSource().sendSuccess(() -> Component.literal(message), false);
        return 1;
    }
}
