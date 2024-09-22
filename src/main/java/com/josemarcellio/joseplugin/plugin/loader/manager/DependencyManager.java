package com.josemarcellio.joseplugin.plugin.loader.manager;

import com.josemarcellio.joseplugin.exception.JosePluginException;
import com.josemarcellio.joseplugin.plugin.loader.PluginDownloader;
import com.josemarcellio.joseplugin.plugin.loader.PluginLoader;
import org.bukkit.Server;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class DependencyManager implements IDependencyManager {

    private final PluginDownloader pluginDownloader = new PluginDownloader();
    private final PluginLoader pluginLoader = new PluginLoader();

    @Override
    public void downloadAndLoadDependencies(JavaPlugin plugin, List<Plugin> loadedPlugins) {
        try {
            List<File> downloadedFiles = pluginDownloader.downloadPlugins(plugin);
            pluginDownloader.loadPlugins(plugin, new ArrayList<>(), downloadedFiles);
        } catch (JosePluginException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void disableDependencies(List<Plugin> loadedPlugins, Server server) {
        pluginLoader.disablePlugins(loadedPlugins, server);
    }
}
