package ru.kainlight.lighthub.COMMANDS

import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import ru.kainlight.lighthub.Main
import ru.kainlight.lighthub.UTILS.HIDER_HIDDEN_MESSAGE
import ru.kainlight.lighthub.UTILS.HIDER_SHOWN_MESSAGE
import ru.kainlight.lighthub.getAudience
import ru.kainlight.lightlibrary.multiMessage

class HideCommand : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        if(!sender.hasPermission("lighthub.visibility")) return false
        if(sender !is Player) return true

        Main.INSTANCE.server.onlinePlayers.forEach { sender.hidePlayer(it) }
        sender.getAudience().multiMessage(HIDER_HIDDEN_MESSAGE)
        return true
    }
}

class ShowCommand : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        if(!sender.hasPermission("lighthub.visibility")) return false
        if(sender !is Player) return true

        Main.INSTANCE.server.onlinePlayers.forEach { sender.showPlayer(it) }
        sender.getAudience().multiMessage(HIDER_SHOWN_MESSAGE)
        return true
    }
}