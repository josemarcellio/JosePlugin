package com.josemarcellio.joseplugin.inventory;

import com.josemarcellio.joseplugin.inventory.action.GUIAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class GUIItem {
    private final ItemStack itemStack;
    private final GUIAction action;

    public GUIItem(ItemStack itemStack, GUIAction action) {
        this.itemStack = itemStack;
        this.action = action;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public void executeAction(InventoryClickEvent event) {
        if (action != null) {
            action.onClick(event);
        }
    }
}
