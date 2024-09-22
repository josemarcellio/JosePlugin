package com.josemarcellio.joseplugin.location;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public interface LocationChecker {
    boolean isSafe(Location location, Player player);
}
