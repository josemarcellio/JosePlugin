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

package com.josemarcellio.joseplugin.location.module;

import com.josemarcellio.joseplugin.location.LocationChecker;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class GroundChecker implements LocationChecker {

    @Override
    public boolean isNotSafe(Location location, Player player) {
        World world = location.getWorld();
        if (world == null) {
            return true;
        }

        Location groundLocation = location.clone().subtract(0, 1, 0);
        Block groundBlock = world.getBlockAt(groundLocation);
        return groundBlock.getType() == Material.AIR;
    }

    @Override
    public String getMessage() {
        return "Lokasi ini tidak aman karena tidak ditemukan block yang solid!";
    }
}
