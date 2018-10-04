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

    private RainbowBracesHighliting rainbowBracesHighliting;
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
        rainbowBracesHighliting = new RainbowBracesHighliting(doc);
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
        HighlightsSequence highlightsSequence = rainbowBracesHighliting.getHighlights(0, doc.getLength());
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
    public void testHighlightsWithComment() throws BadLocationException {
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
        HighlightsSequence highlightsSequence = rainbowBracesHighliting.getHighlights(0, doc.getLength());
        assertTrue(highlightsSequence.moveNext());
        assertTrue(highlightsSequence.moveNext());
        assertEquals(highlightsSequence.getStartOffset(), 20 + 10 + 16 + 20);
        assertEquals(highlightsSequence.getEndOffset(), 20 + 10 + 16 + 21);
    }

    @Test
    public void testHighlightsWithString() throws BadLocationException {
        // ignore string
        String contents = ""
                + "public class Test {\n" // 20
                + "    public void test() {\n" //25
                + "        String string = \"{} () []\"\n" // 35
                + "    }\n" // 6
                + "}\n";
        doc.insertString(0, contents, null);
        HighlightsSequence highlightsSequence = rainbowBracesHighliting.getHighlights(0, doc.getLength());
        assertTrue(highlightsSequence.moveNext());
        assertTrue(highlightsSequence.moveNext());
        assertTrue(highlightsSequence.moveNext());
        assertTrue(highlightsSequence.moveNext());
        assertTrue(highlightsSequence.moveNext());
        assertEquals(highlightsSequence.getStartOffset(), 20 + 25 + 35 + 4);
        assertEquals(highlightsSequence.getEndOffset(), 20 + 25 + 35 + 5);
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
        HighlightsSequence highlightsSequence = rainbowBracesHighliting.getHighlights(0, doc.getLength());
        assertTrue(highlightsSequence.moveNext());
        assertTrue(highlightsSequence.moveNext());
        assertTrue(highlightsSequence.moveNext());
        assertTrue(highlightsSequence.moveNext());
        assertTrue(highlightsSequence.moveNext());
        assertEquals(highlightsSequence.getStartOffset(), 20 + 25 + (23 * 6) + 4);
        assertEquals(highlightsSequence.getEndOffset(), 20 + 25 + (23 * 6) + 5);
    }
}
