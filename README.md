# EnoSpigot

Fork of [PandaSpigot](https://github.com/hpfxd/PandaSpigot) for Minecraft 1.8.8,
customized for the [Enofight](https://github.com/mimiflo/EnoInfrastructure) network.

PandaSpigot itself is a fork of Paper 1.8.8, focused on performance and
stability. EnoSpigot keeps that base and layers our own patches on top so we can
modify the server however we need for Enofight.

## Why a fork?

Running a competitive 1.8 network means we need the freedom to patch the server
itself — custom knockback, new events, protocol tweaks, etc. Forking
PandaSpigot lets us:

- Add our own patches in `patches/server/` and `patches/api/`
- Pull upstream improvements from PandaSpigot whenever we want
- Ship a single `enospigot-paperclip.jar` we fully control

## Building

Requirements:
- JDK 17 (required to run the decompiler — the output JAR stays Java 8 compatible)
- Git
- Bash

```bash
./panda jar
```

Produces `paperclip.jar` at the repository root. Copy it into
`EnoInfrastructure/docker/gameserver/enospigot.jar` to ship it with the
gameserver image.

## Useful commands

- `./panda setup` — remap, decompile, apply patches (first run only)
- `./panda jar` — apply patches and build (alias `./panda j`)
- `./panda patch` — apply patches without building (alias `./panda p`)
- `./panda rebuild` — regenerate patches from current source state (alias `./panda rb`)
- `./panda clean` — remove generated dirs (alias `./panda c`)

## Contributing patches

Add new changes as patches on top of the existing stack. When modifying
upstream (Paper/PandaSpigot) code:

- Multi-line changes: `// EnoSpigot start` / `// EnoSpigot end`
- Multi-line with reason: `// EnoSpigot start - <reason>`
- One-line: `// EnoSpigot` at end of line

Existing PandaSpigot patches keep their `// PandaSpigot` markers so upstream
merges stay clean.

## Upstream

Remote name `upstream` points at `hpfxd/PandaSpigot`. To pull their latest:

```bash
git fetch upstream
git merge upstream/master
```

Resolve any patch conflicts, then `./panda rebuild` to refresh `patches/`.

## License

Inherited from PandaSpigot / Paper / Spigot. See `LICENSE.md`.
