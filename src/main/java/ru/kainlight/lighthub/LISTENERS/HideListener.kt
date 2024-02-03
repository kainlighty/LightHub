package ru.kainlight.lighthub.LISTENERS

import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerJoinEvent
import ru.kainlight.lighthub.Main
import ru.kainlight.lighthub.UTILS.ItemBuilder

class HideListener(private val plugin: Main) : Listener {

    var ENABLED: Boolean = Main.getInstance().config.getBoolean("hider.enable", false)
    private val SLOT: Int = plugin.config.getInt("hider.slot", 4)

    private val HIDDEN_SOUND_ENABLED = plugin.config.getBoolean("hider.hidden.sound.enabled", false)
    private val HIDDEN_SOUND_NAME = plugin.config.getString("hider.hidden.sound.name")!!
    private val HIDDEN_SOUND_VOLUME = plugin.config.getDouble("hider.shown.sound.volume",1.0)
    private val HIDDEN_SOUND_PITCH = plugin.config.getDouble("hider.shown.sound.pitch",1.0)

    private val SHOWN_SOUND_ENABLED = plugin.config.getBoolean("hider.shown.sound.enabled", false)
    private val SHOWN_SOUND_NAME = plugin.config.getString("hider.shown.sound.name")!!
    private val SHOWN_SOUND_VOLUME = plugin.config.getDouble("hider.shown.sound.volume",1.0)
    private val SHOWN_SOUND_PITCH = plugin.config.getDouble("hider.shown.sound.pitch",1.0)

    private val HIDE_ITEM = ItemBuilder(Material.valueOf(plugin.config.getString("hider.hidden.material")!!)).displayName(plugin.config.getString("hider.hidden.name")).build()
    private val SHOW_ITEM = ItemBuilder(Material.valueOf(plugin.config.getString("hider.shown.material")!!)).displayName(plugin.config.getString("hider.shown.name")).build()

    @EventHandler
    fun onJoinHider(event: PlayerJoinEvent) {
        if (ENABLED) {
            plugin.server.onlinePlayers.forEach { if (it.itemInHand.isSimilar(SHOW_ITEM)) it.hidePlayer(plugin, event.player) }
            event.player.inventory.setItem(SLOT, this.HIDE_ITEM)
        }
    }

    @EventHandler
    fun hide(event: PlayerInteractEvent) {
        if (event.action != Action.RIGHT_CLICK_AIR || event.action != Action.RIGHT_CLICK_BLOCK) return
        val item = event.player.inventory.getItem(SLOT) ?: return
        if(item != HIDE_ITEM || item != SHOW_ITEM) return

        val player = event.player
        val itemInHand = player.itemInHand

        if (itemInHand.isSimilar(HIDE_ITEM)) {
            plugin.server.onlinePlayers.forEach { player.hidePlayer(plugin, it) }
            player.inventory.setItem(SLOT, SHOW_ITEM)

            if(HIDDEN_SOUND_ENABLED) player.playSound(player.location, HIDDEN_SOUND_NAME, HIDDEN_SOUND_VOLUME.toFloat(), HIDDEN_SOUND_PITCH.toFloat())
        } else if (itemInHand.isSimilar(SHOW_ITEM)) {
            plugin.server.onlinePlayers.forEach { player.showPlayer(plugin, it) }
            player.inventory.setItem(SLOT, HIDE_ITEM)
            if(SHOWN_SOUND_ENABLED) player.playSound(player.location, SHOWN_SOUND_NAME, SHOWN_SOUND_VOLUME.toFloat(), SHOWN_SOUND_PITCH.toFloat())
        }
    }
}
