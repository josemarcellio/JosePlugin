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

package com.josemarcellio.joseplugin.job.data;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;

import java.util.HashMap;
import java.util.Map;

public class JobsProgressionData {

    private final Map<Material, Double> minerBlockExpMap = new HashMap<>();

    private final Map<EntityType, Material> hunterMobSpawnEggMap = new HashMap<>();
    private final Map<EntityType, Double> hunterMobExpMap = new HashMap<>();

    private final Map<Material, Double> farmerBlockExpMap = new HashMap<>();
    private final Map<Material, Double> farmerMaterialExpMap = new HashMap<>();

    private final Map<Material, Double> lumberjackBlockExpMap = new HashMap<>();

    private final Map<Material, Double> fishermanFishExpMap = new HashMap<>();

    private final Map<EntityType, Material> breederMobSpawnEggMap = new HashMap<>();
    private final Map<EntityType, Double> breederMobExpMap = new HashMap<>();

    public JobsProgressionData() {
        setupMinerData();
        setupHunterData();
        setupFarmerBlockExpMap();
        setupLumberjackBlockExpMap();
        setupFishermanFishExpMap();
        setupBreederBlockExpMap();
    }

    private void setupMinerData() {
        minerBlockExpMap.put(Material.STONE, 2.5);
        minerBlockExpMap.put(Material.COAL_ORE, 7.5);
        minerBlockExpMap.put(Material.IRON_ORE, 10.5);
        minerBlockExpMap.put(Material.DIAMOND_ORE, 50.0);
        minerBlockExpMap.put(Material.EMERALD_ORE, 50.0);
        minerBlockExpMap.put(Material.DEEPSLATE_COAL_ORE, 5.5);
        minerBlockExpMap.put(Material.DEEPSLATE_IRON_ORE, 5.5);
        minerBlockExpMap.put(Material.DEEPSLATE_REDSTONE_ORE, 4.5);
        minerBlockExpMap.put(Material.DEEPSLATE_LAPIS_ORE, 4.5);
        minerBlockExpMap.put(Material.DEEPSLATE_EMERALD_ORE, 8.5);
        minerBlockExpMap.put(Material.DEEPSLATE_DIAMOND_ORE, 8.5);
        minerBlockExpMap.put(Material.DEEPSLATE, 2.0);
    }

    private void setupHunterData() {
        hunterMobSpawnEggMap.put(EntityType.ZOMBIE, Material.ZOMBIE_HEAD);
        hunterMobSpawnEggMap.put(EntityType.SKELETON, Material.SKELETON_SKULL);
        hunterMobSpawnEggMap.put(EntityType.CREEPER, Material.CREEPER_HEAD);
        hunterMobSpawnEggMap.put(EntityType.PLAYER, Material.PLAYER_HEAD);
        hunterMobSpawnEggMap.put(EntityType.WITHER_SKELETON, Material.WITHER_SKELETON_SKULL);
        hunterMobSpawnEggMap.put(EntityType.PHANTOM, Material.PHANTOM_MEMBRANE);
        hunterMobSpawnEggMap.put(EntityType.BLAZE, Material.BLAZE_ROD);

        hunterMobExpMap.put(EntityType.ZOMBIE, 10.5);
        hunterMobExpMap.put(EntityType.SKELETON, 10.5);
        hunterMobExpMap.put(EntityType.CREEPER, 15.5);
        hunterMobExpMap.put(EntityType.PLAYER, 20.0);
        hunterMobExpMap.put(EntityType.WITHER_SKELETON, 25.5);
        hunterMobExpMap.put(EntityType.PHANTOM, 15.0);
        hunterMobExpMap.put(EntityType.BLAZE, 20.0);
    }

