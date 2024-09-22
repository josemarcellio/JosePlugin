package com.josemarcellio.joseplugin.job.command;

import com.josemarcellio.joseplugin.JosePlugin;
import com.josemarcellio.joseplugin.job.gui.JobsGUI;
import org.bukkit.Bukkit;
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
            if (strings.length < 4) {
                JobsGUI jobsGUI = new JobsGUI(plugin);
                jobsGUI.openGUI(player);
                return true;
            }
        }

        String action = strings[1];
        String playerName = strings[2];
        int amount;

        try {
            amount = Integer.parseInt(strings[3]);
        } catch (NumberFormatException e) {
            commandSender.sendMessage("Amount must be a number.");
            return false;
        }

        Player targetPlayer = Bukkit.getPlayer(playerName);

        if (targetPlayer == null) {
            commandSender.sendMessage("Player not found.");
            return false;
        }

        if (action.equalsIgnoreCase("give")) {
            plugin.getJobsManager().giveExp(targetPlayer.getUniqueId(), amount);
            commandSender.sendMessage("berhasil kirim " + amount + " experience ke " + playerName);
        } else {
            commandSender.sendMessage("/jobs exp give <nama> <jumlah>");
        }

        return true;
    }
}
