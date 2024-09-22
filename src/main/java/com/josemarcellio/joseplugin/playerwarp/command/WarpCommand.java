package com.josemarcellio.joseplugin.playerwarp.command;

import com.josemarcellio.joseplugin.JosePlugin;
import com.josemarcellio.joseplugin.blacklist.Blacklist;
import com.josemarcellio.joseplugin.location.SafeLocation;
import com.josemarcellio.joseplugin.location.module.GroundChecker;
import com.josemarcellio.joseplugin.location.module.HazardChecker;
import com.josemarcellio.joseplugin.location.module.ObstructionChecker;
import com.josemarcellio.joseplugin.location.module.VoidChecker;
import com.josemarcellio.joseplugin.location.module.hook.RedProtectChecker;
import com.josemarcellio.joseplugin.playerwarp.gui.WarpListGUI;
import com.josemarcellio.joseplugin.playerwarp.manager.Warp;
import com.josemarcellio.joseplugin.playerwarp.manager.WarpManager;
import com.josemarcellio.joseplugin.text.component.ComponentBuilder;

import net.milkbowl.vault.economy.Economy;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

import java.util.Map;

public class WarpCommand implements CommandExecutor {
    private final JosePlugin plugin;
    private final WarpManager warpManager;
    private final ComponentBuilder componentBuilder = new ComponentBuilder();
    private final Blacklist blacklist = new Blacklist();

    public WarpCommand(JosePlugin plugin, WarpManager warpManager) {
        this.plugin = plugin;
        this.warpManager = warpManager;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (args.length == 0) {
            return handleHelp(sender);
        }

        return switch (args[0].toLowerCase()) {
            case "create" -> handleCreateCommand(sender, args);
            case "location" -> handleSetLocationCommand(sender, args);
            case "list" -> handleListCommand(sender);
            case "visit" -> handleVisitCommand(sender, args);
            case "delete" -> handleDeleteCommand(sender, args);
            case "description" -> handleDescriptionCommand(sender, args);
            case "icon" -> handleSetIcon(sender, args);
            default -> {
                handleHelp(sender);
                yield true;
            }
        };
    }

    private boolean handleHelp(CommandSender sender) {
        sender.sendMessage(componentBuilder.singleComponentBuilder("<green> 🛸 <color:#fae7b5>PlayerWarp <color:#c4c3d0>• <color:white>/playerwarp create <name>").build());
        sender.sendMessage(componentBuilder.singleComponentBuilder("<green> 🛸 <color:#fae7b5>PlayerWarp <color:#c4c3d0>• <color:white>/playerwarp location").build());
        sender.sendMessage(componentBuilder.singleComponentBuilder("<green> 🛸 <color:#fae7b5>PlayerWarp <color:#c4c3d0>• <color:white>/playerwarp icon <material>").build());
        sender.sendMessage(componentBuilder.singleComponentBuilder("<green> 🛸 <color:#fae7b5>PlayerWarp <color:#c4c3d0>• <color:white>/playerwarp visit <warp>").build());
        sender.sendMessage(componentBuilder.singleComponentBuilder("<green> 🛸 <color:#fae7b5>PlayerWarp <color:#c4c3d0>• <color:white>/playerwarp delete <warp>").build());
        sender.sendMessage(componentBuilder.singleComponentBuilder("<green> 🛸 <color:#fae7b5>PlayerWarp <color:#c4c3d0>• <color:white>/playerwarp description <warp> <description>").build());
        sender.sendMessage(componentBuilder.singleComponentBuilder("<green> 🛸 <color:#fae7b5>PlayerWarp <color:#c4c3d0>• <color:white>/playerwarp list").build());
        return true;
    }

    private boolean handleCreateCommand(CommandSender sender, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("This command can only be used by players.");
            return false;
        }

