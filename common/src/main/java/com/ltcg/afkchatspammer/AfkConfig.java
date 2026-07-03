package com.ltcg.afkchatspammer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class AfkConfig {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public String message = "I'm currently AFK, I'll be back soon!";
    public int intervalSeconds = 60;
    public boolean enabled = false;

    public static AfkConfig load(Path path) {
        if (Files.exists(path)) {
            try (var reader = Files.newBufferedReader(path)) {
                AfkConfig loaded = GSON.fromJson(reader, AfkConfig.class);
                if (loaded != null) {
                    return loaded.sanitize();
                }
            } catch (IOException e) {
                System.err.println("[AfkChatSpammer] Failed to read config, using defaults: " + e.getMessage());
            }
        }
        AfkConfig defaults = new AfkConfig();
        defaults.save(path);
        return defaults;
    }

    public void save(Path path) {
        try {
            Files.createDirectories(path.getParent());
            try (var writer = Files.newBufferedWriter(path)) {
                GSON.toJson(this, writer);
            }
        } catch (IOException e) {
            System.err.println("[AfkChatSpammer] Failed to save config: " + e.getMessage());
        }
    }

    private AfkConfig sanitize() {
        if (intervalSeconds < 1) {
            intervalSeconds = 1;
        }
        if (message == null) {
            message = "";
        }
        return this;
    }
}
