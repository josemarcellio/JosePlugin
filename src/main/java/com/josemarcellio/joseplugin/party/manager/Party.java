package com.josemarcellio.joseplugin.party.manager;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class Party {

    public enum PartyMode {
        FRIENDLY, DUEL
    }

    private UUID leader;
    private final Set<UUID> members;
    private PartyMode mode;

    public Party(Player leader) {
        this.leader = leader.getUniqueId();
        this.members = new HashSet<>();
        this.members.add(leader.getUniqueId());
        this.mode = PartyMode.FRIENDLY;
    }

    public UUID getLeader() {
        return leader;
    }

    public Set<UUID> getMembers() {
        return members;
    }

    public void addMember(Player player) {
        members.add(player.getUniqueId());
    }

    public void removeMember(OfflinePlayer player) {
        members.remove(player.getUniqueId());
    }

    public boolean isLeader(Player player) {
        return leader.equals(player.getUniqueId());
    }

    public PartyMode getMode() {
        return mode;
    }

    public void setMode(PartyMode mode) {
        this.mode = mode;
    }

    public void setLeader(UUID newLeader) {
        this.leader = newLeader;
    }
}