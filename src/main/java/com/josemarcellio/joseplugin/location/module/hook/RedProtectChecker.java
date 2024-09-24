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

package com.josemarcellio.joseplugin.location.module.hook;

import br.net.fabiozumbi12.RedProtect.Bukkit.RedProtect;
import br.net.fabiozumbi12.RedProtect.Bukkit.Region;
import com.josemarcellio.joseplugin.location.LocationChecker;
import com.josemarcellio.joseplugin.plugin.PluginStateChecker;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class RedProtectChecker implements LocationChecker {

    @Override
    public boolean isSafe(Location location, Player player) {
        PluginStateChecker pluginLoaded = new PluginStateChecker();
        if (pluginLoaded.isPluginLoaded("RedProtect")) {
            World world = location.getWorld();
            if (world == null) {
                return false;
            }

            Region region = RedProtect.get().getAPI().getRegion(player.getLocation());
            if (region != null) {
                return region.canBuild(player);
            }
        }
        return true;
    }
}
