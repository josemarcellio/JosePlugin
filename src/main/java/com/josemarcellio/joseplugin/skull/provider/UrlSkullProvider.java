package com.josemarcellio.joseplugin.skull.provider;

import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Base64;

public class UrlSkullProvider extends Base64SkullProvider {

    @Override
    public @NotNull ItemStack getSkull(String url) {
        String base64 = Base64.getEncoder().encodeToString(
                String.format("{textures:{SKIN:{url:\"%s\"}}}", url).getBytes());
        return super.getSkull(base64);
    }
}