    private void setupFarmerBlockExpMap() {
        farmerMaterialExpMap.put(Material.WHEAT, 1.0);
        farmerMaterialExpMap.put(Material.CARROT, 1.0);
        farmerMaterialExpMap.put(Material.POTATO, 1.5);
        farmerMaterialExpMap.put(Material.BEETROOT, 1.5);
        farmerMaterialExpMap.put(Material.MELON, 5.0);
        farmerMaterialExpMap.put(Material.PUMPKIN, 3.0);
        farmerMaterialExpMap.put(Material.SUGAR_CANE, 1.0);

        farmerBlockExpMap.put(Material.WHEAT, 1.0);
        farmerBlockExpMap.put(Material.CARROTS, 1.0);
        farmerBlockExpMap.put(Material.POTATOES, 1.5);
        farmerBlockExpMap.put(Material.BEETROOTS, 1.5);
        farmerBlockExpMap.put(Material.MELON, 5.0);
        farmerBlockExpMap.put(Material.PUMPKIN, 3.0);
        farmerBlockExpMap.put(Material.SUGAR_CANE, 1.0);
    }

    private void setupLumberjackBlockExpMap() {
        lumberjackBlockExpMap.put(Material.ACACIA_LOG, 1.5);
        lumberjackBlockExpMap.put(Material.BIRCH_LOG, 1.5);
        lumberjackBlockExpMap.put(Material.CHERRY_LOG, 2.5);
        lumberjackBlockExpMap.put(Material.JUNGLE_LOG, 2.0);
        lumberjackBlockExpMap.put(Material.DARK_OAK_LOG, 1.5);
        lumberjackBlockExpMap.put(Material.MANGROVE_LOG, 3.0);
        lumberjackBlockExpMap.put(Material.OAK_LOG, 1.0);
        lumberjackBlockExpMap.put(Material.SPRUCE_LOG, 1.5);

    }

    private void setupFishermanFishExpMap() {
        fishermanFishExpMap.put(Material.COD, 2.5);
        fishermanFishExpMap.put(Material.SALMON, 3.0);
        fishermanFishExpMap.put(Material.PUFFERFISH, 3.5);
        fishermanFishExpMap.put(Material.TROPICAL_FISH, 4.0);
    }

    private void setupBreederBlockExpMap() {
        breederMobSpawnEggMap.put(EntityType.COW, Material.COW_SPAWN_EGG);
        breederMobSpawnEggMap.put(EntityType.CHICKEN, Material.CHICKEN_SPAWN_EGG);
        breederMobSpawnEggMap.put(EntityType.SHEEP, Material.SHEEP_SPAWN_EGG);
        breederMobSpawnEggMap.put(EntityType.PIG, Material.PIG_SPAWN_EGG);
        breederMobSpawnEggMap.put(EntityType.WOLF, Material.WOLF_SPAWN_EGG);

        breederMobExpMap.put(EntityType.COW, 2.5);
        breederMobExpMap.put(EntityType.CHICKEN, 2.5);
        breederMobExpMap.put(EntityType.SHEEP, 2.5);
        breederMobExpMap.put(EntityType.PIG, 2.5);
        breederMobExpMap.put(EntityType.WOLF, 2.5);
    }


    public Map<Material, Double> getMinerBlockExpMap() {
        return minerBlockExpMap;
    }

    public Map<EntityType, Material> getHunterMobSpawnEggMap() {
        return hunterMobSpawnEggMap;
    }

    public Map<EntityType, Double> getHunterMobExpMap() {
        return hunterMobExpMap;
    }

    public Map<Material, Double> getFarmerBlockExpMap() {
        return farmerBlockExpMap;
    }

    public Map<Material, Double> getFarmerMaterialExpMap() {
        return farmerMaterialExpMap;
    }

    public Map<Material, Double> getLumberjackBlockExpMap() {
        return lumberjackBlockExpMap;
    }

    public Map<Material, Double> getFishermanFishExpMap() {
        return fishermanFishExpMap;
    }

    public Map<EntityType, Double> getBreederMobExpMap() {
        return breederMobExpMap;
    }

    public Map<EntityType, Material> getBreederMobSpawnEggMap() {
        return breederMobSpawnEggMap;
    }
}