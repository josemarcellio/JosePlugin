package com.josemarcellio.joseplugin.plugin.manager;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class PluginListenerManager {

    private final JavaPlugin plugin;

    public PluginListenerManager(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public void registerListener(Listener listener) {
        Bukkit.getPluginManager().registerEvents(listener, plugin);
    }
}
