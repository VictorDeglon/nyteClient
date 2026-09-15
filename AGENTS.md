# Instructions for Nyte Client Repository

## Purpose
This repository contains the code for **Nyte Client**, a Fabric mod targeting Minecraft 1.21. The project focuses on client-side movement, player, and render toggles for the author's own server/singleplayer use, controlled from an in-game ClickGUI.

## Scope
This is a personal QoL/movement toolkit, not anti-cheat-evasion tooling. The
line isn't "would real anti-cheat catch this?" — several modules here
(`Flight`, `Speed`, `JumpBoost`, `AirJump`) plainly would, and that's fine.
The line is **honest vs. deceptive**:
- OK: a plain, visible toggle that does what its name says, even if a real
  anti-cheat would flag the resulting movement. It's up to the server
  operator to allow or reject it — the module doesn't try to hide what it's
  doing or work around detection.
- Not OK: anything built specifically to defeat, spoof, or hide from a
  server's detection (e.g. "make the server think we aren't flying"),
  auto-clickers, killaura/aimbot-style combat automation, or timing/lag
  exploits (e.g. automating a combo to land a hit a human couldn't time,
  or exploiting network latency to bypass a mechanic like shield
  blocking). If asked to extend this project in that direction, treat it
  the same way `targetLock_MacePvP` is scoped: a personal practice tool,
  not something built to beat real anti-cheat on a server the user doesn't
  control.
- A feature that can't work as an honest client-side toggle at all (e.g. a
  damage multiplier — damage is server-computed, a client can't just
  declare a bigger number) isn't a "make it sneakier" problem, it's a "this
  needs to be a server-side change instead" answer — point to the relevant
  vanilla mechanic (attributes, effects, gamerules) instead of building a
  fake client feature.
- `Flight` only works if the target server allows it (`allow-flight=true`)
  or the player already has flight — it doesn't and shouldn't try to defeat
  the server's own anti-fly check. Same principle for `Speed`/`JumpBoost`/
  `AirJump`: no packet spoofing, no hiding, just the plain movement change.

## Layout
- `src/main/java/client` – entry point (`NyteClientMod`) and the shared `Theme` color palette (see `THEME.md`).
- `src/main/java/client/modules` – one class per feature, all extending `ModuleBase`.
- `src/main/java/client/gui` – `ClickGui`, the in-game panel UI.
- `src/main/java/client/utils` – tick/keybind plumbing.
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
- Each module extending `ModuleBase` should pass its name/description/category to the `super(...)` constructor and override `onEnable()`/`onDisable()`/`onTick()` as needed.
- UI colors always come from `client.Theme` (see `THEME.md`) — never a hard-coded hex value in `ClickGui` or a module.

## Commit Messages
- Use short, imperative sentences (e.g., `Add new HUD module`).
- Mention related files or modules when relevant.

## Additional Notes
- Avoid changing version numbers or Gradle properties unless required for a feature or release.
- Before committing, run `gradle build` to ensure the project still compiles.
- New modules should be registered in `NyteClientMod` so they can be toggled in game.
