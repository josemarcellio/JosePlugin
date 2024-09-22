package com.josemarcellio.joseplugin.chat.filter;

import com.josemarcellio.joseplugin.chat.manager.BadWordManager;

public class BadWordFilter {

    private final BadWordManager badWordManager;

    public BadWordFilter(BadWordManager badWordManager) {
        this.badWordManager = badWordManager;
    }

    public String filterBadWords(String message) {
        for (String badWord : badWordManager.getBadWords()) {
            String regex = createFlexibleRegex(badWord);
            if (message.toLowerCase().matches(".*" + regex + ".*")) {
                message = message.replaceAll("(?i)" + regex, censorWord(badWord));
            }
        }
        return message;
    }

    private String createFlexibleRegex(String word) {
        StringBuilder regex = new StringBuilder();
        for (char c : word.toCharArray()) {
            regex.append(c).append("+");
        }
        return regex.toString();
    }

    private String censorWord(String word) {
        return word.charAt(0) + "*".repeat(word.length() - 1);
    }
}
