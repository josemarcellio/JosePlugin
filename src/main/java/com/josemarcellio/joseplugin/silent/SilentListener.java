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

package com.josemarcellio.joseplugin.silent;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.event.PacketListenerPriority;

import com.josemarcellio.joseplugin.JosePlugin;
import com.josemarcellio.joseplugin.silent.manager.SilentManager;

import com.josemarcellio.joseplugin.silent.packet.PacketSendReceive;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.event.server.PluginEnableEvent;

import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

public class SilentListener implements Listener {

    private final JosePlugin plugin;
    private final SilentManager silentManager;
    private boolean canJoin = false;

    public SilentListener(JosePlugin plugin, SilentManager silentManager) {
        this.plugin = plugin;
        this.silentManager = silentManager;
    }

    @SuppressWarnings({"deprecation", "unused"})
    @EventHandler
    public void onLogin(PlayerLoginEvent event) {
        if (!canJoin) {
            event.disallow(PlayerLoginEvent.Result.KICK_WHITELIST, "");
        }
    }

    @SuppressWarnings("unused")
    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        UUID playerUUID = player.getUniqueId();
        silentManager.removePlayer(playerUUID);
    }

    @SuppressWarnings("unused")
    @EventHandler
    public void onPluginEnable(PluginEnableEvent event) {
        if (event.getPlugin().getName().equals(plugin.getName())) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    PacketEvents.getAPI().getEventManager().registerListener(new PacketSendReceive(silentManager),
                            PacketListenerPriority.HIGHEST);
                    PacketEvents.getAPI().init();
                    canJoin = true;
                }
            }.runTaskLater(plugin, 40L);
        }
    }

    @SuppressWarnings("unused")
    @EventHandler
    public void onPluginDisable(PluginDisableEvent event) {
        if (event.getPlugin().getName().equals(plugin.getName())) {
            PacketEvents.getAPI().terminate();
        }
    }
}
