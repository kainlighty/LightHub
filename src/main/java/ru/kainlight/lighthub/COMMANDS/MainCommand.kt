package ru.kainlight.lighthub.COMMANDS

import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import ru.kainlight.lighthub.Main
import ru.kainlight.lighthub.lightlibrary.message

class MainCommand : CommandExecutor {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if(!sender.hasPermission("lighthub.*")) return true;

        if(args.size == 1 && args[0] == "reload") {
            Main.INSTANCE.reloadConfig()
            Main.INSTANCE.getMessages().reloadConfig()
            Main.INSTANCE.getSpawnConfig().reloadConfig()

            sender.message(Main.INSTANCE.config.getString("reload"))
            return true;
        }

        return false
    }
}