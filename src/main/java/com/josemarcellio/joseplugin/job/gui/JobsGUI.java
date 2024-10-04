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

import com.josemarcellio.joseplugin.inventory.GUIItem;
import com.josemarcellio.joseplugin.inventory.GUIManager;
import com.josemarcellio.joseplugin.inventory.builder.GUIBuilder;
import com.josemarcellio.joseplugin.inventory.page.GUIPage;
import com.josemarcellio.joseplugin.item.ItemBuilderFactory;
import com.josemarcellio.joseplugin.job.manager.JobsManager;
import com.josemarcellio.joseplugin.skull.type.SkullType;
import com.josemarcellio.joseplugin.component.ComponentBuilder;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

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
        GUIBuilder builder = new GUIBuilder(componentBuilder.singleComponentBuilder().text("<aqua>Jobs Menu").build(), 6 * 9);

        addJobItem(builder, player, "miner");
        addJobItem(builder, player, "hunter");
        addJobItem(builder, player, "farmer");
        addJobItem(builder, player, "lumberjack");
        addJobItem(builder, player, "fisherman");
        addJobItem(builder, player, "breeder");

        builder.addGlassPane();
        builder.addCloseItem();

        GUIPage page = builder.build();
        GUIManager.openGUI(player, page);
    }

    private void addJobItem(GUIBuilder builder, Player player, String job) {
        JobsManager jobsManager = plugin.getJobsManager();
        String displayName = jobsManager.getDisplayName(job);

        List<Component> lore = Arrays.asList(
                componentBuilder.singleComponentBuilder().text("").build(),
                componentBuilder.singleComponentBuilder().text(jobsManager.getJobDescription(job)).build(),
                componentBuilder.singleComponentBuilder().text("").build(),
                componentBuilder.singleComponentBuilder().text("<gray>Total Worker: <aqua>" + jobsManager.getTotalWorkers(job) + "<dark_gray>/<aqua>" + jobsManager.getMaxWorkersPerJob()).build(),
                componentBuilder.singleComponentBuilder().text("").build(),
                componentBuilder.singleComponentBuilder().text("<green>Klik kiri <gray>untuk bergabung dengan jobs").build(),
                componentBuilder.singleComponentBuilder().text("<yellow>Klik kanan <gray>untuk melihat required task").build(),
                componentBuilder.singleComponentBuilder().text("<light_purple>Tombol 'Q' <gray>untuk keluar dari jobs").build(),
                componentBuilder.singleComponentBuilder().text("").build(),
                componentBuilder.singleComponentBuilder()
                        .addOperationIf(jobsManager.getJob(player.getUniqueId()).equals(job),
                                "<red>Kamu telah bergabung dengan jobs " + displayName,
                                "<green>Klik untuk bergabung dengan jobs " + displayName)
                        .build()
        );

        GUIItem guiItem = new GUIItem(itemBuilderFactory.createSkullItemBuilder(jobsManager.getTextures(job), SkullType.TEXTURE_ID)
        .setName(componentBuilder.singleComponentBuilder().text(jobsManager.getDisplayName(job)).build())
        .setLore(lore).build(),event -> {
            if (event.getClick().isLeftClick()) {
                if (jobsManager.isJobFull(job)) {
                    event.getWhoClicked().sendMessage(componentBuilder.singleComponentBuilder().text("<yellow> ✪ <color:#fae7b5>Jobs <color:#c4c3d0>• <white>Mohon maaf, job " + displayName + " <white>telah melebihi batas maksimal worker!, silahkan bergabung dengan job lain yang tersedia").build());
                } else {
                    if (jobsManager.joinJob(player.getUniqueId(), job)) {
                        event.getWhoClicked().sendMessage(componentBuilder.singleComponentBuilder().text("<yellow> ✪ <color:#fae7b5>Jobs <color:#c4c3d0>• <white>Kamu berhasil bergabung dengan job " + displayName).build());
                        event.getWhoClicked().closeInventory();
                        openGUI(player);
                    } else {
                        if (!jobsManager.getJob(event.getWhoClicked().getUniqueId()).equals(job)) {
                            event.getWhoClicked().sendMessage(componentBuilder.singleComponentBuilder().text("<yellow> ✪ <color:#fae7b5>Jobs <color:#c4c3d0>• <white>Gagal bergabung dengan job " + displayName + " <white>silahkan keluar dari job " + jobsManager.getDisplayName(jobsManager.getJob(event.getWhoClicked().getUniqueId())) + " <white>terlebih dahulu!").build());
                        }
                    }
                }
            } else if (event.getClick() == ClickType.DROP) {
                if (jobsManager.getJob(event.getWhoClicked().getUniqueId()).equals(job)) {
                    event.getWhoClicked().closeInventory();
                    JobsLeaveConfirmationGUI leaveJobConfirmation = new JobsLeaveConfirmationGUI(plugin);
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

    private int getSlotForJob(String job) {
        return switch (job) {
            case "miner" -> 20;
            case "hunter" -> 21;
            case "farmer" -> 22;
            case "lumberjack" -> 23;
            case "fisherman" -> 24;
            case "breeder" -> 29;
            default -> 0;
        };
    }
}
