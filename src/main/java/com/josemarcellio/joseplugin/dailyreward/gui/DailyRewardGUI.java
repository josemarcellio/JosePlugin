package com.josemarcellio.joseplugin.dailyreward.gui;

import com.josemarcellio.joseplugin.JosePlugin;
import com.josemarcellio.joseplugin.inventory.GUIItem;
import com.josemarcellio.joseplugin.inventory.GUIManager;
import com.josemarcellio.joseplugin.inventory.builder.GUIBuilder;
import com.josemarcellio.joseplugin.inventory.page.GUIPage;
import com.josemarcellio.joseplugin.item.ItemBuilderFactory;
import com.josemarcellio.joseplugin.skull.type.SkullType;
import com.josemarcellio.joseplugin.text.component.ComponentBuilder;
import com.josemarcellio.joseplugin.time.TimeFormatter;

import net.kyori.adventure.text.Component;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.Node;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.time.Instant;
import java.util.Objects;

public class DailyRewardGUI {

    private final JosePlugin plugin;
    private final ComponentBuilder componentBuilder = new ComponentBuilder();
    private final ItemBuilderFactory itemBuilderFactory = new ItemBuilderFactory();

    public DailyRewardGUI(JosePlugin plugin) {
        this.plugin = plugin;
    }

    public void openGUI(Player player) {
        String texture;
        String name;
        String lore;

        if (hasClaimed(player)) {
            texture = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYmVkYTZhNjczYmE3NjUzMWQzMmYyZTM1OGMxMGI3YzllOWNkNjYxNWQxYjc3NjU0Zjk4YWFiNDRhMGQ3YzE3ZiJ9fX0=";
            name = "<red>Already Claimed</red>";
            lore = "<red>Cooldown " + getCooldownTimeFormatted(player) + "</red>";
        } else {
            texture = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvN2Q2MDUzNGUyMmUzNzE0NzVkNTQzNGQwZjQ5YWUwNThkMzk0MWIwM2E3YzUwYTZlZWYyOTYyMGM2OTI3NzhlMCJ9fX0=";
            name = "<aqua>Daily Reward</aqua>";
            lore = "<yellow>Click to claim reward</yellow>";
        }

        openRewardGUI(player, texture, name, lore);
    }

    private void openRewardGUI(Player player, String texture, String name, String lore) {
        GUIBuilder builder = new GUIBuilder(componentBuilder.singleComponentBuilder("<aqua>Daily Reward</aqua>").build(), 6 * 9);

        addDailyReward(builder, player, texture, name, lore);
        addItem(builder);
        addGlassPane(builder);

        GUIPage guiPage = builder.build();
        GUIManager.openGUI(player, guiPage);
    }

    private void addDailyReward(GUIBuilder builder, Player player, String texture, String name, String lore) {
        GUIItem guiReward = new GUIItem(
                itemBuilderFactory.createSkullItemBuilder(texture, SkullType.BASE64)
                        .setName(componentBuilder.singleComponentBuilder(name).build())
                        .addLore(componentBuilder.singleComponentBuilder(lore).build())
                        .build(),
                event -> {
                    if (!hasClaimed(player)) {
                        claimReward(player);
                        player.closeInventory();
                    }
                });
        builder.addItem(22, guiReward);
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

    private boolean hasClaimed(Player player) {
        return player.hasPermission("joseplugin.dailyrewards");
    }

    private void claimReward(Player player) {
        plugin.getEconomy().depositPlayer(player, 500);
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp user " + player.getName() + " permission settemp joseplugin.dailyrewards true 24h");
        player.sendMessage(componentBuilder.singleComponentBuilder("<yellow> ⛃ <color:#fae7b5>Daily Reward <color:#c4c3d0>• <color:white>Berhasil claim <aqua>500 Coins <white>dari Daily Reward!").build());
    }

    private String getCooldownTimeFormatted(Player player) {
        Instant expiryTime = getLuckPermsExpiry(player.getName());
        return new TimeFormatter()
                .setDayString("days")
                .setHourString("hours")
                .setMinuteString("minutes")
                .setSecondString("seconds")
                .formattedTime(expiryTime);
    }

    private Instant getLuckPermsExpiry(String playerName) {
        User user = LuckPermsProvider.get().getUserManager().getUser(playerName);
        if (user == null) {
            return null;
        }

        return user.getNodes().stream()
                .filter(n -> n.getKey().equals("joseplugin.dailyrewards") && n.hasExpiry())
                .map(Node::getExpiry)
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(null);
    }
}
