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

package com.josemarcellio.joseplugin.chat.manager;

import java.util.HashSet;
import java.util.Set;

public class BadWordManager {

    private final Set<String> badWords;

    public BadWordManager() {
        this.badWords = new HashSet<>();
        initializeBadWords();
    }

    private void initializeBadWords() {
        badWords.add("anjing");
        badWords.add("bangsat");
        badWords.add("ngentot");
        badWords.add("fuck");
        badWords.add("nigga");
        badWords.add("nigger");
        badWords.add("kontol");
        badWords.add("memek");
        badWords.add("pepek");
        badWords.add("bajingan");
        badWords.add("sundel");
        badWords.add("vagina");
        badWords.add("penis");
        badWords.add("ngewe");
    }

    public Set<String> getBadWords() {
        return badWords;
    }
}
