This is the README of PickupArrows
Thanks to mushroomhostage for the original plugin!
Thanks for using!
For support visit the dev.bukkit.org page: 
http://dev.bukkit.org/bukkit-plugins/pickuparrows/

This plugin is released under the Creative Commons Attribution-NonCommercial-ShareAlike 3.0 Unported (CC BY-NC-SA 3.0) license.

Standard config:

# For help please either refer to the bukkit dev page:
# http://dev.bukkit.org/bukkit-plugins/pickuparrows/
usePermissions: false
range: 10.0
pickupFrom:
  skeleton: true
  player: true
  other: false
  fire: false
useWorldGuard: false
useListAsBlacklist: false
regions: []

Permissions (if no permissions system is detected, only OPs are able to pickup arrows!)
Only bukkit's permissions system is supported!

pickuparrows.*
Description: Includes all sub permissions

pickuparrows.allow.*
Description: Includes all permissions for collecting arrows

pickuparrows.allow.fire
Description: Allows you to pickup fire arrows

pickuparrows.allow.skeleton
Description: Allows you to pickup arrows from skeletons

pickuparrows.allow.player
Description: Allows you to pickup arrows from players

pickuparrows.allow.other
Description: Allows you to pickup arrows from other sources like a dispenser