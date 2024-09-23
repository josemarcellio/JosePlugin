package com.josemarcellio.joseplugin.inventory;

import com.josemarcellio.joseplugin.exception.JosePluginException;
import com.josemarcellio.joseplugin.inventory.page.GUIPage;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class GUIManager implements Listener {
    private static final Map<UUID, GUIPage> openGUIs = new HashMap<>();

    public static void openGUI(Player player, GUIPage page) {
        try {
        Inventory inventory = page.build();
        openGUIs.put(player.getUniqueId(), page);
        player.openInventory(inventory);
        } catch (Exception e) {
            throw new JosePluginException("Failed to open GUI for player: " + player.getName(), e);
        }
    }

    @SuppressWarnings("unused")
    @EventHandler
    public void handleClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        UUID playerUUID = player.getUniqueId();
        if (openGUIs.containsKey(playerUUID)) {
            GUIPage page = openGUIs.get(playerUUID);
            event.setCancelled(true);
            try {
            page.handleClick(event);
            } catch (Exception e) {
                throw new JosePluginException("Error handling click event for player: " + player.getName(), e);
            }
        }
    }

    @SuppressWarnings("unused")
    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        Player player = (Player) event.getPlayer();
        openGUIs.remove(player.getUniqueId());
    }
}
