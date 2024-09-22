package com.josemarcellio.joseplugin.dailyreward.command;

import com.josemarcellio.joseplugin.JosePlugin;
import com.josemarcellio.joseplugin.dailyreward.gui.DailyRewardGUI;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class DailyRewardCommand implements CommandExecutor {

    private final JosePlugin plugin;

    public DailyRewardCommand(JosePlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (commandSender instanceof Player player) {
            DailyRewardGUI dailyRewardGUI = new DailyRewardGUI(plugin);
            dailyRewardGUI.openGUI(player);
            return true;
        }
        return false;
    }
}
