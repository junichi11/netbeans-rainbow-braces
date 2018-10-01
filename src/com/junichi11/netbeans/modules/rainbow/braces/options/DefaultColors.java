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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author junichi11
 */
public final class DefaultColors {

    static final String DEFAULT_COLOR = "#000000"; // NOI18N
    static final Map<String, List<String>> DEFAULT_COLORS = new HashMap<>();
    static final List<String> DEFAULT_COLORS1 = Arrays.asList(
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
    static final List<String> DEFAULT_COLORS2 = Arrays.asList(
            "#d02742", // NOI18N
            "#7f34af", // NOI18N
            "#1881bf", // NOI18N
            "#00aebb", // NOI18N
            "#4d9e3e", // NOI18N
            "#b0c02f", // NOI18N
            "#e69528", // NOI18N
            "#e3317e", // NOI18N
            "#f2df00" // NOI18N
    );
    static final List<String> DEFAULT_COLORS3 = Arrays.asList(
            "#e79df1", // NOI18N
            "#ee858b", // NOI18N
            "#f5a33b", // NOI18N
            "#fedc5e", // NOI18N
            "#b5d681", // NOI18N
            "#6fb16b", // NOI18N
            "#86b6e2", // NOI18N
            "#6f94cd", // NOI18N
            "#ce93bf" // NOI18N
    );
    static final List<String> PSYCHEDELIC_COLORS = Arrays.asList(
            "#b7007c", // NOI18N
            "#009b85", // NOI18N
            "#382284", // NOI18N
            "#c4c829", // NOI18N
            "#95007e", // NOI18N
            "#e2c80f", // NOI18N
            "#009dc6", // NOI18N
            "#b80e3b", // NOI18N
            "#0178bc" // NOI18N
    );

    private DefaultColors() {
    }

    static {
        DEFAULT_COLORS.put("Default 1", DEFAULT_COLORS1); // NOI18N
        DEFAULT_COLORS.put("Default 2", DEFAULT_COLORS2); // NOI18N
        DEFAULT_COLORS.put("Default 3", DEFAULT_COLORS3); // NOI18N
        DEFAULT_COLORS.put("Psychedelic", PSYCHEDELIC_COLORS); // NOI18N
    }

    static List<String> getDefaultColorNames() {
        ArrayList<String> names = new ArrayList<>(DEFAULT_COLORS.keySet());
        Collections.sort(names);
        return names;
    }

    static List<String> getDefaultColors(String name) {
        List<String> defaultColors = DEFAULT_COLORS.get(name);
        if (defaultColors == null) {
            return Collections.emptyList();
        }
        return defaultColors;
    }

}
