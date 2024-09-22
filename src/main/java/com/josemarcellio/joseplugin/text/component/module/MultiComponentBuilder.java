package com.josemarcellio.joseplugin.text.component.module;

import com.josemarcellio.joseplugin.text.component.ComponentChecker;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class MultiComponentBuilder implements ComponentChecker<List<Component>> {
    private final List<String> texts = new ArrayList<>();

    public MultiComponentBuilder text(String... texts) {
        this.texts.addAll(Arrays.asList(texts));
        return this;
    }

    @Override
    public List<Component> build() {
        return texts.stream()
                .map(text -> MiniMessage.miniMessage().deserialize(text)
                        .decoration(TextDecoration.ITALIC, false))
                .collect(Collectors.toList());
    }
}
