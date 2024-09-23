package com.josemarcellio.joseplugin.server.module;

import com.josemarcellio.joseplugin.server.IChecker;

public class ServerBrandChecker implements IChecker {

    @Override
    public boolean isInvalid() {
        try {
            Class.forName("com.destroystokyo.paper.console.PaperConsole");
            return false;
        } catch (ClassNotFoundException ignored) {
            return true;
        }
    }
}
