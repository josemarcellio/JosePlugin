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

package com.josemarcellio.joseplugin.job.category;

import com.josemarcellio.joseplugin.JosePlugin;
import com.josemarcellio.joseplugin.cooldown.CooldownManager;
import com.josemarcellio.joseplugin.cooldown.ICooldownManager;
import com.josemarcellio.joseplugin.component.ComponentBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import java.util.List;
import java.util.UUID;

public abstract class BaseSkillsHandler {

    private final ComponentBuilder componentBuilder = new ComponentBuilder();
    private final ICooldownManager cooldownManager = new CooldownManager();

    protected abstract JosePlugin getPlugin();
    protected abstract String getJobName();
    protected abstract List<Material> getValidTools();
    protected abstract List<PotionEffect> getEffects();

    public void handleSkillActivation(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        UUID playerUUID = player.getUniqueId();
        String job = getPlugin().getJobsManager().getJob(playerUUID);

        if (!getJobName().equalsIgnoreCase(job)) return;
        if (getPlugin().getJobsManager().getLevel(playerUUID) < 50) return;

        ItemStack item = player.getInventory().getItemInMainHand();
        if (!isValidTool(item.getType())) return;

        String action = "skills";
        if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (!cooldownManager.isOnCooldown(playerUUID, action)) {
                applyEffects(player);
                cooldownManager.startCooldown(playerUUID, action, 3000);
                player.sendMessage(componentBuilder.singleComponentBuilder().text("<yellow> ✪ <color:#fae7b5>Jobs <color:#c4c3d0>• <white>Berhasil mengaktifkan skills " + getPlugin().getJobsManager().getDisplayName(getJobName()) + "<white>, Cooldown 3 menit!").build());
            }
        }
    }

    private boolean isValidTool(Material material) {
        return getValidTools().contains(material);
    }

    private void applyEffects(Player player) {
        for (PotionEffect effect : getEffects()) {
            player.addPotionEffect(effect);
        }
    }
}