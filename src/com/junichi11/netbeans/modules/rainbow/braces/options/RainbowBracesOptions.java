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
    private static final String PARENTHESES = "rainbow.braces.parentheses"; // NOI18N
    private static final String BRACKETS = "rainbow.braces.brackets"; // NOI18N
    private static final String BRACES = "rainbow.braces.braces"; // NOI18N
    private static final String COLOR = "rainbow.braces.color.%s"; // NOI18N
    private static final String COLOR_CODE = "rainbow.braces.color.code.%s"; // NOI18N
    private static final String SKIP_COMMENTS = "rainbow.braces.skip.comments"; // NOI18N
    private static final String SKIP_STRINGS = "rainbow.braces.skip.strings"; // NOI18N
    private static final String MAX_LINES = "rainbow.braces.max.lines"; // NOI18N
    private static final String ONLY_VISIBLE_AREA = "rainbow.braces.only.visible.area"; // NOI18N

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

    public boolean areParenthesesEnabled() {
        return getPreferences().getBoolean(PARENTHESES, true);
    }

    public void setParenthesesEnabled(boolean isParentheses) {
        getPreferences().putBoolean(PARENTHESES, isParentheses);
    }

    public boolean areBracketsEnabled() {
        return getPreferences().getBoolean(BRACKETS, true);
    }

    public void setBracketsEnabled(boolean isBrackets) {
        getPreferences().putBoolean(BRACKETS, isBrackets);
    }

    public boolean areBracesEnabled() {
        return getPreferences().getBoolean(BRACES, true);
    }

    public void setBracesEnabled(boolean isBraces) {
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

    public boolean isCommentSkipped() {
        return getPreferences().getBoolean(SKIP_COMMENTS, true);
    }

    public void setCommentSkipped(boolean isSkipped) {
        getPreferences().putBoolean(SKIP_COMMENTS, isSkipped);
    }

    public boolean isStringSkipped() {
        return getPreferences().getBoolean(SKIP_STRINGS, true);
    }

    public void setStringSkipped(boolean isSkipped) {
        getPreferences().putBoolean(SKIP_STRINGS, isSkipped);
    }

    public int getMaxLines() {
        return getPreferences().getInt(MAX_LINES, 3000);
    }

    public void setMaxLines(int maxLines) {
        getPreferences().putInt(MAX_LINES, maxLines);
    }

    public boolean isOnlyVisibleArea() {
        return getPreferences().getBoolean(ONLY_VISIBLE_AREA, false);
    }

    public void setOnlyVisibleArea(boolean isOnlyVisibleArea) {
        getPreferences().putBoolean(ONLY_VISIBLE_AREA, isOnlyVisibleArea);
    }

    private Preferences getPreferences() {
        return NbPreferences.forModule(RainbowBracesOptions.class);
    }

}
