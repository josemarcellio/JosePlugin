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

package com.josemarcellio.joseplugin.inventory.builder;

import com.josemarcellio.joseplugin.component.ComponentBuilder;
import com.josemarcellio.joseplugin.inventory.GUIItem;
import com.josemarcellio.joseplugin.inventory.page.GUIPage;
import com.josemarcellio.joseplugin.item.ItemBuilderFactory;
import com.josemarcellio.joseplugin.skull.type.SkullType;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class GUIBuilder {
    private final GUIPage page;
    private final ItemBuilderFactory itemBuilderFactory = new ItemBuilderFactory();
    private final ComponentBuilder componentBuilder = new ComponentBuilder();

    public GUIBuilder(Component title, int size) {
        this.page = new GUIPage(title, size);
    }

    public void addItem(int slot, GUIItem item) {
        page.setItem(slot, item);
    }

    public GUIPage build() {
        return page;
    }

    public void addGlassPane() {
        ItemStack glassItem = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
        ItemMeta meta = glassItem.getItemMeta();
        meta.displayName(Component.text(" "));
        glassItem.setItemMeta(meta);

        int[] glassSlots = {
                0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 17, 18, 26, 27, 35,
                36, 44, 45, 46, 47, 48, 50, 51, 52, 53
        };

        for (int slot : glassSlots) {
            addItem(slot, new GUIItem(glassItem, null));
        }
    }

    public void addCloseItem() {
        GUIItem guiClose = new GUIItem(itemBuilderFactory.createSkullItemBuilder(
                        "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYmViNTg4YjIxYTZmOThhZDFmZjRlMDg1YzU1MmRjYjA1MGVmYzljYWI0MjdmNDYwNDhmMThmYzgwMzQ3NWY3In19fQ==", SkullType.BASE64)
                .setName(componentBuilder.singleComponentBuilder().text("<red>Close").build())
                .build(), event -> event.getWhoClicked().closeInventory());

        addItem(49, guiClose);
    }
}