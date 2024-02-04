package ru.kainlight.lighthub.COMMANDS

import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import ru.kainlight.lighthub.Main
import ru.kainlight.lighthub.UTILS.HIDER_HIDDEN_MESSAGE
import ru.kainlight.lighthub.UTILS.HIDER_SHOWN_MESSAGE
import ru.kainlight.lighthub.UTILS.LightPlayer

class HideCommand : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        if(!sender.hasPermission("lighthub.visibility")) return false
        if(sender !is Player) return false;

        Main.getInstance().server.onlinePlayers.forEach { sender.hidePlayer(it) }
        LightPlayer.of(sender).sendMessage(HIDER_HIDDEN_MESSAGE)
        return true
    }
}

class ShowCommand : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        if(!sender.hasPermission("lighthub.visibility")) return false
        if(sender !is Player) return false;

        Main.getInstance().server.onlinePlayers.forEach { sender.showPlayer(it) }
        LightPlayer.of(sender).sendMessage(HIDER_SHOWN_MESSAGE)
        return true
    }
}