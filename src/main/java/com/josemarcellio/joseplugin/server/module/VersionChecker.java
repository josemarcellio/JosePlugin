package com.josemarcellio.joseplugin.server.module;

import com.josemarcellio.joseplugin.server.IChecker;
import org.bukkit.Bukkit;

public class VersionChecker implements IChecker {

    @Override
    public boolean isInvalid() {
        String currentVersion = Bukkit.getVersion();
        return currentVersion.compareTo("1.21.1") < 0;
    }
}
