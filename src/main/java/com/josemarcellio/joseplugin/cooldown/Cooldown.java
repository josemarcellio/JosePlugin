package com.josemarcellio.joseplugin.cooldown;

public class Cooldown {
    private final long endTime;

    public Cooldown(long durationInMillis) {
        this.endTime = System.currentTimeMillis() + durationInMillis;
    }

    public boolean isExpired() {
        return System.currentTimeMillis() >= endTime;
    }

    public long getRemainingTime() {
        return Math.max(0, endTime - System.currentTimeMillis());
    }
}
