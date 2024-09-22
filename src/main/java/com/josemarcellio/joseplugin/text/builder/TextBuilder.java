package com.josemarcellio.joseplugin.text.builder;

import java.util.ArrayList;
import java.util.List;

public class TextBuilder {
    private final List<TextOperation> operations = new ArrayList<>();

    public TextBuilder addOperation(TextOperation operation) {
        operations.add(operation);
        return this;
    }

    public String applyOperations(String input) {
        String result = input;
        for (TextOperation operation : operations) {
            result = operation.process(result);
        }
        return result;
    }
}
