package com.josemarcellio.joseplugin.text.component.module;

import com.josemarcellio.joseplugin.text.component.ComponentChecker;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TagResolverComponentBuilder implements ComponentChecker<Component> {
    private String text;
    private final List<TagResolver> tagResolvers = new ArrayList<>();

    public TagResolverComponentBuilder text(String text) {
        this.text = text;
        return this;
    }

    public TagResolverComponentBuilder placeholders(TagResolver... resolvers) {
        this.tagResolvers.addAll(Arrays.asList(resolvers));
        return this;
    }

    @Override
    public Component build() {
        return MiniMessage.miniMessage().deserialize(text, TagResolver.resolver(tagResolvers))
                .decoration(TextDecoration.ITALIC, false);
    }
}
