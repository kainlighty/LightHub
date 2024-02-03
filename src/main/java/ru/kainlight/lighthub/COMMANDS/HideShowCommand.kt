package ru.kainlight.lighthub.COMMANDS

import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import ru.kainlight.lighthub.Main

class HideCommand : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        if(sender !is Player) return false;

        Main.getInstance().server.onlinePlayers.forEach {
            sender.hidePlayer(it)
        }
        return false
    }
}

class ShowCommand : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        if(sender !is Player) return false;

        Main.getInstance().server.onlinePlayers.forEach {
            sender.showPlayer(it)
        }
        return false
    }
}