package com.josemarcellio.joseplugin.chat;

import com.josemarcellio.joseplugin.chat.manager.ChatManager;
import io.papermc.paper.event.player.AsyncChatEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.entity.Player;

@SuppressWarnings("unused")
public class PlayerChatListener implements Listener {

    private final ChatManager chatManager = new ChatManager();

    @EventHandler
    public void onChat(AsyncChatEvent event) {
        Player player = event.getPlayer();

        if (chatManager.handleChat(player)) {
            event.renderer(chatManager.getChatRenderer());
        } else {
            event.setCancelled(true);
        }
    }
}
