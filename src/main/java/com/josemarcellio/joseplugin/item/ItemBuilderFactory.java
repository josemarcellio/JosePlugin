package com.josemarcellio.joseplugin.item;

import com.josemarcellio.joseplugin.item.type.BaseItemBuilder;
import com.josemarcellio.joseplugin.item.type.SkullItemBuilder;
import com.josemarcellio.joseplugin.skull.type.SkullType;
import org.bukkit.Material;

public class ItemBuilderFactory {

    public IItemBuilder createItemBuilder(Material material) {
        return new BaseItemBuilder(material);
    }

    public IItemBuilder createSkullItemBuilder(String textures, SkullType skullType) {
        return new SkullItemBuilder(textures, skullType);
    }
}
