package ru.kainlight.lighthub.UTILS

import net.kyori.adventure.bossbar.BossBar
import net.kyori.adventure.platform.bukkit.BukkitAudiences
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.event.ClickEvent
import net.kyori.adventure.text.event.HoverEvent
import net.kyori.adventure.title.Title
import net.kyori.adventure.title.Title.Times
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Sound
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin
import java.time.Duration
import java.util.function.Consumer

private var AUDIENCE: BukkitAudiences? = null
fun getAudience(): BukkitAudiences {
    return AUDIENCE!!;
}
fun setAudience(plugin: JavaPlugin) {
    AUDIENCE = BukkitAudiences.create(plugin);
}

@Suppress("unused")
class LightPlayer(var sender: CommandSender) {

    fun sendHexMessage(message: String) {
        getAudience().sender(sender).sendMessage(Parser.hex(message))
    }

    fun sendClickableHoverMessage(message: String?, hover: String, event: ClickEvent.Action?, action: String?) {
        if (message == null) return

        val component: Component = Parser.mini(message)
        val component2: Component = Parser.mini(hover)
        val hoverComponent = component
            .clickEvent(ClickEvent.clickEvent(event!!, action!!))
            .hoverEvent(HoverEvent.showText(component2))

        getAudience().sender(sender).sendMessage(hoverComponent)
    }

    fun sendClickableMessage(message: String?, event: ClickEvent.Action?, action: String?) {
        if (message == null) return
        var component: Component = Parser.mini(message)
        component = component.clickEvent(ClickEvent.clickEvent(event!!, action!!))

        getAudience().sender(sender).sendMessage(component)
    }

    fun sendMessage(component: Component?) {
        if (component == null) return

        getAudience().sender(sender).sendMessage(component)
    }

    fun sendMessage(message: String?) {
        if (message == null) return
        val component: Component = Parser.mini(message)

        getAudience().sender(sender).sendMessage(component)
    }

    fun sendClearedMessage(message: String?) {
        if (message == null) return
        val component: Component = Component.text(message)

        getAudience().sender(sender).sendMessage(component)
    }

    fun sendMessage(message: List<String?>?) {
        if (message == null || message.isEmpty()) return
        message.forEach(Consumer { this.sendMessage(it) })
    }

    fun sendActionbar(message: String?) {
        if (message == null) return
        val component: Component = Parser.mini(message)
        getAudience().sender(sender).sendActionBar(component)
    }

    fun sendHoverMessage(message: String?, hover: String?) {
        if (message == null) return
        if(hover == null) return

        val component: Component = Parser.mini(message)
        val hoverComponent = component.hoverEvent(HoverEvent.showText(Parser.mini(hover)))

        getAudience().sender(sender).sendMessage(hoverComponent)
    }

    fun sendTitle(title: String, subtitle: String, fadeIn: Long, stay: Long, fadeOut: Long) {
        val titleComponent: Component = Parser.mini(title)
        val subtitleComponent: Component = Parser.mini(subtitle)

        val times = Times.of(Duration.ofSeconds(fadeIn), Duration.ofSeconds(stay), Duration.ofSeconds(fadeOut))
        val titleToSend = Title.title(titleComponent, subtitleComponent, times)

        getAudience().sender(sender).showTitle(titleToSend)
    }

    fun sendTitle(title: String, subtitle: String) {
        val titleComponent: Component = Parser.mini(title)
        val subtitleComponent: Component = Parser.mini(subtitle)

        val titleToSend = Title.title(titleComponent, subtitleComponent)

        getAudience().sender(sender).showTitle(titleToSend)
    }

    fun clearTitle() {
        getAudience().sender(sender).clearTitle()
    }

    fun showBossBar(bossBar: BossBar?) {
        getAudience().sender(sender).showBossBar(bossBar!!)
    }

    fun hideBossBar(bossBar: BossBar?) {
        getAudience().sender(sender).hideBossBar(bossBar!!)
    }

    companion object {
        fun of(sender: CommandSender): LightPlayer {
            return LightPlayer(sender)
        }

        fun sendMessage(message: String?, vararg players: Player) {
            if (message == null) return

            val component: Component = Parser.mini(message)
            for (player in players) {
                getAudience().player(player).sendMessage(component)
            }
        }

        fun sendMessageForAll(message: String?) {
            if (message == null) return
            val component: Component = Parser.mini(message)

            Bukkit.getServer().onlinePlayers.forEach { getAudience().player(it).sendMessage(component) }
        }

        fun sendMessageForAll(messages: List<String?>?) {
            if (messages == null || messages.isEmpty()) return

            messages.forEach(Consumer { sendMessageForAll(it) })
        }

        fun playSoundForAll(sound: Sound?) {
            if (sound == null) return
            Bukkit.getServer().onlinePlayers.forEach { player: Player ->
                player.playSound(
                    player.location,
                    sound,
                    1f,
                    1f
                )
            }
        }

        fun playSound(location: Location, sound: Sound?) {
            if (sound == null) return
            location.world?.playSound(location, sound, 1f, 1f)
        }

        fun sendTitleForAll(title: String, subtitle: String) {
            val titleComponent: Component = title.let { Parser.mini(it) }
            val subtitleComponent: Component = subtitle.let { Parser.mini(it) }

            Bukkit.getServer().onlinePlayers.forEach { player: Player -> getAudience().player(player).showTitle(Title.title(titleComponent, subtitleComponent)) }
        }
    }
}