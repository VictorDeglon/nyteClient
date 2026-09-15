# Nyte Client — Color Theme Guide

All UI color comes from one place: [`Theme.java`](src/main/java/client/Theme.java).
Nothing in [`ClickGui`](src/main/java/client/gui/ClickGui.java) or a HUD
module should hard-code a hex value — pull a constant from `Theme` instead.
That's what keeps the panel, tabs, and HUD text looking like one coherent
piece of UI, and it means a full re-theme is a one-file change.

## Presets

`Theme` ships three presets (`Theme.Preset`: `VIOLET`, `CRIMSON`, `TEAL`).
Every `Theme.*` field below is a mutable static, reassigned by
`Theme.apply(Preset)` — so existing call sites (`Theme.PANEL`,
`Theme.ACCENT`, ...) never change, only the values they currently hold. The
ClickGUI header's small swatch button in the top-right calls
`Theme.cyclePreset()` on click. To add a fourth preset: add an enum
constant to `Preset` and a matching `case` in `Theme.apply()` assigning all
thirteen fields — the table below documents the `VIOLET` values; follow the
same design rules for a new preset's palette.

## Format

Every constant is a 32-bit ARGB int: `0xAARRGGBB`. The first byte is alpha
(`00` transparent → `FF` opaque), then red, green, blue. This is the format
`DrawContext.fill`/`drawTextWithShadow`/`drawBorder` all expect directly —
no conversion needed at the call site.

## Palette

| Token | Value | Swatch | Used for |
|---|---|---|---|
| `BACKGROUND` | `#FF0D0B12` | near-black, fully opaque | Full-screen dim behind the panel |
| `PANEL` | `#F0191622` | very dark violet-grey | Panel body, module list background |
| `PANEL_HEADER` | `#FF241F30` | dark violet-grey | Header strip, hovered-row fill |
| `BORDER` | `#33FFFFFF` | white, ~20% opaque | 1px panel outline |
| `ACCENT` | `#FF8A5CFF` | violet | Selected-tab stripe, primary brand color |
| `ACCENT_HOVER` | `#FFA57BFF` | lighter violet | Reserved for hover states that need to read as "accent" |
| `ACCENT_MUTED` | `#668A5CFF` | violet, ~40% opaque | Selected-tab background (calmer than full `ACCENT`) |
| `ENABLED` | `#FF4CE0B3` | teal-green | Module-on indicator dot |
| `DISABLED` | `#FF4A4658` | muted grey-violet | Module-off indicator dot |
| `DANGER` | `#FFFF5C7A` | coral-red | Reserved for destructive/warning UI |
| `TEXT_PRIMARY` | `#FFF2F0FA` | near-white | Titles, enabled module names, tab labels |
| `TEXT_MUTED` | `#FF9D97B5` | soft lavender-grey | Descriptions, disabled module names, inactive tabs |
| `TEXT_ON_ACCENT` | `#FF0D0B14` | near-black | Reserved for text drawn on top of a solid `ACCENT` fill |

## Design rules

1. **One accent color.** `ACCENT` (and its `_HOVER`/`_MUTED` variants) is the
   only saturated color in the palette. Everything else is a near-neutral
   violet-grey. This keeps the accent meaningful — when something is
   violet, it's telling you it's selected/active, not just decorative.
2. **State, not just color.** Enabled/disabled is communicated by *both* a
   color (`ENABLED`/`DISABLED`) and a change in text color
   (`TEXT_PRIMARY`/`TEXT_MUTED`), so it still reads correctly for a
   color-blind viewer.
3. **Contrast floor.** `TEXT_MUTED` against `PANEL`/`PANEL_HEADER` and
   `TEXT_PRIMARY` against the same are both well above WCAG AA for UI text
   at this size (~9:1 and ~13:1 respectively) — don't introduce a text
   color that reads lighter than `TEXT_MUTED`, it'll stop being legible in
   the corner of the screen during gameplay.
4. **No pure black/white.** Backgrounds lean dark-violet rather than `#000`,
   and text leans off-white rather than `#FFF`, which reads as less harsh
   against Minecraft's own lighting.
5. **`BACKGROUND` is fully opaque.** `ClickGui.shouldPause()` returns
   `false` (the world keeps animating behind the panel, by design), so any
   translucency at all lets that motion bleed through and reads as a blur
   rather than a dim — a partial-opacity `BACKGROUND` still did this. Keep
   `BACKGROUND` at `0xFF` alpha in any new preset.

## Changing the theme

Edit the constants in `Theme.java` — every value is documented inline with
what it's for. `ClickGui` and every HUD module reference `Theme.*` fields
directly, so a new palette applies everywhere the moment you rebuild.
