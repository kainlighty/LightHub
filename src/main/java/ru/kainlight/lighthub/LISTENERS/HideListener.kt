package ru.kainlight.lighthub.LISTENERS

import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.inventory.InventoryMoveItemEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.inventory.ItemStack
import ru.kainlight.lighthub.Main
import ru.kainlight.lighthub.UTILS.HIDER_HIDDEN_MESSAGE
import ru.kainlight.lighthub.UTILS.HIDER_SHOWN_MESSAGE
import ru.kainlight.lighthub.UTILS.JAVA.ItemBuilder
import ru.kainlight.lighthub.UTILS.message
import ru.kainlight.lighthub.UTILS.sound

class HideListener(private val plugin: Main) : Listener {

    var HIDE_ITEM: ItemStack? = null
    var SHOW_ITEM: ItemStack? = null

    init {
        val hiderSection = plugin.config.getConfigurationSection("hider")!!
        HIDE_ITEM = ItemBuilder(Material.getMaterial(hiderSection.getString("hidden.material")!!)!!)
            .displayName(hiderSection.getString("hidden.name")!!)
            .defaultFlags()
            .build()
        SHOW_ITEM = ItemBuilder(Material.getMaterial(hiderSection.getString("shown.material")!!)!!)
            .displayName(hiderSection.getString("shown.name")!!)
            .defaultFlags()
            .build()
    }

    private val SLOT: Int = plugin.config.getInt("hider.slot", 4)

    private val HIDDEN_SOUND_ENABLED = plugin.config.getBoolean("hider.hidden.sound.enable", false)
    private val HIDDEN_SOUND_NAME = plugin.config.getString("hider.hidden.sound.name")!!
    private val HIDDEN_SOUND_VOLUME = plugin.config.getDouble("hider.shown.sound.volume", 1.0)
    private val HIDDEN_SOUND_PITCH = plugin.config.getDouble("hider.shown.sound.pitch", 1.0)

    private val SHOWN_SOUND_ENABLED = plugin.config.getBoolean("hider.shown.sound.enable", false)
    private val SHOWN_SOUND_NAME = plugin.config.getString("hider.shown.sound.name")!!
    private val SHOWN_SOUND_VOLUME = plugin.config.getDouble("hider.shown.sound.volume", 1.0)
    private val SHOWN_SOUND_PITCH = plugin.config.getDouble("hider.shown.sound.pitch", 1.0)

    @EventHandler
    fun onJoinHider(event: PlayerJoinEvent) {
        event.player.inventory.setItem(SLOT, this.HIDE_ITEM)
    }

    @EventHandler
    fun onHiderMoving(event: InventoryMoveItemEvent) {
        if(event.item.isSimilar(this.HIDE_ITEM) || event.item.isSimilar(this.SHOW_ITEM)) event.isCancelled = true
    }

    @EventHandler
    fun hide(event: PlayerInteractEvent) {
        if (event.action != Action.RIGHT_CLICK_AIR && event.action != Action.RIGHT_CLICK_BLOCK) return;
        val player = event.player
        if (player.inventory.getItem(SLOT) == null) return
        if(!player.hasPermission("lighthub.visibility")) return

        val itemInHand = player.itemInHand

        if (itemInHand.isSimilar(HIDE_ITEM)) {
            plugin.server.onlinePlayers.forEach { player.hidePlayer(plugin, it) }
            player.inventory.setItem(SLOT, SHOW_ITEM)

            if (HIDDEN_SOUND_ENABLED) player.sound(
                Sound.valueOf(HIDDEN_SOUND_NAME),
                HIDDEN_SOUND_VOLUME.toFloat(),
                HIDDEN_SOUND_PITCH.toFloat()
            )
            player.message(HIDER_HIDDEN_MESSAGE)
        } else if (itemInHand.isSimilar(SHOW_ITEM)) {
            plugin.server.onlinePlayers.forEach { player.showPlayer(plugin, it) }
            player.inventory.setItem(SLOT, HIDE_ITEM)

            if (SHOWN_SOUND_ENABLED) player.sound(
                Sound.valueOf(SHOWN_SOUND_NAME),
                SHOWN_SOUND_VOLUME.toFloat(),
                SHOWN_SOUND_PITCH.toFloat()
            )
            player.message(HIDER_SHOWN_MESSAGE)
        }
    }
}
