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

package com.josemarcellio.joseplugin.cooldown;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CooldownManager implements ICooldownManager {

    private final Map<UUID, Map<String, Cooldown>> cooldowns = new HashMap<>();

    @Override
    public void startCooldown(UUID playerUUID, String action, long durationInMillis) {
        cooldowns.putIfAbsent(playerUUID, new HashMap<>());
        cooldowns.get(playerUUID).put(action, new Cooldown(durationInMillis));
    }

    @Override
    public boolean isOnCooldown(UUID playerUUID, String action) {
        if (!cooldowns.containsKey(playerUUID)) {
            return false;
        }

        Cooldown cooldown = cooldowns.get(playerUUID).get(action);
        if (cooldown == null || cooldown.isExpired()) {
            cooldowns.get(playerUUID).remove(action);
            return false;
        }

        return true;
    }

    @Override
    public long getRemainingTime(UUID playerUUID, String action) {
        if (isOnCooldown(playerUUID, action)) {
            return cooldowns.get(playerUUID).get(action).getRemainingTime();
        }
        return 0;
    }

    @Override
    public void removeCooldown(UUID playerUUID, String action) {
        if (cooldowns.containsKey(playerUUID)) {
            cooldowns.get(playerUUID).remove(action);
        }
    }
}
