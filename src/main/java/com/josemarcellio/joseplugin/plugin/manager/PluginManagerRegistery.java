package com.josemarcellio.joseplugin.plugin.manager;

import com.josemarcellio.joseplugin.JosePlugin;
import com.josemarcellio.joseplugin.chat.PlayerChatListener;
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

public class PluginManagerRegistery implements PluginManager {

    private final PluginListenerManager listenerManager;
    private final PluginCommandManager commandManager;
    private final WarpManager warpManager;
    private final PartyManager partyManager;
    private final JosePlugin plugin;

    public PluginManagerRegistery(JosePlugin plugin) {
        this.plugin = plugin;
        this.listenerManager = new PluginListenerManager(plugin);
        this.commandManager = new PluginCommandManager(plugin);
        this.partyManager = new PartyManager();
        this.warpManager = new WarpManager(plugin);
    }

    @Override
    public void registerListeners() {
        listenerManager.registerListener(new PlayerChatListener());
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
    }

    @Override
    public void registerCommands() {
        commandManager.registerCommand("dailyreward", new DailyRewardCommand(plugin));
        commandManager.registerCommand("party", new PartyCommand(partyManager));
        commandManager.registerCommand("playerwarp", new WarpCommand(plugin, warpManager));
        commandManager.registerCommand("job", new JobsCommand(plugin));

    }
}
