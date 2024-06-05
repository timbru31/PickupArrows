# PickupArrows

[![Build Status](https://ci.dustplanet.de/job/PickupArrows/badge/icon)](https://ci.dustplanet.de/job/PickupArrows/)
[![Build the plugin](https://github.com/timbru31/PickupArrows/workflows/Build%20the%20plugin/badge.svg)](https://github.com/timbru31/PickupArrows/actions?query=workflow%3A%22Build+the+plugin%22)

[![BukkitDev](https://img.shields.io/badge/BukkitDev-v6.2.4-orange.svg)](https://dev.bukkit.org/projects/pickuparrows)
[![SpigotMC](https://img.shields.io/badge/SpigotMC-v6.2.4-orange.svg)](https://www.spigotmc.org/resources/pickuparrows.8073/)

[![License](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)

## Info

This CraftBukkit/Spigot plugin aims to offer the ability to pickup arrows from various source up again.
You can define any shooter source and configure burning, normal, spectral and tipped arrows.
Special features are

- Flexible configuration support, add or remove own mobs/sources of shooters
- Permission to control the pickup
- WorldGuard support. Allow or deny regions where the plugin should be active!
- Support for normal, fire, spectral or tipped arrows and tridents
- Command to toggle the status of picking up arrows
- Crossbow, Drowned and Pillager support

Third party features, all of them can be disabled

- bStats for usage statistics

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
    trident: true
  player:
    fire: true
    normal: true
    spectral: true
    tipped: true
    trident: true
    crossbow:
      fire: true
      normal: true
      spectral: true
      tipped: true
      multishot:
        fire: false
        normal: false
        spectral: false
        tipped: false
  dispenser:
    fire: true
    normal: true
    spectral: true
    tipped: true
    trident: true
  drowned:
    fire: true
    normal: true
    spectral: true
    tipped: true
    trident: true
  pillager:
    fire: true
    normal: true
    spectral: true
    tipped: true
    trident: true
  unknown:
    fire: false
    normal: false
    spectral: false
    tipped: false
    trident: false
ignoreCreativeArrows: false
useWorldGuard: false
useListAsDenylist: false
regions: []
```

## Permissions

(Fallback to OPs, if no permissions system is found)

#### General permissions

| Permission node             | Description                                   |
| :-------------------------- | :-------------------------------------------- |
| pickuparrows.allow.normal   | Ability to pickup normal arrows (non burning) |
| picluparrpws.allow.fire     | Ability to pickup fire arrows (burning)       |
| picluparrpws.allow.spectral | Ability to pickup spectral arrows             |
| picluparrpws.allow.tipped   | Ability to pickup tipped arrows               |

#### Special permissions

- pickuparrows.\* - Grants access to ALL other permissions (better use the allow permission instead!)
- pickuparrows.allow.\* - Grants access to ALL permissions for pickup

## Commands

Aliases:

- pa
- pickuparrow
- arrow
- arrows

|    Command    |        Description        |
| :-----------: | :-----------------------: |
| /pickuparrows | Toggles the pickup status |

## Credits

- mushroomhostage for the neat idea!

## Support

For support visit the dev.bukkit.org page: https://dev.bukkit.org/projects/pickuparrows

## Pull Requests

Feel free to submit any PRs here. :)
Please follow the Sun Coding Guidelines, thanks!

## Usage statistics

[![Usage statistics](https://bstats.org/signatures/bukkit/PickupArrows.svg)](https://bstats.org/plugin/bukkit/PickupArrows/284)

## Data usage collection of bStats

#### Disabling bStats

The file `./plugins/bStats/config.yml` contains an option to opt-out.

#### The following data is **read and sent** to https://bstats.org and can be seen under https://bstats.org/plugin/bukkit/PickupArrows

- Your server's randomly generated UUID
- The amount of players on your server
- The online mode of your server
- The bukkit version of your server
- The java version of your system (e.g. Java 8)
- The name of your OS (e.g. Windows)
- The version of your OS
- The architecture of your OS (e.g. amd64)
- The system cores of your OS (e.g. 8)
- bStats-supported plugins
- Plugin version of bStats-supported plugins

## Donation

[![paypal logo](https://www.paypalobjects.com/en_US/i/btn/btn_donateCC_LG.gif "Donation via PayPal")](https://www.paypal.com/cgi-bin/webscr?cmd=_s-xclick&hosted_button_id=T9TEV7Q88B9M2)

![BitCoin](https://dustplanet.de/wp-content/uploads/2015/01/bitcoin-logo-plain.png "Donation via BitCoins")  
1NnrRgdy7CfiYN63vKHiypSi3MSctCP55C

## Support

In addition to reporting bugs here on GitHub you can join my Discord and ask your questions right away!
[![Discord support](https://discordapp.com/api/guilds/387315912283521027/widget.png?style=banner2)](https://discord.gg/ZjKUkAXus6)

---

Built by (c) Tim Brust and contributors. Released under the MIT license.  
Originally released under the BSD-3 license by Mushroom Hostage
