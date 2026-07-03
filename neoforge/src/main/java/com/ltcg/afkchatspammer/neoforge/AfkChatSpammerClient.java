package com.ltcg.afkchatspammer.neoforge;

import com.ltcg.afkchatspammer.AfkChatSpammerCore;
import com.ltcg.afkchatspammer.AfkConfig;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.loading.FMLPaths;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RegisterClientCommandsEvent;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.common.NeoForge;

final class AfkChatSpammerClient {
    static AfkChatSpammerCore CORE;

    private AfkChatSpammerClient() {
    }

    static void init(ModContainer modContainer) {
        var configPath = FMLPaths.CONFIGDIR.get().resolve("afkchatspammer.json");
        CORE = new AfkChatSpammerCore(configPath, message -> {
            var mc = Minecraft.getInstance();
            if (mc.player != null && mc.getConnection() != null) {
                mc.getConnection().sendChat(message);
            }
        });

        NeoForge.EVENT_BUS.addListener(ClientTickEvent.Post.class, event -> CORE.clientTick());
        NeoForge.EVENT_BUS.addListener(RegisterClientCommandsEvent.class, AfkChatSpammerClient::registerCommands);

        modContainer.registerExtensionPoint(IConfigScreenFactory.class,
            (minecraft, modListScreen) -> buildConfigScreen(modListScreen));
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

    private static Screen buildConfigScreen(Screen parent) {
        AfkConfig config = CORE.config();

        ConfigBuilder builder = ConfigBuilder.create()
            .setParentScreen(parent)
            .setTitle(Component.literal("AFK Chat Spammer"))
            .setSavingRunnable(() -> {
                CORE.resetTimer();
                CORE.save();
            });

        ConfigCategory category = builder.getOrCreateCategory(Component.literal("General"));
        ConfigEntryBuilder entryBuilder = builder.entryBuilder();

        category.addEntry(entryBuilder.startBooleanToggle(Component.literal("Enabled"), config.enabled)
            .setDefaultValue(false)
            .setSaveConsumer(value -> config.enabled = value)
            .build());

        category.addEntry(entryBuilder.startStrField(Component.literal("Message"), config.message)
            .setDefaultValue("I'm currently AFK, I'll be back soon!")
            .setSaveConsumer(value -> config.message = value)
            .build());

        category.addEntry(entryBuilder.startIntField(Component.literal("Interval (seconds)"), config.intervalSeconds)
            .setDefaultValue(60)
            .setMin(1)
            .setSaveConsumer(value -> config.intervalSeconds = value)
            .build());

        return builder.build();
    }
}
