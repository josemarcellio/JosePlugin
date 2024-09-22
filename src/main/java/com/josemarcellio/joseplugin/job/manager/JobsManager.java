package com.josemarcellio.joseplugin.job.manager;

import com.josemarcellio.joseplugin.JosePlugin;
import com.josemarcellio.joseplugin.job.rewards.JobsLevelRewards;
import com.josemarcellio.joseplugin.text.component.ComponentBuilder;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.sql.*;
import java.util.*;

public class JobsManager {

    private final JosePlugin plugin;
    private final Map<UUID, PlayerJobData> playerJobs = new HashMap<>();
    private Connection connection;
    private final String[] validJobs = {"miner", "hunter", "farmer", "lumberjack"};
    private final Map<String, String> jobTextures = new HashMap<>();
    private final Map<String, String> jobDisplayNames = new HashMap<>();
    private final Map<String, Integer> jobWorkerCount = new HashMap<>();
    private final int maxWorkersPerJob = 30;
    private final JobsLevelRewards rewardsManager;
    private final Set<UUID> dirtyPlayers = new HashSet<>();
    private final ComponentBuilder componentBuilder = new ComponentBuilder();


    public JobsManager(JosePlugin plugin, Economy econ) {
        this.plugin = plugin;
        this.rewardsManager = new JobsLevelRewards(plugin, econ);
        setupDatabase();
        setupJobData();
        startAutoSaveTask();
    }

    private void setupJobData() {
        jobTextures.put("miner", "a236b0e63ecbbe2a0090e4bd4f043d36b6068d25bb981389765450d8d7ee6d8c");
        jobTextures.put("hunter", "d1d0e900e5484d8d562a8566588c8b6390065b68a279c7c7bf275ebdd2ee9b03");
        jobTextures.put("farmer", "5205c1a85929832b118b62a352efa09db4fc035aa44aac660059c0fde579fde");
        jobTextures.put("lumberjack", "63c143640079253eff861872b6c5580c755eda6bdc3ff74a9846d0ce790c7b2e");

        jobDisplayNames.put("miner", "<aqua>Miner");
        jobDisplayNames.put("hunter", "<blue>Hunter");
        jobDisplayNames.put("farmer", "<yellow>Farmer");
        jobDisplayNames.put("lumberjack", "<red>Lumberjack");
    }

    public int getMaxWorkersPerJob() {
        return maxWorkersPerJob;
    }

    public String getTextures(String job) {
        return jobTextures.getOrDefault(job, "");
    }

    public String getDisplayName(String job) {
        return jobDisplayNames.getOrDefault(job, "None");
    }

    public boolean isValidJob(String job) {
        for (String validJob : validJobs) {
            if (validJob.equalsIgnoreCase(job)) {
                return true;
            }
        }
        return false;
    }

    private void startAutoSaveTask() {
        new BukkitRunnable() {
            @Override
            public void run() {
                saveAllDirtyPlayers();
            }
        }.runTaskTimerAsynchronously(plugin, 0L, 6000L);
    }

    public void saveAllDirtyPlayers() {
        for (UUID playerUUID : dirtyPlayers) {
            PlayerJobData data = playerJobs.get(playerUUID);
            if (data != null) {
                saveJobToDatabase(playerUUID, data.getJob(), data.getLevel(), data.getExp());
            }
        }
        dirtyPlayers.clear();
    }

