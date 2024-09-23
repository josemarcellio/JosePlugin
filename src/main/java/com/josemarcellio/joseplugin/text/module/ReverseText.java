package com.josemarcellio.joseplugin.text.module;

import com.josemarcellio.joseplugin.text.TextOperation;

public class ReverseText implements TextOperation {
    @Override
    public String process(String input) {
        return new StringBuilder(input).reverse().toString();
    }
}