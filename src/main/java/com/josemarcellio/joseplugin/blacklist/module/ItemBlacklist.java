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

package com.josemarcellio.joseplugin.blacklist.module;

import com.josemarcellio.joseplugin.blacklist.BlacklistChecker;

import java.util.Arrays;
import java.util.List;

public class ItemBlacklist implements BlacklistChecker<String> {

    private final List<String> blacklistItems = Arrays.asList("BARRIER", "DRAGON_HEAD", "DRAGON_EGG");

    @Override
    public boolean isBlacklisted(String material) {
        return blacklistItems.contains(material.toUpperCase());
    }
}
