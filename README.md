PickupArrows - pickup skeleton arrows

A simple plugin to allow players to pickup arrows shot by skeletons.

**[Download PickupArrows 1.2](http://dev.bukkit.org/server-mods/pickuparrows/files/3-pickup-arrows-1-2/)** - updated for 1.2.5-R1.0

[Forum thread](http://forums.bukkit.org/threads/mech-pickuparrows-v1-2-pickup-skeleton-arrows-1-2-5-r1-0.70050/)

## Configuration
skeletonsOnly (true): If true, only affects arrows shot by skeletons. If false, all arrows
are affected, including those shot by bows with the Infinity enchantment.

usePermissions (false): If false, anyone can pickup arrows. If true, there must be a player
with the **pickuparrows.allow** permission within *range* of the skeleton shooter in order
for the arrow to be made pickup-able.

*Note: If there is a player with the appropriate permissions in range, the arrow is
collectable by anyone. This plugin does not allow control of who can pickup the arrows.*

range (10.0): Size of the bounding box, in meters, a player must be from the shooter
in order to change the arrow pickupability.

debug (false): Set to true for debug messages.

## Permissions
pickuparrows.allow (false): Allows you to pickup arrows you normally would not be able to

## Notes
Requested on Bukkit plugin requests forum here: [Pickup Skeleton Arrows](http://forums.bukkit.org/threads/pickup-skeleton-arrows.57424/)

and on MC Port Central here: [REQ Immibis Mods](http://www.mcportcentral.co.za/index.php?topic=1609.0)

This plugin is similar to the [Pickuppable Skeleton Arrows](http://www.minecraftforum.net/topic/1001131-110-immibiss-mods-smp/) mod by immibis, 
but shares no code, and was written from scratch.

