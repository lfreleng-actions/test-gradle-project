/*
 * SPDX-License-Identifier: Apache-2.0
 * SPDX-FileCopyrightText: 2026 The Linux Foundation
 */

package org.lfreleng.test.app;

import org.lfreleng.test.core.Greeter;

/**
 * Entry point demonstrating a cross-module dependency on the core
 * library, so the build has a real task order to resolve.
 */
public final class App {

    private App() {
        // Utility class; not instantiable.
    }

    /**
     * Builds the greeting for the supplied name.
     *
     * @param name the name to greet
     * @return the greeting message
     */
    public static String message(final String name) {
        return new Greeter(name).greet();
    }

    /**
     * Prints a greeting.
     *
     * @param args optional single argument naming who to greet
     */
    public static void main(final String[] args) {
        final String name = args.length > 0 ? args[0] : null;
        System.out.println(message(name));
    }
}
