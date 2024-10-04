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

package com.josemarcellio.joseplugin.dailyreward.gui;

import com.josemarcellio.joseplugin.JosePlugin;
import com.josemarcellio.joseplugin.inventory.GUIItem;
import com.josemarcellio.joseplugin.inventory.GUIManager;
import com.josemarcellio.joseplugin.inventory.builder.GUIBuilder;
import com.josemarcellio.joseplugin.inventory.page.GUIPage;
import com.josemarcellio.joseplugin.item.ItemBuilderFactory;
import com.josemarcellio.joseplugin.skull.type.SkullType;
import com.josemarcellio.joseplugin.component.ComponentBuilder;
import com.josemarcellio.joseplugin.time.TimeFormatter;

import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.Node;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

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
        GUIBuilder builder = new GUIBuilder(componentBuilder.singleComponentBuilder().text("<aqua>Daily Reward</aqua>").build(), 6 * 9);

        addDailyReward(builder, player, texture, name, lore);

        builder.addGlassPane();
        builder.addCloseItem();

        GUIPage guiPage = builder.build();
        GUIManager.openGUI(player, guiPage);
    }

    private void addDailyReward(GUIBuilder builder, Player player, String texture, String name, String lore) {
        GUIItem guiReward = new GUIItem(
                itemBuilderFactory.createSkullItemBuilder(texture, SkullType.BASE64)
                        .setName(componentBuilder.singleComponentBuilder().text(name).build())
                        .addLore(componentBuilder.singleComponentBuilder().text(lore).build())
                        .build(),
                event -> {
                    if (!hasClaimed(player)) {
                        claimReward(player);
                        player.closeInventory();
                    }
                });
        builder.addItem(22, guiReward);
    }

    private boolean hasClaimed(Player player) {
        return player.hasPermission("joseplugin.dailyrewards");
    }

    private void claimReward(Player player) {
        plugin.getEconomyManager().getEconomy().depositPlayer(player, 500);
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp user " + player.getName() + " permission settemp joseplugin.dailyrewards true 24h");
        player.sendMessage(componentBuilder.singleComponentBuilder().text("<yellow> ⛃ <color:#fae7b5>Daily Reward <color:#c4c3d0>• <white>Berhasil claim <aqua>500 Coins <white>dari Daily Reward!").build());
    }

    private String getCooldownTimeFormatted(Player player) {
        Instant expiryTime = getLuckPermsExpiry(player.getName());
        return new TimeFormatter()
                .setDayString("hari")
                .setHourString("jam")
                .setMinuteString("menit")
                .setSecondString("detik")
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
