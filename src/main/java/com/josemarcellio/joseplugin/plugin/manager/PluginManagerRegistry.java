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

package com.josemarcellio.joseplugin.plugin.manager;

import com.josemarcellio.joseplugin.JosePlugin;
import com.josemarcellio.joseplugin.chat.PlayerChatListener;
import com.josemarcellio.joseplugin.chat.troll.TrollCommand;
import com.josemarcellio.joseplugin.cooldown.CooldownManager;
import com.josemarcellio.joseplugin.cooldown.ICooldownManager;
import com.josemarcellio.joseplugin.dailyreward.command.DailyRewardCommand;
import com.josemarcellio.joseplugin.inventory.GUIManager;
import com.josemarcellio.joseplugin.job.category.farmer.FarmerListener;
import com.josemarcellio.joseplugin.job.category.farmer.FarmerSkills;
import com.josemarcellio.joseplugin.job.category.hunter.HunterListener;
import com.josemarcellio.joseplugin.job.category.hunter.HunterSkills;
import com.josemarcellio.joseplugin.job.category.lumberjack.LumberjackListener;
import com.josemarcellio.joseplugin.job.category.lumberjack.LumberjackSkills;
import com.josemarcellio.joseplugin.job.category.miner.MinerListener;
import com.josemarcellio.joseplugin.job.category.miner.MinerSkills;
import com.josemarcellio.joseplugin.job.command.JobsCommand;
import com.josemarcellio.joseplugin.party.PartyListener;
import com.josemarcellio.joseplugin.party.command.PartyCommand;
import com.josemarcellio.joseplugin.party.manager.PartyManager;
import com.josemarcellio.joseplugin.playerwarp.command.WarpCommand;
import com.josemarcellio.joseplugin.playerwarp.manager.WarpManager;
import com.josemarcellio.joseplugin.playtime.command.PlaytimeCommand;

import com.josemarcellio.joseplugin.silent.command.SilentKickCommand;
import com.josemarcellio.joseplugin.silent.manager.SilentManager;
import com.josemarcellio.joseplugin.silent.packet.SilentListener;

public class PluginManagerRegistry implements PluginManager {

    private final JosePlugin plugin;
    private final PluginListenerManager listenerManager;
    private final PluginCommandManager commandManager;
    private final WarpManager warpManager;
    private final PartyManager partyManager = new PartyManager();
    private final ICooldownManager cooldownManager = new CooldownManager();
    private final SilentManager silentManager = new SilentManager();

    public PluginManagerRegistry(JosePlugin plugin) {
        this.plugin = plugin;
        this.listenerManager = new PluginListenerManager(plugin);
        this.commandManager = new PluginCommandManager(plugin);
        this.warpManager = new WarpManager(plugin);
    }

    @Override
    public void registerListeners() {
        listenerManager.registerListener(new PlayerChatListener(cooldownManager));
        listenerManager.registerListener(new GUIManager());
        listenerManager.registerListener(new PartyListener(partyManager));

        listenerManager.registerListener(new FarmerListener(plugin, partyManager));
        listenerManager.registerListener(new HunterListener(plugin, partyManager));
        listenerManager.registerListener(new MinerListener(plugin, partyManager));
        listenerManager.registerListener(new LumberjackListener(plugin, partyManager));
        listenerManager.registerListener(new FarmerSkills(plugin));
        listenerManager.registerListener(new HunterSkills(plugin));
        listenerManager.registerListener(new MinerSkills(plugin));
        listenerManager.registerListener(new LumberjackSkills(plugin));

        listenerManager.registerListener(new SilentListener(plugin, silentManager));
    }

    @Override
    public void registerCommands() {
        commandManager.registerCommand("dailyreward", new DailyRewardCommand(plugin));
        commandManager.registerCommand("party", new PartyCommand(partyManager));
        commandManager.registerCommand("playerwarp", new WarpCommand(plugin, warpManager));
        commandManager.registerCommand("job", new JobsCommand(plugin));
        commandManager.registerCommand("troll", new TrollCommand(cooldownManager));
        commandManager.registerCommand("playtime", new PlaytimeCommand());
        commandManager.registerCommand("silentkick", new SilentKickCommand(silentManager));
    }
}
