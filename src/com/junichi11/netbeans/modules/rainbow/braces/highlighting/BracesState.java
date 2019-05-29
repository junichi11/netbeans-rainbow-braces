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

/**
 *
 * @author junichi11
 */
enum BracesState {
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

    public boolean isClose() {
        return this == ParenthesisClose
                || this == BracketClose
                || this == BraceClose;
    }

    public static BracesState valueOfChar(char c) {
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
