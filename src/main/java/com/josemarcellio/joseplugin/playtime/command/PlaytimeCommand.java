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

package com.josemarcellio.joseplugin.playtime.command;

import com.josemarcellio.joseplugin.component.ComponentBuilder;
import com.josemarcellio.joseplugin.playtime.PlaytimeManager;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class PlaytimeCommand implements CommandExecutor {

    private final PlaytimeManager playtimeManager = new PlaytimeManager();
    private final ComponentBuilder componentBuilder = new ComponentBuilder();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (args.length == 0) {
            return handleSelfPlaytime(sender);
        }

        return switch (args[0].toLowerCase()) {
            case "check" -> handleCheckPlaytime(sender, args);
            case "set" -> handleSetPlaytime(sender, args);
            case "reset" -> handleResetPlaytime(sender, args);
            default -> {
                handleHelp(sender);
                yield true;
            }
        };
    }

    private boolean handleSelfPlaytime(CommandSender sender) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("This command can only be used by a player.");
            return false;
        }
        player.sendMessage(componentBuilder.singleComponentBuilder().text("<blue> ⏰ <color:#fae7b5>PlayTime <color:#c4c3d0>• <white>Total Playtime: <aqua>" + playtimeManager.getFormattedPlaytime(player)).build());
        return true;
    }

    private boolean handleCheckPlaytime(CommandSender sender, String[] args) {
        if (args.length < 2) {
            sender.sendMessage(componentBuilder.singleComponentBuilder().text("<blue> ⏰ <color:#fae7b5>PlayTime <color:#c4c3d0>• <white>/playtime check <player>").build());
            return false;
        }

        String playerName = args[1];
        OfflinePlayer targetPlayer = Bukkit.getOfflinePlayer(playerName);

        if (!targetPlayer.hasPlayedBefore()) {
            sender.sendMessage(componentBuilder.singleComponentBuilder().text("<blue> ⏰ <color:#fae7b5>PlayTime <color:#c4c3d0>• <white>Player dengan nama <aqua>" + playerName + " <white>tidak memiliki playertime").build());
            return true;
        }

        Component checkPlaytime = componentBuilder.singleComponentBuilder()
                .addOperationIf(playtimeManager.getFormattedPlaytime(targetPlayer).equals("-1 detik"),
                        "<blue> ⏰ <color:#fae7b5>PlayTime <color:#c4c3d0>• <white>Player dengan nama <aqua>" + playerName + " <white>tidak memiliki playertime",
                        "<blue> ⏰ <color:#fae7b5>PlayTime <color:#c4c3d0>• <white>Total Playtime <aqua>" + playerName + " <white>selama <aqua>" + playtimeManager.getFormattedPlaytime(targetPlayer)).build();

        sender.sendMessage(checkPlaytime);
        return true;
    }

    private boolean handleResetPlaytime(CommandSender sender, String[] args) {
        if (!sender.hasPermission("joseplugin.playtime")) {
            sender.sendMessage(componentBuilder.singleComponentBuilder().text("<blue> ⏰ <color:#fae7b5>PlayTime <color:#c4c3d0>• <white>Kamu tidak memiliki permission joseplugin.playtime untuk mengakses command ini!").build());
            return false;
        }

        if (args.length < 2) {
            sender.sendMessage(componentBuilder.singleComponentBuilder().text("<blue> ⏰ <color:#fae7b5>PlayTime <color:#c4c3d0>• <white>/playtime reset <player>").build());
            return false;
        }

        String playerName = args[1];
        OfflinePlayer targetPlayer = Bukkit.getOfflinePlayer(playerName);

        if (!targetPlayer.hasPlayedBefore()) {
            sender.sendMessage(componentBuilder.singleComponentBuilder().text("<blue> ⏰ <color:#fae7b5>PlayTime <color:#c4c3d0>• <white>Player dengan nama <aqua>" + playerName + " <white>tidak memiliki playertime").build());
            return true;
        }

        playtimeManager.resetPlaytime(targetPlayer);
        sender.sendMessage(componentBuilder.singleComponentBuilder().text("<blue> ⏰ <color:#fae7b5>PlayTime <color:#c4c3d0>• <white>Total Playtime dari <aqua>" + playerName + " <white>berhasil di reset").build());
        return true;
    }

    private boolean handleSetPlaytime(CommandSender sender, String[] args) {
        if (!sender.hasPermission("joseplugin.playtime")) {
            sender.sendMessage(componentBuilder.singleComponentBuilder().text("<blue> ⏰ <color:#fae7b5>PlayTime <color:#c4c3d0>• <white>Kamu tidak memiliki permission joseplugin.playtime untuk mengakses command ini!").build());
            return false;
        }

        if (args.length < 3) {
            sender.sendMessage(componentBuilder.singleComponentBuilder().text("<blue> ⏰ <color:#fae7b5>PlayTime <color:#c4c3d0>• <white>/playtime set <player> <seconds>").build());
            return false;
        }

        String playerName = args[1];
        OfflinePlayer targetPlayer = Bukkit.getOfflinePlayer(playerName);

        if (!targetPlayer.hasPlayedBefore()) {
            sender.sendMessage(componentBuilder.singleComponentBuilder().text("<blue> ⏰ <color:#fae7b5>PlayTime <color:#c4c3d0>• <white>Player dengan nama <aqua>" + playerName + " <white>tidak memiliki playertime").build());
            return true;
        }

        try {
            long playtimeSeconds = Long.parseLong(args[2]);
            playtimeManager.setPlaytime(targetPlayer, playtimeSeconds);
            sender.sendMessage(componentBuilder.singleComponentBuilder().text("<blue> ⏰ <color:#fae7b5>PlayTime <color:#c4c3d0>• <white>Playtime dari <aqua>" + playerName + " <white>telah diatur ke <aqua>" + playtimeSeconds + " detik").build());
            return true;
        } catch (NumberFormatException e) {
            sender.sendMessage(componentBuilder.singleComponentBuilder().text("<blue> ⏰ <color:#fae7b5>PlayTime <color:#c4c3d0>• <white>Jumlah detik tidak valid! Harus berupa angka.").build());
            return false;
        }
    }

    private void handleHelp(CommandSender sender) {
        sender.sendMessage(componentBuilder.singleComponentBuilder().text("<blue> ⏰ <color:#fae7b5>PlayTime <color:#c4c3d0>• <white>/playtime").build());
        sender.sendMessage(componentBuilder.singleComponentBuilder().text("<blue> ⏰ <color:#fae7b5>PlayTime <color:#c4c3d0>• <white>/playtime check <player>").build());
        sender.sendMessage(componentBuilder.singleComponentBuilder().text("<blue> ⏰ <color:#fae7b5>PlayTime <color:#c4c3d0>• <white>/playtime set <player>").build());
        sender.sendMessage(componentBuilder.singleComponentBuilder().text("<blue> ⏰ <color:#fae7b5>PlayTime <color:#c4c3d0>• <white>/playtime reset <player>").build());
    }
}
