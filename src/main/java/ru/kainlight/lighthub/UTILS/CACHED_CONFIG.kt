package ru.kainlight.lighthub.UTILS

import org.bukkit.Bukkit
import org.bukkit.Location
import ru.kainlight.lighthub.Main

lateinit var CANCEL_CHAT_MESSAGE: String
lateinit var HIDER_SHOWN_MESSAGE: String
lateinit var HIDER_HIDDEN_MESSAGE: String

var JOIN_MESSAGE_ENABLED: Boolean = false
var QUIT_MESSAGE_ENABLED: Boolean = false
var CLEAR_INVENTORY_ON_EXIT: Boolean = false
var TOGGLE_FLY: Boolean = false
var DEFAULT_SPEED: Float = 1L / 10.0f;
var MIN_Y: Double = 0.0
var ALLOWED_COMMANDS: MutableList<String> = mutableListOf();
var DEFAULT_HEALTH: Double = 20.0

var SPAWN_LOCATION: Location? = null

fun loadDefaultConfig() {
    val messages = Main.INSTANCE.getMessages().getConfig();
    CANCEL_CHAT_MESSAGE = messages.getString("chat", "")!!
    HIDER_SHOWN_MESSAGE = messages.getString("hider.shown", "")!!
    HIDER_HIDDEN_MESSAGE = messages.getString("hider.hidden", "")!!

    val config = Main.INSTANCE.config.getConfigurationSection("settings")!!
    JOIN_MESSAGE_ENABLED = config.getBoolean("join-message")
    QUIT_MESSAGE_ENABLED = config.getBoolean("quit-message")
    CLEAR_INVENTORY_ON_EXIT = config.getBoolean("clear-inventory-on-exit", false)
    TOGGLE_FLY = config.getBoolean("toggle-fly", false)
    DEFAULT_SPEED = config.getLong("speed", 1) / 10.0f
    MIN_Y = config.getDouble("min-y", 0.0)
    ALLOWED_COMMANDS = config.getStringList("allowed-commands")
    DEFAULT_HEALTH = config.getDouble("health", 20.0)

    val spawnSection = Main.INSTANCE.getSpawnConfig().getConfig();
    spawnSection.getString("world")?.let {
        if(it == "null") {
            Main.INSTANCE.logger.warning("World is null in config.yml")
        } else {
            val spawn = Location(
                Bukkit.getWorld(it),
                spawnSection.getDouble("x"),
                spawnSection.getDouble("y"),
                spawnSection.getDouble("z")
            )
            spawn.yaw = spawnSection.getInt("yaw").toFloat()
            spawn.pitch = spawnSection.getInt("pitch").toFloat()

            SPAWN_LOCATION = spawn;
        }
    }
}