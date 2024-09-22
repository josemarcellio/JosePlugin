package com.josemarcellio.joseplugin.plugin.loader.manager;

import org.bukkit.Server;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public interface IDependencyManager {
    void downloadAndLoadDependencies(JavaPlugin plugin, List<Plugin> loadedPlugins);
    void disableDependencies(List<Plugin> loadedPlugins, Server server);
}
