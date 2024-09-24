/*
 * Copyright (C) 2024 Jose Marcellio
 * GitHub: https://github.com/josemarcellio
 *
 * This software is open-source and distributed under the GNU General Public License (GPL), version 3.
 * You are free to modify, share, and distribute it as long as the same freedoms are preserved.
 *
 * No warranties are provided with this software. It is distributed in the hope that it will be useful,
 * but WITHOUT ANY IMPLIED WARRANTIES, including but not limited to the implied warranties of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 *
 * For more details, refer to the full license at <https://www.gnu.org/licenses/>.
 */

package com.josemarcellio.joseplugin.silent.command;

import com.josemarcellio.joseplugin.component.ComponentBuilder;
import com.josemarcellio.joseplugin.silent.manager.SilentManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class SilentKickCommand implements CommandExecutor {

    private final SilentManager silentManager;
    private final ComponentBuilder componentBuilder = new ComponentBuilder();

    public SilentKickCommand(SilentManager silentManager) {
        this.silentManager = silentManager;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (args.length  < 1) {
            sender.sendMessage(componentBuilder.singleComponentBuilder().text("<gold> ⚚ <color:#fae7b5>Silent Kick <color:#c4c3d0>• <white>/silentkick <player>").build());
            return false;
        }

        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            sender.sendMessage(componentBuilder.singleComponentBuilder().text("<gold> ⚚ <color:#fae7b5>Silent Kick <color:#c4c3d0>• <white>Player tidak ditemukan").build());
            return false;
        }

        UUID targetUUID = target.getUniqueId();
        silentManager.addPlayer(targetUUID);
        sender.sendMessage(componentBuilder.singleComponentBuilder().text("<gold> ⚚ <color:#fae7b5>Silent Kick <color:#c4c3d0>• <aqua>" + target.getName() + " <white>berhasil di silent").build());
        return true;
    }
}
