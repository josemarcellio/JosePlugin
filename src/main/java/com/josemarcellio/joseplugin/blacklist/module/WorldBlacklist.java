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

package com.josemarcellio.joseplugin.blacklist.module;

import java.util.Arrays;
import java.util.List;

import com.josemarcellio.joseplugin.blacklist.BlacklistChecker;
import org.bukkit.entity.Player;

public class WorldBlacklist implements BlacklistChecker<Player> {

    private final List<String> blacklistWorlds = Arrays.asList("world_nether", "world_the_end");

    @Override
    public boolean isBlacklisted(Player player) {
        return blacklistWorlds.contains(player.getWorld().getName());
    }
}
