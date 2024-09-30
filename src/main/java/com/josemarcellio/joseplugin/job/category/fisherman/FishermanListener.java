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

package com.josemarcellio.joseplugin.job.category.fisherman;

import com.josemarcellio.joseplugin.JosePlugin;
import com.josemarcellio.joseplugin.job.category.BaseJobsHandler;
import com.josemarcellio.joseplugin.party.manager.PartyManager;

import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;

public class FishermanListener extends BaseJobsHandler implements Listener {

    private final JosePlugin plugin;
    private final PartyManager partyManager;

    public FishermanListener(JosePlugin plugin, PartyManager partyManager) {
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
        return "fisherman";
    }

    @SuppressWarnings("unused")
    @EventHandler
    public void onPlayerFish(PlayerFishEvent event) {
        Player player = event.getPlayer();

        if (event.getState().equals(PlayerFishEvent.State.CAUGHT_FISH)) {
            String job = plugin.getJobsManager().getJob(player.getUniqueId());
            if (!getJobName().equalsIgnoreCase(job)) return;

            if (event.getCaught() == null) return;

            if (event.getCaught() instanceof Item) {

                Material caughtItem = ((Item) event.getCaught()).getItemStack().getType();

                if (plugin.getJobProgressionData().getFishermanFishExpMap().containsKey(caughtItem)) {

                    double exp = plugin.getJobProgressionData().getFishermanFishExpMap().get(caughtItem);

                    sendJobProgressActionBar(player, caughtItem);
                }
            }
        }
    }
}