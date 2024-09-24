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

package com.josemarcellio.joseplugin.text;

import java.util.ArrayList;
import java.util.List;

public class TextBuilder {
    private final List<TextOperation> operations = new ArrayList<>();

    public TextBuilder addOperationIf(boolean condition, TextOperation operation) {
        if (condition) {
            operations.add(operation);
        }
        return this;
    }

    public String applyOperations(String input) {
        String result = input;
        for (TextOperation operation : operations) {
            result = operation.process(result);
        }
        operations.clear();
        return result;
    }
}