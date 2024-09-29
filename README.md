# Manage your lobby!

## — Required: Java 17

## › Functions

- ### Chat Management:
  - ### Disabling the chat
  - ### Whitelist of allowed commands
- ### Player Attributes:
  - ### Damage is disabled
  - ### Hunger is disable
  - ### Inventory is cleared on exit
- ### Item Handling:
  - ### Dropping disabled
  - ### Moving in inventory disabled
  - ### Pickup items disabled
- ### World Interaction:
  - ### Breaking blocks is disabled
  - ### Placing blocks is disabled
- ### Player Visibility:
  - ### Players can be hidden using a hider item or /hide and /show commands _(if hider not enabled)_
- ### Teleportation:
  - ### Players are teleported to spawn when falling into the void

## › Commands and Permissions

| Command          | Permission            |
|------------------|-----------------------|
| spawn create     | lighthub.spawn.create |
| spawn            | lighthub.spawn        |
| fly              | lighthub.fly          |
| speed            | lighthub.speed        |
| gm               | lighthub.gamemode     |
| hide             | lighthub.hide         |
| show             | lighthub.show         |
| lighthubreload   | lighthub.reload       |

## › Permissions

| Permission                            | Description                                                                             |
|---------------------------------------|-----------------------------------------------------------------------------------------|
| lighthub.*                            | Full access                                                                             |
| lighthub.spawn.create                 | Create spawnpoint                                                                       |
| lighthub.spawn _(default: true)_      | For spawn teleportation                                                                 |
| lighthub.chat                         | Access to the chat if it is disabled                                                    |
| lighthub.chat.commands                | Allows all commands to be used _(those that are not in the allowed_commands parameter)_ |
| lighthub.chat.*                       | Full access to chat                                                                     |
| lighthub.visibility _(default: true)_ | Allows you to hide players with hider item or command                                   |
| lighthub.block.*                      | Allows all interactions with blocks                                                     | 
| lighthub.block.place                  | Allows to put blocks                                                                    |
| lighthub.block.break                  | Allows to break blocks                                                                  |
| lighthub.inv.*                        | Allows all interactions with inventory                                                  |
| lighthub.inv.drop                     | Allows to drop items from inventory                                                     |
| lighthub.inv.move                     | Allows to move items in inventory                                                       |
| lighthub.inv.pickup                   | Allows to pick up items in inventory                                                    |
| lighthub.gamemode.*                   | Full access to gamemode                                                                 |
| lighthub.gamemode                     | Allows to change the gamemode                                                           |
| lighthub.gamemode                     | Allows to change the gamemode                                                           |
| lighthub.gamemode.other               | Allows changing the gamemode of another player                                          |
| lighthub.fly.*                        | Full access to fly                                                                      |
| lighthub.fly                          | Allows to change fly mode                                                               |
| lighthub.fly.other                    | Allows changing the fly mode of another player                                          |
| lighthub.speed.*                      | Full access to speed                                                                    |
| lighthub.speed                        | Allows to change speed                                                                  |
| lighthub.speed.other                  | Allows changing the speed of another player                                             |
| lighthub.reload                       | Access to reload configurations                                                         |