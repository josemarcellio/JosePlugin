package com.josemarcellio.joseplugin.text.builder.module;

import com.josemarcellio.joseplugin.text.builder.TextOperation;

public class ReverseText implements TextOperation {

    @Override
    public String process(String input) {
        return new StringBuilder(input).reverse().toString();
    }
}
