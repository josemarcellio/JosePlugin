package com.josemarcellio.joseplugin.component.module;

import com.josemarcellio.joseplugin.component.ComponentChecker;
import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;

import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class PlaceholderComponentBuilder implements ComponentChecker<List<Component>> {
    private Player player;
    private final List<String> texts = new ArrayList<>();

    public PlaceholderComponentBuilder player(Player player) {
        this.player = player;
        return this;
    }

    public PlaceholderComponentBuilder text(String... texts) {
        this.texts.addAll(Arrays.asList(texts));
        return this;
    }

    @Override
    public List<Component> build() {
        return texts.stream()
                .map(text -> MiniMessage.miniMessage().deserialize(PlaceholderAPI.setPlaceholders(player, text))
                        .decoration(TextDecoration.ITALIC, false))
                .collect(Collectors.toList());
    }

}
