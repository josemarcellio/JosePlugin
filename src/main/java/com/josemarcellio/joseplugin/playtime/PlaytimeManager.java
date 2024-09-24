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

package com.josemarcellio.joseplugin.playtime;

import com.josemarcellio.joseplugin.time.TimeFormatter;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;

import java.time.Duration;
import java.time.Instant;

public class PlaytimeManager {

    private final TimeFormatter timeFormatter = new TimeFormatter()
            .setDayString("hari")
            .setHourString("jam")
            .setMinuteString("menit")
            .setSecondString("detik");

    public String getFormattedPlaytime(OfflinePlayer player) {
        long playtimeTicks = player.getStatistic(Statistic.PLAY_ONE_MINUTE);
        long playtimeSeconds = playtimeTicks / 20;
        Duration duration = Duration.ofSeconds(playtimeSeconds);
        Instant durationInstant = Instant.now().plus(duration);

        return timeFormatter.formattedTime(durationInstant);
    }

    public void resetPlaytime(OfflinePlayer player) {
        if (player.isOnline()) {
            Player onlinePlayer = (Player) player;
            onlinePlayer.setStatistic(Statistic.PLAY_ONE_MINUTE, 0);
        } else {
            Bukkit.getOfflinePlayer(player.getUniqueId()).setStatistic(Statistic.PLAY_ONE_MINUTE, 0);
        }
    }

    public void setPlaytime(OfflinePlayer player, long playtimeSeconds) {
        long playtimeTicks = playtimeSeconds * 20;
        if (player.isOnline()) {
            Player onlinePlayer = (Player) player;
            onlinePlayer.setStatistic(Statistic.PLAY_ONE_MINUTE, (int) playtimeTicks);
        } else {
            Bukkit.getOfflinePlayer(player.getUniqueId()).setStatistic(Statistic.PLAY_ONE_MINUTE, (int) playtimeTicks);
        }
    }

}
