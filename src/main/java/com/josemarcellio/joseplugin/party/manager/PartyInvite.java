package com.josemarcellio.joseplugin.party.manager;

import org.bukkit.entity.Player;

public class PartyInvite {
    private final Party party;
    private final Player invitedPlayer;

    public PartyInvite(Party party, Player invitedPlayer) {
        this.party = party;
        this.invitedPlayer = invitedPlayer;
    }

    public Party getParty() {
        return party;
    }

    public Player getInvitedPlayer() {
        return invitedPlayer;
    }
}