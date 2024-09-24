/*
 * Copyright (C) 2024 Jose Marcellio
 * GitHub: https://github.com/josemarcellio
 *
 * This software is open-source and distributed under the GNU General Public License (GPL), version 3.
 * You are free to modify, share, and distribute it as long as the same freedoms are preserved.
 *
 * No warranties are provided with this software. It is distributed in the hope that it will be useful,
 * but WITHOUT ANY IMPLIED WARRANTIES, including but not limited to the implied warranties of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 *
 * For more details, refer to the full license at <https://www.gnu.org/licenses/>.
 */

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
