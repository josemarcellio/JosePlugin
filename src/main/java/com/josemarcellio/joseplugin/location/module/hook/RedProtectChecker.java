package com.josemarcellio.joseplugin.location.module.hook;

import br.net.fabiozumbi12.RedProtect.Bukkit.RedProtect;
import br.net.fabiozumbi12.RedProtect.Bukkit.Region;
import com.josemarcellio.joseplugin.location.LocationChecker;
import com.josemarcellio.joseplugin.plugin.PluginStateChecker;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class RedProtectChecker implements LocationChecker {

    @Override
    public boolean isSafe(Location location, Player player) {
        PluginStateChecker pluginLoaded = new PluginStateChecker();
        if (pluginLoaded.isPluginLoaded("RedProtect")) {
            World world = location.getWorld();
            if (world == null) {
                return false;
            }

            Region region = RedProtect.get().getAPI().getRegion(player.getLocation());
            if (region != null) {
                return region.canBuild(player);
            }
            return true;
        } else {
            return true;
        }
    }
}
