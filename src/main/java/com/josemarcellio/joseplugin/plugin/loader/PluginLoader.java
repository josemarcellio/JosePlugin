package com.josemarcellio.joseplugin.plugin.loader;

import com.josemarcellio.joseplugin.exception.JosePluginException;
import org.bukkit.Server;
import org.bukkit.plugin.InvalidDescriptionException;
import org.bukkit.plugin.InvalidPluginException;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.List;

public class PluginLoader {

    public void loadJars(JavaPlugin plugin, List<Plugin> loadedPlugins, List<File> downloadedFiles) throws JosePluginException {
        for (File file : downloadedFiles) {
            try {
                Plugin loadedPlugin = plugin.getServer().getPluginManager().loadPlugin(file);
                if (loadedPlugin != null) {
                    loadedPlugin.onLoad();
                    plugin.getServer().getPluginManager().enablePlugin(loadedPlugin);
                    loadedPlugins.add(loadedPlugin);
                    plugin.getLogger().info("Successfully loaded plugin: " + loadedPlugin.getName());
                }
            } catch (InvalidPluginException | InvalidDescriptionException e) {
                throw new JosePluginException("Failed to load JAR from file: " + file.getName(), e);
            }
        }
    }

    public void disablePlugins(List<Plugin> loadedPlugins, Server server) {
        for (Plugin plugin : loadedPlugins) {
            if (plugin != null) {
                server.getPluginManager().disablePlugin(plugin);
            }
        }
        loadedPlugins.clear();
    }
}
