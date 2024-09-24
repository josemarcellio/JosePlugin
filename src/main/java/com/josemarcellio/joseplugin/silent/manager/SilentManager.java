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

package com.josemarcellio.joseplugin.silent.manager;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SilentManager {

    private final Map<UUID, Boolean> silentKickedPlayers = new HashMap<>();

    public void addPlayer(UUID uuid) {
        silentKickedPlayers.put(uuid, true);
    }

    public void removePlayer(UUID uuid) {
        silentKickedPlayers.remove(uuid);
    }

    public boolean isSilentKicked(UUID uuid) {
        return silentKickedPlayers.containsKey(uuid);
    }
}
