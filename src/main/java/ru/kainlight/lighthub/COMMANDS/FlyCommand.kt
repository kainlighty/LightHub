package ru.kainlight.lighthub.COMMANDS

import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import ru.kainlight.lighthub.Main

class FlyCommand(private val plugin: Main) : CommandExecutor {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        if (!sender.hasPermission("lighthub.fly")) return true
        if (sender !is Player) return true

        if (args.size == 1) {
            if (!args[0].matches("\\d".toRegex())) {
                val username = args[0]
                val player = plugin.server.getPlayer(username) ?: return false
                this.toggleFly(player)
                return true;
            } else {
                sender.flySpeed = args[0].toFloat() / 10.0f
                return true
            }
        }

        this.toggleFly(sender)

        return true
    }

    private fun toggleFly(player: Player): Boolean {
        if (!player.allowFlight) {
            player.allowFlight = true
            player.isFlying = true
            return true
        } else {
            player.allowFlight = false
            player.isFlying = false
            return false
        }
    }
}
