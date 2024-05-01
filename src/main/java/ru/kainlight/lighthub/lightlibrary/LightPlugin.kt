package ru.kainlight.lighthub.lightlibrary

import org.bukkit.Bukkit
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import org.bukkit.event.Listener
import org.bukkit.plugin.java.JavaPlugin

open class LightPlugin : JavaPlugin() {

    // -------------------- //
    fun registerListener(listener: Listener): LightPlugin {
        Bukkit.getPluginManager().registerEvents(listener, this)
        return this
    }

    fun registerCommand(command: String, executor: CommandExecutor, tabCompleter: TabCompleter? = null): LightPlugin {
        this.getCommand(command)?.let {
            it.setExecutor(executor)
            if (tabCompleter != null) it.tabCompleter = tabCompleter
        }
        return this
    }


    // ------------------------ //

    fun coloredLogger(message: String): LightPlugin {
        this.server.consoleSender.message(message)
        return this
    }

    fun command(command: String, sender: CommandSender = Bukkit.getConsoleSender()): Boolean {
        return Bukkit.dispatchCommand(sender, command)
    }
}