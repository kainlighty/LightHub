package ru.kainlight.lighthub

import org.bukkit.event.Listener
import ru.kainlight.lighthub.COMMANDS.*
import ru.kainlight.lighthub.LISTENERS.HideListener
import ru.kainlight.lighthub.LISTENERS.PlayerListener
import ru.kainlight.lighthub.UTILS.loadDefaultConfig
import ru.kainlight.lighthub.lightlibrary.CONFIGS.LightConfig
import ru.kainlight.lighthub.lightlibrary.LightPlugin
import ru.kainlight.lighthub.lightlibrary.setAudience

class Main : LightPlugin(), Listener {

    private var spawnConfig: LightConfig? = null
    private var messages: LightConfig? = null

    override fun onLoad() {
        this.saveDefaultConfig()
        this.spawnConfig = LightConfig(this, "spawn.yml")
        this.messages = LightConfig(this, "messages.yml")
    }

    override fun onEnable() {
        INSTANCE = this
        setAudience(this);
        loadDefaultConfig()

        registerListener(PlayerListener())

        this.registerCommand("lightgamemode", GamemodeCommand(this))
        .registerCommand("lightfly", FlyCommand(this))
        .registerCommand("lighthub", MainCommand())
        .registerCommand("lightspawn", SpawnCommand())

        if(!this.config.getBoolean("hider.enable", false)) {
            this.registerCommand("hide", HideCommand())
                .registerCommand("show", ShowCommand())
        } else this.registerListener(HideListener(this))
    }

    fun getSpawnConfig(): LightConfig { return spawnConfig!!; }
    fun getMessages(): LightConfig { return messages!!; }

    companion object { lateinit var INSTANCE: Main }
}
