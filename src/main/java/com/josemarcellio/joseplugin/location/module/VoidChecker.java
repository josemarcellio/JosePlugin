package com.josemarcellio.joseplugin.location.module;

import com.josemarcellio.joseplugin.location.LocationChecker;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class VoidChecker implements LocationChecker {

    @Override
    public boolean isSafe(Location location, Player player) {
        if (location.getY() <= 0) {
            return false;
        }
        return true;
    }
}
