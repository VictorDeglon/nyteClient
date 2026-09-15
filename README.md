# Nyte Client

A small Fabric mod for personal-server / singleplayer use: a handful of
client-side movement, player, and render tweaks, toggled from an in-game
ClickGUI. **Scope on purpose:** these are QoL/movement toggles, not
anti-cheat-evading combat automation — see [AGENTS.md](AGENTS.md) for the
project's charter if you're extending this.

**[Download / landing page →](https://victordeglon.github.io/nyteClient/)**
&middot; **[Latest release](https://github.com/VictorDeglon/nyteClient/releases/latest)**

## Modules

| Module | Category | What it does |
|---|---|---|
| **Flight** | Movement | Creative-style flight in Survival. Only works if the server allows it — see below. |
| **Hitbox** | Player | Auto-crouches for the smaller (1.5-block) hitbox vanilla already gives you for holding shift. No speed-penalty removal. |
| **FullBright** | Render | Maxes out gamma so dark areas look lit. |
| **Zoom** | Render | Hold `C` to narrow the FOV, like a spyglass. |
| **Coordinates** | Render | Draws your XYZ position in the top-left corner. |

Open the ClickGUI with **Right Shift** (rebindable in Minecraft's controls
menu, listed under "Nyte Client").

### A note on Flight

This module only controls your own client's movement flags — it can't make
a server trust movement it doesn't already allow. Vanilla dedicated servers
kick players who stay airborne too long unless the player already has
flight (creative/spectator) **or** the server has `allow-flight=true` in
`server.properties` — that setting exists specifically for "a client mod
may legitimately provide flight." Set that on your own server if you want
Flight to actually work there instead of getting you kicked.

## Architecture

- [`Module`](src/main/java/client/modules/Module.java) — the interface
  every feature implements (name, description, category, enabled state,
  tick hook).
- [`ModuleBase`](src/main/java/client/modules/ModuleBase.java) — handles
  the enabled flag and enable/disable dispatch so a module only needs to
  override what it actually uses.
- [`ModuleManager`](src/main/java/client/modules/ModuleManager.java) —
  holds every registered module and calls `onTick()` on the enabled ones
  once per client tick.
- [`ClickGui`](src/main/java/client/gui/ClickGui.java) — the panel UI:
  category tabs on the left, module list on the right, hover tooltips.
  Colors come entirely from [`Theme`](src/main/java/client/Theme.java) —
  see [`THEME.md`](THEME.md) for the full palette guide.
- [`ModTickHandler`](src/main/java/client/utils/ModTickHandler.java) —
  registers the Fabric client-tick callback that drives `ModuleManager`
  and listens for the GUI-open keybind.

## Adding a new module

1. Create a class in `client.modules` extending `ModuleBase`, passing a
   name, description, and `Category` to the constructor.
2. Override `onEnable()`/`onDisable()` for one-time setup/teardown, and/or
   `onTick()` for per-tick behavior while enabled.
3. Register it in `NyteClientMod.onInitialize()`.

It'll show up in the ClickGUI under its category automatically — no GUI
code to touch.

## Building

A Gradle wrapper pinned to **8.8** is checked in (Loom 1.6.x doesn't support
Gradle 9+). Fabric Loom needs a **Java 21** runtime to set up the Minecraft
toolchain even though the mod itself compiles for Java 17 bytecode.

```bash
JAVA_HOME="$(/usr/libexec/java_home -v 21)" ./gradlew build
```

The built mod JAR will be in `build/libs/nyteclient-<version>.jar`. Verified
working with a clean `./gradlew build` against Temurin/Homebrew OpenJDK 21.

## Target Minecraft version

`gradle.properties` currently targets **1.21**. If you're on a newer
release, update `minecraft_version`, `yarn_mappings`, and `fabric_version`
there to match — check [fabricmc.net](https://fabricmc.net/develop/) for
current values, and confirm the exact `fabric_version` string exists at
`https://maven.fabricmc.net/net/fabricmc/fabric-api/fabric-api/maven-metadata.xml`
before building (a stale/guessed version was the first build failure here).

## Releasing

Pushing a tag matching `v*` triggers
[`.github/workflows/release.yml`](.github/workflows/release.yml), which
builds the jar and publishes a GitHub Release with it attached (plus a
stable-named `nyteclient-latest.jar` copy that the landing page always
links to). To cut a release:

```bash
# bump mod_version in gradle.properties, commit it, then:
git tag v0.2.0
git push origin v0.2.0
```

The landing page in [`docs/`](docs/index.html) (served via GitHub Pages)
pulls the latest release info live from the GitHub API, so it stays current
automatically — no site edits needed per release.
