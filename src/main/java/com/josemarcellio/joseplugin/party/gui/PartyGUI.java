package com.josemarcellio.joseplugin.party.gui;

import com.josemarcellio.joseplugin.inventory.GUIItem;
import com.josemarcellio.joseplugin.inventory.GUIManager;
import com.josemarcellio.joseplugin.inventory.builder.GUIBuilder;
import com.josemarcellio.joseplugin.inventory.page.GUIPage;
import com.josemarcellio.joseplugin.item.ItemBuilderFactory;
import com.josemarcellio.joseplugin.party.manager.Party;
import com.josemarcellio.joseplugin.party.manager.PartyManager;
import com.josemarcellio.joseplugin.skull.type.SkullType;
import com.josemarcellio.joseplugin.text.component.ComponentBuilder;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

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
        GUIBuilder guiBuilder = new GUIBuilder(componentBuilder.singleComponentBuilder("<aqua>Party GUI</aqua>").build(), 54);
        createPartyPane(guiBuilder, player, page);
        createNavigationPane(guiBuilder, player, page);

        addItem(guiBuilder);
        addGlassPane(guiBuilder);

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
                String leader = memberUUID.equals(party.getLeader()) ? "<red>(Leader)</red>" : "<yellow>(Member)</yellow>";

                guiBuilder.addItem(SLOT_INDEXES[i - startIndex], new GUIItem(
                        itemBuilderFactory.createSkullItemBuilder(
                                        "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvN2Q2MDUzNGUyMmUzNzE0NzVkNTQzNGQwZjQ5YWUwNThkMzk0MWIwM2E3YzUwYTZlZWYyOTYyMGM2OTI3NzhlMCJ9fX0=",
                                        SkullType.BASE64)
                                .setName(componentBuilder.singleComponentBuilder("<aqua>" + memberPlayer.getName() + "</aqua>" + " " + leader).build())
                                .build(),
                        event -> {
                            if (player.getUniqueId().equals(party.getLeader()) && !memberUUID.equals(party.getLeader())) {
                                partyManager.kickPlayer(player, memberPlayer);
                                player.closeInventory();
                                open(player, page);
                            }
                        }
                ));
            }

            String modeName = party.getMode() == Party.PartyMode.FRIENDLY ? "<green>Friendly Mode" : "<red>Duel Mode";
            guiBuilder.addItem(53, new GUIItem(itemBuilderFactory.createItemBuilder(Material.PAPER)
                    .setName(componentBuilder.singleComponentBuilder(modeName).build())
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
                    .setName(componentBuilder.singleComponentBuilder("<red>Back").build())
                    .build(), event -> {
                event.setCancelled(true);
                open(player, page - 1);
            }));
        }

        if ((page - 1) * ITEMS_PER_PAGE + ITEMS_PER_PAGE < memberCount) {
            guiBuilder.addItem(53, new GUIItem(itemBuilderFactory.createItemBuilder(Material.ARROW)
                    .setName(componentBuilder.singleComponentBuilder("<red>Next").build())
                    .build(), event -> {
                event.setCancelled(true);
                open(player, page + 1);
            }));
        }
    }

    private void addGlassPane(GUIBuilder builder) {
        ItemStack glassItem = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
        ItemMeta meta = glassItem.getItemMeta();
        meta.displayName(Component.text(" "));
        glassItem.setItemMeta(meta);

        int[] glassSlots = {
                0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 17, 18, 26, 27, 35,
                36, 44, 45, 46, 47, 48, 50, 51, 52, 53
        };

        for (int slot : glassSlots) {
            builder.addItem(slot, new GUIItem(glassItem, null));
        }
    }

    private void addItem(GUIBuilder builder) {

        GUIItem guiClose = new GUIItem(itemBuilderFactory.createSkullItemBuilder("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYmViNTg4YjIxYTZmOThhZDFmZjRlMDg1YzU1MmRjYjA1MGVmYzljYWI0MjdmNDYwNDhmMThmYzgwMzQ3NWY3In19fQ==", SkullType.BASE64)
                .setName(componentBuilder.singleComponentBuilder("<red>Close</red>").build()).build(), event ->
                event.getWhoClicked().closeInventory());
        builder.addItem(49, guiClose);
    }

    private int getMemberCount(Player player) {
        if (partyManager.isPlayerInParty(player.getUniqueId())) {
            Party party = partyManager.getPlayerParty(player.getUniqueId());
            return party.getMembers().size();
        }
        return 0;
    }
}
