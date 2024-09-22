package com.josemarcellio.joseplugin.location.module;

import com.josemarcellio.joseplugin.location.LocationChecker;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class HazardChecker implements LocationChecker {

    @Override
    public boolean isSafe(Location location, Player player) {
        World world = location.getWorld();
        if (world == null) {
            return false;
        }

        for (int x = -1; x <= 1; x++) {
            for (int y = -1; y <= 1; y++) {
                for (int z = -1; z <= 1; z++) {
                    Location checkLocation = location.clone().add(x, y, z);
                    Block block = world.getBlockAt(checkLocation);
                    if (block.getType() == Material.LAVA || block.getType() == Material.MAGMA_BLOCK) {
                        return false;
                    }
                }
            }
        }
        return true;
    }
}
