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
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.regex.Pattern;
import javax.swing.text.AttributeSet;
import javax.swing.text.Document;
import org.netbeans.api.lexer.Token;
import org.netbeans.api.lexer.TokenId;
import org.netbeans.api.lexer.TokenSequence;
import org.netbeans.lib.editor.util.swing.DocumentUtilities;
import org.netbeans.spi.editor.highlighting.HighlightsSequence;
import org.netbeans.spi.editor.highlighting.support.AbstractHighlightsContainer;

/**
 *
 * @author junichi11
 */
public class RainbowBracesHighlighting extends AbstractHighlightsContainer {

    public static final String LAYER_TYPE_ID = "com.junichi11.modules.rainbow.braces.highlighting.RainbowBracesHighlighting"; // NOI18N
    private static volatile Pattern MIME_TYPE_PATTERN;
    private static volatile int MAX_LINES;
    private static volatile boolean IS_ENABLED;
    private static volatile boolean IS_ONLY_VISIBLE_AREA;
    private final Document document;
    private final String mimeType;

    static {
        setEnabled();
        setMimeTypeRegex();
        setMaxLines();
        setOnlyVisibleArea();
    }

    RainbowBracesHighlighting(Document document) {
        this.document = document;
        this.mimeType = DocumentUtilities.getMimeType(document);
    }

    static void setEnabled() {
        IS_ENABLED = RainbowBracesOptions.getInstance().isEnabled();
    }

    static void setOnlyVisibleArea() {
        IS_ONLY_VISIBLE_AREA = RainbowBracesOptions.getInstance().isOnlyVisibleArea();
    }

    static void setMimeTypeRegex() {
        MIME_TYPE_PATTERN = Pattern.compile(RainbowBracesOptions.getInstance().getMimeTypeRegex());
    }

    static void setMaxLines() {
        MAX_LINES = RainbowBracesOptions.getInstance().getMaxLines();
    }

    @Override
    public HighlightsSequence getHighlights(int startOffset, int endOffset) {
        if (!IS_ENABLED
                || !MIME_TYPE_PATTERN.matcher(mimeType).matches()) {
            return HighlightsSequence.EMPTY;
        }
        int lineNumber = document.getDefaultRootElement().getElementIndex(document.getLength()) + 1;
        if (lineNumber > MAX_LINES) {
            return HighlightsSequence.EMPTY;
        }
        return BracesHighlightsSequenceFactory.create(startOffset, endOffset, document);
    }

    private static final class BracesHighlightsSequenceFactory {

        private BracesHighlightsSequenceFactory() {
        }

        public static final HighlightsSequence create(int startOffset, int endOffset, Document document) {
            if (IS_ONLY_VISIBLE_AREA) {
                return new HighlightsSequenceForward(startOffset, endOffset, document, true);
            }
            int endDelta = document.getLength() - endOffset;
            if (startOffset <= endDelta) {
                return new HighlightsSequenceForward(startOffset, endOffset, document, false);
            }
            HighlightsSequenceBackward highlightsSequenceBackward = new HighlightsSequenceBackward(startOffset, endOffset, document);
            highlightsSequenceBackward.parse();
            return highlightsSequenceBackward;
        }
    }

    static class HighlightsSequenceForward implements HighlightsSequence {

        private final int startOffset;
        private final int endOffset;
        private final CharSequence documentText;
        private final TokenSequence<? extends TokenId> ts;
        private int highlightsStartOffset;
        private int highlightsEndOffset;
        private int bracesBalance = 0;
        private int parenthesisBalance = 0;
        private int bracketsBalance = 0;
        private BracesState state = BracesState.None;

        private HighlightsSequenceForward(int startOffset, int endOffset, Document document, boolean isOnlyVisibleArea) {
            this.startOffset = startOffset;
            this.endOffset = endOffset;
            this.highlightsStartOffset = isOnlyVisibleArea ? startOffset : 0;
            this.highlightsEndOffset =  isOnlyVisibleArea ? startOffset : 0;
            this.documentText = DocumentUtilities.getText(document);
            this.ts = HighlightingUtils.getTokenSequence(document);
        }

        @Override
        public boolean moveNext() {
            if (highlightsEndOffset >= endOffset) {
                return false;
            }
            for (int i = highlightsEndOffset; i < endOffset; i++) {
                char c = documentText.charAt(i);
                // skip string and comment
                int skipedPosition = skipToken(i);
                if (skipedPosition != -1) {
                    i = skipedPosition;
                    continue;
                }
                state = BracesState.valueOfChar(c);
                if (!HighlightingUtils.isEnabledState(state)) {
                    continue;
                }
                setBalance(state);
                setHighlightsOffsets(state, i);
                if (state != BracesState.None
                        && startOffset <= highlightsStartOffset) {
                    return true;
                }
            }
            return false;
        }

