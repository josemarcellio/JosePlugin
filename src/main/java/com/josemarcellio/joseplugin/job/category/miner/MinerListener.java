package com.josemarcellio.joseplugin.job.category.miner;

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
import org.bukkit.event.block.BlockPlaceEvent;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MinerListener extends BaseJobsHandler implements Listener {

    private final ComponentBuilder componentBuilder = new ComponentBuilder();
    private final JosePlugin plugin;
    private final PartyManager partyManager;
    private final Set<Block> placedBlocks = new HashSet<>();

    public MinerListener(JosePlugin plugin, PartyManager partyManager) {
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
        return "miner";
    }

    @Override
    protected List<Material> getValidTools() {
        return Arrays.asList(
                Material.WOODEN_PICKAXE,
                Material.STONE_PICKAXE,
                Material.IRON_PICKAXE,
                Material.GOLDEN_PICKAXE,
                Material.DIAMOND_PICKAXE,
                Material.NETHERITE_PICKAXE
        );
    }

    @Override
    protected List<Particle> getParticles() {
        return Arrays.asList(Particle.WHITE_SMOKE, Particle.WITCH, Particle.FIREWORK);
    }

    @SuppressWarnings("unused")
    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Block block = event.getBlock();
        placedBlocks.add(block);
    }

    @SuppressWarnings("unused")
    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        String job = plugin.getJobsManager().getJob(player.getUniqueId());
        if (!getJobName().equalsIgnoreCase(job)) return;

        Material brokenBlock = event.getBlock().getType();

        Block block = event.getBlock();

        if (placedBlocks.contains(block)) {
            placedBlocks.remove(block);
            return;
        }

        if (plugin.getJobProgressionData().getMinerBlockExpMap().containsKey(brokenBlock)) {
            double exp = plugin.getJobProgressionData().getMinerBlockExpMap().get(brokenBlock);
            handleSharedExp(player, exp);

            Component format = componentBuilder.singleComponentBuilder("<gray>Level: <aqua>" + plugin.getJobsManager().getLevel(player.getUniqueId()) + " <gray>(<aqua>" + plugin.getJobsManager().getExp(player.getUniqueId()) +  "<dark_gray>/<aqua>" + (plugin.getJobsManager().getLevel(player.getUniqueId()) * 100) + "<gray>)" + " (<light_purple>" + brokenBlock + "<gray>)").build();
            player.sendActionBar(format);

            handleParticleAndDrop(player, block.getLocation());
        }
    }
}