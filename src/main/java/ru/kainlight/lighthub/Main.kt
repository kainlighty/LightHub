package ru.kainlight.lighthub

import lombok.Getter
import org.bukkit.command.CommandExecutor
import org.bukkit.event.HandlerList
import org.bukkit.event.Listener
import org.bukkit.plugin.java.JavaPlugin
import ru.kainlight.lighthub.COMMANDS.FlyCommand
import ru.kainlight.lighthub.COMMANDS.GamemodeCommand
import ru.kainlight.lighthub.COMMANDS.MainCommand
import ru.kainlight.lighthub.LISTENERS.PlayerListener
import ru.kainlight.lighthub.UTILS.LightConfig
import ru.kainlight.lighthub.UTILS.loadDefaultConfig
import ru.kainlight.lighthub.UTILS.setAudience

@Getter
class Main : JavaPlugin() {

    private var spawnConfig: LightConfig? = null
    private var messages: LightConfig? = null

    override fun onLoad() {
        this.saveDefaultConfig()
        this.spawnConfig = LightConfig(this, "spawn.yml")
        this.messages = LightConfig(this, "messages.yml")
    }

    override fun onEnable() {
        instance = this

        setAudience(this);

        loadDefaultConfig()

        this.registerListener(PlayerListener(this))
        this.registerCommand("fly", FlyCommand(this))
        this.registerCommand("gm", GamemodeCommand(this))
        this.registerCommand("lighthub", MainCommand())
    }

    override fun onDisable() {
        HandlerList.unregisterAll(this)
        this.server.scheduler.cancelTasks(this)
    }

    fun registerListener(listener: Listener) {
        server.pluginManager.registerEvents(listener, this)
    }

    fun registerCommand(name: String, executor: CommandExecutor) {
        getCommand(name)!!.setExecutor(executor)
    }

    fun getSpawnConfig(): LightConfig {
        return spawnConfig!!;
    }
    fun getMessages(): LightConfig {
        return messages!!;
    }

    companion object {
        private lateinit var instance: Main

        @JvmStatic fun getInstance() : Main {
            return instance;
        }
    }
}
