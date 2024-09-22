package com.josemarcellio.joseplugin.chat.render;

import com.josemarcellio.joseplugin.text.component.ComponentBuilder;

import io.papermc.paper.chat.ChatRenderer;
import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class CustomChatRenderer implements ChatRenderer {

    private final String chatFormat;
    private final ComponentBuilder componentBuilder = new ComponentBuilder();

    public CustomChatRenderer(String chatFormat) {
        this.chatFormat = chatFormat;
    }

    @Override
    public @NotNull Component render(@NotNull Player source, @NotNull Component sourceDisplayName,
                                     @NotNull Component message, @NotNull Audience viewer) {

        String resolved = PlaceholderAPI.setPlaceholders(source, chatFormat);

        return componentBuilder.tagResolverBuilder(resolved)
                .placeholders(Placeholder.component("player", sourceDisplayName))
                .placeholders(Placeholder.component("message", message)).build();
    }
}