package com.josemarcellio.joseplugin.blacklist;

import com.josemarcellio.joseplugin.blacklist.module.ItemBlacklist;
import com.josemarcellio.joseplugin.blacklist.module.WorldBlacklist;
import org.bukkit.entity.Player;

public class Blacklist {

    private final WorldBlacklist worldBlacklist = new WorldBlacklist();
    private final ItemBlacklist itemBlacklist = new ItemBlacklist();

    public boolean isBlacklistWorld(Player player) {
        return worldBlacklist.isBlacklisted(player);
    }

    public boolean isBlacklistItem(String material) {
        return itemBlacklist.isBlacklisted(material);
    }
}
