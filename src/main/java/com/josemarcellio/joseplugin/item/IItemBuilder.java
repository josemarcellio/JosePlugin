package com.josemarcellio.joseplugin.item;

import net.kyori.adventure.text.Component;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import java.util.List;


public interface IItemBuilder {
    IItemBuilder setName(Component name);
    IItemBuilder addLore(Component... lore);
    IItemBuilder setLore(List<Component> lore);
    IItemBuilder addEnchantment(Enchantment enchantment, int level);
    IItemBuilder hideFlags(ItemFlag... flags);
    IItemBuilder setCustomModelData(int modelData);
    IItemBuilder setUnbreakable(boolean unbreakable);
    ItemStack build();
}
