/*
 * Copyright 2018 junichi11.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.junichi11.netbeans.modules.rainbow.braces.options;

import java.util.Arrays;
import java.util.List;
import java.util.prefs.Preferences;
import org.openide.util.NbPreferences;

/**
 *
 * @author junichi11
 */
public final class RainbowBracesOptions {

    private static final List<String> DEFAULT_COLORS1 = Arrays.asList(
            "#d03a27", // NOI18N
            "#4d9e3e", // NOI18N
            "#1881bf", // NOI18N
            "#e3317e", // NOI18N
            "#b0c02f", // NOI18N
            "#00aebb", // NOI18N
            "#e69528", // NOI18N
            "#af3480", // NOI18N
            "#f2df00" // NOI18N
    );
    private static final List<String> DEFAULT_COLORS2 = Arrays.asList(
            "#d03a27", // NOI18N
            "#af3480", // NOI18N
            "#1881bf", // NOI18N
            "#00aebb", // NOI18N
            "#4d9e3e", // NOI18N
            "#b0c02f", // NOI18N
            "#e69528", // NOI18N
            "#e3317e", // NOI18N
            "#f2df00" // NOI18N
    );
    private static final List<String> DEFAULT_COLORS3 = Arrays.asList(
            "#f19db5", // NOI18N
            "#ee858b", // NOI18N
            "#f5a33b", // NOI18N
            "#fedc5e", // NOI18N
            "#b5d681", // NOI18N
            "#6fb16b", // NOI18N
            "#86b6e2", // NOI18N
            "#6f94cd", // NOI18N
            "#ce93bf" // NOI18N
    );
    private static final String DEFAULT_MIME_TYPE_REGEX = "^text/(x-)?(java|php5|javascript|json|css|less|sass|scss)$"; // NOI18N
    private static final String MIME_TYPE_REGEX = "rainbow.braces.mimetype.regex"; // NOI18N
    private static final String ENABLED = "rainbow.braces.enabled"; // NOI18N
    private static final String PARENTHESIS = "rainbow.braces.parenthesis"; // NOI18N
    private static final String BRACKETS = "rainbow.braces.brackets"; // NOI18N
    private static final String BRACES = "rainbow.braces.braces"; // NOI18N
    private static final String COLOR = "rainbow.braces.color.%s"; // NOI18N
    private static final String COLOR_CODE = "rainbow.braces.color.code.%s"; // NOI18N
    private static final String DEFAULT_COLOR = "#000000"; // NOI18N
    private static final RainbowBracesOptions INSTANCE = new RainbowBracesOptions();

    private RainbowBracesOptions() {
    }

    public static RainbowBracesOptions getInstance() {
        return INSTANCE;
    }

    public boolean isEnabled() {
        return getPreferences().getBoolean(ENABLED, true);
    }

    public void setEnabled(boolean isEnabled) {
        getPreferences().putBoolean(ENABLED, isEnabled);
    }

    public String getMimeTypeRegex() {
        return getPreferences().get(MIME_TYPE_REGEX, DEFAULT_MIME_TYPE_REGEX);
    }

    public void setMimeTypeRegex(String regex) {
        getPreferences().put(MIME_TYPE_REGEX, regex);
    }

    public boolean isParenthesis() {
        return getPreferences().getBoolean(PARENTHESIS, true);
    }

    public void setParenthesis(boolean isParenthesis) {
        getPreferences().putBoolean(PARENTHESIS, isParenthesis);
    }

    public boolean isBrackets() {
        return getPreferences().getBoolean(BRACKETS, true);
    }

    public void setBrackets(boolean isBrackets) {
        getPreferences().putBoolean(BRACKETS, isBrackets);
    }

    public boolean isBraces() {
        return getPreferences().getBoolean(BRACES, true);
    }

    public void setBraces(boolean isBraces) {
        getPreferences().putBoolean(BRACES, isBraces);
    }

    public boolean isColorEnabled(int number) {
        return getPreferences().getBoolean(String.format(COLOR, number), true);
    }

    public void setEnabledColor(int number, boolean isEnabled) {
        getPreferences().putBoolean(String.format(COLOR, number), isEnabled);
    }

    public String getColorCode(int number) {
        return getPreferences().get(String.format(COLOR_CODE, number), getDefaultColorCode(number));
    }

    public void setColorCode(int number, String color) {
        getPreferences().put(String.format(COLOR_CODE, number), color);
    }

    public String getDefaultColorCode(int number) {
        if (number <= 0 || 10 <= number) {
            return DEFAULT_COLOR;
        }
        return DEFAULT_COLORS1.get(number - 1);
    }

    private Preferences getPreferences() {
        return NbPreferences.forModule(RainbowBracesOptions.class);
    }
}
