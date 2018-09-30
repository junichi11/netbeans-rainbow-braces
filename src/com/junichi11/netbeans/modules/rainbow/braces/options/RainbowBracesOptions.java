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

import static com.junichi11.netbeans.modules.rainbow.braces.options.DefaultColors.DEFAULT_COLOR;
import static com.junichi11.netbeans.modules.rainbow.braces.options.DefaultColors.DEFAULT_COLORS;
import static com.junichi11.netbeans.modules.rainbow.braces.options.DefaultColors.DEFAULT_COLORS1;
import java.util.List;
import java.util.prefs.Preferences;
import org.openide.util.NbPreferences;

/**
 *
 * @author junichi11
 */
public final class RainbowBracesOptions {

    private static final String DEFAULT_MIME_TYPE_REGEX = "^text/(x-)?(java|php5|javascript|json|css|less|sass|scss)$"; // NOI18N
    private static final String MIME_TYPE_REGEX = "rainbow.braces.mimetype.regex"; // NOI18N
    private static final String ENABLED = "rainbow.braces.enabled"; // NOI18N
    private static final String PARENTHESIS = "rainbow.braces.parenthesis"; // NOI18N
    private static final String BRACKETS = "rainbow.braces.brackets"; // NOI18N
    private static final String BRACES = "rainbow.braces.braces"; // NOI18N
    private static final String COLOR = "rainbow.braces.color.%s"; // NOI18N
    private static final String COLOR_CODE = "rainbow.braces.color.code.%s"; // NOI18N

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

    public String getDefaultColorCode(String name, int number) {
        List<String> defaultColors = DEFAULT_COLORS.get(name);
        if (defaultColors == null || defaultColors.isEmpty()) {
            defaultColors = DEFAULT_COLORS1;
        }
        if (number <= 0 || defaultColors.size() < number) {
            return DEFAULT_COLOR;
        }
        return defaultColors.get(number - 1);
    }

    public String getDefaultColorCode(int number) {
        if (number <= 0 || DEFAULT_COLORS1.size() < number) {
            return DEFAULT_COLOR;
        }
        return DEFAULT_COLORS1.get(number - 1);
    }

    private Preferences getPreferences() {
        return NbPreferences.forModule(RainbowBracesOptions.class);
    }

}
