# Changelog for [Fish N' Ships](https://github.com/hybridlabs/fish-n-ships)

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]

- Updated to Minecraft 26.3
- Fixed boats, canoes, dinghies and rafts stuttering while moving, turning, drifting unoccupied,
  or being led on a lead
- Fixed boat, canoe and dinghy paddles not rowing and resting in the wrong position
- Fixed sailboats' sail animating when first rendered (extending down, then vanishing) after the
  GeckoLib 5 migration

<details>
<summary>Developer notes</summary>

- Migrated to GeckoLib 5: models, renderers and layers are driven by render states, and bone
  adjustments happen in the renderer instead of the model
- GeckoLib assets moved to `assets/<namespace>/geckolib/{models,animations}` and resource ids use
  the stripped form
- Items set their registry id before construction, as required since 1.21.5
- Updated entity save data, damage handling, container loot tables and the fuel lookup to the
  1.21.11 APIs

</details>

## [1.0.0] 2025-08-22

Initial public release.