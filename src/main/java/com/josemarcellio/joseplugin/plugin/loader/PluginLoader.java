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
