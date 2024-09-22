package com.josemarcellio.joseplugin.text.builder.module;

import com.josemarcellio.joseplugin.text.builder.TextOperation;

import java.util.Arrays;

public class UnscrambleText implements TextOperation {

    @Override
    public String process(String input) {
        char[] chars = input.toCharArray();
        Arrays.sort(chars);
        return new String(chars);
    }
}
