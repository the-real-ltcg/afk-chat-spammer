package com.ltcg.afkchatspammer.forge;

import com.ltcg.afkchatspammer.AfkChatSpammerCore;
import com.ltcg.afkchatspammer.AfkConfig;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Checkbox;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

final class AfkConfigScreen extends Screen {
    private final Screen parent;
    private final AfkChatSpammerCore core;

    private Checkbox enabledCheckbox;
    private EditBox messageBox;
    private EditBox intervalBox;

    AfkConfigScreen(Screen parent, AfkChatSpammerCore core) {
        super(Component.literal("AFK Chat Spammer"));
        this.parent = parent;
        this.core = core;
    }

    @Override
    protected void init() {
        AfkConfig config = core.config();
        int centerX = this.width / 2;

        this.enabledCheckbox = Checkbox.builder(Component.literal("Enabled"), this.font)
            .pos(centerX - 75, 40)
            .selected(config.enabled)
            .build();
        this.addRenderableWidget(this.enabledCheckbox);

        this.messageBox = new EditBox(this.font, centerX - 100, 75, 200, 20, Component.literal("Message"));
        this.messageBox.setMaxLength(256);
        this.messageBox.setValue(config.message);
        this.addRenderableWidget(this.messageBox);

        this.intervalBox = new EditBox(this.font, centerX - 100, 110, 200, 20, Component.literal("Interval (seconds)"));
        this.intervalBox.setMaxLength(6);
        this.intervalBox.setValue(Integer.toString(config.intervalSeconds));
        this.addRenderableWidget(this.intervalBox);

        this.addRenderableWidget(Button.builder(Component.literal("Done"), button -> this.onDone())
            .pos(centerX - 100, 145)
            .size(95, 20)
            .build());

        this.addRenderableWidget(Button.builder(Component.literal("Cancel"), button -> this.onClose())
            .pos(centerX + 5, 145)
            .size(95, 20)
            .build());
    }

    private void onDone() {
        AfkConfig config = core.config();
        config.enabled = this.enabledCheckbox.selected();
        config.message = this.messageBox.getValue();
        try {
            config.intervalSeconds = Math.max(1, Integer.parseInt(this.intervalBox.getValue().trim()));
        } catch (NumberFormatException ignored) {
            // Keep the previous interval if the field doesn't parse as a number.
        }
        core.resetTimer();
        core.save();
        this.onClose();
    }

    @Override
    public void onClose() {
        this.minecraft.setScreenAndShow(this.parent);
    }
}
