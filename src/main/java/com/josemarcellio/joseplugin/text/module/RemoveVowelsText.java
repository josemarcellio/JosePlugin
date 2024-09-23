package com.josemarcellio.joseplugin.text.module;

import com.josemarcellio.joseplugin.text.TextOperation;

public class RemoveVowelsText implements TextOperation {

    @Override
    public String process(String input) {
        return input.replaceAll("[AEIOUaeiou]", "");
    }
}