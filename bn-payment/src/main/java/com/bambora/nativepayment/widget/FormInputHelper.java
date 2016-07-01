/*
 * Copyright (c) 2016 Bambora ( http://bambora.com/ )
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.bambora.nativepayment.widget;

/**
 * TODO
 */
public class FormInputHelper {

    public static String clearNonDigits(String input) {
        return input.replaceAll("[^\\d]", "");
    }

    public static String getDeletedChars(CharSequence sequence, int startBefore, int countBefore) {
        StringBuilder deletingChars = new StringBuilder();
        for (int i = startBefore; i < startBefore + countBefore; i++) {
            deletingChars.append(sequence.charAt(i));
        }
        return deletingChars.toString();
    }

    public static CharSequence formatNumberSequence(String sequence, int groupSize, char groupDelimiter) {
        sequence = clearNonDigits(sequence);
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < sequence.length() + 1; i++) {
            if (shouldBeDelimiter(i, groupSize)) {
                stringBuilder.append(String.valueOf(groupDelimiter));
            }
            if (i < sequence.length()) {
                stringBuilder.append(sequence.charAt(i));
            }
        }
        return stringBuilder;
    }

    private static boolean shouldBeDelimiter(int index, int groupSize) {
        return index > 0 && index % groupSize == 0;
    }
}
