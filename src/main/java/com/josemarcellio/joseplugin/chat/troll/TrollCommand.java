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
            sender.sendMessage(componentBuilder.singleComponentBuilder("<red>Player not found.").build());
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
                sender.sendMessage(componentBuilder.singleComponentBuilder("<light_purple> ☣ <color:#fae7b5>Troll <color:#c4c3d0>• <white>Masukkan format durasi!").build());
                return false;
            }
            try {
                duration = Long.parseLong(args[2]) * 1000L;
            } catch (NumberFormatException e) {
                sender.sendMessage(componentBuilder.singleComponentBuilder("<light_purple> ☣ <color:#fae7b5>Troll <color:#c4c3d0>• <white>Masukkan format angka dengan benar!").build());
                return false;
            }
        }

        return switch (module) {
            case "reverse" -> handleReverse(sender, target, duration, args);
            case "unscramble" -> handleUnscramble(sender, target, duration, args);
            case "reset" -> handleReset(sender, target, module, args);
            default -> {
                handleHelp(sender);
                yield true;
            }
        };
    }


    private boolean handleHelp(CommandSender sender) {
        sender.sendMessage(componentBuilder.singleComponentBuilder("<light_purple> ☣ <color:#fae7b5>Troll <color:#c4c3d0>• <white>/troll <player> unscramble <duration>").build());
        sender.sendMessage(componentBuilder.singleComponentBuilder("<light_purple> ☣ <color:#fae7b5>Troll <color:#c4c3d0>• <white>/troll <player> reverse <duration>").build());
        sender.sendMessage(componentBuilder.singleComponentBuilder("<light_purple> ☣ <color:#fae7b5>Troll <color:#c4c3d0>• <white>/troll <player> reset <module>").build());
        return true;
    }

    private boolean handleReverse(CommandSender sender, Player target, long duration, String[] args) {
        if (!sender.hasPermission("joseplugin.troll")) {
            sender.sendMessage(componentBuilder.singleComponentBuilder("<light_purple> ☣ <color:#fae7b5>Troll <color:#c4c3d0>• <white>Kamu tidak memiliki permission joseplugin.troll untuk mengakses command ini!").build());
            return false;
        }

        cooldownManager.startCooldown(target.getUniqueId(), "reverse", duration);
        target.sendMessage(componentBuilder.singleComponentBuilder("<light_purple> ☣ <color:#fae7b5>Troll <color:#c4c3d0>• <white>Admin mengaktifkan mode reverse kepadamu, sekarang ketika kamu chat maka text mu akan terbalik selama <aqua>" + (duration / 1000) + " detik").build());
        return true;
    }

    private boolean handleUnscramble(CommandSender sender, Player target, long duration, String[] args) {
        if (!sender.hasPermission("joseplugin.troll")) {
            sender.sendMessage(componentBuilder.singleComponentBuilder("<light_purple> ☣ <color:#fae7b5>Troll <color:#c4c3d0>• <white>Kamu tidak memiliki permission joseplugin.troll untuk mengakses command ini!").build());
            return false;
        }

        cooldownManager.startCooldown(target.getUniqueId(), "unscramble", duration);
        target.sendMessage(componentBuilder.singleComponentBuilder("<light_purple> ☣ <color:#fae7b5>Troll <color:#c4c3d0>• <white>Admin mengaktifkan mode unscramble kepadamu, sekarang ketika kamu chat maka text mu akan menjadi acak selama <aqua>" + (duration / 1000) + " detik").build());
        return true;
    }

    private boolean handleReset(CommandSender sender, Player target, String module, String[] args) {
        if (!sender.hasPermission("joseplugin.troll")) {
            sender.sendMessage(componentBuilder.singleComponentBuilder("<light_purple> ☣ <color:#fae7b5>Troll <color:#c4c3d0>• <white>Kamu tidak memiliki permission joseplugin.troll untuk mengakses command ini!").build());
            return false;
        }

        cooldownManager.removeCooldown(target.getUniqueId(), module);
        target.sendMessage(componentBuilder.singleComponentBuilder("<light_purple> ☣ <color:#fae7b5>Troll <color:#c4c3d0>• <white>Admin telah menghapus mode <aqua>" + module + " <white>sekarang text mu akan kembali normal").build());
        return true;
    }
}
