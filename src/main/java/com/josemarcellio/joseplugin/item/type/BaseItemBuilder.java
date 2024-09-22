package com.josemarcellio.joseplugin.item.type;

import com.josemarcellio.joseplugin.exception.JosePluginException;
import com.josemarcellio.joseplugin.item.IItemBuilder;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BaseItemBuilder implements IItemBuilder {
    protected ItemStack itemStack;
    protected ItemMeta itemMeta;

    public BaseItemBuilder(Material material) {
        if (material == Material.AIR) {
            throw new JosePluginException("Material cannot be AIR for ItemBuilder.", null);
        }
        this.itemStack = new ItemStack(material);
        this.itemMeta = itemStack.getItemMeta();
        if (this.itemMeta == null) {
            throw new JosePluginException("Failed to retrieve ItemMeta for material: " + material, null);
        }
    }

    @Override
    public IItemBuilder setName(Component name) {
        this.itemMeta.displayName(name);
        return this;
    }

    @Override
    public IItemBuilder addLore(Component... lore) {
        List<Component> loreList = this.itemMeta.lore() == null ? new ArrayList<>() : this.itemMeta.lore();
        loreList.addAll(Arrays.asList(lore));
        this.itemMeta.lore(loreList);
        return this;
    }

    @Override
    public IItemBuilder setLore(List<Component> lore) {
        this.itemMeta.lore(lore);
        return this;
    }

    @Override
    public IItemBuilder addEnchantment(Enchantment enchantment, int level) {
        this.itemMeta.addEnchant(enchantment, level, true);
        return this;
    }

    @Override
    public IItemBuilder hideFlags(ItemFlag... flags) {
        this.itemMeta.addItemFlags(flags);
        return this;
    }

    @Override
    public IItemBuilder setCustomModelData(int modelData) {
        this.itemMeta.setCustomModelData(modelData);
        return this;
    }

    @Override
    public IItemBuilder setUnbreakable(boolean unbreakable) {
        this.itemMeta.setUnbreakable(unbreakable);
        return this;
    }

    @Override
    public ItemStack build() {
        this.itemStack.setItemMeta(this.itemMeta);
        return this.itemStack;
    }
}
