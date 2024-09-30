/*
 * Copyright (C) 2024 Jose Marcellio
 * GitHub: https://github.com/josemarcellio
 *
 * This software is open-source and distributed under the GNU General Public License (GPL), version 3.
 * You are free to modify, share, and distribute it as long as the same freedoms are preserved.
 *
 * No warranties are provided with this software. It is distributed in the hope that it will be useful,
 * but WITHOUT ANY IMPLIED WARRANTIES, including but not limited to the implied warranties of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 *
 * For more details, refer to the full license at <https://www.gnu.org/licenses/>.
 */

package com.josemarcellio.joseplugin.job.category.lumberjack;

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

public class LumberjackSkills extends BaseSkillsHandler implements Listener {

    private final JosePlugin plugin;

    public LumberjackSkills(JosePlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    protected JosePlugin getPlugin() {
        return plugin;
    }

    @Override
    protected String getJobName() {
        return "lumberjack";
    }

    @Override
    protected List<Material> getValidTools() {
        return Arrays.asList(
                Material.WOODEN_AXE,
                Material.STONE_AXE,
                Material.IRON_AXE,
                Material.GOLDEN_AXE,
                Material.DIAMOND_AXE,
                Material.NETHERITE_AXE
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
    public void onRightClickAxe(PlayerInteractEvent event) {
        handleSkillActivation(event);
    }
}