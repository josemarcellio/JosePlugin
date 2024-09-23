package com.josemarcellio.joseplugin.text.module;

import com.josemarcellio.joseplugin.text.TextOperation;

public class LeetSpeakText implements TextOperation {

    @Override
    public String process(String input) {
        return input
                .replaceAll("[aA]", "4")
                .replaceAll("[eE]", "3")
                .replaceAll("[iI]", "1")
                .replaceAll("[oO]", "0")
                .replaceAll("[sS]", "5")
                .replaceAll("[bB]", "8")
                .replaceAll("[gG]", "6")
                .replaceAll("[tT]", "7")
                .replaceAll("[zZ]", "2");
    }
}
