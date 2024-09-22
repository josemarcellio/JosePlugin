package com.josemarcellio.joseplugin.blacklist.module;

import com.josemarcellio.joseplugin.blacklist.BlacklistChecker;

import java.util.Arrays;
import java.util.List;

public class ItemBlacklist implements BlacklistChecker<String> {

    private final List<String> blacklistItems = Arrays.asList("BARRIER", "DRAGON_HEAD", "DRAGON_EGG");

    @Override
    public boolean isBlacklisted(String material) {
        return blacklistItems.contains(material.toUpperCase());
    }
}
