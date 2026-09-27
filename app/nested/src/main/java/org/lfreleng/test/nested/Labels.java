/*
 * SPDX-License-Identifier: Apache-2.0
 * SPDX-FileCopyrightText: 2026 The Linux Foundation
 */

package org.lfreleng.test.nested;

import org.apache.commons.lang3.StringUtils;

/**
 * Trivial helper that exists to give the nested module a genuine
 * compile-scope use of its own dependency.
 *
 * <p>A declared dependency nothing compiles against would still appear
 * in a resolved graph, but it would also be one this project has no
 * reason to keep. Using it means the module's presence in an aggregated
 * SBOM reflects a real classpath rather than an unused declaration.
 */
public final class Labels {

    private Labels() {
        // Utility class; not instantiable.
    }

    /**
     * Returns a display label for the supplied name.
     *
     * @param name the candidate name; blank or null yields a fallback
     * @return the label
     */
    public static String label(final String name) {
        if (StringUtils.isBlank(name)) {
            return "unnamed";
        }
        return StringUtils.capitalize(StringUtils.strip(name));
    }
}
