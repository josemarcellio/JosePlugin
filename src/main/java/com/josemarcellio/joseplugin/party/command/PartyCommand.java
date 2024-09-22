package com.josemarcellio.joseplugin.party.command;

import com.josemarcellio.joseplugin.party.gui.PartyGUI;
import com.josemarcellio.joseplugin.party.manager.PartyManager;
import com.josemarcellio.joseplugin.text.component.ComponentBuilder;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class PartyCommand implements CommandExecutor {
    private final PartyManager partyManager;
    private final ComponentBuilder componentBuilder = new ComponentBuilder();

    public PartyCommand(PartyManager partyManager) {
        this.partyManager = partyManager;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Only players can use this command.");
            return false;
        }

        if (command.getName().equalsIgnoreCase("party")) {
            if (args.length == 0) {
                handleHelp(player);
                return true;
            }

            return switch (args[0].toLowerCase()) {
                case "create" -> handleCreateParty(player);
                case "invite" -> handleInvite(player, args);
                case "accept" -> handleAcceptInvite(player, args);
                case "deny" -> handleDenyInvite(player);
                case "disband" -> handleDisbandParty(player);
                case "kick" -> handleKickPlayer(player, args);
                case "leave" -> handleLeaveParty(player);
                case "member" -> handleShowMembers(player);
                case "gui" -> handleGUI(player);
                case "transfer" -> handleTransferLeader(player, args);
                case "chat" -> handlePartyChat(player, args);
                default -> handleHelp(player);
            };
        }
        return true;
    }

    private boolean handleHelp(Player player) {
        player.sendMessage(componentBuilder.singleComponentBuilder("<aqua> 🎉 <color:#fae7b5>Party <color:#c4c3d0>• <white>/party create").build());
        player.sendMessage(componentBuilder.singleComponentBuilder("<aqua> 🎉 <color:#fae7b5>Party <color:#c4c3d0>• <white>/party invite <player>").build());
        player.sendMessage(componentBuilder.singleComponentBuilder("<aqua> 🎉 <color:#fae7b5>Party <color:#c4c3d0>• <white>/party accept <player>").build());
        player.sendMessage(componentBuilder.singleComponentBuilder("<aqua> 🎉 <color:#fae7b5>Party <color:#c4c3d0>• <white>/party deny <player>").build());
        player.sendMessage(componentBuilder.singleComponentBuilder("<aqua> 🎉 <color:#fae7b5>Party <color:#c4c3d0>• <white>/party kick <player>").build());
        player.sendMessage(componentBuilder.singleComponentBuilder("<aqua> 🎉 <color:#fae7b5>Party <color:#c4c3d0>• <white>/party disband").build());
        player.sendMessage(componentBuilder.singleComponentBuilder("<aqua> 🎉 <color:#fae7b5>Party <color:#c4c3d0>• <white>/party leave").build());
        player.sendMessage(componentBuilder.singleComponentBuilder("<aqua> 🎉 <color:#fae7b5>Party <color:#c4c3d0>• <white>/party member").build());
        player.sendMessage(componentBuilder.singleComponentBuilder("<aqua> 🎉 <color:#fae7b5>Party <color:#c4c3d0>• <white>/party gui").build());
        player.sendMessage(componentBuilder.singleComponentBuilder("<aqua> 🎉 <color:#fae7b5>Party <color:#c4c3d0>• <white>/party transfer <player>").build());
        player.sendMessage(componentBuilder.singleComponentBuilder("<aqua> 🎉 <color:#fae7b5>Party <color:#c4c3d0>• <white>/party chat <message>").build());
        return true;
    }

    private boolean handleCreateParty(Player player) {
        partyManager.createParty(player);
        return true;
    }

    private boolean handleInvite(Player player, String[] args) {
        if (args.length < 2) {
            player.sendMessage(componentBuilder.singleComponentBuilder("<aqua> 🎉 <color:#fae7b5>Party <color:#c4c3d0>• <white>/party invite <player>").build());
            return false;
        }
        Player target = Bukkit.getPlayer(args[1]);
        partyManager.invitePlayer(player, target);
        return true;
    }

    private boolean handleAcceptInvite(Player player, String[] args) {
        if (args.length < 2) {
            player.sendMessage(componentBuilder.singleComponentBuilder("<aqua> 🎉 <color:#fae7b5>Party <color:#c4c3d0>• <white>/party accept <leader>").build());
            return false;
        }

        OfflinePlayer leader = Bukkit.getOfflinePlayer(args[1]);
        partyManager.acceptInvite(leader, player);
        return true;
    }

    private boolean handleDenyInvite(Player player) {
        partyManager.denyInvite(player);
        return true;
    }

    private boolean handleDisbandParty(Player player) {
        partyManager.disbandParty(player);
        return true;
    }


    private boolean handleKickPlayer(Player player, String[] args) {
        if (args.length < 2) {
            player.sendMessage(componentBuilder.singleComponentBuilder("<aqua> 🎉 <color:#fae7b5>Party <color:#c4c3d0>• <white>/party kick <player>").build());
            return false;
        }
        OfflinePlayer target = Bukkit.getOfflinePlayer(args[1]);
        partyManager.kickPlayer(player, target);
        return true;
    }

    private boolean handleLeaveParty(Player player) {
        partyManager.leaveParty(player);
        return true;
    }

    private boolean handleShowMembers(Player player) {
        partyManager.handleShowMember(player);
        return true;
    }

    private boolean handleTransferLeader(Player player, String[] args) {
        if (args.length < 2) {
            player.sendMessage(componentBuilder.singleComponentBuilder("<aqua> 🎉 <color:#fae7b5>Party <color:#c4c3d0>• <white>/party transfer <player>").build());
            return false;
        }
        OfflinePlayer target = Bukkit.getOfflinePlayer(args[1]);
        partyManager.handleTransferParty(player, target);
        return true;
    }

    private boolean handleGUI(Player player) {
        PartyGUI partyGUI = new PartyGUI(partyManager);
        partyGUI.open(player, 1);
        return true;
    }

    private boolean handlePartyChat(Player player, String[] args) {
        if (args.length < 2) {
            player.sendMessage(componentBuilder.singleComponentBuilder("<aqua> 🎉 <color:#fae7b5>Party <color:#c4c3d0>• <white>/party chat <message>").build());
            return false;
        }
        String message = String.join(" ", Arrays.copyOfRange(args, 1, args.length));
        partyManager.sendPartyMessage(player, message);
        return true;
    }
}