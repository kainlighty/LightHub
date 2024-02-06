package ru.kainlight.lighthub

import org.bukkit.command.CommandExecutor
import org.bukkit.event.Listener
import org.bukkit.plugin.java.JavaPlugin
import ru.kainlight.lighthub.COMMANDS.*
import ru.kainlight.lighthub.LISTENERS.HideListener
import ru.kainlight.lighthub.LISTENERS.PlayerListener
import ru.kainlight.lighthub.UTILS.LightConfig
import ru.kainlight.lighthub.UTILS.loadDefaultConfig
import ru.kainlight.lighthub.UTILS.setAudience

class Main : JavaPlugin(), Listener {

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

        registerListener(PlayerListener(this))

        this.registerCommand("fly", FlyCommand(this))
        .registerCommand("gm", GamemodeCommand(this))
        .registerCommand("lighthub", MainCommand())
        .registerCommand("spawn", SpawnCommand())

        if(!this.config.getBoolean("hider.enable", false)) {
            this.registerCommand("hide", HideCommand())
                .registerCommand("show", ShowCommand())
        } else this.registerListener(HideListener(this))
    }

    private fun registerListener(listener: Listener): Main {
        server.pluginManager.registerEvents(listener, this)
        return this
    }

    private fun registerCommand(name: String, executor: CommandExecutor): Main {
        getCommand(name)!!.setExecutor(executor)
        return this;
    }

    fun getSpawnConfig(): LightConfig { return spawnConfig!!; }
    fun getMessages(): LightConfig { return messages!!; }

    companion object {
        private lateinit var instance: Main
        @JvmStatic fun getInstance() : Main { return instance; }

        fun isInteger(str: String?) = str?.toIntOrNull()?.let { true } ?: false
    }
}
