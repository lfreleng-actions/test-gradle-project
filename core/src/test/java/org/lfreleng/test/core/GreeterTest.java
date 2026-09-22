/*
 * SPDX-License-Identifier: Apache-2.0
 * SPDX-FileCopyrightText: 2026 The Linux Foundation
 */

package org.lfreleng.test.core;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

/** Tests for {@link Greeter}. */
class GreeterTest {

    @Test
    @DisplayName("greets a supplied name")
    void greetsSuppliedName() {
        assertEquals("Hello, Linux Foundation!", new Greeter("Linux Foundation").greet());
    }

    @Test
    @DisplayName("trims surrounding whitespace from the name")
    void trimsName() {
        assertEquals("Hello, Bob!", new Greeter("  Bob  ").greet());
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "\t"})
    @DisplayName("falls back to a default for a missing name")
    void fallsBackToDefault(final String candidate) {
        assertEquals("Hello, world!", new Greeter(candidate).greet());
    }

    @Test
    @DisplayName("exposes the resolved name")
    void exposesResolvedName() {
        assertEquals("world", new Greeter(null).name());
    }

    @Test
    @DisplayName("serialises the greeting as JSON")
    void serialisesAsJson() {
        assertEquals(
                "{\"greeting\":\"Hello, Bob!\",\"name\":\"Bob\"}",
                new Greeter("Bob").toJson());
    }
}
