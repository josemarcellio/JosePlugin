package com.josemarcellio.joseplugin.text;

import java.util.ArrayList;
import java.util.List;

public class TextBuilder {
    private final List<TextOperation> operations = new ArrayList<>();

    public TextBuilder addOperationIf(boolean condition, TextOperation operation) {
        if (condition) {
            operations.add(operation);
        }
        return this;
    }

    public String applyOperations(String input) {
        String result = input;
        for (TextOperation operation : operations) {
            result = operation.process(result);
        }
        operations.clear();
        return result;
    }
}