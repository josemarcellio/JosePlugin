package com.josemarcellio.joseplugin.plugin;

import org.bukkit.plugin.Plugin;

public interface PluginChecker {
    boolean isPluginNull(Plugin plugin);
    boolean isPluginLoaded(Plugin plugin);
    boolean isPluginLoaded(String plugin);
}
