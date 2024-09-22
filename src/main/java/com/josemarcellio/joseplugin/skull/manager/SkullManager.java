package com.josemarcellio.joseplugin.skull.manager;

import com.josemarcellio.joseplugin.skull.provider.Base64SkullProvider;
import com.josemarcellio.joseplugin.skull.provider.ISkullProvider;
import com.josemarcellio.joseplugin.skull.provider.TextureIdSkullProvider;
import com.josemarcellio.joseplugin.skull.provider.UrlSkullProvider;
import com.josemarcellio.joseplugin.skull.type.SkullType;

public class SkullManager {

    public static ISkullProvider getSkullProvider(SkullType type) {
        switch (type) {
            case BASE64:
                return new Base64SkullProvider();
            case URL:
                return new UrlSkullProvider();
            case TEXTURE_ID:
                return new TextureIdSkullProvider();
            default:
                throw new IllegalArgumentException("Unknown skull type: " + type);
        }
    }
}

