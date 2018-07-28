# spigot-graveyard
Yet another spigot plugin that adds graves
## Commands
 * gtp - Teleports you to a grave(a death info card is required). Usage: ```/gtp``` holding a death info card.
 * keepinventory - Per-player one time keepinventory. Usage: ```/keepinventory <player>``` or (in-game only) ```/keepinventory```
 * giveexpvoucher - Give you a piece of paper that is a EXP voucher. Usage: ```/giveexpvoucher <amount>```
 * removeinfos - Removes death info cards from your inventory. Usage: ```/removeinfos```
## Config.yml
Default config.yml file
```
expvoucher:
  spawn_on_death: true
  value: 1
  # How much of the player's exp should be saved?
  # Value 0.1 -> 1 (all)
  enable_command: true
  # Should /giveexpvoucher be enabled?
removeinfos:
  enable_command: true
  # Should /removeinfos be enabled?
gtp:
  enable_command: true
  # Should /gtp be enabled?
keepinventory:
  enable_command: true
  # Should /keepinventory be enabled?
```
