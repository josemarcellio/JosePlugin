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

package com.josemarcellio.joseplugin.chat.filter;

import com.josemarcellio.joseplugin.chat.manager.BadWordManager;

public class BadWordFilter {

    private final BadWordManager badWordManager;

    public BadWordFilter(BadWordManager badWordManager) {
        this.badWordManager = badWordManager;
    }

    public String filterBadWords(String message) {
        for (String badWord : badWordManager.getBadWords()) {
            String regex = createFlexibleRegex(badWord);
            if (message.toLowerCase().matches(".*" + regex + ".*")) {
                message = message.replaceAll("(?i)" + regex, censorWord(badWord));
            }
        }
        return message;
    }

    private String createFlexibleRegex(String word) {
        StringBuilder regex = new StringBuilder();
        for (char c : word.toCharArray()) {
            regex.append(c).append("+");
        }
        return regex.toString();
    }

    private String censorWord(String word) {
        return word.charAt(0) + "*".repeat(word.length() - 1);
    }
}
