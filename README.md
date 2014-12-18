# PickupArrows [![Build Status](http://ci.dustplanet.de/job/PickupArrows/badge/icon)](http://ci.dustplanet.de/job/PickupArrows/)

## Info
This CraftBukkit plugin aims to offer the ability to pickup arrows from various source up again.
You can define any shooter source and configure burning and normal arrows.
Special features are
* Flexible configuration support, add or remove own mobs/sources of shooters
* Basic permission support on a range check
* WorldGuard support. Black or whitelist regions, where the plugin should be active!

## License
This plugin is released under the  
*Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International (CC BY-NC-SA 4.0)* license.  
Please see [LICENSE.md](LICENSE.md) for more information.

## Standard config
To add new mobs/blocks just add the name in lowercase(!) like the scheme to the config.
````yaml
# For help please refer to the bukkit dev page: http://dev.bukkit.org/bukkit-plugins/pickuparrows/
usePermissions: false
pickupFrom:
  skeleton:
    range: 10.0
    fire: true
    normal: true
  player:
    range: 10.0
    fire: true
    normal: true
  dispenser:
    range: 10.0
    fire: true
    normal: true
  unknown:
    range: 5.0
    fire: false
    normal: false
ignoreCreativeArrows: false
useWorldGuard: false
useListAsBlacklist: false
regions: []
````

## Permissions
(Fallback to OPs, if no permissions system is found)

####General permissions (EXAMPLES)
| Permission node | Description |
|:----------:|:----------:|
| pickuparrows.allow.skeleton.normal | Ability to pickup arrows (non burning) from a skeleton |
| picluparrpws.allow.skeleton.fire | Ability to pickup arrows (burning) from a skeleton |
| pickuparrows.allow.dispenser.normal | Ability to pickup arrows (non burning) from a dispenser |
| picluparrpws.allow.dispenser.fire | Ability to pickup arrows (burning) from a dispenser |


####Special permissions
* pickuparrows.* - Grants access to ALL other permissions (better use the allow permission instead!)
* pickuparrows.allow.* - Grants access to ALL permissions for pickup

## Credits
* mushroomhostage for the neat idea!

## Support
For support visit the dev.bukkit.org page: http://dev.bukkit.org/bukkit-plugins/pickuparrows/

## Pull Requests
Feel free to submit any PRs here. :)  
Please follow the Sun Coding Guidelines, thanks!

## Donation
[![alt text](https://www.paypalobjects.com/en_US/i/btn/btn_donateCC_LG.gif "Donation via PayPal")](https://www.paypal.com/cgi-bin/webscr?cmd=_s-xclick&hosted_button_id=T9TEV7Q88B9M2)

![alt text](https://dl.dropboxusercontent.com/u/26476995/bitcoin_logo.png "Donation via BitCoins")  
Address: 1NnrRgdy7CfiYN63vKHiypSi3MSctCP55C
