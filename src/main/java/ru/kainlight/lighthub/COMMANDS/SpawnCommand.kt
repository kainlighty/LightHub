package ru.kainlight.lighthub.COMMANDS

import org.bukkit.Location
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import org.bukkit.entity.Player
import ru.kainlight.lighthub.Main
import ru.kainlight.lighthub.UTILS.LightPlayer
import ru.kainlight.lighthub.UTILS.SPAWN_LOCATION

class SpawnCommand : CommandExecutor, TabCompleter {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        if(sender !is Player) return false;

        if(args.isEmpty() && sender.hasPermission("lighthub.spawn")) SPAWN_LOCATION?.let { sender.teleport(it) }
        else if (args.size == 1) {
            if (sender.hasPermission("lighthub.spawn.create") && args[0] == "create") {
                val spawnSection = Main.getInstance().getSpawnConfig().getConfig();
                val location: Location = sender.location;

                spawnSection.set("world", sender.world.name)
                spawnSection.set("x", location.x)
                spawnSection.set("y", location.y)
                spawnSection.set("z", location.z)
                spawnSection.set("yaw", location.yaw)
                spawnSection.set("pitch", location.pitch)

                SPAWN_LOCATION = sender.location
                Main.getInstance().getSpawnConfig().saveConfig()
                LightPlayer.of(sender).sendMessage("<green>The location for spawn is set")
            } else {
                Main.getInstance().server.getPlayer(args[0])?.teleport(SPAWN_LOCATION!!) ?: run {
                    LightPlayer.of(sender).sendMessage(Main.getInstance().getMessages().getConfig().getString("player-not-found"))
                    return false;
                }
            }
        }

        return false;
    }

    override fun onTabComplete(sender: CommandSender, command: Command, label: String, args: Array<String>): MutableList<String>? {
        if(sender.hasPermission("lighthub.spawn.create") && args.size == 1) {
            return mutableListOf("create")
        }

        return null
    }
}