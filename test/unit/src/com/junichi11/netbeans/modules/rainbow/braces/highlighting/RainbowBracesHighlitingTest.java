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
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Document;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.netbeans.api.java.lexer.JavaTokenId;
import org.netbeans.api.lexer.Language;
import org.netbeans.junit.NbTestCase;
import org.netbeans.spi.editor.highlighting.HighlightsSequence;

/**
 *
 * @author junichi11
 */
public class RainbowBracesHighlitingTest extends NbTestCase {

    private RainbowBracesHighlighting rainbowBracesHighlighting;
    private Document doc;

    public RainbowBracesHighlitingTest(String name) {
        super(name);
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    @Override
    public void setUp() throws Exception {
        super.setUp();
        doc = new DefaultStyledDocument();
        doc.putProperty(Language.class, JavaTokenId.language());
        doc.putProperty("mimeType", "text/java");
        rainbowBracesHighlighting = new RainbowBracesHighlighting(doc);
        RainbowBracesOptions.getInstance().setStringSkipped(true);
        RainbowBracesOptions.getInstance().setCommentSkipped(true);
    }

    @After
    @Override
    public void tearDown() {
    }

    @Test
    public void testHighlights() throws BadLocationException {
        String contents = ""
                + "public class Test {\n" // 20
                + "    public void test() {\n" // 25
                + "        new String[]{};\n" // 24
                + "    }\n" // 6
                + "}\n";
        doc.insertString(0, contents, null);
        HighlightsSequence highlightsSequence = rainbowBracesHighlighting.getHighlights(0, doc.getLength());
        assertTrue(highlightsSequence instanceof RainbowBracesHighlighting.HighlightsSequenceForward);
        assertTrue(highlightsSequence.moveNext());
        assertEquals(highlightsSequence.getStartOffset(), 18);
        assertEquals(highlightsSequence.getEndOffset(), 19);
        AttributeSet attributesBrace1 = highlightsSequence.getAttributes();

        assertTrue(highlightsSequence.moveNext());
        assertEquals(highlightsSequence.getStartOffset(), 40);
        assertEquals(highlightsSequence.getEndOffset(), 41);
        AttributeSet attributesParenthesis1 = highlightsSequence.getAttributes();

        assertTrue(highlightsSequence.moveNext());
        assertEquals(highlightsSequence.getStartOffset(), 41);
        assertEquals(highlightsSequence.getEndOffset(), 42);
        assertEquals(attributesParenthesis1, highlightsSequence.getAttributes());

        assertTrue(highlightsSequence.moveNext());
        assertEquals(highlightsSequence.getStartOffset(), 43);
        assertEquals(highlightsSequence.getEndOffset(), 44);
        AttributeSet attributesBrace2 = highlightsSequence.getAttributes();

        assertTrue(highlightsSequence.moveNext());
        assertEquals(highlightsSequence.getStartOffset(), 63);
        assertEquals(highlightsSequence.getEndOffset(), 64);
        AttributeSet attributesBracket1 = highlightsSequence.getAttributes();

        assertTrue(highlightsSequence.moveNext());
        assertEquals(highlightsSequence.getStartOffset(), 64);
        assertEquals(highlightsSequence.getEndOffset(), 65);
        assertEquals(attributesBracket1, highlightsSequence.getAttributes());

        assertTrue(highlightsSequence.moveNext());
        assertEquals(highlightsSequence.getStartOffset(), 65);
        assertEquals(highlightsSequence.getEndOffset(), 66);
        AttributeSet attributesBrace3 = highlightsSequence.getAttributes();

        assertTrue(highlightsSequence.moveNext());
        assertEquals(highlightsSequence.getStartOffset(), 66);
        assertEquals(highlightsSequence.getEndOffset(), 67);
        assertEquals(attributesBrace3, highlightsSequence.getAttributes());

        assertTrue(highlightsSequence.moveNext());
        assertEquals(highlightsSequence.getStartOffset(), 73);
        assertEquals(highlightsSequence.getEndOffset(), 74);
        assertEquals(attributesBrace2, highlightsSequence.getAttributes());

        assertTrue(highlightsSequence.moveNext());
        assertEquals(highlightsSequence.getStartOffset(), 75);
        assertEquals(highlightsSequence.getEndOffset(), 76);
        assertEquals(attributesBrace1, highlightsSequence.getAttributes());
    }

    @Test
    public void testHighlightsBackward() throws BadLocationException {
        String contents = ""
                + "public class Test {\n" // 20
                + "    public void test() {\n" // 25
                + "        new String[]{};\n" // 24
                + "    }\n" // 6
                + "}\n";
        doc.insertString(0, contents, null);
        HighlightsSequence highlightsSequence = rainbowBracesHighlighting.getHighlights(5, doc.getLength());
        assertTrue(highlightsSequence instanceof RainbowBracesHighlighting.HighlightsSequenceBackward);
        assertTrue(highlightsSequence.moveNext());
        assertEquals(highlightsSequence.getStartOffset(), 18);
        assertEquals(highlightsSequence.getEndOffset(), 19);
        AttributeSet attributesBrace1 = highlightsSequence.getAttributes();

        assertTrue(highlightsSequence.moveNext());
        assertEquals(highlightsSequence.getStartOffset(), 40);
        assertEquals(highlightsSequence.getEndOffset(), 41);
        AttributeSet attributesParenthesis1 = highlightsSequence.getAttributes();

        assertTrue(highlightsSequence.moveNext());
        assertEquals(highlightsSequence.getStartOffset(), 41);
        assertEquals(highlightsSequence.getEndOffset(), 42);
        assertEquals(attributesParenthesis1, highlightsSequence.getAttributes());

        assertTrue(highlightsSequence.moveNext());
        assertEquals(highlightsSequence.getStartOffset(), 43);
        assertEquals(highlightsSequence.getEndOffset(), 44);
        AttributeSet attributesBrace2 = highlightsSequence.getAttributes();

        assertTrue(highlightsSequence.moveNext());
        assertEquals(highlightsSequence.getStartOffset(), 63);
        assertEquals(highlightsSequence.getEndOffset(), 64);
        AttributeSet attributesBracket1 = highlightsSequence.getAttributes();

        assertTrue(highlightsSequence.moveNext());
        assertEquals(highlightsSequence.getStartOffset(), 64);
        assertEquals(highlightsSequence.getEndOffset(), 65);
        assertEquals(attributesBracket1, highlightsSequence.getAttributes());

        assertTrue(highlightsSequence.moveNext());
        assertEquals(highlightsSequence.getStartOffset(), 65);
        assertEquals(highlightsSequence.getEndOffset(), 66);
        AttributeSet attributesBrace3 = highlightsSequence.getAttributes();

        assertTrue(highlightsSequence.moveNext());
        assertEquals(highlightsSequence.getStartOffset(), 66);
        assertEquals(highlightsSequence.getEndOffset(), 67);
        assertEquals(attributesBrace3, highlightsSequence.getAttributes());

        assertTrue(highlightsSequence.moveNext());
        assertEquals(highlightsSequence.getStartOffset(), 73);
        assertEquals(highlightsSequence.getEndOffset(), 74);
        assertEquals(attributesBrace2, highlightsSequence.getAttributes());

        assertTrue(highlightsSequence.moveNext());
        assertEquals(highlightsSequence.getStartOffset(), 75);
        assertEquals(highlightsSequence.getEndOffset(), 76);
        assertEquals(attributesBrace1, highlightsSequence.getAttributes());
    }

    @Test
    public void testHighlightsSkipComment() throws BadLocationException {
        // ignore comment
        String contents = ""
                + "public class Test {\n" // 20
                + "    // {}\n" // 10
                + "    /* () [] */\n" // 16
                + "    public void test() {\n" // 25
                + "        new String[]{};\n" // 24
                + "    }\n" // 6
                + "}\n";
        doc.insertString(0, contents, null);
        HighlightsSequence highlightsSequence = rainbowBracesHighlighting.getHighlights(0, doc.getLength());
        assertTrue(highlightsSequence instanceof RainbowBracesHighlighting.HighlightsSequenceForward);
        assertTrue(highlightsSequence.moveNext());
        assertTrue(highlightsSequence.moveNext());
        assertEquals(highlightsSequence.getStartOffset(), 20 + 10 + 16 + 20);
        assertEquals(highlightsSequence.getEndOffset(), 20 + 10 + 16 + 21);
    }

    @Test
    public void testHighlightsBackwardSkipComment() throws BadLocationException {
        // ignore comment
        String contents = ""
                + "public class Test {\n" // 20
                + "    // {}\n" // 10
                + "    /* () [] */\n" // 16
                + "    public void test() {\n" // 25
                + "        new String[]{};\n" // 24
                + "    }\n" // 6
                + "}\n";
        doc.insertString(0, contents, null);
        HighlightsSequence highlightsSequence = rainbowBracesHighlighting.getHighlights(5, doc.getLength());
        assertTrue(highlightsSequence instanceof RainbowBracesHighlighting.HighlightsSequenceBackward);
        assertTrue(highlightsSequence.moveNext());
        assertTrue(highlightsSequence.moveNext());
        assertEquals(highlightsSequence.getStartOffset(), 20 + 10 + 16 + 20);
        assertEquals(highlightsSequence.getEndOffset(), 20 + 10 + 16 + 21);
    }

    @Test
    public void testHighlightsBackwardSkipJavadoc() throws BadLocationException {
        // ignore comment
        String contents = ""
                + "/**\n" // 4
                + " * () []. \n" // 11
                + " */\n" // 4
                + "package test;\n" // 14
                + "public class Test {\n" // 20
                + "    public void test() {\n" // 25
                + "        new String[]{};\n" // 24
                + "    }\n" // 6
                + "}\n";
        doc.insertString(0, contents, null);
        HighlightsSequence highlightsSequence = rainbowBracesHighlighting.getHighlights(5, doc.getLength());
        assertTrue(highlightsSequence instanceof RainbowBracesHighlighting.HighlightsSequenceBackward);
        assertTrue(highlightsSequence.moveNext());
        assertTrue(highlightsSequence.moveNext());
        assertEquals(highlightsSequence.getStartOffset(), 4 + 11 + 4 + 14 + 20 + 20);
        assertEquals(highlightsSequence.getEndOffset(), 4 + 11 + 4 + 14 + 20 + 21);
    }

    @Test
    public void testHighlightsSkipString() throws BadLocationException {
        // ignore string
        String contents = ""
                + "public class Test {\n" // 20
                + "    public void test() {\n" //25
                + "        String string = \"{} () []\"\n" // 35
                + "    }\n" // 6
                + "}\n";
        doc.insertString(0, contents, null);
        HighlightsSequence highlightsSequence = rainbowBracesHighlighting.getHighlights(0, doc.getLength());
        assertTrue(highlightsSequence instanceof RainbowBracesHighlighting.HighlightsSequenceForward);
        assertTrue(highlightsSequence.moveNext());
        assertTrue(highlightsSequence.moveNext());
        assertTrue(highlightsSequence.moveNext());
        assertTrue(highlightsSequence.moveNext());
        assertTrue(highlightsSequence.moveNext());
        assertEquals(highlightsSequence.getStartOffset(), 20 + 25 + 35 + 4);
        assertEquals(highlightsSequence.getEndOffset(), 20 + 25 + 35 + 5);
    }

    @Test
    public void testHighlightsBackwardSkipString() throws BadLocationException {
        // ignore string
        String contents = ""
                + "public class Test {\n" // 20
                + "    public void test() {\n" //25
                + "        String string = \"{} () []\"\n" // 35
                + "    }\n" // 6
                + "}\n";
        doc.insertString(0, contents, null);
        HighlightsSequence highlightsSequence = rainbowBracesHighlighting.getHighlights(5, doc.getLength());
        assertTrue(highlightsSequence instanceof RainbowBracesHighlighting.HighlightsSequenceBackward);
        assertTrue(highlightsSequence.moveNext());
        assertTrue(highlightsSequence.moveNext());
        assertTrue(highlightsSequence.moveNext());
        assertTrue(highlightsSequence.moveNext());
        assertTrue(highlightsSequence.moveNext());
        assertEquals(highlightsSequence.getStartOffset(), 20 + 25 + 35 + 4);
        assertEquals(highlightsSequence.getEndOffset(), 20 + 25 + 35 + 5);
    }

    @Test
    public void testHighlightsSkipString_02() throws BadLocationException {
        // ignore string
        String contents = ""
                + "public class Test {\n" // 20
                + "    public void test() {\n" //25
                + "        String string = \"{} () []\"\n" // 35
                + "        if (prefix.endsWith(\"::\")) {\n" // 37
                + "            return \"\";\n" // 23
                + "        }\n" // 10
                + "    }\n" // 6
                + "}\n";
        doc.insertString(0, contents, null);
        HighlightsSequence highlightsSequence = rainbowBracesHighlighting.getHighlights(0, doc.getLength());
        assertTrue(highlightsSequence instanceof RainbowBracesHighlighting.HighlightsSequenceForward);
        assertTrue(highlightsSequence.moveNext());
        assertTrue(highlightsSequence.moveNext());
        assertTrue(highlightsSequence.moveNext());
        assertTrue(highlightsSequence.moveNext());
        assertTrue(highlightsSequence.moveNext());
        assertEquals(highlightsSequence.getStartOffset(), 20 + 25 + 35 + 11);
        assertEquals(highlightsSequence.getEndOffset(), 20 + 25 + 35 + 12);

        assertTrue(highlightsSequence.moveNext());
        assertEquals(highlightsSequence.getStartOffset(), 20 + 25 + 35 + 27);
        assertEquals(highlightsSequence.getEndOffset(), 20 + 25 + 35 + 28);

        assertTrue(highlightsSequence.moveNext());
        assertEquals(highlightsSequence.getStartOffset(), 20 + 25 + 35 + 32);
        assertEquals(highlightsSequence.getEndOffset(), 20 + 25 + 35 + 33);

        assertTrue(highlightsSequence.moveNext());
        assertEquals(highlightsSequence.getStartOffset(), 20 + 25 + 35 + 33);
        assertEquals(highlightsSequence.getEndOffset(), 20 + 25 + 35 + 34);

        assertTrue(highlightsSequence.moveNext());
        assertEquals(highlightsSequence.getStartOffset(), 20 + 25 + 35 + 35);
        assertEquals(highlightsSequence.getEndOffset(), 20 + 25 + 35 + 36);

        assertTrue(highlightsSequence.moveNext());
        assertEquals(highlightsSequence.getStartOffset(), 20 + 25 + 35 + 37 + 23 + 8);
        assertEquals(highlightsSequence.getEndOffset(), 20 + 25 + 35 + 37 + 23 + 9);

        assertTrue(highlightsSequence.moveNext());
        assertEquals(highlightsSequence.getStartOffset(), 20 + 25 + 35 + 37 + 23 + 10 + 4);
        assertEquals(highlightsSequence.getEndOffset(), 20 + 25 + 35 + 37 + 23 + 10 + 5);

        assertTrue(highlightsSequence.moveNext());
        assertEquals(highlightsSequence.getStartOffset(), 20 + 25 + 35 + 37 + 23 + 10 + 6 + 0);
        assertEquals(highlightsSequence.getEndOffset(), 20 + 25 + 35 + 37 + 23 + 10 + 6 + 1);
    }

    @Test
    public void testHighlightsBackwardSkipString_02() throws BadLocationException {
        // ignore string
        String contents = ""
                + "public class Test {\n" // 20
                + "    public void test() {\n" //25
                + "        String string = \"{} () []\"\n" // 35
                + "        if (prefix.endsWith(\"::\")) {\n" // 37
                + "            return \"\";\n" // 23
                + "        }\n" // 10
                + "    }\n" // 6
                + "}\n";
        doc.insertString(0, contents, null);
        HighlightsSequence highlightsSequence = rainbowBracesHighlighting.getHighlights(5, doc.getLength());
        assertTrue(highlightsSequence instanceof RainbowBracesHighlighting.HighlightsSequenceBackward);
        assertTrue(highlightsSequence.moveNext());
        assertTrue(highlightsSequence.moveNext());
        assertTrue(highlightsSequence.moveNext());
        assertTrue(highlightsSequence.moveNext());
        assertTrue(highlightsSequence.moveNext());
        assertEquals(highlightsSequence.getStartOffset(), 20 + 25 + 35 + 11);
        assertEquals(highlightsSequence.getEndOffset(), 20 + 25 + 35 + 12);

        assertTrue(highlightsSequence.moveNext());
        assertEquals(highlightsSequence.getStartOffset(), 20 + 25 + 35 + 27);
        assertEquals(highlightsSequence.getEndOffset(), 20 + 25 + 35 + 28);

        assertTrue(highlightsSequence.moveNext());
        assertEquals(highlightsSequence.getStartOffset(), 20 + 25 + 35 + 32);
        assertEquals(highlightsSequence.getEndOffset(), 20 + 25 + 35 + 33);

        assertTrue(highlightsSequence.moveNext());
        assertEquals(highlightsSequence.getStartOffset(), 20 + 25 + 35 + 33);
        assertEquals(highlightsSequence.getEndOffset(), 20 + 25 + 35 + 34);

        assertTrue(highlightsSequence.moveNext());
        assertEquals(highlightsSequence.getStartOffset(), 20 + 25 + 35 + 35);
        assertEquals(highlightsSequence.getEndOffset(), 20 + 25 + 35 + 36);

        assertTrue(highlightsSequence.moveNext());
        assertEquals(highlightsSequence.getStartOffset(), 20 + 25 + 35 + 37 + 23 + 8);
        assertEquals(highlightsSequence.getEndOffset(), 20 + 25 + 35 + 37 + 23 + 9);

        assertTrue(highlightsSequence.moveNext());
        assertEquals(highlightsSequence.getStartOffset(), 20 + 25 + 35 + 37 + 23 + 10 + 4);
        assertEquals(highlightsSequence.getEndOffset(), 20 + 25 + 35 + 37 + 23 + 10 + 5);

        assertTrue(highlightsSequence.moveNext());
        assertEquals(highlightsSequence.getStartOffset(), 20 + 25 + 35 + 37 + 23 + 10 + 6 + 0);
        assertEquals(highlightsSequence.getEndOffset(), 20 + 25 + 35 + 37 + 23 + 10 + 6 + 1);
    }

    @Test
    public void testHighlightsWithCharacter() throws BadLocationException {
        // ignore character
        String contents = ""
                + "public class Test {\n" // 20
                + "    public void test() {\n" //25
                + "        char c1 = '{';\n" // 23
                + "        char c2 = '}';\n" // 23
                + "        char c3 = '(';\n" // 23
                + "        char c4 = ')';\n" // 23
                + "        char c5 = '[';\n" // 23
                + "        char c6 = ']';\n" // 23
                + "    }\n" // 6
                + "}\n";
        doc.insertString(0, contents, null);
        HighlightsSequence highlightsSequence = rainbowBracesHighlighting.getHighlights(0, doc.getLength());
        assertTrue(highlightsSequence instanceof RainbowBracesHighlighting.HighlightsSequenceForward);
        assertTrue(highlightsSequence.moveNext());
        assertTrue(highlightsSequence.moveNext());
        assertTrue(highlightsSequence.moveNext());
        assertTrue(highlightsSequence.moveNext());
        assertTrue(highlightsSequence.moveNext());
        assertEquals(highlightsSequence.getStartOffset(), 20 + 25 + (23 * 6) + 4);
        assertEquals(highlightsSequence.getEndOffset(), 20 + 25 + (23 * 6) + 5);
    }

    @Test
    public void testHighlightsBackwardWithCharacter() throws BadLocationException {
        // ignore character
        String contents = ""
                + "public class Test {\n" // 20
                + "    public void test() {\n" //25
                + "        char c1 = '{';\n" // 23
                + "        char c2 = '}';\n" // 23
                + "        char c3 = '(';\n" // 23
                + "        char c4 = ')';\n" // 23
                + "        char c5 = '[';\n" // 23
                + "        char c6 = ']';\n" // 23
                + "    }\n" // 6
                + "}\n";
        doc.insertString(0, contents, null);
        HighlightsSequence highlightsSequence = rainbowBracesHighlighting.getHighlights(5, doc.getLength());
        assertTrue(highlightsSequence instanceof RainbowBracesHighlighting.HighlightsSequenceBackward);
        assertTrue(highlightsSequence.moveNext());
        assertTrue(highlightsSequence.moveNext());
        assertTrue(highlightsSequence.moveNext());
        assertTrue(highlightsSequence.moveNext());
        assertTrue(highlightsSequence.moveNext());
        assertEquals(highlightsSequence.getStartOffset(), 20 + 25 + (23 * 6) + 4);
        assertEquals(highlightsSequence.getEndOffset(), 20 + 25 + (23 * 6) + 5);
    }

    @Test
    public void testHighlightsWithComment() throws BadLocationException {
        RainbowBracesOptions.getInstance().setCommentSkipped(false);
        String contents = ""
                + "public class Test {\n" // 20
                + "    // {}\n" // 10
                + "    /* () [] */\n" // 16
                + "    public void test() {\n" // 25
                + "        new String[]{};\n" // 24
                + "    }\n" // 6
                + "}\n";
        doc.insertString(0, contents, null);
        HighlightsSequence highlightsSequence = rainbowBracesHighlighting.getHighlights(0, doc.getLength());
        assertTrue(highlightsSequence instanceof RainbowBracesHighlighting.HighlightsSequenceForward);
        assertTrue(highlightsSequence.moveNext());
        assertTrue(highlightsSequence.moveNext());
        assertEquals(highlightsSequence.getStartOffset(), 20 + 7);
        assertEquals(highlightsSequence.getEndOffset(), 20 + 8);

        assertTrue(highlightsSequence.moveNext());
        assertEquals(highlightsSequence.getStartOffset(), 20 + 8);
        assertEquals(highlightsSequence.getEndOffset(), 20 + 9);

        assertTrue(highlightsSequence.moveNext());
        assertEquals(highlightsSequence.getStartOffset(), 20 + 10 + 7);
        assertEquals(highlightsSequence.getEndOffset(), 20 + 10 + 8);

        assertTrue(highlightsSequence.moveNext());
        assertEquals(highlightsSequence.getStartOffset(), 20 + 10 + 8);
        assertEquals(highlightsSequence.getEndOffset(), 20 + 10 + 9);

        assertTrue(highlightsSequence.moveNext());
        assertEquals(highlightsSequence.getStartOffset(), 20 + 10 + 10);
        assertEquals(highlightsSequence.getEndOffset(), 20 + 10 + 11);

        assertTrue(highlightsSequence.moveNext());
        assertEquals(highlightsSequence.getStartOffset(), 20 + 10 + 11);
        assertEquals(highlightsSequence.getEndOffset(), 20 + 10 + 12);

        assertTrue(highlightsSequence.moveNext());
        assertEquals(highlightsSequence.getStartOffset(), 20 + 10 + 16 + 20);
        assertEquals(highlightsSequence.getEndOffset(), 20 + 10 + 16 + 21);

        assertTrue(highlightsSequence.moveNext());
        assertEquals(highlightsSequence.getStartOffset(), 20 + 10 + 16 + 21);
        assertEquals(highlightsSequence.getEndOffset(), 20 + 10 + 16 + 22);

        assertTrue(highlightsSequence.moveNext());
        assertEquals(highlightsSequence.getStartOffset(), 20 + 10 + 16 + 23);
        assertEquals(highlightsSequence.getEndOffset(), 20 + 10 + 16 + 24);

        assertTrue(highlightsSequence.moveNext());
        assertEquals(highlightsSequence.getStartOffset(), 20 + 10 + 16 + 25 + 18);
        assertEquals(highlightsSequence.getEndOffset(), 20 + 10 + 16 + 25 + 19);

        assertTrue(highlightsSequence.moveNext());
        assertEquals(highlightsSequence.getStartOffset(), 20 + 10 + 16 + 25 + 19);
        assertEquals(highlightsSequence.getEndOffset(), 20 + 10 + 16 + 25 + 20);

        assertTrue(highlightsSequence.moveNext());
        assertEquals(highlightsSequence.getStartOffset(), 20 + 10 + 16 + 25 + 20);
        assertEquals(highlightsSequence.getEndOffset(), 20 + 10 + 16 + 25 + 21);

        assertTrue(highlightsSequence.moveNext());
        assertEquals(highlightsSequence.getStartOffset(), 20 + 10 + 16 + 25 + 21);
        assertEquals(highlightsSequence.getEndOffset(), 20 + 10 + 16 + 25 + 22);

        assertTrue(highlightsSequence.moveNext());
        assertEquals(highlightsSequence.getStartOffset(), 20 + 10 + 16 + 25 + 24 + 4);
        assertEquals(highlightsSequence.getEndOffset(), 20 + 10 + 16 + 25 + 24 + 5);

        assertTrue(highlightsSequence.moveNext());
        assertEquals(highlightsSequence.getStartOffset(), 20 + 10 + 16 + 25 + 24 + 6 + 0);
        assertEquals(highlightsSequence.getEndOffset(), 20 + 10 + 16 + 25 + 24 + 6 + 1);
    }

    @Test
    public void testHighlightsBackwardWithComment() throws BadLocationException {
        RainbowBracesOptions.getInstance().setCommentSkipped(false);
        String contents = ""
                + "public class Test {\n" // 20
                + "    // {}\n" // 10
                + "    /* () [] */\n" // 16
                + "    public void test() {\n" // 25
                + "        new String[]{};\n" // 24
                + "    }\n" // 6
                + "}\n";
        doc.insertString(0, contents, null);
        HighlightsSequence highlightsSequence = rainbowBracesHighlighting.getHighlights(5, doc.getLength());
        assertTrue(highlightsSequence instanceof RainbowBracesHighlighting.HighlightsSequenceBackward);
        assertTrue(highlightsSequence.moveNext());
        assertTrue(highlightsSequence.moveNext());
        assertEquals(highlightsSequence.getStartOffset(), 20 + 7);
        assertEquals(highlightsSequence.getEndOffset(), 20 + 8);

        assertTrue(highlightsSequence.moveNext());
        assertEquals(highlightsSequence.getStartOffset(), 20 + 8);
        assertEquals(highlightsSequence.getEndOffset(), 20 + 9);

        assertTrue(highlightsSequence.moveNext());
        assertEquals(highlightsSequence.getStartOffset(), 20 + 10 + 7);
        assertEquals(highlightsSequence.getEndOffset(), 20 + 10 + 8);

        assertTrue(highlightsSequence.moveNext());
        assertEquals(highlightsSequence.getStartOffset(), 20 + 10 + 8);
        assertEquals(highlightsSequence.getEndOffset(), 20 + 10 + 9);

        assertTrue(highlightsSequence.moveNext());
        assertEquals(highlightsSequence.getStartOffset(), 20 + 10 + 10);
        assertEquals(highlightsSequence.getEndOffset(), 20 + 10 + 11);

        assertTrue(highlightsSequence.moveNext());
        assertEquals(highlightsSequence.getStartOffset(), 20 + 10 + 11);
        assertEquals(highlightsSequence.getEndOffset(), 20 + 10 + 12);

        assertTrue(highlightsSequence.moveNext());
        assertEquals(highlightsSequence.getStartOffset(), 20 + 10 + 16 + 20);
        assertEquals(highlightsSequence.getEndOffset(), 20 + 10 + 16 + 21);

        assertTrue(highlightsSequence.moveNext());
        assertEquals(highlightsSequence.getStartOffset(), 20 + 10 + 16 + 21);
        assertEquals(highlightsSequence.getEndOffset(), 20 + 10 + 16 + 22);

        assertTrue(highlightsSequence.moveNext());
        assertEquals(highlightsSequence.getStartOffset(), 20 + 10 + 16 + 23);
        assertEquals(highlightsSequence.getEndOffset(), 20 + 10 + 16 + 24);

        assertTrue(highlightsSequence.moveNext());
        assertEquals(highlightsSequence.getStartOffset(), 20 + 10 + 16 + 25 + 18);
        assertEquals(highlightsSequence.getEndOffset(), 20 + 10 + 16 + 25 + 19);

        assertTrue(highlightsSequence.moveNext());
        assertEquals(highlightsSequence.getStartOffset(), 20 + 10 + 16 + 25 + 19);
        assertEquals(highlightsSequence.getEndOffset(), 20 + 10 + 16 + 25 + 20);

        assertTrue(highlightsSequence.moveNext());
        assertEquals(highlightsSequence.getStartOffset(), 20 + 10 + 16 + 25 + 20);
        assertEquals(highlightsSequence.getEndOffset(), 20 + 10 + 16 + 25 + 21);

        assertTrue(highlightsSequence.moveNext());
        assertEquals(highlightsSequence.getStartOffset(), 20 + 10 + 16 + 25 + 21);
        assertEquals(highlightsSequence.getEndOffset(), 20 + 10 + 16 + 25 + 22);

        assertTrue(highlightsSequence.moveNext());
        assertEquals(highlightsSequence.getStartOffset(), 20 + 10 + 16 + 25 + 24 + 4);
        assertEquals(highlightsSequence.getEndOffset(), 20 + 10 + 16 + 25 + 24 + 5);

        assertTrue(highlightsSequence.moveNext());
        assertEquals(highlightsSequence.getStartOffset(), 20 + 10 + 16 + 25 + 24 + 6 + 0);
        assertEquals(highlightsSequence.getEndOffset(), 20 + 10 + 16 + 25 + 24 + 6 + 1);
    }

    @Test
    public void testHighlightsWithString() throws BadLocationException {
        RainbowBracesOptions.getInstance().setStringSkipped(false);
        String contents = ""
                + "public class Test {\n" // 20
                + "    public void test() {\n" //25
                + "        String string = \"{} () []\"\n" // 35
                + "    }\n" // 6
                + "}\n";
        doc.insertString(0, contents, null);
        HighlightsSequence highlightsSequence = rainbowBracesHighlighting.getHighlights(0, doc.getLength());
        assertTrue(highlightsSequence instanceof RainbowBracesHighlighting.HighlightsSequenceForward);
        assertTrue(highlightsSequence.moveNext());
        assertTrue(highlightsSequence.moveNext());
        assertTrue(highlightsSequence.moveNext());
        assertTrue(highlightsSequence.moveNext());
        assertTrue(highlightsSequence.moveNext());
        assertEquals(highlightsSequence.getStartOffset(), 20 + 25 + 25);
        assertEquals(highlightsSequence.getEndOffset(), 20 + 25 + 26);

        assertTrue(highlightsSequence.moveNext());
        assertEquals(highlightsSequence.getStartOffset(), 20 + 25 + 26);
        assertEquals(highlightsSequence.getEndOffset(), 20 + 25 + 27);

        assertTrue(highlightsSequence.moveNext());
        assertEquals(highlightsSequence.getStartOffset(), 20 + 25 + 28);
        assertEquals(highlightsSequence.getEndOffset(), 20 + 25 + 29);

        assertTrue(highlightsSequence.moveNext());
        assertEquals(highlightsSequence.getStartOffset(), 20 + 25 + 29);
        assertEquals(highlightsSequence.getEndOffset(), 20 + 25 + 30);

        assertTrue(highlightsSequence.moveNext());
        assertEquals(highlightsSequence.getStartOffset(), 20 + 25 + 31);
        assertEquals(highlightsSequence.getEndOffset(), 20 + 25 + 32);

        assertTrue(highlightsSequence.moveNext());
        assertEquals(highlightsSequence.getStartOffset(), 20 + 25 + 32);
        assertEquals(highlightsSequence.getEndOffset(), 20 + 25 + 33);

        assertTrue(highlightsSequence.moveNext());
        assertEquals(highlightsSequence.getStartOffset(), 20 + 25 + 35 + 4);
        assertEquals(highlightsSequence.getEndOffset(), 20 + 25 + 35 + 5);

        assertTrue(highlightsSequence.moveNext());
        assertEquals(highlightsSequence.getStartOffset(), 20 + 25 + 35 + 6 + 0);
        assertEquals(highlightsSequence.getEndOffset(), 20 + 25 + 35 + 6 + 1);
    }

    @Test
    public void testHighlightsBackwardWithString() throws BadLocationException {
        RainbowBracesOptions.getInstance().setStringSkipped(false);
        String contents = ""
                + "public class Test {\n" // 20
                + "    public void test() {\n" //25
                + "        String string = \"{} () []\"\n" // 35
                + "    }\n" // 6
                + "}\n";
        doc.insertString(0, contents, null);
        HighlightsSequence highlightsSequence = rainbowBracesHighlighting.getHighlights(5, doc.getLength());
        assertTrue(highlightsSequence instanceof RainbowBracesHighlighting.HighlightsSequenceBackward);
        assertTrue(highlightsSequence.moveNext());
        assertTrue(highlightsSequence.moveNext());
        assertTrue(highlightsSequence.moveNext());
        assertTrue(highlightsSequence.moveNext());
        assertTrue(highlightsSequence.moveNext());
        assertEquals(highlightsSequence.getStartOffset(), 20 + 25 + 25);
        assertEquals(highlightsSequence.getEndOffset(), 20 + 25 + 26);

        assertTrue(highlightsSequence.moveNext());
        assertEquals(highlightsSequence.getStartOffset(), 20 + 25 + 26);
        assertEquals(highlightsSequence.getEndOffset(), 20 + 25 + 27);

        assertTrue(highlightsSequence.moveNext());
        assertEquals(highlightsSequence.getStartOffset(), 20 + 25 + 28);
        assertEquals(highlightsSequence.getEndOffset(), 20 + 25 + 29);

        assertTrue(highlightsSequence.moveNext());
        assertEquals(highlightsSequence.getStartOffset(), 20 + 25 + 29);
        assertEquals(highlightsSequence.getEndOffset(), 20 + 25 + 30);

        assertTrue(highlightsSequence.moveNext());
        assertEquals(highlightsSequence.getStartOffset(), 20 + 25 + 31);
        assertEquals(highlightsSequence.getEndOffset(), 20 + 25 + 32);

        assertTrue(highlightsSequence.moveNext());
        assertEquals(highlightsSequence.getStartOffset(), 20 + 25 + 32);
        assertEquals(highlightsSequence.getEndOffset(), 20 + 25 + 33);

        assertTrue(highlightsSequence.moveNext());
        assertEquals(highlightsSequence.getStartOffset(), 20 + 25 + 35 + 4);
        assertEquals(highlightsSequence.getEndOffset(), 20 + 25 + 35 + 5);

        assertTrue(highlightsSequence.moveNext());
        assertEquals(highlightsSequence.getStartOffset(), 20 + 25 + 35 + 6 + 0);
        assertEquals(highlightsSequence.getEndOffset(), 20 + 25 + 35 + 6 + 1);
    }

}
