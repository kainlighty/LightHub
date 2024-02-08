package ru.kainlight.lighthub.COMMANDS

import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import ru.kainlight.lighthub.Main
import ru.kainlight.lighthub.UTILS.message

class FlyCommand(private val plugin: Main) : CommandExecutor {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        if (!sender.hasPermission("lighthub.fly") || sender !is Player) return true

        val player: Player? = if (args.size == 1 && sender.hasPermission("lighthub.fly.other")) plugin.server.getPlayer(args[0]) else sender
        player?.let {
            this.toggleFly(plugin, sender, it)
            return true;
        } ?: run {
            sender.message(plugin.getMessages().getConfig().getString("player-not-found"))
            return true
        }
    }

    private fun toggleFly(plugin: Main, sender: CommandSender, player: Player, instant: Boolean? = null): Boolean {
        if (!player.allowFlight || instant == true) {
            player.allowFlight = true
            player.isFlying = true
        } else {
            player.allowFlight = false
            player.isFlying = false
        }

        sender.message(plugin.getMessages().getConfig().getString("fly")
            ?.replace("{VALUE}", player.isFlying.toString())
            ?.replace("{PLAYER}", player.name))
        return player.allowFlight
    }
}
