# Instructions for Nyte Client Repository

## Purpose
This repository contains the code for **Nyte Client**, a Fabric mod targeting Minecraft 1.21. The project focuses on client-side performance and quality-of-life features that can be toggled in-game.

## Layout
- `src/main/java` – Java source code.
- `src/main/resources` – resources such as `fabric.mod.json`.
- `build.gradle`, `gradle.properties`, `settings.gradle` – Gradle build configuration (no wrapper script is included).

## Building
1. Ensure **Java 17+** and **Gradle** are available on the command line.
2. Run `gradle build` from the repository root.
3. The built mod JAR can be found in `build/libs`.

There are currently no automated tests, so a successful build verifies compilation.

## Java Style Guidelines
- Use **4 spaces** for indentation (no tabs).
- Keep line length under **120 characters**.
- Include Javadoc comments for public classes and methods when adding new code.
- Organize imports using your IDE's standard formatter.
- Each module extending `ModuleBase` should implement `getName()` and handle enable/disable logic.

## Commit Messages
- Use short, imperative sentences (e.g., `Add new HUD module`).
- Mention related files or modules when relevant.

## Additional Notes
- Avoid changing version numbers or Gradle properties unless required for a feature or release.
- Before committing, run `gradle build` to ensure the project still compiles.
- New modules should be registered in `NyteClientMod` so they can be toggled in game.
