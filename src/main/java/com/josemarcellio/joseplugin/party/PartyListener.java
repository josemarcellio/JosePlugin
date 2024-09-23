package com.josemarcellio.joseplugin.party;

import com.josemarcellio.joseplugin.party.manager.Party;
import com.josemarcellio.joseplugin.party.manager.PartyManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class PartyListener implements Listener {

    private final PartyManager partyManager;

    public PartyListener(PartyManager partyManager) {
        this.partyManager = partyManager;
    }


    @SuppressWarnings("unused")
    @EventHandler
    public void onPlayerDamage(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player damaged && event.getDamager() instanceof Player damager) {

            Party partyDamaged = partyManager.getPlayerParty(damaged.getUniqueId());
            Party partyDamager = partyManager.getPlayerParty(damager.getUniqueId());

            if (partyDamaged != null && partyDamaged.equals(partyDamager)) {
                if (partyDamaged.getMode() == Party.PartyMode.FRIENDLY) {
                    event.setCancelled(true);
                }
            }
        }
    }
}