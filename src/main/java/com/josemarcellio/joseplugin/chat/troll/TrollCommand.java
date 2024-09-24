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

package com.josemarcellio.joseplugin.chat.troll;

import com.josemarcellio.joseplugin.component.ComponentBuilder;
import com.josemarcellio.joseplugin.cooldown.ICooldownManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class TrollCommand implements CommandExecutor {

    private final ICooldownManager cooldownManager;
    private final ComponentBuilder componentBuilder = new ComponentBuilder();

    public TrollCommand(ICooldownManager cooldownManager) {
        this.cooldownManager = cooldownManager;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (args.length < 1) {
            return handleHelp(sender);
        }

        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            sender.sendMessage(componentBuilder.singleComponentBuilder().text("<red>Player not found.").build());
            return false;
        }

        if (args.length < 2) {
            handleHelp(sender);
            return false;
        }

        String module = args[1].toLowerCase();
        long duration = 0;

        if (!module.equals("reset")) {
            if (args.length < 3) {
                sender.sendMessage(componentBuilder.singleComponentBuilder().text("<light_purple> ☣ <color:#fae7b5>Troll <color:#c4c3d0>• <white>Masukkan format durasi!").build());
                return false;
            }
            try {
                duration = Long.parseLong(args[2]) * 1000L;
            } catch (NumberFormatException e) {
                sender.sendMessage(componentBuilder.singleComponentBuilder().text("<light_purple> ☣ <color:#fae7b5>Troll <color:#c4c3d0>• <white>Masukkan format angka dengan benar!").build());
                return false;
            }
        }

        return switch (module) {
            case "reverse" -> handleReverse(sender, target, duration);
            case "unscramble" -> handleUnscramble(sender, target, duration);
            case "leetspeak" -> handleLeetSpeak(sender, target, duration);
            case "removevowels" -> handleRemoveVowels(sender, target, duration);
            case "reset" -> handleReset(sender, target, module);
            default -> {
                handleHelp(sender);
                yield true;
            }
        };
    }


    private boolean handleHelp(CommandSender sender) {
        sender.sendMessage(componentBuilder.singleComponentBuilder().text("<light_purple> ☣ <color:#fae7b5>Troll <color:#c4c3d0>• <white>/troll <player> unscramble <duration>").build());
        sender.sendMessage(componentBuilder.singleComponentBuilder().text("<light_purple> ☣ <color:#fae7b5>Troll <color:#c4c3d0>• <white>/troll <player> reverse <duration>").build());
        sender.sendMessage(componentBuilder.singleComponentBuilder().text("<light_purple> ☣ <color:#fae7b5>Troll <color:#c4c3d0>• <white>/troll <player> leetspeak <duration>").build());
        sender.sendMessage(componentBuilder.singleComponentBuilder().text("<light_purple> ☣ <color:#fae7b5>Troll <color:#c4c3d0>• <white>/troll <player> removevowels <duration>").build());
        sender.sendMessage(componentBuilder.singleComponentBuilder().text("<light_purple> ☣ <color:#fae7b5>Troll <color:#c4c3d0>• <white>/troll <player> reset <module>").build());
        return true;
    }

    private boolean handleReverse(CommandSender sender, Player target, long duration) {
        if (!sender.hasPermission("joseplugin.troll")) {
            sender.sendMessage(componentBuilder.singleComponentBuilder().text("<light_purple> ☣ <color:#fae7b5>Troll <color:#c4c3d0>• <white>Kamu tidak memiliki permission joseplugin.troll untuk mengakses command ini!").build());
            return false;
        }

        cooldownManager.startCooldown(target.getUniqueId(), "reverse", duration);
        target.sendMessage(componentBuilder.singleComponentBuilder().text("<light_purple> ☣ <color:#fae7b5>Troll <color:#c4c3d0>• <white>Admin mengaktifkan mode reverse kepadamu, sekarang ketika kamu chat maka text mu akan terbalik selama <aqua>" + (duration / 1000) + " detik").build());
        return true;
    }

    private boolean handleUnscramble(CommandSender sender, Player target, long duration) {
        if (!sender.hasPermission("joseplugin.troll")) {
            sender.sendMessage(componentBuilder.singleComponentBuilder().text("<light_purple> ☣ <color:#fae7b5>Troll <color:#c4c3d0>• <white>Kamu tidak memiliki permission joseplugin.troll untuk mengakses command ini!").build());
            return false;
        }

        cooldownManager.startCooldown(target.getUniqueId(), "unscramble", duration);
        target.sendMessage(componentBuilder.singleComponentBuilder().text("<light_purple> ☣ <color:#fae7b5>Troll <color:#c4c3d0>• <white>Admin mengaktifkan mode unscramble kepadamu, sekarang ketika kamu chat maka text mu akan menjadi acak selama <aqua>" + (duration / 1000) + " detik").build());
        return true;
    }

    private boolean handleLeetSpeak(CommandSender sender, Player target, long duration) {
        if (!sender.hasPermission("joseplugin.troll")) {
            sender.sendMessage(componentBuilder.singleComponentBuilder().text("<light_purple> ☣ <color:#fae7b5>Troll <color:#c4c3d0>• <white>Kamu tidak memiliki permission joseplugin.troll untuk mengakses command ini!").build());
            return false;
        }

        cooldownManager.startCooldown(target.getUniqueId(), "leetspeak", duration);
        target.sendMessage(componentBuilder.singleComponentBuilder().text("<light_purple> ☣ <color:#fae7b5>Troll <color:#c4c3d0>• <white>Admin mengaktifkan mode leetspeak kepadamu, sekarang ketika kamu chat maka text mu akan menjadi alay selama <aqua>" + (duration / 1000) + " detik").build());
        return true;
    }

    private boolean handleRemoveVowels(CommandSender sender, Player target, long duration) {
        if (!sender.hasPermission("joseplugin.troll")) {
            sender.sendMessage(componentBuilder.singleComponentBuilder().text("<light_purple> ☣ <color:#fae7b5>Troll <color:#c4c3d0>• <white>Kamu tidak memiliki permission joseplugin.troll untuk mengakses command ini!").build());
            return false;
        }

        cooldownManager.startCooldown(target.getUniqueId(), "removevowels", duration);
        target.sendMessage(componentBuilder.singleComponentBuilder().text("<light_purple> ☣ <color:#fae7b5>Troll <color:#c4c3d0>• <white>Admin mengaktifkan mode removevowels kepadamu, sekarang ketika kamu chat maka text mu akan kehilangan huruf vokal selama <aqua>" + (duration / 1000) + " detik").build());
        return true;
    }


    private boolean handleReset(CommandSender sender, Player target, String module) {
        if (!sender.hasPermission("joseplugin.troll")) {
            sender.sendMessage(componentBuilder.singleComponentBuilder().text("<light_purple> ☣ <color:#fae7b5>Troll <color:#c4c3d0>• <white>Kamu tidak memiliki permission joseplugin.troll untuk mengakses command ini!").build());
            return false;
        }

        cooldownManager.removeCooldown(target.getUniqueId(), module);
        target.sendMessage(componentBuilder.singleComponentBuilder().text("<light_purple> ☣ <color:#fae7b5>Troll <color:#c4c3d0>• <white>Admin telah menghapus mode <aqua>" + module + " <white>sekarang text mu akan kembali normal").build());
        return true;
    }
}
