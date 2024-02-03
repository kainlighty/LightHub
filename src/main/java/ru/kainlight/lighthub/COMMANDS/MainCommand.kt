package ru.kainlight.lighthub.COMMANDS

import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import ru.kainlight.lighthub.Main
import ru.kainlight.lighthub.UTILS.LightPlayer

class MainCommand : CommandExecutor {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if(!sender.isOp) return true;

        Main.getInstance().reloadConfig()
        Main.getInstance().getSpawnConfig().reloadConfig()
        Main.getInstance().getMessages().reloadConfig()

        LightPlayer.of(sender).sendMessage("<red>Configurations reloaded")
        return true;
    }
}