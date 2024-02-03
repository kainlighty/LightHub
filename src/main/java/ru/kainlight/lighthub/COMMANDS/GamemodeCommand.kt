package ru.kainlight.lighthub.COMMANDS

import org.bukkit.GameMode
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import ru.kainlight.lighthub.Main
import java.util.*

class GamemodeCommand(private val plugin: Main) : CommandExecutor {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        if (!sender.hasPermission("lighthub.gamemode")) return true
        if (sender !is Player) return true
        if(args.isEmpty()) return false

        if (args.size == 3) {
            val username = args[1]
            val value: Int = args[2].toInt();
            val player = plugin.server.getPlayer(username) ?: return false;

            this.setGameMode(player, value)
            return true;
        }

        val gamemodeNumber = args[0].lowercase(Locale.getDefault())
        this.setGameMode(sender, gamemodeNumber.toInt())
        return true
    }

    private fun setGameMode(player: Player, gamemode: Int): Unit {
        when (gamemode) {
            0 -> player.setGameMode(GameMode.SURVIVAL)
            1 -> player.setGameMode(GameMode.CREATIVE)
            2 -> player.setGameMode(GameMode.ADVENTURE)
            3 -> player.setGameMode(GameMode.SPECTATOR)
        }
    }
}
