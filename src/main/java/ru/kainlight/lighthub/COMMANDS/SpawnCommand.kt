package ru.kainlight.lighthub.COMMANDS

import org.bukkit.Location
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import org.bukkit.entity.Player
import ru.kainlight.lighthub.Main
import ru.kainlight.lighthub.UTILS.SPAWN_LOCATION
import ru.kainlight.lighthub.lightlibrary.message

class SpawnCommand : CommandExecutor, TabCompleter {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        if(sender !is Player) return false;

        if(args.isEmpty() && sender.hasPermission("lighthub.spawn")) SPAWN_LOCATION?.let {
            sender.teleport(it)
            return true
        }
        else if (args.size == 1) {
            if (sender.hasPermission("lighthub.spawn.create") && args[0] == "create") {
                val spawnSection = Main.INSTANCE.getSpawnConfig().getConfig();
                val location: Location = sender.location;

                spawnSection.set("world", sender.world.name)
                spawnSection.set("x", location.x)
                spawnSection.set("y", location.y)
                spawnSection.set("z", location.z)
                spawnSection.set("yaw", location.yaw)
                spawnSection.set("pitch", location.pitch)

                SPAWN_LOCATION = sender.location
                Main.INSTANCE.getSpawnConfig().saveConfig()
                sender.message("<green>The location for spawn is set")
                return true
            } else {
                Main.INSTANCE.server.getPlayer(args[0])?.let {
                    it.teleport(SPAWN_LOCATION!!)
                    return true
                } ?: run {
                    sender.message(Main.INSTANCE.getMessages().getConfig().getString("player-not-found"))
                    return true;
                }
            }
        }

        return true;
    }

    override fun onTabComplete(sender: CommandSender, command: Command, label: String, args: Array<String>): MutableList<String>? {
        if(sender.hasPermission("lighthub.spawn.create") && args.size == 1) {
            return mutableListOf("create")
        }

        return null
    }
}