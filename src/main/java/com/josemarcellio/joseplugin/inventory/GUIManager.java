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
