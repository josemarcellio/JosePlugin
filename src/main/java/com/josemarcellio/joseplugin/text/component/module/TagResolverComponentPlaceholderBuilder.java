package com.josemarcellio.joseplugin.text.component.module;

import com.josemarcellio.joseplugin.text.component.ComponentChecker;
import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TagResolverComponentPlaceholderBuilder implements ComponentChecker<Component> {
    private Player player;
    private String text;
    private final List<TagResolver> tagResolvers = new ArrayList<>();

    public TagResolverComponentPlaceholderBuilder player(Player player) {
        this.player = player;
        return this;
    }

    public TagResolverComponentPlaceholderBuilder text(String text) {
        this.text = text;
        return this;
    }

    public TagResolverComponentPlaceholderBuilder placeholders(TagResolver... resolvers) {
        this.tagResolvers.addAll(Arrays.asList(resolvers));
        return this;
    }

    @Override
    public Component build() {
        return MiniMessage.miniMessage().deserialize(PlaceholderAPI.setPlaceholders(player, text), TagResolver.resolver(tagResolvers))
                .decoration(TextDecoration.ITALIC, false);
    }
}
