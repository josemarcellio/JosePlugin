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

package com.josemarcellio.joseplugin.blacklist;

import com.josemarcellio.joseplugin.blacklist.module.ItemBlacklist;
import com.josemarcellio.joseplugin.blacklist.module.WorldBlacklist;
import com.josemarcellio.joseplugin.blacklist.type.BlacklistType;
import org.bukkit.entity.Player;

public class Blacklist {

    private final WorldBlacklist worldBlacklist = new WorldBlacklist();
    private final ItemBlacklist itemBlacklist = new ItemBlacklist();

    public boolean isBlacklisted(BlacklistType type, Object object) {
        return switch (type) {
            case ITEM -> object instanceof String && itemBlacklist.isBlacklisted((String) object);
            case WORLD -> object instanceof Player && worldBlacklist.isBlacklisted((Player) object);
        };
    }
}
