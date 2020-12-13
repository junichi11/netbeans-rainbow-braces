/*
 * Copyright 2019 junichi11.
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
import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.Document;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import org.netbeans.api.annotations.common.CheckForNull;
import org.netbeans.api.lexer.TokenHierarchy;
import org.netbeans.api.lexer.TokenId;
import org.netbeans.api.lexer.TokenSequence;

/**
 *
 * @author junichi11
 */
public final class HighlightingUtils {

    public static AttributeSet[] ATTRIBUTE_SETS;
    private static final List<String> SKIP_DEFAULT_CATEGORIES = Arrays.asList(
            "character", // NOI18N
            "whitespace", // NOI18N
            "identifier", // NOI18N
            "keyword" // NOI18N
    );
    private static final List<String> SKIP_COMMENT_CATEGORIES = Arrays.asList(
            "commentline", // NOI18N
            "comment" // NOI18N
    );
    private static final List<String> SKIP_STRING_CATEGORIES = Arrays.asList(
            "string" // NOI18N
    );
    private static final int MAX_COLOR_SIZE = 9;
    private static volatile boolean areBracesEnabled;
    private static volatile boolean areBracketsEnabled;
    private static volatile boolean areParenthesesEnabled;
    private static volatile boolean skipComments;
    private static volatile boolean skipString;

    static {
        setColors();
        setBracesEnabled();
        setBracketsEnabled();
        setParenthesesEnabled();
        setSkipComments();
        setSkipString();
    }

    private HighlightingUtils() {
    }

    static void setBracesEnabled() {
        areBracesEnabled = RainbowBracesOptions.getInstance().areBracesEnabled();
    }

    static void setBracketsEnabled() {
        areBracketsEnabled = RainbowBracesOptions.getInstance().areBracketsEnabled();
    }

    static void setParenthesesEnabled() {
        areParenthesesEnabled = RainbowBracesOptions.getInstance().areParenthesesEnabled();
    }

    static void setSkipComments() {
        skipComments = RainbowBracesOptions.getInstance().isCommentSkipped();
    }

    static void setSkipString() {
        skipString = RainbowBracesOptions.getInstance().isStringSkipped();
    }

    static void setColors() {
        RainbowBracesOptions options = RainbowBracesOptions.getInstance();
        ArrayList<AttributeSet> attSets = new ArrayList<>(MAX_COLOR_SIZE);
        for (int i = 1; i <= MAX_COLOR_SIZE; i++) {
            if (options.isColorEnabled(i)) {
                SimpleAttributeSet simpleAttributeSet = new SimpleAttributeSet();
                StyleConstants.setForeground(simpleAttributeSet, Color.decode(options.getColorCode(i)));
                attSets.add(simpleAttributeSet);
            }
        }
        ATTRIBUTE_SETS = attSets.toArray(new AttributeSet[attSets.size()]);
    }

    @CheckForNull
    public static TokenSequence<? extends TokenId> getTokenSequence(Document document) {
        if (!(document instanceof AbstractDocument)) {
            return null;
        }
        AbstractDocument ad = (AbstractDocument) document;
        ad.readLock();
        TokenSequence<? extends TokenId> tokenSequence;
        try {
            TokenHierarchy<Document> th = TokenHierarchy.get(document);
            if (th == null) {
                return null;
            }
            tokenSequence = th.tokenSequence();
        } finally {
            ad.readUnlock();
        }
        if (tokenSequence == null) {
            return null;
        }
        tokenSequence.move(0);
        if (!tokenSequence.moveNext()) {
            return null;
        }

        while (tokenSequence.embedded() != null) {
            tokenSequence = tokenSequence.embedded();
            tokenSequence.move(0);
            tokenSequence.moveNext();
        }
        return tokenSequence;
    }

    public static boolean isCategorySkipped(String primaryCategory) {
        return isSkippedDefaultCategory(primaryCategory)
                || isCommentSkipped(primaryCategory)
                || isStringSkipped(primaryCategory);
    }

    private static boolean isSkippedDefaultCategory(String primaryCategory) {
        return SKIP_DEFAULT_CATEGORIES.contains(primaryCategory);
    }

    private static boolean isCommentSkipped(String primaryCategory) {
        return skipComments
                && SKIP_COMMENT_CATEGORIES.contains(primaryCategory);
    }

    private static boolean isStringSkipped(String primaryCategory) {
        return skipString
                && SKIP_STRING_CATEGORIES.contains(primaryCategory);
    }

    static boolean isEnabledState(BracesState state) {
        switch (state) {
            case ParenthesisOpen: // no break
            case ParenthesisClose:
                return areParenthesesEnabled;
            case BracketOpen: // no break
            case BracketClose:
                return areBracketsEnabled;
            case BraceOpen: // no break
            case BraceClose:
                return areBracesEnabled;
            default:
                return true;
        }
    }
}
