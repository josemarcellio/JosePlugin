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

import org.bukkit.Location;
import org.bukkit.Material;

public class Warp {
    private String world;
    private double x;
    private double y;
    private double z;
    private float yaw;
    private float pitch;
    private final String owner;
    private final String ownerUUID;
    private String description;
    private int visitor;
    private Material material;
    private String textures;

    public Warp(String world, double x, double y, double z, float yaw, float pitch, String owner, String ownerUUID, String description, int visitor, String material, String textures) {
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
        this.owner = owner;
        this.ownerUUID = ownerUUID;
        this.description = description;
        this.visitor = visitor;
        this.material = validateMaterial(material);
        this.textures = textures;
    }

    private Material validateMaterial(String material) {
        Material matchedMaterial = (material != null) ? Material.matchMaterial(material) : null;
        return (matchedMaterial != null) ? matchedMaterial : Material.CHEST;
    }


    public String getWorld() { return world; }
    public double getX() { return x; }
    public double getY() { return y; }
    public double getZ() { return z; }
    public float getYaw() { return yaw; }
    public float getPitch() { return pitch; }
    public String getOwner() { return owner; }
    public String getOwnerUUID() { return ownerUUID; }
    public String getDescription() { return description; }
    public void setDescription(String description) {
        this.description = description;
    }
    public void setLocation(Location location) {
        if (location != null) {
            this.world = location.getWorld().getName();
            this.x = location.getX();
            this.y = location.getY();
            this.z = location.getZ();
            this.yaw = location.getYaw();
            this.pitch = location.getPitch();
        }
    }
    public int getVisitor() { return visitor; }
    public void incrementVisitor() { this.visitor++; }

    public Material getMaterial() {
        return material;
    }

    public String getTextures() {
        return textures;
    }

    public void setMaterial(String material) {
        this.material = validateMaterial(material);
    }

    public void setTextures(String textures) {
        this.textures = textures;
    }
}