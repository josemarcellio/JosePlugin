package com.josemarcellio.joseplugin.job.category.farmer;

import com.josemarcellio.joseplugin.JosePlugin;
import com.josemarcellio.joseplugin.job.category.BaseJobsHandler;
import com.josemarcellio.joseplugin.party.manager.PartyManager;
import com.josemarcellio.joseplugin.text.component.ComponentBuilder;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.Arrays;
import java.util.List;

public class FarmerListener extends BaseJobsHandler implements Listener {

    private final ComponentBuilder componentBuilder = new ComponentBuilder();
    private final JosePlugin plugin;
    private final PartyManager partyManager;

    public FarmerListener(JosePlugin plugin, PartyManager partyManager) {
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
        return "farmer";
    }

    @Override
    protected List<Material> getValidTools() {
        return Arrays.asList(
                Material.WOODEN_HOE,
                Material.STONE_HOE,
                Material.IRON_HOE,
                Material.GOLDEN_HOE,
                Material.DIAMOND_HOE,
                Material.NETHERITE_HOE
        );
    }

    @Override
    protected List<Particle> getParticles() {
        return Arrays.asList(Particle.WHITE_SMOKE, Particle.WITCH, Particle.FIREWORK);
    }

    @SuppressWarnings("unused")
    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        String job = plugin.getJobsManager().getJob(player.getUniqueId());
        if (!getJobName().equalsIgnoreCase(job)) return;

        Material brokenBlock = event.getBlock().getType();

        Block block = event.getBlock();

        if (plugin.getJobProgressionData().getFarmerBlockExpMap().containsKey(brokenBlock)) {
            double exp = plugin.getJobProgressionData().getFarmerBlockExpMap().get(brokenBlock);
            handleSharedExp(player, exp);

            Component format = componentBuilder.singleComponentBuilder("&7Level: &b" + plugin.getJobsManager().getLevel(player.getUniqueId()) + " &7(&b" + plugin.getJobsManager().getExp(player.getUniqueId()) +  "&8/&b" + (plugin.getJobsManager().getLevel(player.getUniqueId()) * 100) + "&7)" + " &7(&d" + brokenBlock + "&7)").build();
            player.sendActionBar(format);

            handleParticleAndDrop(player, block.getLocation());
        }
    }
}