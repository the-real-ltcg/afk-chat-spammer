package com.ltcg.afkchatspammer;

import java.nio.file.Path;

public class AfkChatSpammerCore {
    private final AfkConfig config;
    private final ChatSender sender;
    private final Path configPath;
    private long ticksUntilNextSend;

    public AfkChatSpammerCore(Path configPath, ChatSender sender) {
        this.configPath = configPath;
        this.config = AfkConfig.load(configPath);
        this.sender = sender;
        resetTimer();
    }

    public void clientTick() {
        if (!config.enabled) {
            return;
        }
        if (ticksUntilNextSend <= 0) {
            sender.send(config.message);
            resetTimer();
        } else {
            ticksUntilNextSend--;
        }
    }

    public void resetTimer() {
        ticksUntilNextSend = config.intervalSeconds * 20L;
    }

    public AfkConfig config() {
        return config;
    }

    public void save() {
        config.save(configPath);
    }

    public String setEnabled(boolean enabled) {
        config.enabled = enabled;
        resetTimer();
        save();
        return enabled ? "AFK chat spammer enabled." : "AFK chat spammer disabled.";
    }

    public String toggle() {
        return setEnabled(!config.enabled);
    }

    public String setMessage(String message) {
        config.message = message;
        save();
        return "AFK message set to: " + message;
    }

    public String setInterval(int seconds) {
        if (seconds < 1) {
            return "Interval must be at least 1 second.";
        }
        config.intervalSeconds = seconds;
        resetTimer();
        save();
        return "AFK interval set to " + seconds + " second(s).";
    }

    public String status() {
        return String.format("AFK chat spammer is %s. Message: \"%s\". Interval: %ds.",
                config.enabled ? "enabled" : "disabled", config.message, config.intervalSeconds);
    }
}