        if (args.length < 2) {
            sender.sendMessage(componentBuilder.singleComponentBuilder("<green> 🛸 <color:#fae7b5>PlayerWarp <color:#c4c3d0>• <color:white>/pwarp create <name-warp>").build());
            return false;
        }


        String name = args[1].toLowerCase();

        int minLength = 5;
        int maxLength = 10;

        if (!StringUtils.isAlphanumeric(name)) {
            player.sendMessage(componentBuilder.singleComponentBuilder("<green> 🛸 <color:#fae7b5>PlayerWarp <color:#c4c3d0>• <color:white>Hanya huruf dan angka yang diperbolehkan!").build());
            return false;
        }

        if (name.length() < minLength || name.length() > maxLength) {
            player.sendMessage(componentBuilder.singleComponentBuilder("<green> 🛸 <color:#fae7b5>PlayerWarp <color:#c4c3d0>• <color:white>Nama warp minimal <aqua>" + minLength + " <white>dan maksimal <aqua>" + maxLength + " <white>huruf!").build());
            return false;
        }

        WarpManager warpManager = plugin.getWarpManager();
        Map<String, Warp> warpMap = warpManager.getWarps();

        if (warpMap.containsKey(name)) {
            player.sendMessage(componentBuilder.singleComponentBuilder("<green> 🛸 <color:#fae7b5>PlayerWarp <color:#c4c3d0>• <color:white>Nama <aqua>" + name + " <white>sudah ada yang menggunakan!").build());
            return false;
        }

        if (blacklist.isBlacklistWorld(player)) {
            player.sendMessage(componentBuilder.singleComponentBuilder("<green> 🛸 <color:#fae7b5>PlayerWarp <color:#c4c3d0>• <color:white>Tidak bisa membuat warp di tempat ini, karena world <aqua>" + player.getWorld().getName() + " <white>diblacklist!").build());
            return false;
        }


        SafeLocation safeLocation = new SafeLocation()
                .addCheck(new GroundChecker())
                .addCheck(new HazardChecker())
                .addCheck(new ObstructionChecker())
                .addCheck(new VoidChecker())
                .addCheck(new RedProtectChecker());

        if (!safeLocation.isSafeLocation(player, player.getLocation())) {
            player.sendMessage(componentBuilder.singleComponentBuilder("<green> 🛸 <color:#fae7b5>PlayerWarp <color:#c4c3d0>• <color:white>Tidak bisa membuat warp di tempat ini karena tempat ini tidak aman!").build());
            return false;
        }

        long playerWarpCount = warpMap.values().stream()
                .filter(warp -> warp.getOwnerUUID().equals(player.getUniqueId().toString()))
                .count();

        int maxWarps = 5;
        if (playerWarpCount >= maxWarps) {
            player.sendMessage(componentBuilder.singleComponentBuilder("<green> 🛸 <color:#fae7b5>PlayerWarp <color:#c4c3d0>• <color:white>Kamu sudah mencapai batas maksimal pembuatan warp!").build());
            return false;
        }

        double price = 500;
        Economy econ = plugin.getEconomy();

        if (econ != null && !econ.has(player, price)) {
            player.sendMessage(componentBuilder.singleComponentBuilder("<green> 🛸 <color:#fae7b5>PlayerWarp <color:#c4c3d0>• <color:white>Kamu tidak memiliki cukup coins untuk membuat warp, biaya pembuatan: " + price + " coins!").build());
            return false;
        }

        if (econ != null) {
            econ.withdrawPlayer(player, price);
        }

        Warp warp = new Warp(
                player.getWorld().getName(),
                player.getLocation().getX(),
                player.getLocation().getY(),
                player.getLocation().getZ(),
                player.getLocation().getYaw(),
                player.getLocation().getPitch(),
                player.getName(),
                player.getUniqueId().toString(),
                "No description",
                0,
                "CHEST",
                "NONE"
        );

