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

## Pace: one real feature at a time, verified
This project's owner can't run the game to test changes live — every module
here is verified by (a) `./gradlew build` actually succeeding, and (b) their
own in-game testing after a release, which surfaces real bugs (the ClickGUI
redesign and the Crosshair size fix both came from that loop). Given that,
don't pile many new, untested subsystems into one pass on top of an open bug
report — fix known bugs first, then add **one** new fully-working feature,
even when a broader spec asks for much more at once. Everything from a
larger spec that isn't being built this pass belongs in `ROADMAP.md` and, if
it's a nameable feature, a `PlaceholderModule` entry (greyed out, "SOON",
not clickable) in `NyteClientMod.registerRoadmapPlaceholders()` — so the
full scope stays visible without any of it pretending to work. When a
placeholder gets built for real, delete its `PlaceholderModule` entry in the
same commit; never leave a feature registered as both.

## Layout
- `src/main/java/client` – entry point (`NyteClientMod`) and the shared `Theme` color palette (see `THEME.md`).
- `src/main/java/client/modules` – one class per feature, all extending `ModuleBase` (except `PlaceholderModule`, which implements `Module` directly — see `ROADMAP.md`).
- `src/main/java/client/waypoints` – `Waypoint` data class and `WaypointStore` (Gson-backed JSON persistence).
- `src/main/java/client/mixin` – Mixins, registered via `nyteclient.mixins.json`.
- `src/main/java/client/gui` – `ClickGui`, the in-game panel UI, and `BlurSuppressor`.
- `src/main/java/client/utils` – tick/keybind plumbing.
- `src/main/resources` – resources such as `fabric.mod.json`.
- `build.gradle`, `gradle.properties`, `settings.gradle` – Gradle build configuration.
- `gradlew`/`gradlew.bat`/`gradle/` – Gradle wrapper pinned to 8.8 (Loom 1.6.x doesn't support Gradle 9+).

## Building
1. Ensure a **Java 21** JDK is available (Loom needs it to set up the Minecraft toolchain, even though the mod compiles to Java 17 bytecode).
2. Run `JAVA_HOME="$(/usr/libexec/java_home -v 21)" ./gradlew build` from the repository root (use the checked-in wrapper, not a system `gradle` — see README's Building section for why).
3. The built mod JAR can be found in `build/libs`.

There are currently no automated tests, so a successful build verifies compilation — always run this before saying a change works.

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
