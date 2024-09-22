package com.josemarcellio.joseplugin.job.gui;

import com.josemarcellio.joseplugin.JosePlugin;
import com.josemarcellio.joseplugin.inventory.GUIItem;
import com.josemarcellio.joseplugin.inventory.GUIManager;
import com.josemarcellio.joseplugin.inventory.builder.GUIBuilder;
import com.josemarcellio.joseplugin.inventory.page.GUIPage;
import com.josemarcellio.joseplugin.item.ItemBuilderFactory;
import com.josemarcellio.joseplugin.job.manager.JobsManager;
import com.josemarcellio.joseplugin.skull.type.SkullType;
import com.josemarcellio.joseplugin.text.component.ComponentBuilder;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.List;

public class JobsGUI {

    private final JosePlugin plugin;
    private final ComponentBuilder componentBuilder = new ComponentBuilder();
    private final ItemBuilderFactory itemBuilderFactory = new ItemBuilderFactory();

    public JobsGUI(JosePlugin plugin) {
        this.plugin = plugin;
    }

    public  void openGUI(Player player) {
        GUIBuilder builder = new GUIBuilder(componentBuilder.singleComponentBuilder("<aqua>Jobs Menu").build(), 6 * 9);

        addJobItem(builder, player, "miner");
        addJobItem(builder, player, "hunter");
        addJobItem(builder, player, "farmer");
        addJobItem(builder, player, "lumberjack");

        addItem(builder);
        addGlassPane(builder);

        GUIPage page = builder.build();
        GUIManager.openGUI(player, page);
    }

    private void addJobItem(GUIBuilder builder, Player player, String job) {
        JobsManager jobsManager = plugin.getJobsManager();
        String displayName = jobsManager.getDisplayName(job);

        String joined;
        if (jobsManager.getJob(player.getUniqueId()).equals(job)) {
            joined = "<red>Kamu telah bergabung dengan jobs " + displayName;
        } else {
            joined = "<green>Klik untuk bergabung dengan jobs " + displayName;
        }

        List<Component> lore = Arrays.asList(
                componentBuilder.singleComponentBuilder("").build(),
                componentBuilder.singleComponentBuilder("<gray>Total Worker: <aqua>" + jobsManager.getTotalWorkers(job) + "<dark_gray>/<aqua>" + jobsManager.getMaxWorkersPerJob()).build(),
                componentBuilder.singleComponentBuilder("").build(),
                componentBuilder.singleComponentBuilder("<green>Klik kiri <gray>untuk bergabung dengan jobs").build(),
                componentBuilder.singleComponentBuilder("<yellow>Klik kanan <gray>untuk melihat required task").build(),
                componentBuilder.singleComponentBuilder("<light_purple>Tombol 'Q' <gray>untuk keluar dari jobs").build(),
                componentBuilder.singleComponentBuilder("").build(),
                componentBuilder.singleComponentBuilder(joined).build()
        );

        GUIItem guiItem = new GUIItem(itemBuilderFactory.createSkullItemBuilder(jobsManager.getTextures(job), SkullType.TEXTURE_ID)
        .setName(componentBuilder.singleComponentBuilder(jobsManager.getDisplayName(job)).build())
        .setLore(lore).build(),event -> {
            if (event.getClick().isLeftClick()) {
                if (jobsManager.joinJob(player.getUniqueId(), job)) {
                    event.getWhoClicked().sendMessage(componentBuilder.singleComponentBuilder("<yellow> ✪ <color:#fae7b5>Jobs <color:#c4c3d0>• <white>Kamu berhasil bergabung dengan job " + displayName).build());
                    event.getWhoClicked().closeInventory();
                    openGUI(player);
                } else {
                    if (!jobsManager.getJob(event.getWhoClicked().getUniqueId()).equals(job)) {
                        event.getWhoClicked().sendMessage(componentBuilder.singleComponentBuilder("<yellow> ✪ <color:#fae7b5>Jobs <color:#c4c3d0>• <white>Gagal bergabung dengan job " + displayName + " <white>silahkan keluar dari job " + jobsManager.getDisplayName(jobsManager.getJob(event.getWhoClicked().getUniqueId())) + " <white>terlebih dahulu!").build());
                    }
                }
            } else if (event.getClick() == ClickType.DROP) {
                if (jobsManager.getJob(event.getWhoClicked().getUniqueId()).equals(job)) {
                    event.getWhoClicked().closeInventory();
                    JobsLeaveJobConfirmation leaveJobConfirmation = new JobsLeaveJobConfirmation(plugin);
                    leaveJobConfirmation.openGUI((Player) event.getWhoClicked());
                }
            } else if (event.getClick().isRightClick()) {
                event.getWhoClicked().closeInventory();
                JobsProgressionGUI jobsProgressionGUI = new JobsProgressionGUI(plugin);
                jobsProgressionGUI.openGUI(player, job);
            }
        });

        builder.addItem(getSlotForJob(job), guiItem);
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
                .setName(componentBuilder.singleComponentBuilder("<red>Close</red>").build()).build(), event ->
                event.getWhoClicked().closeInventory());
        builder.addItem(49, guiClose);
    }

    private int getSlotForJob(String job) {
        switch (job) {
            case "miner": return 20;
            case "hunter": return 21;
            case "farmer": return 22;
            case "lumberjack": return 23;
            default: return 0;
        }
    }
}
