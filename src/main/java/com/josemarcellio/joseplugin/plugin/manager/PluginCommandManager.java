package com.josemarcellio.joseplugin.plugin.manager;

import org.bukkit.command.CommandExecutor;
import org.bukkit.plugin.java.JavaPlugin;

@SuppressWarnings("ConstantConditions")
public class PluginCommandManager {

    private final JavaPlugin plugin;

    public PluginCommandManager(JavaPlugin plugin) {
        this.plugin = plugin;
    }


    public void registerCommand(String commandName, CommandExecutor executor) {
        plugin.getCommand(commandName).setExecutor(executor);
    }
}

