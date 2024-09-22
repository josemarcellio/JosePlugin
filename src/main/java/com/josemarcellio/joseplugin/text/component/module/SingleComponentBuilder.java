package com.josemarcellio.joseplugin.text.component.module;

import com.josemarcellio.joseplugin.text.component.ComponentChecker;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;

public class SingleComponentBuilder implements ComponentChecker<Component> {
    private String text;

    public SingleComponentBuilder text(String text) {
        this.text = text;
        return this;
    }

    @Override
    public Component build() {
        return MiniMessage.miniMessage().deserialize(text)
                .decoration(TextDecoration.ITALIC, false);
    }
}
