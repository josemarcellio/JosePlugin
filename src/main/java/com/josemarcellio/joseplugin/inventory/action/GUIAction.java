package com.josemarcellio.joseplugin.inventory.action;

import org.bukkit.event.inventory.InventoryClickEvent;

@FunctionalInterface
public interface GUIAction {
    void onClick(InventoryClickEvent event);
}
