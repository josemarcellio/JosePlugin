package com.josemarcellio.joseplugin.blacklist;

public interface BlacklistChecker<T> {
    boolean isBlacklisted(T t);
}
