package com.josemarcellio.joseplugin.skull.provider;

import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class TextureIdSkullProvider extends UrlSkullProvider {

    @NotNull
    @Override
    public ItemStack getSkull(String textureId) {
        String url = "http://textures.minecraft.net/texture/" + textureId;
        return super.getSkull(url);
    }
}

