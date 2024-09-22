package com.josemarcellio.joseplugin.job.category;

import com.destroystokyo.paper.ParticleBuilder;

import com.josemarcellio.joseplugin.JosePlugin;
import com.josemarcellio.joseplugin.party.manager.Party;
import com.josemarcellio.joseplugin.party.manager.PartyManager;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

public abstract class BaseJobsHandler {

    protected abstract JosePlugin getPlugin();
    protected abstract PartyManager getPlayerParty();
    protected abstract String getJobName();
    protected abstract List<Material> getValidTools();
    protected abstract List<Particle> getParticles();

    public void handleParticleAndDrop(Player player, Location location) {
        int playerLevel = getPlugin().getJobsManager().getLevel(player.getUniqueId());

        ItemStack item = player.getInventory().getItemInMainHand();
        if (getValidTools().contains(item.getType())) {
            Particle particle = getParticleForLevel(playerLevel);
            sendParticle(location.add(0.5, 1, 0.5), particle);
        }
    }

    private Particle getParticleForLevel(int level) {
        if (level >= 1 && level <= 15) {
            return getParticles().getFirst();
        } else if (level >= 16 && level <= 30) {
            return getParticles().get(1);
        } else if (level >= 31 && level <= 50) {
            return getParticles().get(2);
        } else {
            return getParticles().get(2);
        }
    }

    public void handleSharedExp(Player player, double amount) {
        Party party = getPlayerParty().getPlayerParty(player.getUniqueId());
        if (party != null) {
            Collection<UUID> memberUUIDs = party.getMembers();
            int memberCount = memberUUIDs.size();
            for (UUID memberUUID : memberUUIDs) {
                Player member = Bukkit.getPlayer(memberUUID);
                if (member != null && member.isOnline() && getPlugin().getJobsManager().getLevel(memberUUID) > 0) {
                    double shared = amount / memberCount;
                    if (!memberUUID.equals(player.getUniqueId())) {
                        getPlugin().getJobsManager().giveExp(memberUUID, shared);
                    } else {
                        getPlugin().getJobsManager().giveExp(memberUUID, amount);
                    }
                }
            }
        } else {
            getPlugin().getJobsManager().giveExp(player.getUniqueId(), amount);
        }

    }

    public void sendParticle(Location location, Particle particle) {
        ParticleBuilder particleBuilder = new ParticleBuilder(particle);
        particleBuilder.offset(0, 0, 0);
        particleBuilder.count(15);
        particleBuilder.location(location);
        particleBuilder.receivers(10);
        particleBuilder.extra(0);
        particleBuilder.allPlayers();
        particleBuilder.spawn();
    }
}