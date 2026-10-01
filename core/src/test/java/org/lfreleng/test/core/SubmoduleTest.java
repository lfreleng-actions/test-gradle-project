/*
 * SPDX-License-Identifier: Apache-2.0
 * SPDX-FileCopyrightText: 2026 The Linux Foundation
 */

package org.lfreleng.test.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Proves the repository's submodule was checked out.
 *
 * <p>Skipped, not failed, when the submodule directory is empty, so a
 * consumer that checks out without submodules still builds green.
 * Workflows that test submodule checkout assert that this test passed:
 * a skip there means the checkout left the submodule out.
 */
class SubmoduleTest {

    /** The README at the commit the submodule pins, read verbatim. */
    private static final String PINNED_README =
            "# test-maven-project\nSample Maven project used for testing actions\n";

    @Test
    @DisplayName("reads the README the pinned submodule commit carries")
    void readsPinnedReadme() throws IOException {
        // Gradle runs each module's tests from the module directory.
        final Path readme = Path.of("..", "submodule", "README.md");
        assumeTrue(Files.isRegularFile(readme), "submodule not checked out");
        assertEquals(PINNED_README, Files.readString(readme, StandardCharsets.UTF_8));
    }
}
