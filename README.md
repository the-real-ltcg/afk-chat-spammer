# AFK Chat Spammer

A client-only Minecraft mod (Fabric + NeoForge + Forge) that periodically sends a configurable
chat message, so your friends know you're AFK instead of ignoring them.

Built for both **Minecraft 26.2** and **Minecraft 26.1.2**.

## Features

- Sends your message on a configurable interval (seconds).
- Configure via the `/afkspam` command or an in-game config screen.
- Works on Fabric, NeoForge, and Forge. Quilt is not currently supported: as of this release,
  Quilt Loader and Quilted Fabric API have no build for either Minecraft version yet, independent
  of this mod.

## Commands

- `/afkspam on` / `/afkspam off` / `/afkspam toggle`
- `/afkspam message <text>`
- `/afkspam interval <seconds>`
- `/afkspam status`

## Config screen

- **Fabric**: install [Mod Menu](https://modrinth.com/mod/modmenu) and open it from the
  Mods screen. Requires [Cloth Config](https://modrinth.com/mod/cloth-config).
- **NeoForge**: open the Mods screen, select AFK Chat Spammer, click **Config**. Requires
  [Cloth Config](https://modrinth.com/mod/cloth-config).
- **Forge**: open the Mods screen, select AFK Chat Spammer, click **Config**. No extra dependency
  — Cloth Config dropped Forge support in late 2024, so the Forge build ships its own small
  built-in config screen using vanilla widgets.

## Downloads

Grab the jar matching your Minecraft version and loader from
[Releases](https://github.com/the-real-ltcg/afk-chat-spammer/releases):

- `afk-chat-spammer-fabric-26.2-*.jar` / `afk-chat-spammer-neoforge-26.2-*.jar` / `afk-chat-spammer-forge-26.2-*.jar` — Minecraft 26.2
- `afk-chat-spammer-fabric-26.1-*.jar` / `afk-chat-spammer-neoforge-26.1-*.jar` / `afk-chat-spammer-forge-26.1-*.jar` — Minecraft 26.1.2

## Building

Requires JDK 25.

```sh
./gradlew build
```

Builds all six platform/version combinations at once. Build a single one with, e.g.,
`./gradlew :fabric-26.2:build`. Outputs land in `<module>/build/libs/`.

### Project layout

- `common/` — shared platform-agnostic logic (config, spam timer). Not a Gradle module; compiled
  directly into every platform jar.
- `fabric/`, `neoforge/`, `forge/` — shared per-loader source and resources (identical across both
  MC versions, except one spot in the Forge screen code where MC 26.2 renamed
  `Minecraft.setScreen` to `setScreenAndShow`, so that name is used since both versions have it).
  Also not Gradle modules.
- `fabric-26.2/`, `fabric-26.1/`, `neoforge-26.2/`, `neoforge-26.1/`, `forge-26.2/`, `forge-26.1/`
  — the actual buildable Gradle modules, one per Minecraft version per loader, each pointing at the
  shared source folders above with that version's dependency coordinates.

## License

[MIT](LICENSE)
