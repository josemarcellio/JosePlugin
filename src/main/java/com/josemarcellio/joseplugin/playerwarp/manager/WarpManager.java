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

package com.josemarcellio.joseplugin.playerwarp.manager;

import com.josemarcellio.joseplugin.exception.JosePluginException;
import com.josemarcellio.joseplugin.playerwarp.runnable.WarpsTask;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.sql.*;
import java.util.*;

public class WarpManager {
    private final JavaPlugin plugin;
    private final Map<String, Warp> warps = new HashMap<>();
    private final Set<UUID> dirtyPlayers = new HashSet<>();
    private Connection connection;

    public WarpManager(JavaPlugin plugin) {
        this.plugin = plugin;
        setupDatabase();
        createTableIfNotExists();
        loadWarps();
        startAutoSaveTask();
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private void setupDatabase() {
        File dataFolder = plugin.getDataFolder();
        if (!dataFolder.exists()) {
            plugin.getLogger().info("Creating plugin data folder...");
            dataFolder.mkdir();
        }
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:" + plugin.getDataFolder() + "/warps.db");
            plugin.getLogger().info("Successfully connected to SQLite database.");
        } catch (SQLException e) {
            plugin.getLogger().severe("Could not connect to SQLite database.");
            throw new JosePluginException("Could not connect to SQLite database.", e);
        }
    }

    public void startAutoSaveTask() {
        new WarpsTask(this).runTaskTimerAsynchronously(plugin, 0L, 6000L);
    }

    public void saveAllDirtyPlayers() {
        for (UUID playerUUID : dirtyPlayers) {
            Set<String> playerWarps = new HashSet<>();

            warps.entrySet().stream()
                    .filter(entry -> entry.getValue().getOwnerUUID().equals(playerUUID.toString()))
                    .forEach(entry -> {
                        playerWarps.add(entry.getKey());
                        saveWarp(entry.getKey());
                    });

            deleteWarpFromDatabase(playerUUID, playerWarps);
        }
        dirtyPlayers.clear();
    }

    private void createTableIfNotExists() {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS warps (" +
                "name TEXT PRIMARY KEY, " +
                "world TEXT, " +
                "x REAL, " +
                "y REAL, " +
                "z REAL, " +
                "yaw REAL, " +
                "pitch REAL, " +
                "owner TEXT, " +
                "ownerUUID TEXT, " +
                "description TEXT, " +
                "visitor INTEGER, " +
                "material TEXT, " +
                "textures TEXT)";
        try (Statement statement = connection.createStatement()) {
            statement.execute(createTableSQL);
        } catch (SQLException e) {
            plugin.getLogger().severe("Could not create table in SQLite database.");
            throw new JosePluginException("Could not create table in SQLite database.", e);
        }
    }

    public void loadWarps() {
        String query = "SELECT * FROM warps";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                String name = resultSet.getString("name");
                Warp warp = new Warp(
                        resultSet.getString("world"),
                        resultSet.getDouble("x"),
                        resultSet.getDouble("y"),
                        resultSet.getDouble("z"),
                        (float) resultSet.getDouble("yaw"),
                        (float) resultSet.getDouble("pitch"),
                        resultSet.getString("owner"),
                        resultSet.getString("ownerUUID"),
                        resultSet.getString("description"),
                        resultSet.getInt("visitor"),
                        resultSet.getString("material"),
                        resultSet.getString("textures")
                );
                warps.put(name, warp);
            }
        } catch (SQLException e) {
            plugin.getLogger().severe("Could not load warps from SQLite database.");
            throw new JosePluginException("Could not load warps from SQLite database.", e);
        }
    }

    public void saveWarp(String name) {
        Warp warp = warps.get(name);
        if (warp == null) return;

        String insertOrUpdateSQL = "INSERT OR REPLACE INTO warps (name, world, x, y, z, yaw, pitch, owner, ownerUUID, description, visitor, material, textures) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(insertOrUpdateSQL)) {
            statement.setString(1, name);
            statement.setString(2, warp.getWorld());
            statement.setDouble(3, warp.getX());
            statement.setDouble(4, warp.getY());
            statement.setDouble(5, warp.getZ());
            statement.setFloat(6, warp.getYaw());
            statement.setFloat(7, warp.getPitch());
            statement.setString(8, warp.getOwner());
            statement.setString(9, warp.getOwnerUUID());
            statement.setString(10, warp.getDescription());
            statement.setInt(11, warp.getVisitor());
            statement.setString(12, warp.getMaterial().name());
            statement.setString(13, warp.getTextures());
            statement.executeUpdate();
        } catch (SQLException e) {
            plugin.getLogger().severe("Could not save warp to SQLite database.");
            throw new JosePluginException("Could not save warp to SQLite database.", e);
        }
    }

    public void addWarp(String name, Warp warp) {
        warps.put(name, warp);
        dirtyPlayers.add(UUID.fromString(warp.getOwnerUUID()));
    }

    public void deleteWarp(String name) {
        Warp removedWarp = warps.remove(name);
        if (removedWarp != null) {
            dirtyPlayers.add(UUID.fromString(removedWarp.getOwnerUUID()));
        }
    }

    private void deleteWarpFromDatabase(UUID playerUUID, Set<String> playerWarps) {
        String query = "SELECT name FROM warps WHERE ownerUUID = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, playerUUID.toString());
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String warpName = resultSet.getString("name");
                if (!playerWarps.contains(warpName)) {
                    String deleteSQL = "DELETE FROM warps WHERE name = ?";
                    try (PreparedStatement deleteStatement = connection.prepareStatement(deleteSQL)) {
                        deleteStatement.setString(1, warpName);
                        deleteStatement.executeUpdate();
                    }
                }
            }
        } catch (SQLException e) {
            plugin.getLogger().severe("Could not delete warp from SQLite database.");
            throw new JosePluginException("Could not delete warp from SQLite database.", e);
        }
    }

    public Map<String, Warp> getWarps() {
        return warps;
    }

    public Warp getWarp(String name) {
        return warps.get(name);
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
}