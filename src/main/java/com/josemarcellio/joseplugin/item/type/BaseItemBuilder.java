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
    protected Material material;

    public BaseItemBuilder(Material material) {
        if (material == Material.AIR) {
            throw new JosePluginException("Material cannot be AIR for ItemBuilder.", null);
        }
        this.material = material;
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
        if (loreList == null) {
            throw new JosePluginException("Lore components cannot be null", null);
        }
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
