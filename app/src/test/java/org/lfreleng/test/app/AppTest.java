/*
 * SPDX-License-Identifier: Apache-2.0
 * SPDX-FileCopyrightText: 2026 The Linux Foundation
 */

package org.lfreleng.test.app;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/** Tests for {@link App}. */
class AppTest {

    @Test
    @DisplayName("delegates to the core greeter")
    void delegatesToCore() {
        assertEquals("Hello, Gradle!", App.message("Gradle"));
    }

    @Test
    @DisplayName("uses the default name when none is supplied")
    void usesDefaultName() {
        assertTrue(App.message(null).contains("world"));
    }
}
