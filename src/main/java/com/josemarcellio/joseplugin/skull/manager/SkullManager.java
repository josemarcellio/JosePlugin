/*
 * Copyright (C) 2024 Jose Marcellio
 * GitHub: https://github.com/josemarcellio
 *
 * This software is open-source and distributed under the GNU General Public License (GPL), version 3.
 * You are free to modify, share, and distribute it as long as the same freedoms are preserved.
 *
 * No warranties are provided with this software. It is distributed in the hope that it will be useful,
 * but WITHOUT ANY IMPLIED WARRANTIES, including but not limited to the implied warranties of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 *
 * For more details, refer to the full license at <https://www.gnu.org/licenses/>.
 */

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

