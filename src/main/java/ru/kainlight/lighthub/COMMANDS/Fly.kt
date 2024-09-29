package ru.kainlight.lighthub.COMMANDS

import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import ru.kainlight.lighthub.Main
import ru.kainlight.lighthub.getAudience
import ru.kainlight.lightlibrary.multiMessage

class Fly(private val plugin: Main) : CommandExecutor {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        if (!sender.hasPermission("lighthub.fly") || sender !is Player) return true

        val player: Player? = if (args.size == 1 && sender.hasPermission("lighthub.fly.other")) plugin.server.getPlayer(args[0]) else sender
        val username = player?.name ?: sender.name

        player?.let {
            this.toggleFly(plugin, sender, it)
            return true
        } ?: run {
            plugin.messageConfig.getConfig().getString("player-not-found")?.let {
                sender.getAudience().multiMessage(it.replace("{PLAYER}", username))
            }
            return true
        }
    }

    private fun toggleFly(plugin: Main, sender: CommandSender, player: Player, instant: Boolean? = null) {
        if (!player.allowFlight || instant == true) {
            player.allowFlight = true
            player.isFlying = true
        } else {
            player.allowFlight = false
            player.isFlying = false
        }

        plugin.messageConfig.getConfig().getString("fly")?.let {
            sender.getAudience().multiMessage(it.replace("{VALUE}", player.isFlying.toString()).replace("{PLAYER}", player.name))
        }
    }
}
