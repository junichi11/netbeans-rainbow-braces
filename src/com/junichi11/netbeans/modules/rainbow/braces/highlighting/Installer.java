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
package com.junichi11.netbeans.modules.rainbow.braces.highlighting;

import com.junichi11.netbeans.modules.rainbow.braces.options.RainbowBracesOptions;
import java.util.prefs.Preferences;
import org.openide.modules.ModuleInstall;
import org.openide.util.NbPreferences;

public class Installer extends ModuleInstall {

    private static final long serialVersionUID = -181224699661964423L;

    @Override
    public void restored() {
        // add listener
        Preferences preferences = NbPreferences.forModule(RainbowBracesOptions.class);
        preferences.addPreferenceChangeListener(evt -> {
            if (evt.getKey().equals("rainbow.braces.mimetype.regex")) { // NOI18N
                RainbowBracesHighlighting.setMimeTypeRegex();
            }

            if (evt.getKey().startsWith("rainbow.braces.color.")) { // NOI18N
                RainbowBracesHighlighting.setColors();
            }
        });
    }

}
