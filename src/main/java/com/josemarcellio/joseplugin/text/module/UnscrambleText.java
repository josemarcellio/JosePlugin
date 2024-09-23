package com.josemarcellio.joseplugin.text.module;

import com.josemarcellio.joseplugin.text.TextOperation;

import java.util.Arrays;

public class UnscrambleText implements TextOperation {

    @Override
    public String process(String input) {
        char[] chars = input.replaceAll(" ", "").toCharArray();
        Arrays.sort(chars);

        StringBuilder result = new StringBuilder();
        int charIndex = 0;

        for (char c : input.toCharArray()) {
            if (c == ' ') {
                result.append(' ');
            } else {
                result.append(chars[charIndex++]);
            }
        }

        return result.toString();
    }
}
