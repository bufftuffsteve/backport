![Wilderness Bound: Dappled Forest Backport](docs/banner.png)

# Wilderness Bound: Dappled Forest Backport

A mod for NeoForge, Forge and Fabric that backports the autumn content from **Minecraft 26.3 "Wilderness Bound"** to **1.21.1**: the Dappled Forest biome, poplar wood, cushions, straw beds, shelf mushrooms and abandoned camps.

Everything is registered under vanilla's own IDs (`minecraft:poplar_log`, `minecraft:dappled_forest`, ...), so worlds made with this mod keep their blocks when upgraded to 26.3.

## Features

- **Dappled Forest biome**: orange grass, red/orange/yellow poplar trees, shelf mushrooms, fallen logs, leaf litter, red shrubs and coarse dirt patches
- **Abandoned camps**: tents and campsites with cushions, straw beds and loot
- **Poplar wood set**: logs, wood, planks, stairs, slabs, fences, gates, doors, trapdoors, buttons, pressure plates, signs, hanging signs, boats and chest boats
- **Cushions** (16 colors), **straw beds**, **shelf mushrooms**, **leaf litter**
- **Wool and concrete stairs and slabs** in all 16 colors
- Wandering trader trades for poplar saplings, logs and shelf mushrooms

## Requirements

Minecraft 1.21.1, one of these loaders, and [TerraBlender](https://modrinth.com/mod/terrablender) 4.1.0.8 or newer (required on every loader).

| Loader | Version | Also needs |
| --- | --- | --- |
| NeoForge | 21.1.250 or newer | |
| Forge | 52.0.0 or newer | |
| Fabric | Loader 0.16.9 or newer | [Fabric API](https://modrinth.com/mod/fabric-api) |

A 1.20.1 version for Forge and Fabric lives on the `1.20.1` branch.

## Building from source

Requires JDK 21.

```sh
./gradlew build                 # all jars; each loader's ends up in <loader>/build/libs
./gradlew :neoforge:runData     # regenerate models, recipes, loot tables, tags and worldgen into common/src/generated
./gradlew :fabric:runClient     # launch a test game (also :forge:runClient, :neoforge:runClient)
./gradlew :fabric:prodServer    # run a real Fabric server with the finished jar
```

On Windows use `gradlew.bat`, or run the same tasks from IntelliJ's Gradle panel.

## Project layout

The mod is written once in `common` and wrapped by a small module per loader.

| Path | Contents |
| --- | --- |
| `common/src/main/java/.../block` | Custom blocks: shelf mushroom, red shrub, straw bed, leaf litter, poplar leaves and sapling |
| `common/src/main/java/.../entity` | The cushion entity and item |
| `common/src/main/java/.../world` | Poplar tree shapes, tree decorations, fallen trees, the Dappled Forest biome and its TerraBlender region |
| `common/src/main/java/.../registry` | Block, item, entity and other registrations, plus creative tabs, trades and other game rules |
| `common/src/main/java/.../mixin` | Small hooks into vanilla code, shared by all loaders |
| `common/src/main/java/.../platform` | The few things each loader does its own way (registration, flower pots, the boat type) |
| `common/src/main/resources` | Textures, hand-written models, camp structures and loot tables |
| `common/src/generated/resources` | Output of `runData`. Don't edit by hand |
| `neoforge/`, `forge/`, `fabric/` | Each loader's entry point, events and metadata. Data generators live in `neoforge/` |

## Differences from 26.3

1.21.1 can't do everything 26.3 does, so a few things differ:

- New 26.3 sounds use the closest 1.21.1 sounds
- Fallen trees only generate when the whole log fits (no lone stumps)
- Copper chests, lanterns and copper gear in camps are replaced with 1.21.1 equivalents
- Placed boats become oak boats when a world is upgraded to 26.3
- NeoForge saves poplar boats as `autumnbackport:poplar` and Forge/Fabric as `poplar`, so placed poplar boats don't carry over if a world switches between NeoForge and Forge/Fabric

## License

MIT, so anyone can use this mod's content in their own projects. See `LICENSE`.
