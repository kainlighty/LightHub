package ru.kainlight.lighthub.COMMANDS

import org.bukkit.GameMode
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import org.bukkit.entity.Player
import ru.kainlight.lighthub.Main
import ru.kainlight.lighthub.UTILS.LightPlayer
import java.util.*

class GamemodeCommand(private val plugin: Main) : CommandExecutor, TabCompleter {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        if (sender !is Player || !sender.hasPermission("lighthub.gamemode")) return false

        val value: Int = args[0].toInt();
        if (args.size == 3 && sender.hasPermission("lighthub.gamemode.other")) {
            plugin.server.getPlayer(args[1])?.
            let {
                this.setGameMode(it, value)
                return true;
            } ?: run {
                LightPlayer.of(sender).sendMessage(plugin.getMessages().getConfig().getString("player-not-found"))
                return false
            }
        } else {
            this.setGameMode(sender, value)
            return true
        }
    }

    private fun setGameMode(player: Player, gamemode: Int): Unit {
        when (gamemode) {
            0 -> player.setGameMode(GameMode.SURVIVAL)
            1 -> player.setGameMode(GameMode.CREATIVE)
            2 -> player.setGameMode(GameMode.ADVENTURE)
            3 -> player.setGameMode(GameMode.SPECTATOR)
        }

        LightPlayer.of(player).sendMessage(plugin.getMessages().getConfig().getString("gamemode")
            ?.replace("{VALUE}", player.gameMode.name))
    }

    override fun onTabComplete(sender: CommandSender, command: Command, label: String, args: Array<String>): MutableList<String>? {
        if(args.size == 1 && sender.hasPermission("lighthub.gamemode")) {
            return mutableListOf("0", "1", "2", "3")
        }

        return null
    }
}
