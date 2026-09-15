# Roadmap

Tracks the full "NEXT-GEN MINECRAFT CLIENT" vision against what's actually
built. Every named feature below also exists as an entry in the ClickGUI —
real ones are toggleable, planned ones show up greyed out with a **SOON**
pill (`PlaceholderModule`) so the full scope stays visible without
pretending anything half-built works. Promoting a placeholder to a real
module means deleting its `PlaceholderModule` registration in
`NyteClientMod.registerRoadmapPlaceholders()` the same commit it's built —
never leave a feature registered twice.

Status key: **Built** (working, in the current release) · **Placeholder**
(visible in the ClickGUI, not implemented) · **Planned** (queued, not yet
visible anywhere) · **Architectural** (not a standalone feature — informs
how everything else is built) · **Out of scope** (not building this, with
why).

| # | Feature | Status | Notes |
|---|---|---|---|
| 1 | Core Client Philosophy | Architectural | Shared so far: `ModuleManager`, `Theme` (3 presets), the toggle+action-bar-announce convention in `ModuleBase`. No shared command/notification/animation/profile/config system yet. |
| 2 | Command Center | Placeholder | "Command Center Search" |
| 3 | Contextual HUD | Planned | Current HUD elements (Coordinates, Xray, Waypoints nav line) are always-on toggles, not activity-aware. |
| 4 | World Memory | Planned | Waypoints (built) covers manual markers + auto-death. Auto-detecting beds/portals/villages/structures is a separate, harder problem — that data isn't cleanly available client-side the way block state is. |
| 5 | Advanced Waypoints | **Built** | `Waypoints` module: press `B` to drop one, auto "Last Death" marker, HUD arrow+distance+vertical-diff to the nearest one in your dimension, persisted to `config/nyteclient/waypoints.json`. Missing: icons/colors/categories/ETA/sharing/temporary waypoints, and any list/rename/delete UI — edit the JSON directly for now. |
| 6 | World Dashboard | Placeholder | |
| 7 | Smart Block Inspector | Placeholder | "Block Inspector" |
| 8 | Smart Entity Inspector | Placeholder | "Entity Inspector" |
| 9 | Modern Inventory | Planned | |
| 10 | Inventory Search | Placeholder | |
| 11 | Modern Chat | Placeholder | |
| 12 | Notification Center | Placeholder | |
| 13 | Modern Menu System | **Built**, partially | `ClickGui` has a sidebar, per-category counts, and name+description rows. Missing: search, favorites, recently-changed, animated transitions. |
| 14 | Themes | **Built**, partially | 3 presets (Violet/Crimson/Teal) vs. the spec's 8 named ones; no per-property sliders (opacity/blur/corner radius/animation intensity). |
| 15 | Client Profiles | Placeholder | |
| 16 | Gameplay Feel Overhaul | Planned | |
| 17 | Modern Damage Feedback | Placeholder | "Damage Feedback" |
| 18 | Item Pickup Experience | Placeholder | "Item Pickup Feedback" |
| 19 | Advancement Experience | Planned | |
| 20 | Modern Crosshair | **Built**, partially | `Crosshair` replaces vanilla's with a themed, outlined gapped cross. Missing: dynamic size/opacity, hit feedback, custom shapes, multiple profiles. |
| 21 | Camera System | Placeholder | |
| 22 | Movement Feel | Planned | |
| 23 | Environment Immersion | Placeholder | Folded into "Ambient Immersion" |
| 24 | Ambient Audio System | Placeholder | Folded into "Ambient Immersion" |
| 25 | Dynamic Lighting Presentation | Placeholder | |
| 26 | Smart Performance Engine | Planned | |
| 27 | Performance Advisor | Placeholder | |
| 28 | Screenshot Mode | Placeholder | |
| 29 | Creator Mode | Planned | |
| 30 | Session Statistics | Placeholder | |
| 31 | Smart Tool Information | Planned | |
| 32 | Quick Action Wheel | Planned | |
| 33 | UI Micro-Animations | Planned | |
| 34 | Accessibility | Planned | |
| 35 | Vanilla+ Mode | Planned | |
| 36 | Immersive Mode | Planned | |
| 37 | Smart UI Auto-Hide | Planned | |
| 38 | UI Favorites | Planned | |
| 39 | Recent Actions | Planned | |
| 40 | Experimental Lab | Planned | |
| 41 | Safety + Server Compatibility | **Built**, as policy | This is `AGENTS.md`'s scope section, written before this spec existed — no cheats, no combat automation, no anti-cheat bypasses. Already the project's rule; this spec just confirms it. |
| 42 | Configuration Architecture | Architectural | `waypoints.json` is the only persisted config today. No profiles, server/world-specific settings, or import/export. |
| 43 | Keybind Architecture | Planned | Current keybinds (GUI open, Zoom hold, waypoint-create) are independent vanilla `KeyBinding`s with no conflict detection between them. |
| 44 | First-Run Experience | Planned | |
| 45 | "The Client Should Feel Alive" | Architectural | Guides future polish passes, not a feature itself. |
| 46 | Design Language | **Built** | `THEME.md`'s design rules already avoid cheap gradients/neon/clutter — this predates the spec and matches it. |
| 47 | Performance Requirement | Architectural | Modules are event-driven (tick/HUD callbacks), not polling — with one exception: `Xray`'s block scan is a naive O(n³) sweep, throttled to every 2s at radius 16. Worth revisiting if it causes stutter on lower-end machines. |
| 48 | Final Product Standard | Architectural | The target, not a task. |

## Also requested, not in the numbered spec

- **Vector logo** (north-star mark, white/black/colored variants) — Planned, not yet built.
- **"Message the client to you"** — Out of scope as a *live* channel: a running Minecraft JVM process can't push into an active Claude Code session — there's no connection between them. A realistic version of this is one-way and pull-based: the mod writes structured "interesting event" entries to a local log file (`config/nyteclient/events.log`), and a Claude Code session with file access reads it on request. That's buildable if still wanted — just not real-time push.
