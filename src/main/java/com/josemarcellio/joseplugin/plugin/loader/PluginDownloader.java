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
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PluginDownloader {

    public static final Map<String, String> PLUGIN_URLS = new HashMap<>() {{
        put("PlaceholderAPI", "http://ci.extendedclip.com/job/PlaceholderAPI/lastSuccessfulBuild/artifact/build/libs/PlaceholderAPI-2.11.7-DEV-200.jar");
        put("LuckPerms", "https://ci.lucko.me/job/LuckPerms/lastSuccessfulBuild/artifact/bukkit/loader/build/libs/LuckPerms-Bukkit-5.4.141.jar");
        put("packetevents", "https://ci.codemc.io/job/retrooper/job/packetevents/lastSuccessfulBuild/artifact/spigot/build/libs/packetevents-spigot-2.5.1-SNAPSHOT.jar");
    }};

    public List<File> downloadPlugins(JavaPlugin plugin) throws JosePluginException {
        List<File> downloadedFiles = new ArrayList<>();

        for (Map.Entry<String, String> entry : PLUGIN_URLS.entrySet()) {
            String pluginName = entry.getKey();
            String url = entry.getValue();

            if (plugin.getServer().getPluginManager().getPlugin(pluginName) != null) {
                plugin.getLogger().info("Plugin already exists: " + pluginName + ", skipping download.");
            } else {
                File file = PluginUtils.getPluginFilePath(url);
                try (InputStream in = PluginUtils.formURL(url).toURL().openStream()) {
                    long downloadedSize = PluginUtils.download(in, file.toPath());
                    plugin.getLogger().info("Downloaded " + downloadedSize + " bytes for plugin: " + pluginName);
                    downloadedFiles.add(file);
                } catch (IOException e) {
                    throw new JosePluginException("Error downloading JAR from " + url, e);
                }
            }
        }

        return downloadedFiles;
    }

    public void loadPlugins(JavaPlugin plugin, List<Plugin> loadedPlugins, List<File> downloadedFiles) throws JosePluginException {
        if (!downloadedFiles.isEmpty()) {
            PluginLoader loader = new PluginLoader();
            loader.loadJars(plugin, loadedPlugins, downloadedFiles);
        } else {
            plugin.getLogger().info("No new plugins to load.");
        }
    }
}
