package com.josemarcellio.joseplugin.job.command;

import com.josemarcellio.joseplugin.JosePlugin;
import com.josemarcellio.joseplugin.job.gui.JobsGUI;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class JobsCommand implements CommandExecutor {

    private final JosePlugin plugin;

    public JobsCommand(JosePlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (commandSender instanceof Player player) {
            JobsGUI jobsGUI = new JobsGUI(plugin);
            jobsGUI.openGUI(player);
            return true;
        }
        return false;
    }
}
