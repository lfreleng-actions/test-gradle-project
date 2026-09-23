<!--
SPDX-License-Identifier: Apache-2.0
SPDX-FileCopyrightText: 2026 The Linux Foundation
-->

# 🐘 Test Gradle Project

Sample multi-project Gradle build used for testing actions.

This repository is a **fixture**: it exists so that GitHub Actions and
reusable workflows have a small, fast, predictable Gradle project to
build, test and scan. Nothing here ships as a published artefact.

This fixture is the Gradle counterpart of
[`test-maven-project`](https://github.com/lfreleng-actions/test-maven-project),
and mirrors that fixture's shape wherever Gradle allows.

## Why multi-project

A single-project build would not exercise the behaviour that matters:

```text
settings.gradle    reactor definition, Groovy DSL
build.gradle       shared configuration and dependency versions
core/              library module, Groovy DSL
app/               depends on core, Kotlin DSL
app/nested/        grandchild of the root, Groovy DSL
```

`app` depends on `core`, so the modules cannot build independently and
the build has a real task order to resolve.

`app/nested` sits a level deeper on purpose. Tooling that walks a Gradle
build commonly stops at the root's immediate subprojects, so a two-level
tree is what separates "handles multi-project" from "handles one level of
multi-project". It carries `commons-lang3`, which nothing else in the
build uses — so whether that coordinate appears in a tool's output
distinguishes one that reaches grandchildren from one that stops short. A
shared dependency could not make that distinction.

Gradle has no separate parent project, so the root script plays the role
the parent POM plays in the Maven fixture. That is a deliberate
difference rather than an oversight: root-level `subprojects` config is
what Gradle projects actually do.

## Toolchain

| Item               | Value                                            |
| ------------------ | ------------------------------------------------ |
| Java               | Toolchain 17 (`JavaLanguageVersion.of(17)`)      |
| Gradle             | 9.7.1, through the committed wrapper             |
| Tests              | JUnit 6 (`junit-bom` 6.1.3)                      |
| Runtime dependency | Jackson (`jackson-bom` 2.22.2, `implementation`) |
| Nested dependency  | Apache Commons Lang 3.20.0                       |
| Coverage           | JaCoCo 0.8.15, XML report per module             |

The root script declares the Java level as a **toolchain** rather than
`sourceCompatibility`/`targetCompatibility`, matching modern practice and
exercising the first path build metadata detection tries.

No toolchain resolver plugin forms part of this build. Gradle matches
the toolchain against JDKs already installed on the machine, and an
unmatched version fails the build with a clear message rather than
reaching out to a provisioning service. That keeps egress to Maven
Central and the Gradle distribution host, both of which the organisation
allow-list carries, so the build lane can run under a `block` egress
policy.

## Why an `implementation` dependency

`core` depends on `jackson-databind` at `implementation` scope, declared
**with no version of its own** — the version arrives from the
`jackson-bom` platform. This is not decoration. The declaration is what
makes the fixture usable for SBOM and vulnerability-scanning tests.

One declared coordinate resolves to three components:

```text
\--- com.fasterxml.jackson.core:jackson-databind -> 2.22.2
     +--- com.fasterxml.jackson.core:jackson-annotations:2.22
     \--- com.fasterxml.jackson.core:jackson-core:2.22.2
```

Note that the three land on **differing** versions (`2.22.2` and `2.22`),
which stops a generator faking the graph by copying one version across.

A dependency confined to the test configuration cannot serve this
purpose, and the two kinds of
generator fail it differently. Because test dependencies stay out of the
shipped artefact, a resolved-graph generator excludes them by default, so
a fixture carrying nothing else yields a BOM with no third-party
components at all. A static scan of the source tree does list the
dependency, but reports its platform-managed version as `UNKNOWN`, which
a scanner cannot match. Either way there is nothing worth measuring.

## Why both DSL dialects

The root and `core` use the Groovy DSL; `app` uses the Kotlin DSL, and
`app/nested` returns to Groovy beneath its Kotlin parent — legal,
because the dialect belongs to each build script rather than to the
tree.

Mixing dialects in one build is legal, and that is the point. Plugin
application and configuration differ between the two, so tooling that
injects behaviour into a Gradle build — SBOM generation through an init
script, for example — has to work against both. One reactor covering both
dialects tests that in a single build, rather than needing a second
fixture repository.

The **root stays Groovy** for a concrete reason. Project-type detection
scans the root directory alone, and `build.gradle.kts` classifies as
Kotlin at a higher precedence than `build.gradle` classifies as Java. A
Kotlin root would have this fixture detected as a Kotlin project. Keeping
the Kotlin dialect in `app` puts it beyond detection's reach.

## No dependency lockfile

Gradle's dependency locking is opt-in, and most projects skip it. The
fixture skips it too, because that is the realistic case and the one that
produces an empty SBOM today — which is the condition worth testing.

## The wrapper is version-controlled

The repository tracks `gradlew`, `gradlew.bat` and `gradle/wrapper/`. The
build lane runs `./gradlew` and validates the wrapper JAR's checksum, so
omitting them breaks the build. `.gitignore` carries no blanket `*.jar`
rule for that reason; build output lands in `build/` instead.

The single `gradle` Dependabot entry covers the wrapper as well as the
dependency versions, so both stay current without manual intervention.

## Tests that fail on purpose

`FailingGreeterTest` fails on purpose. The root script excludes
`Failing*Test` from a normal build, so `./gradlew build` passes.

```bash
./gradlew build                 # passes: failing tests excluded
./gradlew test -PfailingTests   # fails in :core:test, 2 failures
```

The second form lets consumers exercise test-failure handling — soft-fail
inputs, report rendering — against a build that genuinely fails.

## Usage

```bash
./gradlew build                          # compile, test, coverage
./gradlew :core:dependencies \
    --configuration runtimeClasspath     # show the resolved graph
```

Reports land in the usual Gradle locations:

| Report        | Path                                             |
| ------------- | ------------------------------------------------ |
| JUnit XML     | `<module>/build/test-results/test/`              |
| JaCoCo XML    | `<module>/build/reports/jacoco/test/`            |

## Linting baseline

This repository tracks the organisation's `actions-template` linting
baseline, including the Go and JavaScript hooks. Both stay inert here:
each guards on its project manifest, and a Java fixture has neither
`go.mod` nor `package.json`, so the hooks exit 0 without invoking a
toolchain. The template's `.golangci.yml` and `eslint.config.mjs` seeds
are absent for the same reason `.ruff.toml` is.
