package ru.kainlight.lighthub.COMMANDS

import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import ru.kainlight.lighthub.Main
import ru.kainlight.lighthub.UTILS.loadDefaultConfig
import ru.kainlight.lighthub.getAudience
import ru.kainlight.lightlibrary.multiMessage

class Reload(val plugin: Main) : CommandExecutor {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if(!sender.hasPermission("lighthub.reload")) return true

        Main.INSTANCE.reloadConfig()
        Main.INSTANCE.messageConfig.reloadConfig()
        Main.INSTANCE.getSpawnConfig().reloadConfig()

        loadDefaultConfig(plugin)

        Main.INSTANCE.messageConfig.getConfig().getString("reload")?.let {
            sender.getAudience().multiMessage(it)
        }
        return true
    }
}