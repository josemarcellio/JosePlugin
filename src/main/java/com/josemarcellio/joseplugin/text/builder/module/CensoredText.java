package com.josemarcellio.joseplugin.text.builder.module;

import com.josemarcellio.joseplugin.text.builder.TextOperation;

public class CensoredText implements TextOperation {

    @Override
    public String process(String input) {
        if (input.length() <= 2) {
            return input;
        }

        StringBuilder censored = new StringBuilder();
        censored.append(input.charAt(0));
        for (int i = 1; i < input.length() - 1; i++) {
            censored.append('*');
        }
        censored.append(input.charAt(input.length() - 1));

        return censored.toString();
    }
}

