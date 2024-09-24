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

package com.josemarcellio.joseplugin.job.manager;

import com.josemarcellio.joseplugin.JosePlugin;

public class PlayerJobData {
    private final JosePlugin plugin;
    private final String job;
    private int level;
    private double exp;

    public PlayerJobData(JosePlugin plugin, String job, int level, double exp) {
        this.plugin = plugin;
        this.job = job;
        this.level = level;
        this.exp = exp;
    }

    public String getJob() {
        return job;
    }

    public int getLevel() {
        return level;
    }

    public double getExp() {
        return exp;
    }

    public void addExp(double amount) {
        this.exp += amount;
        while (this.exp >= getExpToNextLevel() && this.level < plugin.getJobsManager().getMaxLevel()) {
            this.exp -= getExpToNextLevel();
            this.level++;
        }
    }

    private int getExpToNextLevel() {
        return this.level * 100;
    }
}