package com.josemarcellio.joseplugin.component.module;

import com.josemarcellio.joseplugin.component.ComponentChecker;
import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;

import org.bukkit.entity.Player;

public class SinglePlaceholderComponentBuilder implements ComponentChecker<Component> {
    private Player player;
    private String text;

    public SinglePlaceholderComponentBuilder player(Player player) {
        this.player = player;
        return this;
    }

    public SinglePlaceholderComponentBuilder text(String text) {
        this.text = text;
        return this;
    }

    @Override
    public Component build() {
        return MiniMessage.miniMessage().deserialize(PlaceholderAPI.setPlaceholders(player, text))
                        .decoration(TextDecoration.ITALIC, false);
    }

}
