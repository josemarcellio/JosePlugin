package com.josemarcellio.joseplugin.job.compatibility;

import com.josemarcellio.joseplugin.JosePlugin;
import com.josemarcellio.joseplugin.job.manager.JobsManager;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class JobsPlaceholderAPI extends PlaceholderExpansion {

    private final JosePlugin plugin;

    public JobsPlaceholderAPI(JosePlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public @NotNull String getIdentifier() {
        return "josejobs";
    }

    @Override
    public @NotNull String getAuthor() {
        return "JoseMarcellio";
    }

    @Override
    public @NotNull String getVersion() {
        return "1.0";
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public boolean canRegister() {
        return true;
    }

    @Override
    public @Nullable String onPlaceholderRequest(Player player, @NotNull String identifier) {
        JobsManager jobsManager = plugin.getJobsManager();

        if (jobsManager.getLevel(player.getUniqueId()) < 1) {
            return "";
        }

        switch (identifier) {
            case "name":
                return jobsManager.getDisplayName(jobsManager.getJob(player.getUniqueId()));
            default:
                return null;
        }
    }
}