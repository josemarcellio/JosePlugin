package com.josemarcellio.joseplugin.server.module;

import com.josemarcellio.joseplugin.server.IChecker;

public class ServerBrandChecker implements IChecker {

    @Override
    public boolean isValid() {
        try {
            Class.forName("com.destroystokyo.paper.console.PaperConsole");
            return true;
        } catch (ClassNotFoundException ignored) {
            return false;
        }
    }
}
