package com.josemarcellio.joseplugin.playerwarp.manager;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class WarpManager {
    private final JavaPlugin plugin;
    private final Map<String, Warp> warps = new HashMap<>();
    private Connection connection;

    public WarpManager(JavaPlugin plugin) {
        this.plugin = plugin;
        setupDatabase();
        createTableIfNotExists();
        loadWarps();
        startAutoSaveTask();
    }

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
            e.printStackTrace();
        }
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
            e.printStackTrace();
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
            e.printStackTrace();
        }
    }

    public void saveWarps() {
        String insertOrUpdateSQL = "INSERT OR REPLACE INTO warps (name, world, x, y, z, yaw, pitch, owner, ownerUUID, description, visitor, material, textures) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(insertOrUpdateSQL)) {
            for (Map.Entry<String, Warp> entry : warps.entrySet()) {
                Warp warp = entry.getValue();
                statement.setString(1, entry.getKey());
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
                statement.addBatch();
            }
            statement.executeBatch();
        } catch (SQLException e) {
            plugin.getLogger().severe("Could not save warps to SQLite database.");
            e.printStackTrace();
        }
    }

    public void addWarp(String name, Warp warp) {
        warps.put(name, warp);
    }

    public void deleteWarp(String name) {
        warps.remove(name);
        String deleteSQL = "DELETE FROM warps WHERE name = ?";
        try (PreparedStatement statement = connection.prepareStatement(deleteSQL)) {
            statement.setString(1, name);
            statement.executeUpdate();
        } catch (SQLException e) {
            plugin.getLogger().severe("Could not delete warp from SQLite database.");
            e.printStackTrace();
        }
    }

    public Map<String, Warp> getWarps() {
        return warps;
    }

    public Warp getWarp(String name) {
        return warps.get(name);
    }

    public void startAutoSaveTask() {
        new BukkitRunnable() {
            @Override
            public void run() {
                saveWarps();
            }
        }.runTaskTimerAsynchronously(plugin, 0L, 6000L);
    }
}