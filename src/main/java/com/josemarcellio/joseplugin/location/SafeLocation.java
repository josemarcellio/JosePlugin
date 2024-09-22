package com.josemarcellio.joseplugin.location;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class SafeLocation {

    private final List<LocationChecker> checkers = new CopyOnWriteArrayList<>();

    public SafeLocation addCheck(LocationChecker checker) {
        checkers.add(checker);
        return this;
    }

    public boolean isSafeLocation(Player player, Location location) {
        for (LocationChecker checker : checkers) {
            if (!checker.isSafe(location, player)) {
                return false;
            }
        }
        return true;
    }
}
