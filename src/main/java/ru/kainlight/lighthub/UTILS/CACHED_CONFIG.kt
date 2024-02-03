package ru.kainlight.lighthub.UTILS

import ru.kainlight.lighthub.Main

var CLEAR_INVENTORY_ON_EXIT: Boolean = false
var DEFAULT_SPEED: Int? = null;
var MIN_Y: Double? = null
var ALLOWED_COMMANDS: List<String> = ArrayList();

fun loadDefaultConfig() {
    val config = Main.getInstance().config

    CLEAR_INVENTORY_ON_EXIT = config.getBoolean("clear-inventory-on-exit")
    DEFAULT_SPEED = config.getInt("settings.speed")
    MIN_Y = config.getDouble("settings.min-y")
    ALLOWED_COMMANDS = config.getStringList("settings.allowed-commands")
}