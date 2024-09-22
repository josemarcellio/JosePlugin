package com.josemarcellio.joseplugin.job.gui;

import com.josemarcellio.joseplugin.JosePlugin;
import com.josemarcellio.joseplugin.inventory.GUIItem;
import com.josemarcellio.joseplugin.inventory.GUIManager;
import com.josemarcellio.joseplugin.inventory.builder.GUIBuilder;
import com.josemarcellio.joseplugin.inventory.page.GUIPage;
import com.josemarcellio.joseplugin.item.ItemBuilderFactory;
import com.josemarcellio.joseplugin.skull.type.SkullType;
import com.josemarcellio.joseplugin.text.component.ComponentBuilder;

import org.bukkit.entity.Player;

public class JobsLeaveJobConfirmation {

    private final JosePlugin plugin;
    private final ComponentBuilder componentBuilder = new ComponentBuilder();
    private final ItemBuilderFactory itemBuilderFactory = new ItemBuilderFactory();

    public JobsLeaveJobConfirmation(JosePlugin plugin) {
        this.plugin = plugin;
    }

    public  void openGUI(Player player) {
        GUIBuilder builder = new GUIBuilder(componentBuilder.singleComponentBuilder("<aqua>Jobs Confirmation").build(), 27);

        addItem(builder);

        GUIPage page = builder.build();
        GUIManager.openGUI(player, page);
    }

    private void addItem(GUIBuilder builder) {
        GUIItem leave = new GUIItem(itemBuilderFactory.createSkullItemBuilder("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODA2YTc3NjJlODFmZjFkNGYyODRlMWQwMGZhYWQwOTA4ZWM0OGNjZGU4NjRkZWJhZTVjYmU1M2RjMmFiZWEyYSJ9fX0=", SkullType.BASE64)
                .setName(componentBuilder.singleComponentBuilder("<green>Click to confirm</green>").build()).build(), event -> {
            event.getWhoClicked().sendMessage(componentBuilder.singleComponentBuilder("<yellow> ✪ <color:#fae7b5>Jobs <color:#c4c3d0>• <white>Kamu berhasil keluar dari job " + plugin.getJobsManager().getDisplayName(plugin.getJobsManager().getJob(event.getWhoClicked().getUniqueId()))).build());
            plugin.getJobsManager().leaveJob(event.getWhoClicked().getUniqueId());
                event.getWhoClicked().closeInventory();
        });
        builder.addItem(11, leave);

        GUIItem cancel = new GUIItem(itemBuilderFactory.createSkullItemBuilder("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzg3M2ZjZGUyOGQ2NzZhOGU1NzVmNzkzNjVmM2ViZWE0ZjFiMGZmMjg4NzZkODMxYjY1NTU1N2M1ZDllZTRjNiJ9fX0=", SkullType.BASE64)
                .setName(componentBuilder.singleComponentBuilder("<red>Click to cancel</red>").build()).build(), event -> {
                event.getWhoClicked().closeInventory();
                event.getWhoClicked().sendMessage(componentBuilder.singleComponentBuilder("<yellow> ✪ <color:#fae7b5>Jobs <color:#c4c3d0>• <white>Batal keluar dari job " + plugin.getJobsManager().getDisplayName(plugin.getJobsManager().getJob(event.getWhoClicked().getUniqueId()))).build());
        });
        builder.addItem(15, cancel);
    }
}
