package com.josemarcellio.joseplugin.plugin.manager;

import org.bukkit.command.CommandExecutor;
import org.bukkit.plugin.java.JavaPlugin;

public class PluginCommandManager {

    private final JavaPlugin plugin;

    public PluginCommandManager(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @SuppressWarnings("ConstantConditions")
    public void registerCommand(String commandName, CommandExecutor executor) {
        plugin.getCommand(commandName).setExecutor(executor);
    }
}

