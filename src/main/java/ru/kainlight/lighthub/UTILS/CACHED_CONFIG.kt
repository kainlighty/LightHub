package ru.kainlight.lighthub.UTILS

import org.bukkit.Bukkit
import org.bukkit.Location
import ru.kainlight.lighthub.Main

var CANCEL_CHAT_MESSAGE: String? = null
var HIDER_SHOWN_MESSAGE: String? = null
var HIDER_HIDDEN_MESSAGE: String? = null

var JOIN_MESSAGE_ENABLED: Boolean = false
var QUIT_MESSAGE_ENABLED: Boolean = false
var CLEAR_INVENTORY_ON_EXIT: Boolean = false
var TOGGLE_FLY: Boolean = false
var DEFAULT_SPEED: Float = 1L / 10.0f;
var MIN_Y: Double = 0.0
var ALLOWED_COMMANDS: List<String> = ArrayList();
var DEFAULT_HEALTH: Double = 20.0

var SPAWN_LOCATION: Location? = null

fun loadDefaultConfig() {
    val config = Main.getInstance().config
    val messages = Main.getInstance().getMessages().getConfig();

    CANCEL_CHAT_MESSAGE = messages.getString("chat")
    HIDER_SHOWN_MESSAGE = messages.getString("hider.shown")
    HIDER_HIDDEN_MESSAGE = messages.getString("hider.hidden")

    JOIN_MESSAGE_ENABLED = config.getBoolean("join-message")
    QUIT_MESSAGE_ENABLED = config.getBoolean("quit-message")
    CLEAR_INVENTORY_ON_EXIT = config.getBoolean("clear-inventory-on-exit")
    TOGGLE_FLY = config.getBoolean("toggle-fly")
    DEFAULT_SPEED = config.getLong("settings.speed") / 10.0f
    MIN_Y = config.getDouble("settings.min-y")
    ALLOWED_COMMANDS = config.getStringList("settings.allowed-commands")
    DEFAULT_HEALTH = config.getDouble("settings.health")

    val spawnSection = Main.getInstance().getSpawnConfig().getConfig();
    val world = spawnSection.getString("world") ?: "world"
    val spawn = Location(
        Bukkit.getWorld(world),
        spawnSection.getDouble("x"),
        spawnSection.getDouble("y"),
        spawnSection.getDouble("z")
    )
    spawn.yaw = spawnSection.getInt("yaw").toFloat()
    spawn.pitch = spawnSection.getInt("pitch").toFloat()

    SPAWN_LOCATION = spawn;
}