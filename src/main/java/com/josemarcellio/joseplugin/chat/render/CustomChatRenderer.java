package com.josemarcellio.joseplugin.chat.render;

import com.josemarcellio.joseplugin.chat.filter.BadWordFilter;
import com.josemarcellio.joseplugin.chat.manager.BadWordManager;
import com.josemarcellio.joseplugin.component.ComponentBuilder;

import io.papermc.paper.chat.ChatRenderer;
import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class CustomChatRenderer implements ChatRenderer {

    private final String chatFormat;
    private final ComponentBuilder componentBuilder = new ComponentBuilder();
    private final BadWordFilter badWordFilter = new BadWordFilter(new BadWordManager());

    public CustomChatRenderer(String chatFormat) {
        this.chatFormat = chatFormat;
    }

    @Override
    public @NotNull Component render(@NotNull Player source, @NotNull Component sourceDisplayName,
                                     @NotNull Component message, @NotNull Audience viewer) {

        String resolved = PlaceholderAPI.setPlaceholders(source, chatFormat);
        String componentMessage = PlainTextComponentSerializer.plainText().serialize(message);

        String filteredMessage = badWordFilter.filterBadWords(componentMessage);
        Component finalMessage = Component.text(filteredMessage);

        return componentBuilder.tagResolverBuilder(resolved)
                .placeholders(Placeholder.component("player", sourceDisplayName))
                .placeholders(Placeholder.component("message", finalMessage)).build();
    }
}