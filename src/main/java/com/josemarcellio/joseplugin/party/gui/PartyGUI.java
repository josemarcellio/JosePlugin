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
import com.josemarcellio.joseplugin.party.manager.Party;
import com.josemarcellio.joseplugin.party.manager.PartyManager;
import com.josemarcellio.joseplugin.skull.type.SkullType;
import com.josemarcellio.joseplugin.component.ComponentBuilder;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class PartyGUI {

    private final int ITEMS_PER_PAGE = 28;
    private final int[] SLOT_INDEXES = {
            10, 11, 12, 13, 14, 15, 16, 19, 20, 21, 22, 23, 24, 25, 28, 29, 30, 31, 32, 33, 34, 37, 38, 39, 40, 41, 42, 43
    };
    private final ComponentBuilder componentBuilder = new ComponentBuilder();
    private final ItemBuilderFactory itemBuilderFactory = new ItemBuilderFactory();
    private final PartyManager partyManager;

    public PartyGUI(PartyManager partyManager) {
        this.partyManager = partyManager;
    }

    public void open(Player player, int page) {
        GUIBuilder guiBuilder = new GUIBuilder(componentBuilder.singleComponentBuilder().text("<aqua>Party GUI</aqua>").build(), 54);
        createPartyPane(guiBuilder, player, page);
        createNavigationPane(guiBuilder, player, page);

        guiBuilder.addGlassPane();
        guiBuilder.addCloseItem();

        GUIPage pageGUI = guiBuilder.build();
        GUIManager.openGUI(player, pageGUI);
    }

    private void createPartyPane(GUIBuilder guiBuilder, Player player, int page) {
        if (partyManager.isPlayerInParty(player.getUniqueId())) {
            Party party = partyManager.getPlayerParty(player.getUniqueId());
            List<UUID> members = party.getMembers().stream().toList();
            int startIndex = (page - 1) * ITEMS_PER_PAGE;
            int endIndex = Math.min(startIndex + ITEMS_PER_PAGE, members.size());

            for (int i = startIndex; i < endIndex; i++) {
                UUID memberUUID = members.get(i);
                OfflinePlayer memberPlayer = Bukkit.getOfflinePlayer(memberUUID);

                Component leader = componentBuilder.singleComponentBuilder()
                        .addOperationIf(memberUUID.equals(party.getLeader()),
                                "<aqua>" + memberPlayer.getName() + "</aqua>" + " " +"<red>(Leader)</red>",
                                "<aqua>" + memberPlayer.getName() + "</aqua>" + " " +"<yellow>(Member)</yellow>")
                        .build();

                List<Component> lore = Arrays.asList(
                        componentBuilder.singleComponentBuilder().text("").build(),
                        componentBuilder.singleComponentBuilder()
                                .addOperationIf(memberUUID.equals(party.getLeader()),
                                        "<red>Tidak dapat mengeluarkan Leader party",
                                        "<green>Tombol 'Q' untuk mengeluarkan Anggota")
                                .build());


                guiBuilder.addItem(SLOT_INDEXES[i - startIndex], new GUIItem(
                        itemBuilderFactory.createSkullItemBuilder(
                                        "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvN2Q2MDUzNGUyMmUzNzE0NzVkNTQzNGQwZjQ5YWUwNThkMzk0MWIwM2E3YzUwYTZlZWYyOTYyMGM2OTI3NzhlMCJ9fX0=",
                                        SkullType.BASE64)
                                .setName(leader)
                                .setLore(lore)
                                .build(),
                        event -> {
                            if (event.getClick() == ClickType.DROP) {
                                if (player.getUniqueId().equals(party.getLeader()) && !memberUUID.equals(party.getLeader())) {
                                    player.closeInventory();
                                    PartyKickConfirmationGUI partyKickConfirmationGUI =  new PartyKickConfirmationGUI(partyManager);
                                    partyKickConfirmationGUI.openGUI(player, memberPlayer);
                                }
                            }
                        }
                ));
            }

            Component modeName = componentBuilder.singleComponentBuilder().addOperationIf(
                    party.getMode() == Party.PartyMode.FRIENDLY,
                    "<green>Friendly Mode",
                    "<red>Duel Mode").build();

            guiBuilder.addItem(53, new GUIItem(itemBuilderFactory.createItemBuilder(Material.PAPER)
                    .setName(modeName)
                    .build(), event -> {
                Party.PartyMode newMode = party.getMode() == Party.PartyMode.FRIENDLY ? Party.PartyMode.DUEL : Party.PartyMode.FRIENDLY;
                party.setMode(newMode);
                player.closeInventory();
                open(player, page);
            }));
        }
    }

    private void createNavigationPane(GUIBuilder guiBuilder, Player player, int page) {
        int memberCount = getMemberCount(player);

        if (page > 1) {
            guiBuilder.addItem(45, new GUIItem(itemBuilderFactory.createItemBuilder(Material.ARROW)
                    .setName(componentBuilder.singleComponentBuilder().text("<red>Back").build())
                    .build(), event -> {
                event.setCancelled(true);
                open(player, page - 1);
            }));
        }

        if ((page - 1) * ITEMS_PER_PAGE + ITEMS_PER_PAGE < memberCount) {
            guiBuilder.addItem(53, new GUIItem(itemBuilderFactory.createItemBuilder(Material.ARROW)
                    .setName(componentBuilder.singleComponentBuilder().text("<red>Next").build())
                    .build(), event -> {
                event.setCancelled(true);
                open(player, page + 1);
            }));
        }
    }

    private int getMemberCount(Player player) {
        if (partyManager.isPlayerInParty(player.getUniqueId())) {
            Party party = partyManager.getPlayerParty(player.getUniqueId());
            return party.getMembers().size();
        }
        return 0;
    }
}
