package com.josemarcellio.joseplugin.job.gui;

import com.josemarcellio.joseplugin.JosePlugin;
import com.josemarcellio.joseplugin.inventory.GUIManager;
import com.josemarcellio.joseplugin.inventory.builder.GUIBuilder;
import com.josemarcellio.joseplugin.inventory.GUIItem;
import com.josemarcellio.joseplugin.inventory.page.GUIPage;
import com.josemarcellio.joseplugin.item.ItemBuilderFactory;
import com.josemarcellio.joseplugin.job.data.JobsProgressionData;
import com.josemarcellio.joseplugin.skull.type.SkullType;
import com.josemarcellio.joseplugin.component.ComponentBuilder;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

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
        GUIBuilder builder = new GUIBuilder(componentBuilder.singleComponentBuilder().text("<aqua>Jobs Progression</aqua>").build(), 54); // 6 rows * 9 slots = 54

        addProgressionItems(builder, jobs);

        addGlassPane(builder);
        addItem(builder);

        GUIPage page = builder.build();
        GUIManager.openGUI(player, page);
    }

    private void addProgressionItems(GUIBuilder builder, String job) {
        JobsProgressionData progressionData = plugin.getJobProgressionData();
        int slotIndex = 0;

        if ("miner".equalsIgnoreCase(job)) {
            for (Map.Entry<Material, Double> entry : progressionData.getMinerBlockExpMap().entrySet()) {
                if (slotIndex >= PROGRESSION_SLOTS.length) break;
                addProgressionItem(builder, entry.getKey(), entry.getValue(), PROGRESSION_SLOTS[slotIndex]);
                slotIndex++;
            }
        } else if ("farmer".equalsIgnoreCase(job)) {
            for (Map.Entry<Material, Double> entry : progressionData.getFarmerMaterialExpMap().entrySet()) {
                if (slotIndex >= PROGRESSION_SLOTS.length) break;
                addProgressionItem(builder, entry.getKey(), entry.getValue(), PROGRESSION_SLOTS[slotIndex]);
                slotIndex++;
            }
        } else if ("lumberjack".equalsIgnoreCase(job)) {
            for (Map.Entry<Material, Double> entry : progressionData.getLumberjackBlockExpMap().entrySet()) {
                if (slotIndex >= PROGRESSION_SLOTS.length) break;
                addProgressionItem(builder, entry.getKey(), entry.getValue(), PROGRESSION_SLOTS[slotIndex]);
                slotIndex++;
            }
        } else if ("hunter".equalsIgnoreCase(job)) {
            for (Map.Entry<EntityType, Material> entry : progressionData.getHunterMobSpawnEggMap().entrySet()) {
                if (slotIndex >= PROGRESSION_SLOTS.length) break;
                double expValue = progressionData.getHunterMobExpMap().get(entry.getKey());
                addProgressionItem(builder, entry.getValue(), expValue, PROGRESSION_SLOTS[slotIndex]);
                slotIndex++;
            }
        }
    }

    private void addProgressionItem(GUIBuilder builder, Material material, double expValue, int slot) {

        builder.addItem(slot, new GUIItem(itemBuilderFactory.createItemBuilder(material)
                .setName(componentBuilder.singleComponentBuilder().text(material.name()).build())
                .addLore(componentBuilder.singleComponentBuilder().text("<gray>Exp: <aqua>" + expValue).build()).build(), event -> event.setCancelled(true)));
    }

    private void addGlassPane(GUIBuilder builder) {
        ItemStack glassItem = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
        ItemMeta meta = glassItem.getItemMeta();
        meta.displayName(Component.text(" "));
        glassItem.setItemMeta(meta);

        int[] glassSlots = {
                0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 17, 18, 26, 27, 35,
                36, 44, 45, 46, 47, 48, 50, 51, 52, 53
        };

        for (int slot : glassSlots) {
            builder.addItem(slot, new GUIItem(glassItem, null));
        }
    }

    private void addItem(GUIBuilder builder) {

        GUIItem guiClose = new GUIItem(itemBuilderFactory.createSkullItemBuilder("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYmViNTg4YjIxYTZmOThhZDFmZjRlMDg1YzU1MmRjYjA1MGVmYzljYWI0MjdmNDYwNDhmMThmYzgwMzQ3NWY3In19fQ==", SkullType.BASE64)
                .setName(componentBuilder.singleComponentBuilder().text("<red>Close</red>").build()).build(), event ->
                event.getWhoClicked().closeInventory());
        builder.addItem(49, guiClose);
    }
}
