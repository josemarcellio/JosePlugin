package com.josemarcellio.joseplugin.location.module;

import com.josemarcellio.joseplugin.location.LocationChecker;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class GroundChecker implements LocationChecker {

    @Override
    public boolean isSafe(Location location, Player player) {
        World world = location.getWorld();
        if (world == null) {
            return false;
        }

        Location groundLocation = location.clone().subtract(0, 1, 0);
        Block groundBlock = world.getBlockAt(groundLocation);
        if (groundBlock.getType() == Material.AIR) {
            return false;
        }
        return true;
    }
}
