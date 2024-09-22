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

        player.sendMessage(componentBuilder.singleComponentBuilder("<green>-------------------------").build());
        player.sendMessage(componentBuilder.singleComponentBuilder("<gold> ⭐ <color:#fae7b5>Jobs <color:#c4c3d0>• <white>level " + level + " <white>rewards:").build());
        player.sendMessage(componentBuilder.singleComponentBuilder("<dark_gray> - <yellow>" + econReward + " <gray>Coins").build());
        player.sendMessage(componentBuilder.singleComponentBuilder("<green>-------------------------").build());
    }
}