        private int skipToken(int offset) {
            if (ts != null) {
                ts.move(offset);
                if (ts.moveNext()) {
                    Token<? extends TokenId> token = ts.token();
                    if (token != null) {
                        String primaryCategory = token.id().primaryCategory();
                        if (HighlightingUtils.isCategorySkipped(primaryCategory)) {
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
            assert state != BracesState.None;
            int position = getBalance(state);
            if (state.isOpen()) {
                position--;
            }
            if (position < 0) {
                position = Math.abs(position);
            }
            return HighlightingUtils.ATTRIBUTE_SETS[position % HighlightingUtils.ATTRIBUTE_SETS.length];
        }

        private void setHighlightsOffsets(BracesState state, int startOffset) {
            if (state != BracesState.None) {
                highlightsStartOffset = startOffset;
                highlightsEndOffset = startOffset + 1;
            }
        }

        private int getBalance(BracesState state) {
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

        private void setBalance(BracesState state) {
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

    static class HighlightsSequenceBackward implements HighlightsSequence {

        private final int startOffset;
        private final int endOffset;
        private final CharSequence documentText;
        private final TokenSequence<? extends TokenId> ts;
        private int highlightsStartOffset;
        private int highlightsEndOffset;
        private int bracesBalance = 0;
        private int parenthesisBalance = 0;
        private int bracketsBalance = 0;
        private BracesState state = BracesState.None;
        private final Deque<AttributeOffset> stack = new ArrayDeque<>();
        private AttributeSet attributeSet;

        private HighlightsSequenceBackward(int startOffset, int endOffset, Document document) {
            this.startOffset = startOffset;
            this.endOffset = endOffset;
            this.documentText = DocumentUtilities.getText(document);
            this.ts = HighlightingUtils.getTokenSequence(document);
        }

        void parse() {
            int length = documentText.length();
            for (int i = --length; i >= startOffset; i--) {
                char c = documentText.charAt(i);
                // skip string and comment
                int skipedPosition = skipToken(i);
                if (skipedPosition != -1) {
                    // #11 avoid an infinite loop
                    // Javadoc /** */ continue to return the same position with "*"
                    if (skipedPosition < i) {
                        i = skipedPosition;
                    }
                    continue;
                }
                state = BracesState.valueOfChar(c);
                if (!HighlightingUtils.isEnabledState(state)) {
                    continue;
                }
                setBalance(state);
                if (state != BracesState.None
                        && endOffset >= i + 1) {
                    stack.addFirst(new AttributeOffset(i, i + 1, getAttributeSet()));
                }
            }
        }

        @Override
        public boolean moveNext() {
            if (stack.isEmpty()) {
                return false;
            }
            AttributeOffset attributeOffset = stack.pop();
            highlightsStartOffset = attributeOffset.getStartOffset();
            highlightsEndOffset = attributeOffset.getEndOffset();
            attributeSet = attributeOffset.getAttribute();
            assert attributeSet != null;
            return true;
        }

        private int skipToken(int offset) {
            if (ts != null) {
                ts.move(offset);
                if (ts.moveNext()) {
                    Token<? extends TokenId> token = ts.token();
                    if (token != null) {
                        String primaryCategory = token.id().primaryCategory();
                        if (HighlightingUtils.isCategorySkipped(primaryCategory)) {
                            return ts.offset();
                        }
                    }
                }
            }
            return -1;
        }

        private AttributeSet getAttributeSet() {
            assert state != BracesState.None;
            int position = getBalance(state);
            if (state.isClose()) {
                position--;
            }
            if (position < 0) {
                return HighlightingUtils.ATTRIBUTE_SETS[0];
            }
            return HighlightingUtils.ATTRIBUTE_SETS[position % HighlightingUtils.ATTRIBUTE_SETS.length];
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
            return attributeSet;
        }

        private int getBalance(BracesState state) {
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

        private void setBalance(BracesState state) {
            switch (state) {
                case BraceOpen:
                    bracesBalance--;
                    break;
                case BraceClose:
                    bracesBalance++;
                    break;
                case BracketOpen:
                    bracketsBalance--;
                    break;
                case BracketClose:
                    bracketsBalance++;
                    break;
                case ParenthesisOpen:
                    parenthesisBalance--;
                    break;
                case ParenthesisClose:
                    parenthesisBalance++;
                    break;
                default:
                    break;
            }
        }

    }

}
