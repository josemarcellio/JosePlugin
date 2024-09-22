package com.josemarcellio.joseplugin.cooldown;

import java.util.UUID;

public interface ICooldownManager {
    void startCooldown(UUID playerUUID, String action, long durationInMillis);

    boolean isOnCooldown(UUID playerUUID, String action);

    long getRemainingTime(UUID playerUUID, String action);

    void removeCooldown(UUID playerUUID, String action);
}
