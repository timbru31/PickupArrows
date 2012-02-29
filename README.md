PickupArrows - pickup skeleton arrows

A simple plugin to allow players to pickup arrows shot by skeletons.

## Configuration
skeletonsOnly (true): If true, only affects arrows shot by skeletons. If false, all arrows
are affected, including those shot by bows with the Infinity enchantment.

usePermissions (false): If false, anyone can pickup arrows. If true, there must be a player
with the **pickuparrows.allow** permission within *range* of the skeleton shooter in order
for the arrow to be made pickup-able.

*Note: If there is a player with the appropriate permissions in range, the arrow is
collectable by anyone. This plugin does not allow control of who can pickup the arrows.*

range (10.0): Size of the bounding box, in meters, a player must be from the skeleton
in order to change the arrow pickupability.

debug (false): Set to true for debug messages.

## Permissions
pickuparrows.allow (false): Allows you to pickup arrows you normally would not be able to


