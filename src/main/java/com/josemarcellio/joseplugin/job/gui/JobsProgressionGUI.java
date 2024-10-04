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

package com.josemarcellio.joseplugin.job.gui;

import com.josemarcellio.joseplugin.JosePlugin;
import com.josemarcellio.joseplugin.inventory.GUIManager;
import com.josemarcellio.joseplugin.inventory.builder.GUIBuilder;
import com.josemarcellio.joseplugin.inventory.GUIItem;
import com.josemarcellio.joseplugin.inventory.page.GUIPage;
import com.josemarcellio.joseplugin.item.ItemBuilderFactory;
import com.josemarcellio.joseplugin.job.data.JobsProgressionData;
import com.josemarcellio.joseplugin.component.ComponentBuilder;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.Map;

public class JobsProgressionGUI {

    private final JosePlugin plugin;
    private final ComponentBuilder componentBuilder = new ComponentBuilder();
    private final ItemBuilderFactory itemBuilderFactory = new ItemBuilderFactory();

    private final int[] PROGRESSION_SLOTS = {
            10, 11, 12, 13, 14, 15, 16, 19, 20, 21, 22, 23, 24, 25,
            28, 29, 30, 31, 32, 33, 34, 37, 38, 39, 40, 41, 42, 43
    };

    public JobsProgressionGUI(JosePlugin plugin) {
        this.plugin = plugin;
    }

    public void openGUI(Player player, String jobs) {
        GUIBuilder builder = new GUIBuilder(componentBuilder.singleComponentBuilder().text("<aqua>Jobs Progression</aqua>").build(), 54);

        addProgressionItems(builder, jobs);

        builder.addGlassPane();
        builder.addCloseItem();

        GUIPage page = builder.build();
        GUIManager.openGUI(player, page);
    }

    private void addProgressionItems(GUIBuilder builder, String job) {
        JobsProgressionData progressionData = plugin.getJobProgressionData();
        int slotIndex = 0;

        Map<?, ?> expMap = switch (job.toLowerCase()) {
            case "miner" -> progressionData.getMinerBlockExpMap();
            case "farmer" -> progressionData.getFarmerMaterialExpMap();
            case "lumberjack" -> progressionData.getLumberjackBlockExpMap();
            case "hunter" -> progressionData.getHunterMobSpawnEggMap();
            case "fisherman" -> progressionData.getFishermanFishExpMap();
            case "breeder" -> progressionData.getBreederMobSpawnEggMap();
            default -> throw new IllegalArgumentException("Unknown job: " + job);
        };

        for (Map.Entry<?, ?> entry : expMap.entrySet()) {
            if (slotIndex >= PROGRESSION_SLOTS.length) break;

            Material material;
            double expValue;

            if (job.equalsIgnoreCase("hunter") || job.equalsIgnoreCase("breeder")) {
                EntityType entityType = (EntityType) entry.getKey();
                material = (Material) entry.getValue();
                expValue = progressionData.getHunterMobExpMap().getOrDefault(entityType, 0.0);
            } else {
                material = (Material) entry.getKey();
                expValue = (Double) entry.getValue();
            }

            addProgressionItem(builder, material, expValue, PROGRESSION_SLOTS[slotIndex]);
            slotIndex++;
        }
    }


    private void addProgressionItem(GUIBuilder builder, Material material, double expValue, int slot) {

        builder.addItem(slot, new GUIItem(itemBuilderFactory.createItemBuilder(material)
                .setName(componentBuilder.singleComponentBuilder().text(material.name()).build())
                .addLore(componentBuilder.singleComponentBuilder().text("<gray>Exp: <aqua>" + expValue).build()).build(), event -> event.setCancelled(true)));
    }
}
