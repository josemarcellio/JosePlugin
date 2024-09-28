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

package com.josemarcellio.joseplugin.job.category.hunter;

import com.josemarcellio.joseplugin.JosePlugin;
import com.josemarcellio.joseplugin.job.category.BaseJobsHandler;
import com.josemarcellio.joseplugin.party.manager.PartyManager;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

public class HunterListener extends BaseJobsHandler implements Listener {

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

        sendJobProgressActionBar(killer, entityType.name());
    }
}