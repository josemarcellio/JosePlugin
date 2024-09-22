package com.josemarcellio.joseplugin;

import com.josemarcellio.joseplugin.bstats.Metrics;
import com.josemarcellio.joseplugin.job.data.JobsProgressionData;
import com.josemarcellio.joseplugin.job.manager.JobsManager;
import com.josemarcellio.joseplugin.playerwarp.manager.WarpManager;
import com.josemarcellio.joseplugin.plugin.loader.manager.DependencyManager;
import com.josemarcellio.joseplugin.plugin.loader.manager.IDependencyManager;
import com.josemarcellio.joseplugin.plugin.manager.PluginManagerRegistery;
import com.josemarcellio.joseplugin.server.IChecker;
import com.josemarcellio.joseplugin.server.module.ServerBrandChecker;
import com.josemarcellio.joseplugin.server.module.VersionChecker;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public class JosePlugin extends JavaPlugin {

    private final List<Plugin> loadedPlugins = new ArrayList<>();
    private IDependencyManager dependencyManager;
    private PluginManagerRegistery pluginManagerRegistery;
    private WarpManager warpManager;
    public Economy econ = null;
    public JobsManager jobsManager;
    public JobsProgressionData jobProgressionData;

    @Override
    public void onEnable() {
        IChecker serverBrandChecker = new ServerBrandChecker();
        IChecker versionChecker = new VersionChecker();
        this.dependencyManager = new DependencyManager();

        if (!serverBrandChecker.isValid()) {
            getLogger().info("JosePlugin is only compatible with Paper or Paper Fork!");
            getServer().shutdown();
        } else if (!versionChecker.isValid()) {
            getLogger().info("JosePlugin requires at least version 1.21.1!");
            getServer().shutdown();
        } else {

            int pluginId = 23444;
            new Metrics(this, pluginId);

            dependencyManager.downloadAndLoadDependencies(this, loadedPlugins);
            getLogger().info("JosePlugin plugin enabled.");

            if (!setupEconomy()) {
                getLogger().severe("Disabled due to no Vault dependency found!");
                getServer().getPluginManager().disablePlugin(this);
                return;
            }

            jobsManager = new JobsManager(this, econ);
            jobProgressionData = new JobsProgressionData();

            warpManager = new WarpManager(this);

            pluginManagerRegistery = new PluginManagerRegistery(this);
            pluginManagerRegistery.registerListeners();
            pluginManagerRegistery.registerCommands();
        }
    }

    @Override
    public void onDisable() {
        warpManager.saveWarps();
        jobsManager.closeDatabase();
        pluginManagerRegistery = null;
        dependencyManager.disableDependencies(loadedPlugins, getServer());
        getLogger().info("JosePlugin plugin disabled.");
    }

    public WarpManager getWarpManager() {
        return warpManager;
    }

    public JobsManager getJobsManager() {
        return jobsManager;
    }

    public JobsProgressionData getJobProgressionData() {
        return jobProgressionData;
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }

    public Economy getEconomy() {
        return econ;
    }
}
