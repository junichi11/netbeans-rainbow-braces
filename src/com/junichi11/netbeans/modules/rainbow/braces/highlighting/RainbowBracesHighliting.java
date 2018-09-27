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
import java.awt.Color;
import java.util.ArrayList;
import java.util.regex.Pattern;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.Document;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import org.netbeans.api.lexer.Token;
import org.netbeans.api.lexer.TokenHierarchy;
import org.netbeans.api.lexer.TokenId;
import org.netbeans.api.lexer.TokenSequence;
import org.netbeans.lib.editor.util.swing.DocumentUtilities;
import org.netbeans.spi.editor.highlighting.HighlightsSequence;
import org.netbeans.spi.editor.highlighting.support.AbstractHighlightsContainer;

/**
 *
 * @author junichi11
 */
public class RainbowBracesHighliting extends AbstractHighlightsContainer {

    private enum State {
        ParenthesisOpen,
        ParenthesisClose,
        BracketOpen,
        BracketClose,
        BraceOpen,
        BraceClose,
        None;

        public boolean isOpen() {
            return this == ParenthesisOpen
                    || this == BracketOpen
                    || this == BraceOpen;
        }

        public static State valueOfChar(char c) {
            switch (c) {
                case '{':
                    return BraceOpen;
                case '}':
                    return BraceClose;
                case '(':
                    return ParenthesisOpen;
                case ')':
                    return ParenthesisClose;
                case '[':
                    return BracketOpen;
                case ']':
                    return BracketClose;
                default:
                    return None;
            }
        }
    }

    public static final String LAYER_TYPE_ID = "com.junichi11.modules.rainbow.braces.highlighting.RainbowBracesHighlighting"; // NOI18N
    private static Pattern MIME_TYPE_PATTERN;
    private static AttributeSet[] ATTRIBUTE_SETS;
    private final Document document;
    private final CharSequence documentText;
    private final String mimeType;

    static {
        setColors();
        setMimeTypeRegex();
    }

    RainbowBracesHighliting(Document document) {
        this.document = document;
        this.documentText = DocumentUtilities.getText(document);
        this.mimeType = DocumentUtilities.getMimeType(document);
    }

    static void setMimeTypeRegex() {
        MIME_TYPE_PATTERN = Pattern.compile(RainbowBracesOptions.getInstance().getMimeTypeRegex());
    }

    static void setColors() {
        RainbowBracesOptions options = RainbowBracesOptions.getInstance();
        ArrayList<AttributeSet> attSets = new ArrayList<>(9);
        for (int i = 1; i < 10; i++) {
            if (options.isColorEnabled(i)) {
                SimpleAttributeSet simpleAttributeSet = new SimpleAttributeSet();
                StyleConstants.setForeground(simpleAttributeSet, Color.decode(options.getColorCode(i)));
                attSets.add(simpleAttributeSet);
            }
        }
        ATTRIBUTE_SETS = attSets.toArray(new AttributeSet[attSets.size()]);
    }

    @Override
    public HighlightsSequence getHighlights(int startOffset, int endOffset) {
        if (!RainbowBracesOptions.getInstance().isEnabled()
                || !MIME_TYPE_PATTERN.matcher(mimeType).matches()) {
            return HighlightsSequence.EMPTY;
        }
        return new HighlightsSequenceImpl(startOffset, endOffset);
    }

    private class HighlightsSequenceImpl implements HighlightsSequence {

        private final int startOffset;
        private final int endOffset;
        private final TokenSequence<? extends TokenId> ts;
        private int highlightsStartOffset;
        private int highlightsEndOffset;
        private int bracesBalance = 0;
        private int parenthesisBalance = 0;
        private int bracketsBalance = 0;
        private State state = State.None;

        private HighlightsSequenceImpl(int startOffset, int endOffset) {
            this.startOffset = startOffset;
            this.endOffset = endOffset;
            this.highlightsStartOffset = 0;
            this.highlightsEndOffset = 0;
            this.ts = getTokenSequence(document);
        }

        @Override
        public boolean moveNext() {
            if (highlightsEndOffset >= endOffset) {
                return false;
            }
            for (int i = highlightsEndOffset; i < endOffset; i++) {
                char c = documentText.charAt(i);
                // skip string or comment
                int skipedPosition = skipToken(i);
                if (skipedPosition != -1) {
                    i = skipedPosition;
                    continue;
                }
                state = State.valueOfChar(c);
                if (!isEnabledState()) {
                    continue;
                }
                setBalance(state);
                setHighlightsOffsets(state, i);
                if (state != State.None
                        && startOffset <= highlightsStartOffset) {
                    return true;
                }
            }
            return false;
        }

        private boolean isEnabledState() {
            switch (state) {
                case ParenthesisOpen:
                case ParenthesisClose:
                    return RainbowBracesOptions.getInstance().isParenthesis();
                case BracketOpen:
                case BracketClose:
                    return RainbowBracesOptions.getInstance().isBrackets();
                case BraceOpen:
                case BraceClose:
                    return RainbowBracesOptions.getInstance().isBraces();
                default:
                    return true;
            }
        }

        private int skipToken(int offset) {
            if (ts != null) {
                ts.move(offset);
                if (ts.moveNext()) {
                    Token<? extends TokenId> token = ts.token();
                    if (token != null) {
                        String primaryCategory = token.id().primaryCategory();
                        if ("string".equals(primaryCategory) // NOI18N
                                || "character".equals(primaryCategory) // NOI18N
                                || "commentline".equals(primaryCategory) // NOI18N
                                || "comment".equals(primaryCategory)) { // NOI18N
                            return ts.offset() + token.length() - 1;
                        }
                    }
                }
            }
            return -1;
        }

        @Override
        public int getStartOffset() {
            return highlightsStartOffset;
        }

        @Override
        public int getEndOffset() {
            return highlightsEndOffset;
        }

        @Override
        public AttributeSet getAttributes() {
            assert state != State.None;
            int position = getBalance(state);
            if (state.isOpen()) {
                position--;
            }
            if (position < 0) {
                return ATTRIBUTE_SETS[0];
            }
            return ATTRIBUTE_SETS[position % ATTRIBUTE_SETS.length];
        }

        private void setHighlightsOffsets(State state, int startOffset) {
            if (state != State.None) {
                highlightsStartOffset = startOffset;
                highlightsEndOffset = startOffset + 1;
            }
        }

        private int getBalance(State state) {
            switch (state) {
                case BraceOpen: // no break
                case BraceClose:
                    return bracesBalance;
                case BracketOpen: // no break
                case BracketClose:
                    return bracketsBalance;
                case ParenthesisOpen: // no braek
                case ParenthesisClose:
                    return parenthesisBalance;
                default:
                    return 0;
            }
        }

        private void setBalance(State state) {
            switch (state) {
                case BraceOpen:
                    bracesBalance++;
                    break;
                case BraceClose:
                    bracesBalance--;
                    break;
                case BracketOpen:
                    bracketsBalance++;
                    break;
                case BracketClose:
                    bracketsBalance--;
                    break;
                case ParenthesisOpen:
                    parenthesisBalance++;
                    break;
                case ParenthesisClose:
                    parenthesisBalance--;
                    break;
                default:
                    break;
            }
        }

    }

    private TokenSequence<? extends TokenId> getTokenSequence(Document document) {
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

}
