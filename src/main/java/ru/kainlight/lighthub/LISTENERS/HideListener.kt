package ru.kainlight.lighthub.LISTENERS

import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.inventory.InventoryMoveItemEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.metadata.FixedMetadataValue
import ru.kainlight.lighthub.Main
import ru.kainlight.lighthub.UTILS.HIDER_HIDDEN_MESSAGE
import ru.kainlight.lighthub.UTILS.HIDER_SHOWN_MESSAGE
import ru.kainlight.lighthub.getAudience
import ru.kainlight.lightlibrary.BUILDERS.ItemBuilder
import ru.kainlight.lightlibrary.multiMessage

class HideListener(private val plugin: Main) : Listener {

    var HIDE_ITEM: ItemStack? = null
    var SHOW_ITEM: ItemStack? = null

    init {
        val hiderSection = plugin.config.getConfigurationSection("hider") !!
        HIDE_ITEM = ItemBuilder(Material.valueOf(hiderSection.getString("hidden.material")!!))
            .displayName(hiderSection.getString("hidden.name") !!)
            .defaultFlags()
            .build()
        SHOW_ITEM = ItemBuilder(Material.valueOf(hiderSection.getString("shown.material")!!))
            .displayName(hiderSection.getString("shown.name") !!)
            .defaultFlags()
            .build()
    }

    private var SLOT: Int = plugin.config.getInt("hider.slot", 4)

    private val HIDDEN_SOUND_ENABLED = plugin.config.getBoolean("hider.hidden.sound.enable", false)
    private val HIDDEN_SOUND_NAME = plugin.config.getString("hider.hidden.sound.name") !!
    private val HIDDEN_SOUND_VOLUME = plugin.config.getDouble("hider.shown.sound.volume", 1.0)
    private val HIDDEN_SOUND_PITCH = plugin.config.getDouble("hider.shown.sound.pitch", 1.0)

    private val SHOWN_SOUND_ENABLED = plugin.config.getBoolean("hider.shown.sound.enable", false)
    private val SHOWN_SOUND_NAME = plugin.config.getString("hider.shown.sound.name") !!
    private val SHOWN_SOUND_VOLUME = plugin.config.getDouble("hider.shown.sound.volume", 1.0)
    private val SHOWN_SOUND_PITCH = plugin.config.getDouble("hider.shown.sound.pitch", 1.0)

    @EventHandler
    fun onJoinHider(event: PlayerJoinEvent) {
        if(SLOT < 0 || SLOT > 8) {
            SLOT = 4
            plugin.logger.warning("The SLOT parameter in the Hider is set to an incorrect value in config.yml")
            plugin.logger.warning("The default value is set to 4")
        }
        event.player.inventory.setItem(SLOT, this.HIDE_ITEM)
    }

    @EventHandler
    fun onHiderMoving(event: InventoryMoveItemEvent) {
        val item = event.item
        if (item.isSimilar(this.HIDE_ITEM) || item.isSimilar(this.SHOW_ITEM)) event.isCancelled = true
    }

    @EventHandler
    fun hideEvent(event: PlayerInteractEvent) {
        val action = event.action
        if(action != Action.RIGHT_CLICK_AIR && action != Action.RIGHT_CLICK_BLOCK) return

        val player = event.player
        val metadata = player.hasMetadata("playersHidden")
        if(!metadata) {
            if (player.inventory.getItem(SLOT) == null) return
            if (!player.hasPermission("lighthub.visibility")) return

            val itemInHand = player.itemInHand

            val onlinePlayers = plugin.server.onlinePlayers
            if (this.hide(player, itemInHand, onlinePlayers)) {
                player.setMetadata("playersHidden", FixedMetadataValue(plugin, true))

                plugin.server.scheduler.runTaskLater(plugin, Runnable{
                    player.removeMetadata("playersHidden", plugin)
                    return@Runnable
                },5)
            }

        }

    }

    private fun hide(player: Player, itemInHand: ItemStack, onlinePlayers: Collection<Player>): Boolean {
        if (itemInHand.isSimilar(HIDE_ITEM)) {
            onlinePlayers.forEach { player.hidePlayer(plugin, it) }
            player.inventory.setItem(SLOT, SHOW_ITEM)

            if (HIDDEN_SOUND_ENABLED) player.sound(
                Sound.valueOf(HIDDEN_SOUND_NAME),
                HIDDEN_SOUND_VOLUME.toFloat(),
                HIDDEN_SOUND_PITCH.toFloat()
            )
            player.getAudience().multiMessage(HIDER_HIDDEN_MESSAGE)
            return true
        } else if (itemInHand.isSimilar(SHOW_ITEM)) {
            onlinePlayers.forEach { player.showPlayer(plugin, it) }
            player.inventory.setItem(SLOT, HIDE_ITEM)

            if (SHOWN_SOUND_ENABLED) player.sound(
                Sound.valueOf(SHOWN_SOUND_NAME),
                SHOWN_SOUND_VOLUME.toFloat(),
                SHOWN_SOUND_PITCH.toFloat()
            )
            player.getAudience().multiMessage(HIDER_SHOWN_MESSAGE)
            return true
        } else return false
    }

    private fun Player.sound(sound: Sound?, volume: Float = 1f, pitch: Float = 1f) {
        if(sound == null) return
        playSound(this.location, sound, volume, pitch)
    }
}