        warpManager.addWarp(name, warp);
        player.sendMessage(componentBuilder.singleComponentBuilder("<green> 🛸 <color:#fae7b5>PlayerWarp <color:#c4c3d0>• <color:white>Berhasil membuat warp dengan nama <aqua>" + name).build());
        return true;
    }

    private boolean handleSetIcon(CommandSender sender, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("This command can only be used by players.");
            return false;
        }

        if (args.length < 3) {
            sender.sendMessage(componentBuilder.singleComponentBuilder("<green> 🛸 <color:#fae7b5>PlayerWarp <color:#c4c3d0>• <color:white>/pwarp icon <name-warp> <material>").build());
            return false;
        }

        String warpName = args[1];
        String material = args[2];

        WarpManager warpManager = plugin.getWarpManager();
        Map<String, Warp> warpMap = warpManager.getWarps();

        if (!warpMap.containsKey(warpName)) {
            player.sendMessage(componentBuilder.singleComponentBuilder("<green> 🛸 <color:#fae7b5>PlayerWarp <color:#c4c3d0>• <color:white>Warp dengan nama <aqua>" + warpName + " tidak ditemukan!").build());
            return false;
        }

        Warp warp = warpMap.get(warpName);

        if (!warp.getOwnerUUID().equals(player.getUniqueId().toString())) {
            player.sendMessage(componentBuilder.singleComponentBuilder("<green> 🛸 <color:#fae7b5>PlayerWarp <color:#c4c3d0>• <color:white>Kamu tidak memiliki izin untuk mengubah icon warp <aqua>" + warpName).build());
            return false;
        }

        if (blacklist.isBlacklistItem(material)) {
            player.sendMessage(componentBuilder.singleComponentBuilder("<green> 🛸 <color:#fae7b5>PlayerWarp <color:#c4c3d0>• <color:white>Material <aqua>" + material + " tidak dapat digunakan sebagai icon!").build());
            return false;
        }

        Material materialEnum = Material.matchMaterial(material.toLowerCase());

        if (!canBeDisplayedInGUI(materialEnum)) {
            sender.sendMessage(componentBuilder.singleComponentBuilder("<green> 🛸 <color:#fae7b5>PlayerWarp <color:#c4c3d0>• <color:white>Material ini tidak tersedia!").build());
            return false;
        }

        if (material.equals("reset")) {
            warp.setTextures("NONE");
            warp.setMaterial("CHEST");
            sender.sendMessage(componentBuilder.singleComponentBuilder("<green> 🛸 <color:#fae7b5>PlayerWarp <color:#c4c3d0>• <color:white>Berhasil mereset icon pada warp").build());
        } else if (!material.startsWith("custom_head;")) {
            if (!warp.getTextures().equals("NONE")) {
                warp.setTextures("NONE");
            }

            warp.setMaterial(material);
            sender.sendMessage(componentBuilder.singleComponentBuilder("<green> 🛸 <color:#fae7b5>PlayerWarp <color:#c4c3d0>• <color:white>Berhasil mengubah icon warp dengan <aqua>" + material).build());

        } else {
            if (player.hasPermission("atlaspwarps.icon")) {
                warp.setTextures(material.replace("custom_head;", ""));
                sender.sendMessage(componentBuilder.singleComponentBuilder("<green> 🛸 <color:#fae7b5>PlayerWarp <color:#c4c3d0>• <color:white>Berhasil mengubah icon warp dengan <aqua>" + material).build());
            } else {
                player.sendMessage(componentBuilder.singleComponentBuilder("<green> 🛸 <color:#fae7b5>PlayerWarp <color:#c4c3d0>• <color:white>Kamu tidak memiliki permission untuk menggunakan material ini").build());
            }
        }
        return true;
    }

    private boolean handleSetLocationCommand(CommandSender sender, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("This command can only be used by players.");
            return false;
        }

        if (args.length < 2) {
            sender.sendMessage(componentBuilder.singleComponentBuilder("<green> 🛸 <color:#fae7b5>PlayerWarp <color:#c4c3d0>• <color:white>/pwarp location <name-warp>").build());
            return false;
        }

        String warpName = args[1];

        WarpManager warpManager = plugin.getWarpManager();
        Map<String, Warp> warpMap = warpManager.getWarps();

        if (!warpMap.containsKey(warpName)) {
            player.sendMessage(componentBuilder.singleComponentBuilder("<green> 🛸 <color:#fae7b5>PlayerWarp <color:#c4c3d0>• <color:white>Warp dengan nama <aqua>" + warpName + " tidak ditemukan!").build());
            return false;
        }


        if (blacklist.isBlacklistWorld(player)) {
            player.sendMessage(componentBuilder.singleComponentBuilder("<green> 🛸 <color:#fae7b5>PlayerWarp <color:#c4c3d0>• <color:white>Tidak bisa set location di tempat ini, karena world <aqua>" + player.getWorld().getName() + " <white>diblacklist!").build());
            return false;
        }

        SafeLocation safeLocation = new SafeLocation()
                .addCheck(new GroundChecker())
                .addCheck(new HazardChecker())
                .addCheck(new ObstructionChecker())
                .addCheck(new VoidChecker())
                .addCheck(new RedProtectChecker());

        if (!safeLocation.isSafeLocation(player, player.getLocation())) {
            player.sendMessage(componentBuilder.singleComponentBuilder("<green> 🛸 <color:#fae7b5>PlayerWarp <color:#c4c3d0>• <color:white>Tidak bisa set location disini karena tempat ini tidak aman!").build());
            return false;
        }

        Warp warp = warpMap.get(warpName);

        if (!warp.getOwnerUUID().equals(player.getUniqueId().toString())) {
            player.sendMessage(componentBuilder.singleComponentBuilder("<green> 🛸 <color:#fae7b5>PlayerWarp <color:#c4c3d0>• <color:white>Kamu tidak memiliki izin set location warp <aqua>" + warpName).build());
            return false;
        }

        warp.setLocation(player.getLocation());

        player.sendMessage(componentBuilder.singleComponentBuilder("<green> 🛸 <color:#fae7b5>PlayerWarp <color:#c4c3d0>• <color:white>Berhasil mengubah set location dari warp <aqua>" + warpName).build());

        return true;
    }

    private boolean handleDescriptionCommand(CommandSender sender, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("This command can only be used by players.");
            return false;
        }

        if (args.length < 3) {
            sender.sendMessage(componentBuilder.singleComponentBuilder("<green> 🛸 <color:#fae7b5>PlayerWarp <color:#c4c3d0>• <color:white>/pwarp description <name-warp> <description>").build());
            return false;
        }

        String warpName = args[1];
        String description = String.join(" ", Arrays.copyOfRange(args, 2, args.length));

        int minDescriptionlength = 10;
        int maxDescriptionLength = 50;

        if (description.length() < minDescriptionlength || description.length() > maxDescriptionLength) {
            player.sendMessage(componentBuilder.singleComponentBuilder("<yellow> ⚠ <color:#f0e36e>Deskripsi minimal " + minDescriptionlength + " maksimal " + maxDescriptionLength + " huruf!").build());
            return false;
        }

        WarpManager warpManager = plugin.getWarpManager();
        Map<String, Warp> warpMap = warpManager.getWarps();

        if (!warpMap.containsKey(warpName)) {
            player.sendMessage(componentBuilder.singleComponentBuilder("<green> 🛸 <color:#fae7b5>PlayerWarp <color:#c4c3d0>• <color:white>Warp dengan nama <aqua>" + warpName + " tidak ditemukan!").build());
            return false;
        }

        Warp warp = warpMap.get(warpName);
        warp.setDescription(description);

        player.sendMessage(componentBuilder.singleComponentBuilder("<green> 🛸 <color:#fae7b5>PlayerWarp <color:#c4c3d0>• <color:white>Berhasil mengubah deskripsi warp <aqua>" + warpName + " <white>menjadi <aqua>" + description).build());
        return true;
    }


    private boolean handleListCommand(CommandSender sender) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("This command can only be used by players.");
            return false;
        }

        WarpListGUI warpListGUI = new WarpListGUI(plugin);
        warpListGUI.open((Player) sender, 1);

        return true;
    }

    private boolean handleVisitCommand(CommandSender sender, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("This command can only be used by players.");
            return false;
        }

        if (args.length < 2) {
            sender.sendMessage(componentBuilder.singleComponentBuilder("<green> 🛸 <color:#fae7b5>PlayerWarp <color:#c4c3d0>• <color:white>/pwarp visit <name-warp>").build());
            return false;
        }

        String name = args[1].toLowerCase();
        Warp warp = warpManager.getWarp(name);

        if (warp == null) {
            player.sendMessage(componentBuilder.singleComponentBuilder("<green> 🛸 <color:#fae7b5>PlayerWarp <color:#c4c3d0>• <color:white>Nama dengan nama <aqua>" + name + " tidak ditemukan!").build());
            return false;
        }

        Location location = new Location(
                Bukkit.getWorld(warp.getWorld()),
                warp.getX(),
                warp.getY(),
                warp.getZ(),
                warp.getYaw(),
                warp.getPitch()
        );

        SafeLocation safeLocation = new SafeLocation()
                .addCheck(new GroundChecker())
                .addCheck(new HazardChecker())
                .addCheck(new ObstructionChecker())
                .addCheck(new VoidChecker())
                .addCheck(new RedProtectChecker());

        if (!safeLocation.isSafeLocation(player, location)) {
            player.sendMessage(componentBuilder.singleComponentBuilder("<green> 🛸 <color:#fae7b5>PlayerWarp <color:#c4c3d0>• <color:white>Tidak bisa teleport ke warp <aqua>" + name + ", <white>karena tempat ini tidak aman!").build());
            return false;
        }

        player.teleport(location);
        warp.incrementVisitor();
        player.sendMessage(componentBuilder.singleComponentBuilder("<green> 🛸 <color:#fae7b5>PlayerWarp <color:#c4c3d0>• <color:white>Kamu berhasil di teleport ke warp <aqua>" + name).build());
        return true;
    }

    private boolean handleDeleteCommand(CommandSender sender, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("This command can only be used by players.");
            return false;
        }

        if (args.length < 2) {
            sender.sendMessage(componentBuilder.singleComponentBuilder("<green> 🛸 <color:#fae7b5>PlayerWarp <color:#c4c3d0>• <color:white>/pwarp delete <name-warp>").build());
            return false;
        }

        String name = args[1];
        Warp warp = warpManager.getWarp(name);

        if (warp == null) {
            player.sendMessage(componentBuilder.singleComponentBuilder("<green> 🛸 <color:#fae7b5>PlayerWarp <color:#c4c3d0>• <color:white>Warp dengan nama <aqua>" + name + " tidak ditemukan!").build());
            return false;
        }

        if (!warp.getOwnerUUID().equals(player.getUniqueId().toString())) {
            player.sendMessage(componentBuilder.singleComponentBuilder("<green> 🛸 <color:#fae7b5>PlayerWarp <color:#c4c3d0>• <color:white>Kamu tidak memiliki izin untuk menghapus warp <aqua>" + name).build());
            return false;
        }

        warpManager.deleteWarp(name);
        player.sendMessage(componentBuilder.singleComponentBuilder("<green> 🛸 <color:#fae7b5>PlayerWarp <color:#c4c3d0>• <color:white>Berhasil menghapus warp <aqua>" + name).build());
        return true;
    }

    private boolean canBeDisplayedInGUI(Material material) {
        return material != null && material.isItem() && material != Material.AIR;
    }
}