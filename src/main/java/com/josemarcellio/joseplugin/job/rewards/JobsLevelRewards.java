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

package com.josemarcellio.joseplugin.job.rewards;

import com.josemarcellio.joseplugin.JosePlugin;
import com.josemarcellio.joseplugin.component.ComponentBuilder;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.entity.Player;

public class JobsLevelRewards {

    private final JosePlugin plugin;
    private final Economy econ;
    private final ComponentBuilder componentBuilder = new ComponentBuilder();

    public JobsLevelRewards(JosePlugin plugin, Economy econ) {
        this.plugin = plugin;
        this.econ = econ;
    }

    public void giveLevelUpRewards(Player player, int level) {
        if (level < 2 || level > plugin.getJobsManager().getMaxLevel()) {
            return;
        }

        double econReward = plugin.getJobsManager().getEconReward(level);
        
        econ.depositPlayer(player, econReward);

        player.sendMessage(componentBuilder.singleComponentBuilder().text("<green>-------------------------").build());
        player.sendMessage(componentBuilder.singleComponentBuilder().text("<gold> ⭐ <color:#fae7b5>Jobs <color:#c4c3d0>• <white>level " + level + " <white>rewards:").build());
        player.sendMessage(componentBuilder.singleComponentBuilder().text("<dark_gray> - <yellow>" + econReward + " <gray>Coins").build());
        player.sendMessage(componentBuilder.singleComponentBuilder().text("<green>-------------------------").build());
    }
}