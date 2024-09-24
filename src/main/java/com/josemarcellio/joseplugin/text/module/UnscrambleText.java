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

package com.josemarcellio.joseplugin.text.module;

import com.josemarcellio.joseplugin.text.TextOperation;

import java.util.Arrays;

public class UnscrambleText implements TextOperation {

    @Override
    public String process(String input) {
        char[] chars = input.replaceAll(" ", "").toCharArray();
        Arrays.sort(chars);

        StringBuilder result = new StringBuilder();
        int charIndex = 0;

        for (char c : input.toCharArray()) {
            if (c == ' ') {
                result.append(' ');
            } else {
                result.append(chars[charIndex++]);
            }
        }

        return result.toString();
    }
}
