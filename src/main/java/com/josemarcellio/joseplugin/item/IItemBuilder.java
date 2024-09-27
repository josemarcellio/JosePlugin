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
