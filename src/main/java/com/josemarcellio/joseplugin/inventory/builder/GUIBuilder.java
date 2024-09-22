package com.josemarcellio.joseplugin.inventory.builder;

import com.josemarcellio.joseplugin.inventory.GUIItem;
import com.josemarcellio.joseplugin.inventory.page.GUIPage;
import net.kyori.adventure.text.Component;

public class GUIBuilder {
    private final Component title;
    private final int size;
    private final GUIPage page;

    public GUIBuilder(Component title, int size) {
        this.title = title;
        this.size = size;
        this.page = new GUIPage(title, size);
    }

    public GUIBuilder addItem(int slot, GUIItem item) {
        page.setItem(slot, item);
        return this;
    }

    public GUIPage build() {
        return page;
    }
}
