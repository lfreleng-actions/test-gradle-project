/*
 * SPDX-License-Identifier: Apache-2.0
 * SPDX-FileCopyrightText: 2026 The Linux Foundation
 */

package org.lfreleng.test.core;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import java.util.TreeMap;

/**
 * Builds greeting messages.
 *
 * <p>Deliberately small, but with more than one branch so coverage
 * reports contain something meaningful to measure.
 */
public final class Greeter {

    private static final String DEFAULT_NAME = "world";

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private final String name;

    /**
     * Creates a greeter.
     *
     * @param name the name to greet; blank or null falls back to a default
     */
    public Greeter(final String name) {
        if (name == null || name.isBlank()) {
            this.name = DEFAULT_NAME;
        } else {
            this.name = name.strip();
        }
    }

    /**
     * Returns the greeting for the configured name.
     *
     * @return the greeting message
     */
    public String greet() {
        return "Hello, " + name + "!";
    }

    /**
     * Returns the name this greeter addresses.
     *
     * @return the resolved name
     */
    public String name() {
        return name;
    }

    /**
     * Returns the greeting serialised as a JSON document.
     *
     * <p>Gives the module a genuine compile-scope use of a third-party
     * library. SBOM fixtures need a resolved transitive graph to be
     * worth measuring, and a test-only dependency cannot provide one
     * because it is absent from the shipped artefact.
     *
     * <p>Deliberately touches only {@code jackson-databind} types.
     * {@code writeValueAsString} would throw {@code
     * JsonProcessingException}, which {@code jackson-core} supplies,
     * making this API depend on a transitive coordinate the module does
     * not declare. Building a tree instead keeps {@code jackson-core}
     * and {@code jackson-annotations} purely transitive, so one declared
     * coordinate still resolves to three components.
     *
     * <p>Keys are emitted in sorted order so the output is stable
     * across runs.
     *
     * @return the greeting as JSON
     */
    public String toJson() {
        final Map<String, String> fields = new TreeMap<>();
        fields.put("greeting", greet());
        fields.put("name", name);
        final JsonNode tree = MAPPER.valueToTree(fields);
        return tree.toString();
    }
}
