package com.josemarcellio.joseplugin.location.module;

import com.josemarcellio.joseplugin.location.LocationChecker;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class ObstructionChecker implements LocationChecker {

    @Override
    public boolean isSafe(Location location, Player player) {
        World world = location.getWorld();
        if (world == null) {
            return false;
        }

        Location headLocation = location.clone().add(0, 1, 0);
        if (!world.getBlockAt(location).isPassable() || !world.getBlockAt(headLocation).isPassable()) {
            return false;
        }
        return true;
    }
}
