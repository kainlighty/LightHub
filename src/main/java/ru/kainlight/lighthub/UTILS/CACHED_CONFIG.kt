package ru.kainlight.lighthub.UTILS

import org.bukkit.Bukkit
import org.bukkit.Location
import ru.kainlight.lighthub.Main
import ru.kainlight.lightlibrary.LightPlugin

lateinit var CANCEL_CHAT_MESSAGE: String
lateinit var HIDER_SHOWN_MESSAGE: String
lateinit var HIDER_HIDDEN_MESSAGE: String

var HUNGER = false
var DAMAGE = false
var JOIN_MESSAGE: Boolean = false
var QUIT_MESSAGE: Boolean = false
var DEATH_MESSAGE: Boolean = false
var CLEAR_INVENTORY_ON_EXIT: Boolean = false
var TOGGLE_FLY: Boolean = false
var DEFAULT_SPEED: Float = 1L / 10.0f
var MIN_Y: Double? = null
var ALLOWED_COMMANDS: MutableList<String> = mutableListOf()
var DEFAULT_HEALTH: Double = 20.0

var SPAWN_LOCATION: Location? = null

fun loadDefaultConfig(plugin: LightPlugin) {
    val messages = plugin.messageConfig.getConfig()
    CANCEL_CHAT_MESSAGE = messages.getString("chat", "")!!
    HIDER_SHOWN_MESSAGE = messages.getString("hider.shown", "")!!
    HIDER_HIDDEN_MESSAGE = messages.getString("hider.hidden", "")!!

    val attributes = plugin.config.getConfigurationSection("attributes")!!
    HUNGER = attributes.getBoolean("hunger")
    DAMAGE = attributes.getBoolean("damage")
    JOIN_MESSAGE = attributes.getBoolean("join-message")
    QUIT_MESSAGE = attributes.getBoolean("quit-message")
    DEATH_MESSAGE = attributes.getBoolean("death-message")
    CLEAR_INVENTORY_ON_EXIT = attributes.getBoolean("clear-inventory-on-exit", false)
    TOGGLE_FLY = attributes.getBoolean("toggle-fly", false)
    DEFAULT_SPEED = attributes.getLong("speed", 1) / 10.0f
    MIN_Y = attributes.getDouble("min-y")
    ALLOWED_COMMANDS = attributes.getStringList("allowed-commands")
    DEFAULT_HEALTH = attributes.getDouble("health", 20.0)

    val spawnSection = Main.INSTANCE.getSpawnConfig().getConfig()
    spawnSection.getString("world")?.let {
        if(it == "null") {
            Main.INSTANCE.logger.warning("World is null in spawn.yml")
        } else {
            val spawn = Location(
                Bukkit.getWorld(it),
                spawnSection.getDouble("x"),
                spawnSection.getDouble("y"),
                spawnSection.getDouble("z")
            )
            spawn.yaw = spawnSection.getInt("yaw").toFloat()
            spawn.pitch = spawnSection.getInt("pitch").toFloat()

            SPAWN_LOCATION = spawn
        }
    }
}