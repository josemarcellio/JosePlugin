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

package com.josemarcellio.joseplugin.party.gui;

import com.josemarcellio.joseplugin.inventory.GUIItem;
import com.josemarcellio.joseplugin.inventory.GUIManager;
import com.josemarcellio.joseplugin.inventory.builder.GUIBuilder;
import com.josemarcellio.joseplugin.inventory.page.GUIPage;
import com.josemarcellio.joseplugin.item.ItemBuilderFactory;
import com.josemarcellio.joseplugin.party.manager.PartyManager;
import com.josemarcellio.joseplugin.skull.type.SkullType;
import com.josemarcellio.joseplugin.component.ComponentBuilder;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class PartyKickConfirmationGUI {

    private final PartyManager partyManager;
    private final ComponentBuilder componentBuilder = new ComponentBuilder();
    private final ItemBuilderFactory itemBuilderFactory = new ItemBuilderFactory();

    public PartyKickConfirmationGUI(PartyManager partyManager) {
        this.partyManager = partyManager;
    }

    public  void openGUI(Player player, OfflinePlayer member) {
        GUIBuilder builder = new GUIBuilder(componentBuilder.singleComponentBuilder().text("<aqua>Party Confirmation").build(), 27);

        addItem(builder, player, member);

        GUIPage page = builder.build();
        GUIManager.openGUI(player, page);
    }

    private void addItem(GUIBuilder builder, Player leader, OfflinePlayer member) {
        GUIItem leave = new GUIItem(itemBuilderFactory.createSkullItemBuilder("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODA2YTc3NjJlODFmZjFkNGYyODRlMWQwMGZhYWQwOTA4ZWM0OGNjZGU4NjRkZWJhZTVjYmU1M2RjMmFiZWEyYSJ9fX0=", SkullType.BASE64)
                .setName(componentBuilder.singleComponentBuilder().text("<green>Click to confirm</green>").build()).build(), event -> {
            partyManager.kickPlayer(leader, member);
            event.getWhoClicked().closeInventory();
        });
        builder.addItem(11, leave);

        GUIItem cancel = new GUIItem(itemBuilderFactory.createSkullItemBuilder("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzg3M2ZjZGUyOGQ2NzZhOGU1NzVmNzkzNjVmM2ViZWE0ZjFiMGZmMjg4NzZkODMxYjY1NTU1N2M1ZDllZTRjNiJ9fX0=", SkullType.BASE64)
                .setName(componentBuilder.singleComponentBuilder().text("<red>Click to cancel</red>").build()).build(), event -> {
            event.getWhoClicked().closeInventory();
            event.getWhoClicked().sendMessage(componentBuilder.singleComponentBuilder().text("<aqua> 🎉 <color:#fae7b5>Party <color:#c4c3d0>• <white>Batal mengeluarkan <aqua>" + member.getName() + " <white>dari party").build());
        });
        builder.addItem(15, cancel);
    }
}
