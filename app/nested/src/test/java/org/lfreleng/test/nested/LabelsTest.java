/*
 * SPDX-License-Identifier: Apache-2.0
 * SPDX-FileCopyrightText: 2026 The Linux Foundation
 */

package org.lfreleng.test.nested;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

/** Tests for {@link Labels}. */
class LabelsTest {

    @Test
    @DisplayName("capitalises a supplied name")
    void capitalisesName() {
        assertEquals("Gradle", Labels.label("gradle"));
    }

    @Test
    @DisplayName("trims surrounding whitespace")
    void trimsName() {
        assertEquals("Nested", Labels.label("  nested  "));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "\t"})
    @DisplayName("falls back for a missing name")
    void fallsBack(final String candidate) {
        assertEquals("unnamed", Labels.label(candidate));
    }
}
