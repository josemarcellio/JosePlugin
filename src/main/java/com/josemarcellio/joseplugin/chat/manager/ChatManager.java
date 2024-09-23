package com.josemarcellio.joseplugin.chat.manager;

import com.josemarcellio.joseplugin.chat.render.CustomChatRenderer;
import com.josemarcellio.joseplugin.cooldown.ICooldownManager;
import com.josemarcellio.joseplugin.component.module.SingleComponentBuilder;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

import java.util.UUID;

public class ChatManager {

    private final ICooldownManager cooldownManager;
    private final String chatFormat;
    private final String warningMessage;
    private final int cooldownTime;

    public ChatManager(ICooldownManager cooldownManager) {
        this.cooldownManager = cooldownManager;
        this.chatFormat = "<hover:show_text:\"<gray>Profile:</gray> <aqua>%player_name%</aqua><newline><gray>Player UID:</gray> <aqua>%player_uuid%</aqua>\"><aqua><player></aqua> <white><message></white>";
        this.warningMessage = "<red> ⚠ <color:#fae7b5>Alert <color:#c4c3d0>• <white>Tolong jangan spam!";
        this.cooldownTime = 3 * 1000;
    }

    public boolean handleChat(Player player) {
        UUID playerUUID = player.getUniqueId();
        String action = "chat_cooldown";

        if (!cooldownManager.isOnCooldown(playerUUID, action)) {
            cooldownManager.startCooldown(playerUUID, action, cooldownTime);
            return true;
        } else {
            sendCooldownMessage(player);
            return false;
        }
    }

    public CustomChatRenderer getChatRenderer() {
        return new CustomChatRenderer(chatFormat, cooldownManager);
    }

    private void sendCooldownMessage(Player player) {
        Component cooldownMessage = new SingleComponentBuilder()
                .text(warningMessage)
                .build();
        player.sendMessage(cooldownMessage);
    }
}
