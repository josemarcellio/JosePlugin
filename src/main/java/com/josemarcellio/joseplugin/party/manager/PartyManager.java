package com.josemarcellio.joseplugin.party.manager;

import com.josemarcellio.joseplugin.text.component.ComponentBuilder;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.*;

public class PartyManager {
    private final Map<UUID, Party> playerPartyMap = new HashMap<>();
    private final Map<UUID, PartyInvite> pendingInvites = new HashMap<>();
    private final ComponentBuilder componentBuilder = new ComponentBuilder();
    
    public void createParty(Player leader) {

        if (!isPlayerInParty(leader.getUniqueId())) {
            UUID leaderUUID = leader.getUniqueId();
            if (!isPlayerInParty(leaderUUID)) {
                cancelPendingInvite(leaderUUID);
                Party party = new Party(leader);
                playerPartyMap.put(leaderUUID, party);
            }
            leader.sendMessage(componentBuilder.singleComponentBuilder("<aqua> 🎉 <color:#fae7b5>Party <color:#c4c3d0>• <white>Berhasil membuat party!").build());
        } else {
            leader.sendMessage(componentBuilder.singleComponentBuilder("<aqua> 🎉 <color:#fae7b5>Party <color:#c4c3d0>• <white>Kamu sedang berada di dalam party!").build());
        }
    }

    public void disbandParty(Player leader) {
        Party party = getPlayerParty(leader.getUniqueId());
        if (isPlayerInParty(leader.getUniqueId())) {
            if (!getPlayerParty(leader.getUniqueId()).isLeader(leader)) {
                leader.sendMessage(componentBuilder.singleComponentBuilder("<aqua> 🎉 <color:#fae7b5>Party <color:#c4c3d0>• <white>Hanya party leader yang dapat membubarkan party!").build());
                return;
            }
            for (UUID member : party.getMembers()) {
                if (!member.equals(leader.getUniqueId())) {
                    Player memberPlayer = Bukkit.getPlayer(member);
                    if (memberPlayer != null) {
                        memberPlayer.sendMessage(componentBuilder.singleComponentBuilder("<aqua> 🎉 <color:#fae7b5>Party <color:#c4c3d0>• <aqua>" + leader.getName() + " <white>telah membubarkan party ini!").build());
                    }
                }
            }
            if (party.isLeader(leader)) {
                party.getMembers().forEach(playerPartyMap::remove);
            }
            leader.sendMessage(componentBuilder.singleComponentBuilder("<aqua> 🎉 <color:#fae7b5>Party <color:#c4c3d0>• <white>Party berhasil dibubarkan!").build());
        } else {
            leader.sendMessage(componentBuilder.singleComponentBuilder("<aqua> 🎉 <color:#fae7b5>Party <color:#c4c3d0>• <white>Kamu sedang tidak berada di dalam party!").build());
        }
    }

    public void invitePlayer(Player leader, Player playerToInvite) {

        if (isPlayerInParty(leader.getUniqueId())) {
            if (!getPlayerParty(leader.getUniqueId()).isLeader(leader)) {
                leader.sendMessage(componentBuilder.singleComponentBuilder("<aqua> 🎉 <color:#fae7b5>Party <color:#c4c3d0>• <white>Hanya leader yang dapat mengirimkan undangan party!").build());
                return;
            }

            if (playerToInvite != null) {
                UUID playerToInviteUUID = playerToInvite.getUniqueId();
                if (playerToInvite.equals(leader)) {
                    leader.sendMessage(componentBuilder.singleComponentBuilder("<aqua> 🎉 <color:#fae7b5>Party <color:#c4c3d0>• <white>Kamu tidak dapat mengirimkan undangan party kepada diri sendiri!").build());
                } else if (isPlayerInParty(playerToInvite.getUniqueId())) {
                    leader.sendMessage(componentBuilder.singleComponentBuilder("<aqua> 🎉 <color:#fae7b5>Party <color:#c4c3d0>• <aqua>" + playerToInvite.getName() + " <white>telah berada di dalam party!").build());
                } else {
                    if (pendingInvites.containsKey(playerToInviteUUID)) {
                        leader.sendMessage(componentBuilder.singleComponentBuilder("<aqua> 🎉 <color:#fae7b5>Party <color:#c4c3d0>• <white>Kamu sudah mengirimkan undangan party kepada <aqua>" + playerToInvite.getName()).build());
                        return;
                    }

                    if (!isPlayerInParty(playerToInviteUUID)) {
                        cancelPendingInvite(playerToInviteUUID);
                        Party party = getPlayerParty(leader.getUniqueId());
                        if (party != null && party.isLeader(leader)) {
                            PartyInvite invite = new PartyInvite(party, playerToInvite);
                            pendingInvites.put(playerToInviteUUID, invite);
                            String message = "<hover:show_text:\"<red>Klik untuk bergabung dengan party!\"><click:run_command:/party accept " + leader.getName() + "><aqua> 🎉 <color:#fae7b5>Party <color:#c4c3d0>• <aqua>" + leader.getName() + " <white>telah mengirimkan undangan party. Ketik /party accept <aqua>" + leader.getName() + " <white>atau klik pesan ini untuk bergabung dengan party, undangan ini kadaluarsa dalam 1 menit!</click>";
                            playerToInvite.sendMessage(componentBuilder.singleComponentBuilder(message).build());

                            scheduleInviteExpiration(leader, playerToInvite);
                        }
                    } else {
                        leader.sendMessage(componentBuilder.singleComponentBuilder("<aqua> 🎉 <color:#fae7b5>Party <color:#c4c3d0>• <aqua>" + playerToInvite.getName() + " <white>sudah berada di dalam party!").build());
                    }
                    leader.sendMessage(componentBuilder.singleComponentBuilder("<aqua> 🎉 <color:#fae7b5>Party <color:#c4c3d0>• <white>Kamu telah mengirimkan undangan party kepada <aqua>" + playerToInvite.getName()).build());
                }
            } else {
                leader.sendMessage(componentBuilder.singleComponentBuilder("<aqua> 🎉 <color:#fae7b5>Party <color:#c4c3d0>• <white>Pemain tidak ditemukan!").build());
            }
        } else {
            leader.sendMessage(componentBuilder.singleComponentBuilder("<aqua> 🎉 <color:#fae7b5>Party <color:#c4c3d0>• <white>Kamu sedang tidak berada di dalam party!").build());
        }
    }

