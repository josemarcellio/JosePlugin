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

package com.josemarcellio.joseplugin.chat;

import com.josemarcellio.joseplugin.chat.manager.ChatManager;
import com.josemarcellio.joseplugin.cooldown.ICooldownManager;
import io.papermc.paper.event.player.AsyncChatEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.entity.Player;

public class PlayerChatListener implements Listener {

    private final ICooldownManager cooldownManager;

    public PlayerChatListener(ICooldownManager cooldownManager) {
        this.cooldownManager = cooldownManager;
    }

    @SuppressWarnings("unused")
    @EventHandler
    public void onChat(AsyncChatEvent event) {
        Player player = event.getPlayer();

        ChatManager chatManager = new ChatManager(cooldownManager);

        if (chatManager.handleChat(player)) {
            event.renderer(chatManager.getChatRenderer());
        } else {
            event.setCancelled(true);
        }
    }
}
