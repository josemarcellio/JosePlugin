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

package com.josemarcellio.joseplugin;

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
import org.bstats.bukkit.Metrics;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

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
        dependencyManager = new DependencyManager();

        if (serverBrandChecker.isInvalid()) {
            getLogger().info("JosePlugin is only compatible with Paper or Paper Fork!");
            getServer().shutdown();
        } else if (versionChecker.isInvalid()) {
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

    @SuppressWarnings("ConstantConditions")
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
