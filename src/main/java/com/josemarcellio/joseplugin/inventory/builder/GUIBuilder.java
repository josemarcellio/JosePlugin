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

import com.josemarcellio.joseplugin.inventory.GUIItem;
import com.josemarcellio.joseplugin.inventory.page.GUIPage;
import net.kyori.adventure.text.Component;

public class GUIBuilder {
    private final GUIPage page;

    public GUIBuilder(Component title, int size) {
        this.page = new GUIPage(title, size);
    }

    public void addItem(int slot, GUIItem item) {
        page.setItem(slot, item);
    }

    public GUIPage build() {
        return page;
    }
}