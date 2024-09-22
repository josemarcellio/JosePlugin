package com.josemarcellio.joseplugin.job.category.farmer;

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

public class FarmerSkills extends BaseSkillsHandler implements Listener {

    private final JosePlugin plugin;

    public FarmerSkills(JosePlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    protected JosePlugin getPlugin() {
        return plugin;
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