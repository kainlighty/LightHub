package ru.kainlight.lighthub

import net.kyori.adventure.audience.Audience
import net.kyori.adventure.platform.bukkit.BukkitAudiences
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import ru.kainlight.lighthub.COMMANDS.*
import ru.kainlight.lighthub.LISTENERS.HideListener
import ru.kainlight.lighthub.LISTENERS.PlayerListener
import ru.kainlight.lighthub.UTILS.loadDefaultConfig
import ru.kainlight.lightlibrary.LightConfig
import ru.kainlight.lightlibrary.LightPlugin
import ru.kainlight.lightlibrary.UTILS.Init
import ru.kainlight.lightlibrary.UTILS.Parser

class Main : LightPlugin() {

    lateinit var bukkitAudiences: BukkitAudiences
    private var spawnConfig: LightConfig? = null

    override fun onLoad() {
        this.saveDefaultConfig()
        this.configurationVersion = 1.1
        this.updateConfig()
        LightConfig.saveLanguages(this, "settings.language")
        this.messageConfig.configurationVersion = 1.1
        this.messageConfig.updateConfig()
        this.spawnConfig = LightConfig(this, null, "spawn.yml")
    }

    override fun onEnable() {
        INSTANCE = this

        loadDefaultConfig(this)

        Parser.parseMode = this.config.getString("settings.parse_mode", "MINIMESSAGE")!!

        this.bukkitAudiences = BukkitAudiences.create(this)

        registerListener(PlayerListener())

        this.registerCommand("lightgamemode", Gamemode(this))
        .registerCommand("lightfly", Fly(this))
        .registerCommand("lighthubreload", Reload(this))
        .registerCommand("lightspawn", Spawn())
        .registerCommand("lightspeed", Speed(this))

        if(!this.config.getBoolean("hider.enable", false)) {
            this.registerCommand("hide", HideCommand())
                .registerCommand("show", ShowCommand())
        } else this.registerListener(HideListener(this))

        Init.start(this, true)
    }

    override fun onDisable() {
        Init.stop(this)
    }

    fun getSpawnConfig(): LightConfig { return spawnConfig!! }

    companion object { @JvmStatic lateinit var INSTANCE: Main }
}

fun Player.getAudience(): Audience {
    return Main.INSTANCE.bukkitAudiences.player(this)
}
fun CommandSender.getAudience(): Audience {
    return Main.INSTANCE.bukkitAudiences.sender(this)
}

