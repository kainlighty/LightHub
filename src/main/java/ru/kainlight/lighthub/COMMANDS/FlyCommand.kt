package ru.kainlight.lighthub.COMMANDS

import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import ru.kainlight.lighthub.Main
import ru.kainlight.lighthub.UTILS.LightPlayer

class FlyCommand(private val plugin: Main) : CommandExecutor {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        if (!sender.hasPermission("lighthub.fly") || sender !is Player) return true

        val player: Player? = if (args.size == 1 && sender.hasPermission("lighthub.fly.other")) plugin.server.getPlayer(args[0]) else sender
        player?.
        let {
            this.toggleFly(it)
            return true;
        } ?: run {
            LightPlayer.of(sender).sendMessage(plugin.getMessages().getConfig().getString("player-not-found"))
            return false
        }
    }

    private fun toggleFly(player: Player): Boolean {
        val isFlying = player.allowFlight;

        if (!isFlying) {
            player.allowFlight = true
            player.isFlying = true
            return true
        } else {
            player.allowFlight = false
            player.isFlying = false
            return false
        }

        LightPlayer.of(player).sendMessage(plugin.getMessages().getConfig().getString("fly")
            ?.replace("{VALUE}", isFlying.toString()))
    }
}