    private void setupDatabase() {
        File dataFolder = plugin.getDataFolder();
        if (!dataFolder.exists()) {
            plugin.getLogger().info("Creating plugin data folder...");
            dataFolder.mkdir();
        }
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:" + plugin.getDataFolder() + "/jobs.db");
            try (Statement statement = connection.createStatement()) {
                statement.executeUpdate("CREATE TABLE IF NOT EXISTS player_jobs (uuid TEXT PRIMARY KEY, job TEXT, level INTEGER, exp INTEGER);");
            }
            loadJobsFromDatabase();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadJobsFromDatabase() {
        try (Statement statement = connection.createStatement()) {
            ResultSet rs = statement.executeQuery("SELECT * FROM player_jobs");
            while (rs.next()) {
                UUID uuid = UUID.fromString(rs.getString("uuid"));
                String job = rs.getString("job");
                int level = rs.getInt("level");
                int exp = rs.getInt("exp");

                playerJobs.put(uuid, new PlayerJobData(plugin, job, level, exp));
                jobWorkerCount.put(job, jobWorkerCount.getOrDefault(job, 0) + 1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isJobFull(String job) {
        return getTotalWorkers(job) >= maxWorkersPerJob;
    }


    public boolean joinJob(UUID playerUUID, String job) {
        if (playerJobs.containsKey(playerUUID)) {
            return false;
        }

        playerJobs.put(playerUUID, new PlayerJobData(plugin, job, 1, 0)); // Start at level 1, 0 EXP
        jobWorkerCount.put(job, jobWorkerCount.getOrDefault(job, 0) + 1);
        saveJobToDatabase(playerUUID, job, 1, 0);
        return true;
    }


    public boolean leaveJob(UUID playerUUID) {
        PlayerJobData data = playerJobs.get(playerUUID);
        if (data == null) {
            return false; // Player does not have a job
        }

        String job = data.getJob();
        playerJobs.remove(playerUUID);
        jobWorkerCount.put(job, jobWorkerCount.getOrDefault(job, 1) - 1);
        removeJobFromDatabase(playerUUID);
        return true;
    }

    public int getTotalWorkers(String job) {
        return jobWorkerCount.getOrDefault(job, 0);
    }

    public String getJob(UUID playerUUID) {
        PlayerJobData data = playerJobs.get(playerUUID);
        return data != null ? data.getJob() : "";
    }

    public int getLevel(UUID playerUUID) {
        PlayerJobData data = playerJobs.get(playerUUID);
        return data != null ? data.getLevel() : 0;
    }

    public double getExp(UUID playerUUID) {
        PlayerJobData data = playerJobs.get(playerUUID);
        return data != null ? data.getExp() : 0;
    }

    public void giveExp(UUID playerUUID, double exp) {
        PlayerJobData data = playerJobs.get(playerUUID);
        if (data != null) {
            int previousLevel = data.getLevel();
            data.addExp(exp);

            if (data.getLevel() > previousLevel) {
                Player player = plugin.getServer().getPlayer(playerUUID);
                if (player != null && player.isOnline()) {
                    player.sendMessage(componentBuilder.singleComponentBuilder("<yellow> ✪ <color:#fae7b5>Jobs <color:#c4c3d0>• <white>Selamat jobs " + getDisplayName(data.getJob()) + " <white>telah naik ke level <green>" + data.getLevel() + "<gray>!").build());

                    rewardsManager.giveLevelUpRewards(player, data.getLevel());
                }
            }

            dirtyPlayers.add(playerUUID); // Mark the player as having unsaved data
        }
    }

    private void saveJobToDatabase(UUID playerUUID, String job, int level, double exp) {
        try (PreparedStatement ps = connection.prepareStatement("INSERT OR REPLACE INTO player_jobs (uuid, job, level, exp) VALUES (?, ?, ?, ?)")) {
            ps.setString(1, playerUUID.toString());
            ps.setString(2, job);
            ps.setInt(3, level);
            ps.setDouble(4, exp);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void removeJobFromDatabase(UUID playerUUID) {
        try (PreparedStatement ps = connection.prepareStatement("DELETE FROM player_jobs WHERE uuid = ?")) {
            ps.setString(1, playerUUID.toString());
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void closeDatabase() {
        try {
            if (connection != null && !connection.isClosed()) {
                saveAllDirtyPlayers();
                connection.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public double getEconReward(int level) {
        return 40 + (level * 2);
    }

    public int getMaxLevel() {
        return 114;
    }
}
