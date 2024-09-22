package com.josemarcellio.joseplugin.skull.provider;

import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface ISkullProvider {

    @NotNull
    ItemStack getSkull(String identifier);
}
