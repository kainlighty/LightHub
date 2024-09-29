package ru.kainlight.lighthub.COMMANDS

import org.bukkit.GameMode
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import org.bukkit.entity.Player
import ru.kainlight.lighthub.Main
import ru.kainlight.lighthub.getAudience
import ru.kainlight.lightlibrary.multiMessage

class Gamemode(private val plugin: Main) : CommandExecutor, TabCompleter {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        if (!sender.hasPermission("lighthub.gamemode")) return false
        if(args.isEmpty()) return false

        val value: Int = args[0].toIntOrNull() ?: return false

        if (args.size == 2 && sender.hasPermission("lighthub.gamemode.other")) {
            plugin.server.getPlayer(args[1])?.let {
                this.setGameMode(sender, it, value)
                return true
            } ?: run {
                plugin.messageConfig.getConfig().getString("player-not-found")?.let {
                    sender.getAudience().multiMessage(it.replace("{PLAYER}", args[1]))
                }
                return true
            }
        } else if (args.size == 1 && sender is Player) {
            this.setGameMode(sender, sender, value)
            return true
        } else return false
    }

    private fun setGameMode(sender: CommandSender, player: Player, gamemode: Int) {
        when (gamemode) {
            0 -> player.gameMode = GameMode.SURVIVAL
            1 -> player.gameMode = GameMode.CREATIVE
            2 -> player.gameMode = GameMode.ADVENTURE
            3 -> player.gameMode = GameMode.SPECTATOR
        }

        plugin.messageConfig.getConfig().getString("gamemode")?.let {
            sender.getAudience().multiMessage(it.replace("{VALUE}", player.gameMode.name).replace("{PLAYER}", player.name))
        }
    }

    override fun onTabComplete(sender: CommandSender, command: Command, label: String, args: Array<String>): MutableList<String>? {
        if(args.size == 1 && sender.hasPermission("lighthub.gamemode")) {
            return mutableListOf("0", "1", "2", "3")
        }
        return null
    }
}
