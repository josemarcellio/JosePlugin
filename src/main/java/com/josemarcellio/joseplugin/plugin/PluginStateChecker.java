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
