package ru.kainlight.lighthub.lightlibrary

import net.kyori.adventure.platform.bukkit.BukkitAudiences
import net.kyori.adventure.text.Component
import org.bukkit.Sound
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin
import ru.kainlight.lighthub.lightlibrary.UTILS.Parser

private var AUDIENCE: BukkitAudiences? = null
fun getAudience(): BukkitAudiences {
    return AUDIENCE!!
}
fun setAudience(plugin: JavaPlugin) {
    AUDIENCE = BukkitAudiences.create(plugin)
}

fun CommandSender.message(message: String?) {
    if(message.isNullOrBlank()) return

    val component: Component = Parser.mini(message)
    getAudience().sender(this).sendMessage(component)
}
fun Player.sound(sound: Sound?, volume: Float = 1f, pitch: Float = 1f) {
    if(sound == null) return

    playSound(this.location, sound, volume, pitch)
}