    private void cancelPendingInvite(UUID playerUUID) {
        pendingInvites.remove(playerUUID);
    }

    private void scheduleInviteExpiration(Player leader, Player playerToInvite) {
        UUID playerToInviteUUID = playerToInvite.getUniqueId();
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (pendingInvites.containsKey(playerToInviteUUID)) {
                    pendingInvites.remove(playerToInviteUUID);
                    leader.sendMessage(componentBuilder.singleComponentBuilder("<aqua> 🎉 <color:#fae7b5>Party <color:#c4c3d0>• <white>Undangan party untuk <aqua>" + playerToInvite.getName() + " <white>telah kadaluarsa karena tidak ada jawaban sama sekali!").build());
                    playerToInvite.sendMessage(componentBuilder.singleComponentBuilder("<aqua> 🎉 <color:#fae7b5>Party <color:#c4c3d0>• <white>Undangan party dari <aqua>" + leader.getName() + " <white>telah kadaluarsa karena kamu tidak menerimanya!").build());
                }
            }
        }, 60000);
    }

    public void acceptInvite(OfflinePlayer leader, Player playerToInvite) {

        UUID playerToInviteUUID = playerToInvite.getUniqueId();
        Party party = getPlayerParty(leader.getUniqueId());
        PartyInvite invite = getPendingInvite(playerToInvite.getUniqueId());
        if (invite != null && invite.getParty().getLeader().equals(leader.getUniqueId()) && !isPlayerInParty(playerToInvite.getUniqueId())) {
            if (party != null && !isPlayerInParty(playerToInviteUUID)) {
                cancelPendingInvite(playerToInviteUUID);
                party.addMember(playerToInvite);
                playerPartyMap.put(playerToInviteUUID, party);

                for (UUID member : party.getMembers()) {
                    Player memberPlayer = Bukkit.getPlayer(member);
                    if (memberPlayer != null) {
                        memberPlayer.sendMessage(componentBuilder.singleComponentBuilder("<aqua> 🎉 <color:#fae7b5>Party <color:#c4c3d0>• <aqua>" + playerToInvite.getName() + " <white>telah bergabung kedalam party!").build());
                    }
                }
            }
            playerToInvite.sendMessage(componentBuilder.singleComponentBuilder("<aqua> 🎉 <color:#fae7b5>Party <color:#c4c3d0>• <white>Kamu berhasil bergabung dengan party <aqua>" + leader.getName()).build());
        } else {
            playerToInvite.sendMessage(componentBuilder.singleComponentBuilder("<aqua> 🎉 <color:#fae7b5>Party <color:#c4c3d0>• <white>Tidak ada permintaan undangan dari <aqua>" + leader.getName() + " <white>atau undangan telah kadaluarsa!").build());
        }
    }


    public void denyInvite(Player player) {
        UUID playerUUID = player.getUniqueId();
        if (pendingInvites.containsKey(playerUUID)) {
            PartyInvite invite = pendingInvites.get(playerUUID);
            if (invite != null) {
                Player leader = Bukkit.getPlayer(invite.getParty().getLeader());
                pendingInvites.remove(playerUUID);

                player.sendMessage(componentBuilder.singleComponentBuilder("<aqua> 🎉 <color:#fae7b5>Party <color:#c4c3d0>• <white>Kamu telah menolak undangan party.").build());

                if (leader != null) {
                    player.sendMessage(componentBuilder.singleComponentBuilder("<aqua> 🎉 <color:#fae7b5>Party <color:#c4c3d0>• <white>Kamu telah menolak undangan party dari <aqua>" + leader.getName()).build());
                    leader.sendMessage(componentBuilder.singleComponentBuilder("<aqua> 🎉 <color:#fae7b5>Party <color:#c4c3d0>• <aqua>" + player.getName() + " <white>telah menolak undangan party dari mu!").build());
                }
            }
        } else {
            player.sendMessage(componentBuilder.singleComponentBuilder("<aqua> 🎉 <color:#fae7b5>Party <color:#c4c3d0>• <white>Kamu tidak memiliki undangan party untuk ditolak!").build());
        }
    }


    public void kickPlayer(Player leader, OfflinePlayer playerToKick) {
        UUID playerToKickUUID = playerToKick.getUniqueId();
        Party party = getPlayerParty(leader.getUniqueId());
        if (isPlayerInParty(leader.getUniqueId())) {
            if (!getPlayerParty(leader.getUniqueId()).isLeader(leader)) {
                leader.sendMessage(componentBuilder.singleComponentBuilder("<aqua> 🎉 <color:#fae7b5>Party <color:#c4c3d0>• <white>Hanya leader yang dapat mengeluarkan pemain!").build());
                return;
            }
            if (party != null && party.getMembers().contains(leader.getUniqueId())) {
                if (party.isLeader(leader) && !leader.equals(playerToKick)) {
                    party.removeMember(playerToKick);
                    playerPartyMap.remove(playerToKickUUID);
                    if (playerToKick.isOnline()) {
                        Player onlinePlayer = (Player) playerToKick;
                        onlinePlayer.sendMessage(componentBuilder.singleComponentBuilder("<aqua> 🎉 <color:#fae7b5>Party <color:#c4c3d0>• <white>Kamu telah dikeluarkan dari party!").build());
                    }
                    leader.sendMessage(componentBuilder.singleComponentBuilder("<aqua> 🎉 <color:#fae7b5>Party <color:#c4c3d0>• <aqua>" + playerToKick.getName() + " <white>telah dikeluarkan dari party!").build());
                } else {
                    leader.sendMessage(componentBuilder.singleComponentBuilder("<aqua> 🎉 <color:#fae7b5>Party <color:#c4c3d0>• <white>Kamu tidak dapat mengeluarkan dirimu sendiri dalam party!").build());
                }
            } else {
                leader.sendMessage(componentBuilder.singleComponentBuilder("<aqua> 🎉 <color:#fae7b5>Party <color:#c4c3d0>• <white>Pemain tidak ditemukan atau bukan anggota party").build());
            }
        } else {
            leader.sendMessage(componentBuilder.singleComponentBuilder("<aqua> 🎉 <color:#fae7b5>Party <color:#c4c3d0>• <white>Kamu sedang tidak berada di dalam party!").build());
        }
    }

    public void leaveParty(Player player) {
        UUID playerUUID = player.getUniqueId();
        Party party = getPlayerParty(playerUUID);
        if (party != null) {
            if (party.isLeader(player)) {
                player.sendMessage(componentBuilder.singleComponentBuilder("<aqua> 🎉 <color:#fae7b5>Party <color:#c4c3d0>• <white>Kamu tidak dapat keluar dari party! silahkan gunakan /party disband").build());
            } else {
                for (UUID member : party.getMembers()) {
                    if (!member.equals(playerUUID)) {
                        Player memberPlayer = Bukkit.getPlayer(member);
                        if (memberPlayer != null) {
                            memberPlayer.sendMessage(componentBuilder.singleComponentBuilder("<aqua> 🎉 <color:#fae7b5>Party <color:#c4c3d0>• <aqua>" + player.getName() + " <white>telah keluar dari party!").build());
                        }
                    }
                }
                party.removeMember(player);
                playerPartyMap.remove(playerUUID);
                player.sendMessage(componentBuilder.singleComponentBuilder("<aqua> 🎉 <color:#fae7b5>Party <color:#c4c3d0>• <white>Kamu berhasil keluar dari party").build());
            }
        } else {
            player.sendMessage(componentBuilder.singleComponentBuilder("<aqua> 🎉 <color:#fae7b5>Party <color:#c4c3d0>• <white>Kamu sedang tidak berada di dalam party!").build());
        }
    }

    public void handleTransferParty(Player player, OfflinePlayer target) {
        if (isPlayerInParty(player.getUniqueId())) {
            Party party = getPlayerParty(player.getUniqueId());

            if (!party.isLeader(player)) {
                player.sendMessage(componentBuilder.singleComponentBuilder("<aqua> 🎉 <color:#fae7b5>Party <color:#c4c3d0>• <white>Hanya leader yang dapat memindahkan kepemimpinan!").build());
                return;
            }

            if (party.getMembers().contains(target.getUniqueId())) {
                if (target.getUniqueId().equals(player.getUniqueId())) {
                    player.sendMessage(componentBuilder.singleComponentBuilder("<aqua> 🎉 <color:#fae7b5>Party <color:#c4c3d0>• <white>Kamu tidak dapat memindahkan leader ke diri sendiri!").build());
                    return;
                }

                party.setLeader(target.getUniqueId());
                player.sendMessage(componentBuilder.singleComponentBuilder("<aqua> 🎉 <color:#fae7b5>Party <color:#c4c3d0>• <white>Leader telah dipindahkan ke <aqua>" + target.getName()).build());

                Player targetPlayer = target.getPlayer();
                if (targetPlayer != null && targetPlayer.isOnline()) {
                    targetPlayer.sendMessage(componentBuilder.singleComponentBuilder("<aqua> 🎉 <color:#fae7b5>Party <color:#c4c3d0>• <white>Kamu sekarang menjadi leader dari party ini!").build());
                }
            } else {
                player.sendMessage(componentBuilder.singleComponentBuilder("<aqua> 🎉 <color:#fae7b5>Party <color:#c4c3d0>• <white>Pemain tidak ditemukan atau bukan anggota party!").build());
            }
        } else {
            player.sendMessage(componentBuilder.singleComponentBuilder("<aqua> 🎉 <color:#fae7b5>Party <color:#c4c3d0>• <white>Kamu sedang tidak berada di dalam party!").build());
        }
    }

    public void handleShowMember(Player player) {
        if (isPlayerInParty(player.getUniqueId())) {
            Party party = getPlayerParty(player.getUniqueId());
            StringBuilder members = new StringBuilder("Party members:\n");

            for (UUID member : party.getMembers()) {
                Player memberPlayer = Bukkit.getPlayer(member);
                OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(member);
                String status;

                if (memberPlayer != null && memberPlayer.isOnline()) {
                    status = "<green>⬤ Online</green>";
                } else {
                    status = "<red>⬤ Offline</red>";
                }

                String playerName = (memberPlayer != null ? memberPlayer.getName() : offlinePlayer.getName());

                if (member.equals(party.getLeader())) {
                    members.append("<gray>- <aqua>")
                            .append(playerName)
                            .append(" <light_purple>(Owner) <gray>- ")
                            .append(status)
                            .append("\n");
                } else {
                    members.append("<gray>- <aqua>")
                            .append(playerName)
                            .append(" <gray>- ")
                            .append(status)
                            .append("\n");
                }
            }

            player.sendMessage(componentBuilder.singleComponentBuilder("<gray>" + members).build());
        } else {
            player.sendMessage(componentBuilder.singleComponentBuilder("<aqua> 🎉 <color:#fae7b5>Party <color:#c4c3d0>• <white>Kamu sedang tidak berada di dalam party!").build());
        }
    }

    public void sendPartyMessage(Player sender, String message) {
        Party party = getPlayerParty(sender.getUniqueId());

        if (party == null) {
            sender.sendMessage(componentBuilder.singleComponentBuilder("<aqua> 🎉 <color:#fae7b5>Party <color:#c4c3d0>• <white>Kamu sedang tidak berada di dalam party!").build());
            return;
        }

        for (UUID memberUUID : party.getMembers()) {
            Player member = Bukkit.getPlayer(memberUUID);
            if (member != null) {
                member.sendMessage(componentBuilder.singleComponentBuilder("<light_purple>[Party] <aqua>" + sender.getName() + ": <white>" + message).build());
            }
        }
    }


    public boolean isPlayerInParty(UUID playerUUID) {
        return playerPartyMap.containsKey(playerUUID);
    }

    public Party getPlayerParty(UUID playerUUID) {
        return playerPartyMap.get(playerUUID);
    }

    public PartyInvite getPendingInvite(UUID playerUUID) {
        return pendingInvites.get(playerUUID);
    }
}