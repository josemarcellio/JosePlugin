package com.josemarcellio.joseplugin.blacklist.module;

import java.util.Arrays;
import java.util.List;

import com.josemarcellio.joseplugin.blacklist.BlacklistChecker;
import org.bukkit.entity.Player;

public class WorldBlacklist implements BlacklistChecker<Player> {

    private final List<String> blacklistWorlds = Arrays.asList("world_nether", "world_the_end");

    @Override
    public boolean isBlacklisted(Player player) {
        return blacklistWorlds.contains(player.getWorld().getName());
    }
}
