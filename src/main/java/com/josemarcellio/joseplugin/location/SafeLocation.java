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
