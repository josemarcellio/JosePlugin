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

package com.josemarcellio.joseplugin.job.manager;

import com.josemarcellio.joseplugin.JosePlugin;
import com.josemarcellio.joseplugin.exception.JosePluginException;
import com.josemarcellio.joseplugin.job.rewards.JobsLevelRewards;
import com.josemarcellio.joseplugin.component.ComponentBuilder;
import com.josemarcellio.joseplugin.job.runnable.JobsTask;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.entity.Player;

import java.io.File;
import java.sql.*;
import java.util.*;

public class JobsManager {

    private final JosePlugin plugin;
    private final Map<UUID, PlayerJobData> playerJobs = new HashMap<>();
    private Connection connection;
    private final Map<String, String> jobTextures = new HashMap<>();
    private final Map<String, String> jobDisplayNames = new HashMap<>();
    private final Map<String, Integer> jobWorkerCount = new HashMap<>();
    private final Map<String, String> jobDescription = new HashMap<>();
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
        jobTextures.put("fisherman", "d804e42ec9b07fce1ce0058b78df5763f6e410d9ce82ef1ebb9597a152b6d4c8");
        jobTextures.put("breeder", "19712bc1d2ca42eb512f833cd56139df10284afa29d4489ddd62753dcd7adb30");

        jobDisplayNames.put("miner", "<aqua>Miner");
        jobDisplayNames.put("hunter", "<blue>Hunter");
        jobDisplayNames.put("farmer", "<yellow>Farmer");
        jobDisplayNames.put("lumberjack", "<red>Lumberjack");
        jobDisplayNames.put("fisherman", "<light_purple>Fisherman");
        jobDisplayNames.put("breeder", "<green>Breeder");

        jobDescription.put("miner", "<aqua>Hancurkan block sesuai dengan required task");
        jobDescription.put("hunter", "<aqua>Bunuh mob sesuai dengan required task");
        jobDescription.put("farmer", "<aqua>Panen hasil pertanian sesuai dengan required task");
        jobDescription.put("lumberjack", "<aqua>Tebang pohon sesuai dengan required task");
        jobDescription.put("fisherman", "<aqua>Mancing ikan sesuai dengan required task");
        jobDescription.put("breeder", "<aqua>Kawinkan ternak sesuai dengan required task");
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

    public String getJobDescription(String job) {
        return jobDescription.getOrDefault(job, "None");
    }

    private void startAutoSaveTask() {
        new JobsTask(this).runTaskTimerAsynchronously(plugin, 0L, 6000L);
    }

    public void saveAllDirtyPlayers() {
        for (UUID playerUUID : dirtyPlayers) {
            PlayerJobData data = playerJobs.get(playerUUID);
            if (data != null) {
                saveJobToDatabase(playerUUID, data.getJob(), data.getLevel(), data.getExp());
            } else {
                deleteJobFromDatabase(playerUUID);
            }
        }
        dirtyPlayers.clear();
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
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
            throw new JosePluginException("Failed to setup database", e);
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
            throw new JosePluginException("Failed to load jobs from database", e);
        }
    }

    public boolean isJobFull(String job) {
        return getTotalWorkers(job) >= maxWorkersPerJob;
    }


    public boolean joinJob(UUID playerUUID, String job) {
        if (playerJobs.containsKey(playerUUID)) {
            return false;
        }

        playerJobs.put(playerUUID, new PlayerJobData(plugin, job, 1, 0));
        jobWorkerCount.put(job, jobWorkerCount.getOrDefault(job, 0) + 1);
        dirtyPlayers.add(playerUUID);
        return true;
    }


    public void leaveJob(UUID playerUUID) {
        PlayerJobData data = playerJobs.get(playerUUID);
        if (data == null) {
            return;
        }

        String job = data.getJob();
        playerJobs.remove(playerUUID);
        jobWorkerCount.put(job, jobWorkerCount.getOrDefault(job, 1) - 1);
        dirtyPlayers.add(playerUUID);
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
                    player.sendMessage(componentBuilder.singleComponentBuilder().text("<yellow> ✪ <color:#fae7b5>Jobs <color:#c4c3d0>• <white>Selamat jobs " + getDisplayName(data.getJob()) + " <white>telah naik ke level <green>" + data.getLevel() + "<gray>!").build());

                    rewardsManager.giveLevelUpRewards(player, data.getLevel());
                }
            }

            dirtyPlayers.add(playerUUID);
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
            throw new JosePluginException("Failed to save job to database", e);
        }
    }

    private void deleteJobFromDatabase(UUID playerUUID) {
        try (PreparedStatement ps = connection.prepareStatement("DELETE FROM player_jobs WHERE uuid = ?")) {
            ps.setString(1, playerUUID.toString());
            ps.executeUpdate();
        } catch (Exception e) {
            throw new JosePluginException("Failed to delete job from database", e);
        }
    }

    public void closeDatabase() {
        try {
            if (connection != null && !connection.isClosed()) {
                saveAllDirtyPlayers();
                connection.close();
            }
        } catch (Exception e) {
            throw new JosePluginException("Failed to close database", e);
        }
    }

    public double getEconReward(int level) {
        return 40 + (level * 2);
    }

    public int getMaxLevel() {
        return 1000;
    }
}