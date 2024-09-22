package com.josemarcellio.joseplugin.chat.manager;

import java.util.HashSet;
import java.util.Set;

public class BadWordManager {

    private final Set<String> badWords;

    public BadWordManager() {
        this.badWords = new HashSet<>();
        initializeBadWords();
    }

    private void initializeBadWords() {
        badWords.add("anjing");
        badWords.add("bangsat");
        badWords.add("ngentot");
        badWords.add("fuck");
        badWords.add("nigga");
        badWords.add("nigger");
        badWords.add("kontol");
        badWords.add("memek");
        badWords.add("pepek");
        badWords.add("bajingan");
        badWords.add("sundel");
        badWords.add("vagina");
        badWords.add("penis");
        badWords.add("ngewe");
    }

    public Set<String> getBadWords() {
        return badWords;
    }
}
