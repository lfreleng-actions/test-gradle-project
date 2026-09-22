/*
 * SPDX-License-Identifier: Apache-2.0
 * SPDX-FileCopyrightText: 2026 The Linux Foundation
 */

package org.lfreleng.test.core;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Deliberately failing tests, excluded from a normal build.
 *
 * <p>The root build script excludes {@code Failing*Test} by default. Run
 * with {@code -PfailingTests} to include this class and produce a build
 * that fails during the test task, which lets consumers exercise
 * test-failure handling such as soft-fail inputs and report rendering.
 */
class FailingGreeterTest {

    @Test
    @DisplayName("fails on purpose: wrong greeting expectation")
    void failsOnPurpose() {
        assertEquals("Goodbye, world!", new Greeter(null).greet());
    }

    @Test
    @DisplayName("fails on purpose: wrong name expectation")
    void alsoFailsOnPurpose() {
        assertEquals("nobody", new Greeter("somebody").name());
    }
}
