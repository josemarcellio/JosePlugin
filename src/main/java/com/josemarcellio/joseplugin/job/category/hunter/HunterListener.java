package com.josemarcellio.joseplugin.job.category.hunter;

import com.josemarcellio.joseplugin.JosePlugin;
import com.josemarcellio.joseplugin.job.category.BaseJobsHandler;
import com.josemarcellio.joseplugin.job.manager.JobsManager;
import com.josemarcellio.joseplugin.party.manager.PartyManager;
import com.josemarcellio.joseplugin.text.component.ComponentBuilder;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

import java.util.Arrays;
import java.util.List;

public class HunterListener extends BaseJobsHandler implements Listener {

    private final ComponentBuilder componentBuilder = new ComponentBuilder();
    private final JosePlugin plugin;
    private final PartyManager partyManager;

    public HunterListener(JosePlugin plugin, PartyManager partyManager) {
        this.plugin = plugin;
        this.partyManager = partyManager;
    }

    @Override
    protected JosePlugin getPlugin() {
        return plugin;
    }

    @Override
    protected PartyManager getPlayerParty() {
        return partyManager;
    }

    @Override
    protected String getJobName() {
        return "hunter";
    }

    @Override
    protected List<Material> getValidTools() {
        return Arrays.asList(
                Material.WOODEN_SWORD,
                Material.STONE_SWORD,
                Material.IRON_SWORD,
                Material.GOLDEN_SWORD,
                Material.DIAMOND_SWORD,
                Material.NETHERITE_SWORD
        );
    }

    @Override
    protected List<Particle> getParticles() {
        return Arrays.asList(Particle.HEART, Particle.ANGRY_VILLAGER, Particle.EXPLOSION);
    }

    @SuppressWarnings("unused")
    @EventHandler
    public void onEntityKill(EntityDeathEvent event) {
        Player killer = event.getEntity().getKiller();
        if (killer == null) return;

        String job = plugin.getJobsManager().getJob(killer.getUniqueId());
        if (!getJobName().equalsIgnoreCase(job)) return;

        Entity killedEntity = event.getEntity();
        EntityType entityType = killedEntity.getType();

        double exp = plugin.getJobProgressionData().getHunterMobExpMap().get(entityType);
        handleSharedExp(killer, exp);

        Component format = componentBuilder.singleComponentBuilder("<gray>Level: <aqua>" + plugin.getJobsManager().getLevel(killer.getUniqueId()) + " <gray>(<aqua>" + plugin.getJobsManager().getExp(killer.getUniqueId()) +  "<dark_gray>/<aqua>" + (plugin.getJobsManager().getLevel(killer.getUniqueId()) * 100) + "<gray>)" + " (<light_purple>" + entityType.name() + "<gray>)").build();
        killer.sendActionBar(format);

        handleParticleAndDrop(killer, killedEntity.getLocation());
    }
}