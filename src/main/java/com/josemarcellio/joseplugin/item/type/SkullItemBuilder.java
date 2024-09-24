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
import com.josemarcellio.joseplugin.skull.manager.SkullManager;
import com.josemarcellio.joseplugin.skull.provider.ISkullProvider;
import com.josemarcellio.joseplugin.skull.type.SkullType;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class SkullItemBuilder extends BaseItemBuilder {

    public SkullItemBuilder(String textures, SkullType skullType) {
        super(Material.PLAYER_HEAD);
        ISkullProvider skullProvider;
        skullProvider = SkullManager.getSkullProvider(skullType);
        this.itemStack = skullProvider.getSkull(textures);
        this.itemMeta = itemStack.getItemMeta();
        if (this.itemMeta == null) {
            throw new JosePluginException("Failed to retrieve ItemMeta for this material: ", null);
        }
    }

    @Override
    public ItemStack build() {
        return super.build();
    }
}

