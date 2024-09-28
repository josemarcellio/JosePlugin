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

package com.josemarcellio.joseplugin.job.category.farmer;

import com.josemarcellio.joseplugin.JosePlugin;
import com.josemarcellio.joseplugin.job.category.BaseJobsHandler;
import com.josemarcellio.joseplugin.party.manager.PartyManager;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class FarmerListener extends BaseJobsHandler implements Listener {

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

            sendJobProgressActionBar(player, brokenBlock);
        }
    }
}