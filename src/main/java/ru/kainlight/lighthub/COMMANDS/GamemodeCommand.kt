package ru.kainlight.lighthub.COMMANDS

import org.bukkit.GameMode
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import org.bukkit.entity.Player
import ru.kainlight.lighthub.Main
import ru.kainlight.lighthub.UTILS.message

class GamemodeCommand(private val plugin: Main) : CommandExecutor, TabCompleter {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        if (!sender.hasPermission("lighthub.gamemode")) return false
        if(args.isEmpty()) return false;

        val value: Int = args[0].toIntOrNull() ?: return false;
        if (args.size == 2 && sender.hasPermission("lighthub.gamemode.other")) {
            plugin.server.getPlayer(args[1])?.let {
                this.setGameMode(sender, it, value)
                return true;
            } ?: run {
                sender.message(plugin.getMessages().getConfig().getString("player-not-found"))
                return true
            }
        } else if (args.size == 1 && sender is Player) {
            this.setGameMode(sender, sender, value)
            return true
        } else return false;
    }

    private fun setGameMode(sender: CommandSender, player: Player, gamemode: Int): Unit {
        when (gamemode) {
            0 -> player.setGameMode(GameMode.SURVIVAL)
            1 -> player.setGameMode(GameMode.CREATIVE)
            2 -> player.setGameMode(GameMode.ADVENTURE)
            3 -> player.setGameMode(GameMode.SPECTATOR)
        }

        sender.message(plugin.getMessages().getConfig().getString("gamemode")
            ?.replace("{VALUE}", player.gameMode.name)
            ?.replace("{PLAYER}", player.name))
    }

    override fun onTabComplete(sender: CommandSender, command: Command, label: String, args: Array<String>): MutableList<String>? {
        if(args.size == 1 && sender.hasPermission("lighthub.gamemode")) {
            return mutableListOf("0", "1", "2", "3")
        }

        return null
    }
}
