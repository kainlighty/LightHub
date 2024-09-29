package ru.kainlight.lighthub.COMMANDS

import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import ru.kainlight.lighthub.Main
import ru.kainlight.lighthub.getAudience
import ru.kainlight.lightlibrary.multiMessage

class Speed(private val plugin: Main) : CommandExecutor {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        if (!sender.hasPermission("lighthub.speed") || sender !is Player) return true

        val player: Player? = if (args.size == 2 && sender.hasPermission("lighthub.speed.other")) plugin.server.getPlayer(args[1]) else sender
        var speed = args.getOrNull(0)?.toFloatOrNull() ?: return false

        if (speed < 1) speed = 1.0f
        if (speed > 10) speed = 10.0f

        val normalizedSpeed = when (speed) {
            1.0f -> 0.2f // Стандартная скорость
            else -> 0.2f + ((speed - 1) * 0.1f) // + По 0.1f за каждую единицу после 1
        }

        val username = player?.name ?: sender.name

        player?.let {
            player.walkSpeed = normalizedSpeed.coerceIn(0.0f, 1.0f) // В диапазоне от 0.0 до 1.0
            player.flySpeed = normalizedSpeed / 2 // Здесь скорость вдвое ниже

            plugin.messageConfig.getConfig().getString("speed")?.let { msg ->
                sender.getAudience().multiMessage(msg.replace("{VALUE}", speed.toString()).replace("{PLAYER}", username))
            }
        } ?: run {
            plugin.messageConfig.getConfig().getString("player-not-found")?.let { msg ->
                sender.getAudience().multiMessage(msg.replace("{PLAYER}", username))
            }
        }

        return true
    }

}
