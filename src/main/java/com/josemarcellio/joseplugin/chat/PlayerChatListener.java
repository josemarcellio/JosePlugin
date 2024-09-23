package com.josemarcellio.joseplugin.chat;

import com.josemarcellio.joseplugin.chat.manager.ChatManager;
import com.josemarcellio.joseplugin.cooldown.ICooldownManager;
import io.papermc.paper.event.player.AsyncChatEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.entity.Player;

@SuppressWarnings("unused")
public class PlayerChatListener implements Listener {

    private final ICooldownManager cooldownManager;

    public PlayerChatListener(ICooldownManager cooldownManager) {
        this.cooldownManager = cooldownManager;
    }

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
