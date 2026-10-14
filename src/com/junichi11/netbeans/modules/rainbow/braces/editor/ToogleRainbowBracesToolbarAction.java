/*
 * Copyright 2026 Chris2011.
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
package com.junichi11.netbeans.modules.rainbow.braces.editor;

import com.junichi11.netbeans.modules.rainbow.braces.highlighting.RainbowBracesHighlighting;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JToggleButton;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;
import org.netbeans.api.editor.EditorRegistry;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.util.ContextAwareAction;
import org.openide.util.ImageUtilities;
import org.openide.util.Lookup;
import org.openide.util.NbBundle.Messages;
import org.openide.util.WeakListeners;
import org.openide.util.actions.Presenter;

/**
 * Editor toolbar action to toggle rainbow braces for the currently open file.
 *
 * @author Chris2011
 */
@ActionID(
    category = "Editor",
    id = "com.junichi11.netbeans.modules.rainbow.braces.editor.ToggleRainbowBracesToolbarAction"
)
@ActionRegistration(
    displayName = "#CTL_ToggleRainbowBracesToolbarAction",
    lazy = false
)
@ActionReferences({
    @ActionReference(
        path = "Editors/Toolbars/Default",
        position = 20000,
        separatorBefore = 19999
    )
})
@Messages("CTL_ToggleRainbowBracesToolbarAction=Toggle Rainbow Braces")
public class ToogleRainbowBracesToolbarAction extends AbstractAction
        implements ContextAwareAction, Presenter.Toolbar {

    private static final String ICON_PATH =
            "com/junichi11/netbeans/modules/rainbow/braces/resources/toggleIcon.svg"; // NOI18N

    public ToogleRainbowBracesToolbarAction() {
        putValue(Action.SHORT_DESCRIPTION, Bundle.CTL_ToggleRainbowBracesToolbarAction());
        putValue(Action.SMALL_ICON, ImageUtilities.loadImageIcon(ICON_PATH, false));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JTextComponent editor = EditorRegistry.lastFocusedComponent();
        if (editor != null) {
            toggleDocument(editor.getDocument());
        }
    }

    @Override
    public Action createContextAwareInstance(Lookup actionContext) {
        return this;
    }

    @Override
    public Component getToolbarPresenter() {
        return new ToggleButton(this);
    }

    static boolean isDocumentDisabled(Document doc) {
        return Boolean.TRUE.equals(doc.getProperty(RainbowBracesHighlighting.DOCUMENT_DISABLED_KEY));
    }

    static void toggleDocument(Document doc) {
        boolean currentlyDisabled = isDocumentDisabled(doc);
        doc.putProperty(RainbowBracesHighlighting.DOCUMENT_DISABLED_KEY, !currentlyDisabled ? Boolean.TRUE : null);
        RainbowBracesHighlighting.repaintDocument(doc);
    }

    // --- Toolbar button -------------------------------------------------------

    private static final class ToggleButton extends JToggleButton
            implements PropertyChangeListener {

        private final PropertyChangeListener weakEditorListener;

        ToggleButton(ToogleRainbowBracesToolbarAction action) {
            setFocusable(false);
            setToolTipText(Bundle.CTL_ToggleRainbowBracesToolbarAction());
            setIcon(ImageUtilities.loadImageIcon(ICON_PATH, false));
            addActionListener(e -> performToggle());

            weakEditorListener = WeakListeners.propertyChange(this, EditorRegistry.class);
            EditorRegistry.addPropertyChangeListener(weakEditorListener);

            updateState();
        }

        private void performToggle() {
            JTextComponent editor = EditorRegistry.lastFocusedComponent();
            if (editor != null) {
                toggleDocument(editor.getDocument());
            }
            updateState();
        }

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            updateState();
        }

        private void updateState() {
            JTextComponent editor = EditorRegistry.lastFocusedComponent();
            if (editor != null) {
                setSelected(!isDocumentDisabled(editor.getDocument()));
                setEnabled(true);
            } else {
                setSelected(false);
                setEnabled(false);
            }
        }
    }

}
