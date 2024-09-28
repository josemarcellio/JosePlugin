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
import com.josemarcellio.joseplugin.component.ComponentBuilder;
import com.josemarcellio.joseplugin.cooldown.CooldownManager;
import com.josemarcellio.joseplugin.party.manager.Party;
import com.josemarcellio.joseplugin.party.manager.PartyManager;
import net.kyori.adventure.text.Component;
import org.bukkit.*;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.UUID;

public abstract class BaseJobsHandler {

    protected final ComponentBuilder componentBuilder = new ComponentBuilder();
    protected final CooldownManager cooldownManager = new CooldownManager();

    protected abstract JosePlugin getPlugin();
    protected abstract PartyManager getPlayerParty();
    protected abstract String getJobName();

    public void handleSharedExp(Player player, double amount) {
        Party party = getPlayerParty().getPlayerParty(player.getUniqueId());
        if (party != null) {
            Collection<UUID> memberUUIDs = party.getMembers();
            int memberCount = Math.min(memberUUIDs.size(), 4);
            for (UUID memberUUID : memberUUIDs) {
                Player member = Bukkit.getPlayer(memberUUID);
                if (member != null && member.isOnline() && getPlugin().getJobsManager().getLevel(memberUUID) > 0) {
                    double shared = amount / memberCount;
                    if (!memberUUID.equals(player.getUniqueId())) {
                        getPlugin().getJobsManager().giveExp(memberUUID, shared);
                    } else {
                        getPlugin().getJobsManager().giveExp(memberUUID, amount);
                    }
                }
            }
        } else {
            getPlugin().getJobsManager().giveExp(player.getUniqueId(), amount);
        }

    }

    public void sendActionBar(Player player, String message) {
        Component format = componentBuilder.singleComponentBuilder().text(message).build();
        player.sendActionBar(format);
    }

    public void sendJobProgressActionBar(Player player, Object object) {
        UUID playerUUID = player.getUniqueId();

        int level = getPlugin().getJobsManager().getLevel(player.getUniqueId());
        double exp = getPlugin().getJobsManager().getExp(player.getUniqueId());
        double requiredExp = level * 100.0;

        String message = String.format(
                "<gray>Level: <aqua>%d <gray>(<aqua>%.2f <dark_gray>/<aqua>%.2f<gray>) (<light_purple>%s<gray>)",
                level, exp, requiredExp, object.toString()
        );

        String action = "job_progress";
        int cooldownTime = 1000;

        if (!cooldownManager.isOnCooldown(playerUUID, action)) {
            cooldownManager.startCooldown(playerUUID, action, cooldownTime);
            sendActionBar(player, message);
        }
    }
}