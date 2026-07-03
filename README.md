# AFK Chat Spammer

A client-only Minecraft mod (Fabric + NeoForge, Minecraft 26.2) that periodically sends a
configurable chat message, so you don't get kicked for being AFK.

## Features

- Sends your message on a configurable interval (seconds).
- Configure via the `/afkspam` command or an in-game config screen.
- Works on Fabric and NeoForge. Also runs fine on Quilt (Quilt loads standard Fabric mods).

## Commands

- `/afkspam on` / `/afkspam off` / `/afkspam toggle`
- `/afkspam message <text>`
- `/afkspam interval <seconds>`
- `/afkspam status`

## Config screen

- **Fabric / Quilt**: install [Mod Menu](https://modrinth.com/mod/modmenu) and open it from the
  Mods screen.
- **NeoForge**: open the Mods screen, select AFK Chat Spammer, click **Config**.

Both require [Cloth Config](https://modrinth.com/mod/cloth-config).

## Building

Requires JDK 25.

```sh
./gradlew build
```

Outputs land in `fabric/build/libs/` and `neoforge/build/libs/`.

## License

[MIT](LICENSE)
