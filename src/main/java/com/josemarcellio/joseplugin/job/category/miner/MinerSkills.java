package com.josemarcellio.joseplugin.job.category.miner;

import com.josemarcellio.joseplugin.JosePlugin;
import com.josemarcellio.joseplugin.job.category.BaseSkillsHandler;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Arrays;
import java.util.List;

public class MinerSkills extends BaseSkillsHandler implements Listener {

    private final JosePlugin plugin;

    public MinerSkills(JosePlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    protected JosePlugin getPlugin() {
        return plugin;
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
    protected List<PotionEffect> getEffects() {
        return Arrays.asList(
                new PotionEffect(PotionEffectType.HASTE, 30 * 20, 1),
                new PotionEffect(PotionEffectType.SPEED, 30 * 20, 1)
        );
    }

    @SuppressWarnings("unused")
    @EventHandler
    public void onAxeRightClick(PlayerInteractEvent event) {
        handleSkillActivation(event);
    }
}