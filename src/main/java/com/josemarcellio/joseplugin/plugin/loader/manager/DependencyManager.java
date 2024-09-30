/*
 * Copyright (C) 2024 Jose Marcellio
 * GitHub: https://github.com/josemarcellio
 *
 * This software is open-source and distributed under the GNU General Public License (GPL), version 3.
 * You are free to modify, share, and distribute it as long as the same freedoms are preserved.
 *
 * No warranties are provided with this software. It is distributed in the hope that it will be useful,
 * but WITHOUT ANY IMPLIED WARRANTIES, including but not limited to the implied warranties of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 *
 * For more details, refer to the full license at <https://www.gnu.org/licenses/>.
 */

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
        } catch (Exception e) {
            throw new JosePluginException("Failed to download and load dependencies!", e);
        }
    }

    @Override
    public void disableDependencies(List<Plugin> loadedPlugins, Server server) {
        pluginLoader.disablePlugins(loadedPlugins, server);
    }
}
