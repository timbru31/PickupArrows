# PickupArrows
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/684953fa3f2947fcbd766971f954ab1d)](https://www.codacy.com/app/timbru31/PickupArrows?utm_source=github.com&utm_medium=referral&utm_content=timbru31/PickupArrows&utm_campaign=badger)
[![Build Status](https://ci.dustplanet.de/job/PickupArrows/badge/icon)](https://ci.dustplanet.de/job/PickupArrows/)
[![Build Status](https://travis-ci.org/timbru31/PickupArrows.svg?branch=master)](https://travis-ci.org/timbru31/PickupArrows)
[![Circle CI](https://img.shields.io/circleci/project/timbru31/PickupArrows.svg)](https://circleci.com/gh/timbru31/PickupArrows)
[![Build status](https://ci.appveyor.com/api/projects/status/cbw34npfxv7v4kup?svg=true)](https://ci.appveyor.com/project/timbru31/pickuparrows)
[![BukkitDev](https://img.shields.io/badge/BukkitDev-v3.0.10-orange.svg)](https://dev.bukkit.org/projects/pickuparrows)
[![SpigotMC](https://img.shields.io/badge/SpigotMC-v3.0.10-orange.svg)](https://www.spigotmc.org/resources/pickuparrows.8073/)
[![License](https://img.shields.io/badge/License-CC%20BY--NC--SA%204.0-blue.svg)](LICENSE.md)

## Info
This CraftBukkit/Spigot plugin aims to offer the ability to pickup arrows from various source up again.
You can define any shooter source and configure burning, normal, spectral and tipped arrows.
Special features are
* Flexible configuration support, add or remove own mobs/sources of shooters
* Permission to control the pickup
* WorldGuard support. Black or whitelist regions, where the plugin should be active!
* Support for normal, fire, spectral or tipped arrows

## License
This plugin is released under the
*Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International (CC BY-NC-SA 4.0)* license.
Please see [LICENSE.md](LICENSE.md) for more information.

## Standard config
To add new mobs/blocks just add the name in lowercase(!) like the scheme to the config.
```yaml
# For help please refer to the bukkit dev page: https://dev.bukkit.org/projects/pickuparrows
usePermissions: false
pickupFrom:
  skeleton:
    fire: true
    normal: true
    spectral: true
    tipped: true
  player:
    fire: true
    normal: true
    spectral: true
    tipped: true
  dispenser:
    fire: true
    normal: true
    spectral: true
    tipped: true
  unknown:
    fire: false
    normal: false
    spectral: false
    tipped: false
ignoreCreativeArrows: false
useWorldGuard: false
useListAsBlacklist: false
regions: []
```

## Permissions
(Fallback to OPs, if no permissions system is found)

#### General permissions
| Permission node | Description |
|:----------:|:----------:|
| pickuparrows.allow.normal | Ability to pickup normal arrows (non burning) |
| picluparrpws.allow.fire | Ability to pickup fire arrows (burning) |
| picluparrpws.allow.spectral | Ability to pickup spectral arrows |
| picluparrpws.allow.tipped | Ability to pickup tipped arrows |


#### Special permissions
* pickuparrows.* - Grants access to ALL other permissions (better use the allow permission instead!)
* pickuparrows.allow.* - Grants access to ALL permissions for pickup

## Credits
* mushroomhostage for the neat idea!

## Support
For support visit the dev.bukkit.org page: https://dev.bukkit.org/projects/pickuparrows

## Pull Requests
Feel free to submit any PRs here. :)
Please follow the Sun Coding Guidelines, thanks!

## Donation
[![paypal logo](https://www.paypalobjects.com/en_US/i/btn/btn_donateCC_LG.gif "Donation via PayPal")](https://www.paypal.com/cgi-bin/webscr?cmd=_s-xclick&hosted_button_id=T9TEV7Q88B9M2)

![bitcoin logo](https://dl.dropboxusercontent.com/u/26476995/bitcoin_logo.png "Donation via BitCoins")<br>
Address: 1NnrRgdy7CfiYN63vKHiypSi3MSctCP55C
