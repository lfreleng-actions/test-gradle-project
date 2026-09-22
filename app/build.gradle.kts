// SPDX-License-Identifier: Apache-2.0
// SPDX-FileCopyrightText: 2026 The Linux Foundation

// Application module, deliberately in the Kotlin DSL while the root and
// the core module use Groovy.
//
// Mixing dialects in one build is legal, and it is the point of this
// module: plugin application and configuration differ between the two,
// so tooling that injects behaviour into a Gradle build has to work
// against both. One reactor covering both dialects tests that in a
// single build rather than needing a second fixture repository.

plugins {
    // The java plugin is already applied to every subproject from the
    // root script, and applying it is idempotent. Declaring it here is
    // still necessary: the Kotlin DSL only generates type-safe accessors
    // such as 'implementation' for plugins a script declares itself.
    // Without this block the dependency below would have to be written
    // as a quoted configuration name.
    java
}

description = "Application module of the sample Gradle project"

dependencies {
    // Inter-module dependency: the build must configure and compile core
    // first, so a module-aware build is required.
    implementation(project(":core"))
}
