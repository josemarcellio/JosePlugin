package com.josemarcellio.joseplugin.job.manager;

import com.josemarcellio.joseplugin.JosePlugin;

public class PlayerJobData {
    private final JosePlugin plugin;
    private final String job;
    private int level;
    private double exp;

    public PlayerJobData(JosePlugin plugin, String job, int level, double exp) {
        this.plugin = plugin;
        this.job = job;
        this.level = level;
        this.exp = exp;
    }

    public String getJob() {
        return job;
    }

    public int getLevel() {
        return level;
    }

    public double getExp() {
        return exp;
    }

    public void addExp(double amount) {
        this.exp += amount;
        while (this.exp >= getExpToNextLevel() && this.level < plugin.getJobsManager().getMaxLevel()) {
            this.exp -= getExpToNextLevel();
            this.level++;
        }
    }

    private int getExpToNextLevel() {
        return this.level * 100;
    }
}