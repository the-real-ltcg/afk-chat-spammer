package com.ltcg.afkchatspammer.fabric;

import com.ltcg.afkchatspammer.AfkConfig;
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.network.chat.Component;

public class AfkChatSpammerModMenu implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> {
            AfkConfig config = AfkChatSpammerFabric.CORE.config();

            ConfigBuilder builder = ConfigBuilder.create()
                .setParentScreen(parent)
                .setTitle(Component.literal("AFK Chat Spammer"))
                .setSavingRunnable(() -> {
                    AfkChatSpammerFabric.CORE.resetTimer();
                    AfkChatSpammerFabric.CORE.save();
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
        };
    }
}
