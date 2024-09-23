package com.josemarcellio.joseplugin.chat.render;

import com.josemarcellio.joseplugin.chat.filter.BadWordFilter;
import com.josemarcellio.joseplugin.chat.manager.BadWordManager;

import com.josemarcellio.joseplugin.component.ComponentBuilder;

import com.josemarcellio.joseplugin.cooldown.ICooldownManager;
import com.josemarcellio.joseplugin.text.TextBuilder;
import com.josemarcellio.joseplugin.text.module.LeetSpeakText;
import com.josemarcellio.joseplugin.text.module.RemoveVowelsText;
import com.josemarcellio.joseplugin.text.module.ReverseText;
import com.josemarcellio.joseplugin.text.module.UnscrambleText;
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
    private final ICooldownManager cooldownManager;
    private final TextBuilder textBuilder = new TextBuilder();

    public CustomChatRenderer(String chatFormat, ICooldownManager cooldownManager) {
        this.chatFormat = chatFormat;
        this.cooldownManager = cooldownManager;
    }

    @Override
    public @NotNull Component render(@NotNull Player source, @NotNull Component sourceDisplayName,
                                     @NotNull Component message, @NotNull Audience viewer) {

        String resolved = PlaceholderAPI.setPlaceholders(source, chatFormat);
        String unComponentMessage = PlainTextComponentSerializer.plainText().serialize(message);

        String filteredMessage = badWordFilter.filterBadWords(unComponentMessage);

        filteredMessage = textBuilder
                .addOperationIf(cooldownManager.isOnCooldown(source.getUniqueId(), "reverse"), new ReverseText())
                .addOperationIf(cooldownManager.isOnCooldown(source.getUniqueId(), "unscramble"), new UnscrambleText())
                .addOperationIf(cooldownManager.isOnCooldown(source.getUniqueId(), "leetspeak"), new LeetSpeakText())
                .addOperationIf(cooldownManager.isOnCooldown(source.getUniqueId(), "removevowels"), new RemoveVowelsText())
                .applyOperations(filteredMessage);

        Component finalMessage = Component.text(filteredMessage);

        return componentBuilder.tagResolverBuilder().text(resolved)
                .placeholders(Placeholder.component("player", sourceDisplayName))
                .placeholders(Placeholder.component("message", finalMessage)).build();
    }
}