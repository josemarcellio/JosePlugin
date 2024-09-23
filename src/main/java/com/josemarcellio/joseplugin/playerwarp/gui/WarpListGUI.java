package com.josemarcellio.joseplugin.playerwarp.gui;

import com.josemarcellio.joseplugin.JosePlugin;
import com.josemarcellio.joseplugin.inventory.GUIItem;
import com.josemarcellio.joseplugin.inventory.GUIManager;
import com.josemarcellio.joseplugin.inventory.builder.GUIBuilder;
import com.josemarcellio.joseplugin.inventory.page.GUIPage;

import com.josemarcellio.joseplugin.item.ItemBuilderFactory;
import com.josemarcellio.joseplugin.location.SafeLocation;
import com.josemarcellio.joseplugin.location.module.GroundChecker;
import com.josemarcellio.joseplugin.location.module.HazardChecker;
import com.josemarcellio.joseplugin.location.module.ObstructionChecker;
import com.josemarcellio.joseplugin.location.module.VoidChecker;
import com.josemarcellio.joseplugin.location.module.hook.RedProtectChecker;
import com.josemarcellio.joseplugin.playerwarp.manager.Warp;
import com.josemarcellio.joseplugin.playerwarp.manager.WarpManager;
import com.josemarcellio.joseplugin.skull.type.SkullType;
import com.josemarcellio.joseplugin.component.ComponentBuilder;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class WarpListGUI {

    private final int ITEMS_PER_PAGE = 28;
    private final int[] SLOT_INDEXES = {
            10, 11, 12, 13, 14, 15, 16, 19, 20, 21, 22, 23, 24, 25, 28, 29, 30, 31, 32, 33, 34, 37, 38, 39, 40, 41, 42, 43
    };

    public final ItemBuilderFactory itemBuilderFactory = new ItemBuilderFactory();
    public final ComponentBuilder componentBuilder = new ComponentBuilder();

    private final JosePlugin plugin;

    public WarpListGUI(JosePlugin plugin) {
        this.plugin = plugin;
    }

    public void open(Player player, int page) {
        GUIBuilder guiBuilder = new GUIBuilder(componentBuilder.singleComponentBuilder("<aqua>Daftar Warp</aqua>").build(), 54);

        createWarpPane(guiBuilder, player, page);

        createNavigationPane(guiBuilder, page);

        addItem(guiBuilder);
        addGlassPane(guiBuilder);

        GUIPage pageGUI = guiBuilder.build();
        GUIManager.openGUI(player, pageGUI);
    }

    private void createWarpPane(GUIBuilder guiBuilder, Player player, int page) {
        WarpManager warpManager = plugin.getWarpManager();
        Map<String, Warp> warpMap = warpManager.getWarps();

        Set<Map.Entry<String, Warp>> warpEntries = warpMap.entrySet();

        int startIndex = (page - 1) * ITEMS_PER_PAGE;
        int endIndex = Math.min(startIndex + ITEMS_PER_PAGE, warpEntries.size());

        int slotIndexCounter = 0;
        int currentIndex = 0;

        for (Map.Entry<String, Warp> entry : warpEntries) {
            if (currentIndex >= startIndex && currentIndex < endIndex) {
                String warpName = entry.getKey();
                Warp warp = entry.getValue();

                ItemBuilderFactory itemBuilderFactory = new ItemBuilderFactory();

                List<Component> lore = Arrays.asList(
                        componentBuilder.singleComponentBuilder("<gray>Owner:</gray> <aqua>" + warp.getOwner() + "</aqua>").build(),
                        componentBuilder.singleComponentBuilder("<gray>World:</gray> <aqua>" + warp.getWorld() + "</aqua>").build(),
                        componentBuilder.singleComponentBuilder("<gray>Location:</gray> <aqua>" + String.format("%.2f, %.2f, %.2f", warp.getX(), warp.getY(), warp.getZ()) + "</aqua>").build(),
                        componentBuilder.singleComponentBuilder("<gray>Visitor:</gray> <aqua>" + warp.getVisitor() + "</aqua>").build(),
                        componentBuilder.singleComponentBuilder("").build(),
                        componentBuilder.singleComponentBuilder("<aqua>" + warp.getDescription() + "</aqua>").build(),
                        componentBuilder.singleComponentBuilder("").build(),
                        componentBuilder.singleComponentBuilder("<yellow>Klik kiri untuk teleport</yellow>").build()
                );

                ItemStack item = warp.getTextures().equals("NONE") ?
                        itemBuilderFactory.createItemBuilder(warp.getMaterial())
                                .setName(componentBuilder.singleComponentBuilder("<light_purple>" + warpName).build())
                                .setLore(lore)
                                .build() :
                        itemBuilderFactory.createSkullItemBuilder(warp.getTextures(), SkullType.TEXTURE_ID)
                                .setName(componentBuilder.singleComponentBuilder("<light_purple>" + warpName).build())
                                .setLore(lore)
                                .build();

                guiBuilder.addItem(SLOT_INDEXES[slotIndexCounter], new GUIItem(item, event -> {
                    event.setCancelled(true);
                    if (event.isLeftClick()) {
                        teleportToWarp(player, warp, warpName);
                    }
                }));

                slotIndexCounter++;
            }
            currentIndex++;
        }
    }


    private void teleportToWarp(Player player, Warp warp, String warpName) {
        Location location = new Location(Bukkit.getWorld(warp.getWorld()), warp.getX(), warp.getY(), warp.getZ(), warp.getYaw(), warp.getPitch());

        SafeLocation safeLocation = new SafeLocation()
                .addCheck(new GroundChecker())
                .addCheck(new HazardChecker())
                .addCheck(new ObstructionChecker())
                .addCheck(new VoidChecker())
                .addCheck(new RedProtectChecker());

        if (!safeLocation.isSafeLocation(player, location)) {
            player.sendMessage(componentBuilder.singleComponentBuilder("<green> 🛸 <color:#fae7b5>PlayerWarp <color:#c4c3d0>• <white>Tidak bisa teleport ke warp <aqua>" + warpName + ", <white>karena tempat ini tidak aman!").build());
            return;
        }

        player.teleport(location);
        warp.incrementVisitor();
        player.sendMessage(componentBuilder.singleComponentBuilder("<green> 🛸 <color:#fae7b5>PlayerWarp <color:#c4c3d0>• <white>Kamu berhasil di teleport ke warp <aqua>" + warpName).build());
    }

    private void createNavigationPane(GUIBuilder guiBuilder, int page) {
        if (page > 1) {
            ItemStack backItem = new ItemStack(Material.ARROW);
            ItemMeta backMeta = backItem.getItemMeta();
            backMeta.displayName(componentBuilder.singleComponentBuilder("<red>Back").build());
            backItem.setItemMeta(backMeta);

            guiBuilder.addItem(45, new GUIItem(backItem, event -> {
                event.setCancelled(true);
                event.getWhoClicked().closeInventory();
                open((Player) event.getWhoClicked(), page - 1);
            }));
        }

        if ((page - 1) * ITEMS_PER_PAGE + ITEMS_PER_PAGE < getWarpCount()) {
            ItemStack nextItem = new ItemStack(Material.ARROW);
            ItemMeta nextMeta = nextItem.getItemMeta();
            nextMeta.displayName(componentBuilder.singleComponentBuilder("<red>Next").build());
            nextItem.setItemMeta(nextMeta);

            guiBuilder.addItem(53, new GUIItem(nextItem, event -> {
                event.setCancelled(true);
                event.getWhoClicked().closeInventory();
                open((Player) event.getWhoClicked(), page + 1);
            }));
        }

        ItemBuilderFactory itemBuilderFactory = new ItemBuilderFactory();

        ItemStack closeItem = itemBuilderFactory.createSkullItemBuilder("beb588b21a6f98ad1ff4e085c552dcb050efc9cab427f46048f18fc803475f7", SkullType.TEXTURE_ID).build();
        ItemMeta closeMeta = closeItem.getItemMeta();
        closeMeta.displayName(componentBuilder.singleComponentBuilder("<red>Close").build());
        closeItem.setItemMeta(closeMeta);
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
    private int getWarpCount() {
        WarpManager warpManager = plugin.getWarpManager();
        Map<String, Warp> warpMap = warpManager.getWarps();
        return warpMap.size();
    }
}
