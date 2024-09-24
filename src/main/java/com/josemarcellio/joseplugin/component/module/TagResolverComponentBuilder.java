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

package com.josemarcellio.joseplugin.component.module;

import com.josemarcellio.joseplugin.component.ComponentChecker;
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
