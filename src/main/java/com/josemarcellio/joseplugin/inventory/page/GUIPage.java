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

package com.josemarcellio.joseplugin.inventory.page;

import com.josemarcellio.joseplugin.cooldown.CooldownManager;
import com.josemarcellio.joseplugin.cooldown.ICooldownManager;
import com.josemarcellio.joseplugin.inventory.GUIItem;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class GUIPage {
    private final Component title;
    private final int size;
    private final Map<Integer, GUIItem> items = new HashMap<>();
    private final ICooldownManager cooldownManager;
    private final int cooldownTime;

    public GUIPage(Component title, int size) {
        this.title = title;
        this.size = size;
        this.cooldownManager = new CooldownManager();
        this.cooldownTime = 1000;
    }

    public void setItem(int slot, GUIItem item) {
        items.put(slot, item);
    }

    public Inventory build() {
        Inventory inventory = Bukkit.createInventory(null, size, title);
        for (Map.Entry<Integer, GUIItem> entry : items.entrySet()) {
            inventory.setItem(entry.getKey(), entry.getValue().getItemStack());
        }
        return inventory;
    }

    public void handleClick(InventoryClickEvent event) {
        int slot = event.getSlot();
        if (items.containsKey(slot)) {
            GUIItem item = items.get(slot);
            String action = "inventory";
            UUID playerUUID = event.getWhoClicked().getUniqueId();
            if (!cooldownManager.isOnCooldown(playerUUID, action)) {
                cooldownManager.startCooldown(playerUUID, action, cooldownTime);
                item.executeAction(event);
            }
        }
    }
}
