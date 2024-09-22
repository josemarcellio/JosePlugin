package com.josemarcellio.joseplugin.plugin;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

public class PluginStateChecker implements PluginChecker {

    @Override
    public boolean isPluginNull(Plugin plugin) {
        return plugin == null;
    }

    @Override
    public boolean isPluginLoaded(Plugin plugin) {
        return plugin != null && plugin.isEnabled();
    }

    @Override
    public boolean isPluginLoaded(String pluginName) {
        Plugin plugin = Bukkit.getPluginManager().getPlugin(pluginName);
        return plugin != null && plugin.isEnabled();
    }
}
