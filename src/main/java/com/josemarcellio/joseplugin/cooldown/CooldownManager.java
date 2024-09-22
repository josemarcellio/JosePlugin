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
