package com.josemarcellio.joseplugin.item.type;

import com.josemarcellio.joseplugin.exception.JosePluginException;
import com.josemarcellio.joseplugin.skull.manager.SkullManager;
import com.josemarcellio.joseplugin.skull.provider.ISkullProvider;
import com.josemarcellio.joseplugin.skull.type.SkullType;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class SkullItemBuilder extends BaseItemBuilder {

    public SkullItemBuilder(String textures, SkullType skullType) {
        super(Material.PLAYER_HEAD);
        ISkullProvider skullProvider;
        skullProvider = SkullManager.getSkullProvider(skullType);
        this.itemStack = skullProvider.getSkull(textures);
        this.itemMeta = itemStack.getItemMeta();
        if (this.itemMeta == null) {
            throw new JosePluginException("Failed to retrieve ItemMeta for this material: ", null);
        }
    }

    @Override
    public ItemStack build() {
        return super.build();
    }
}

