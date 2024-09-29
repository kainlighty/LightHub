package ru.kainlight.lighthub.LISTENERS

import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.entity.FoodLevelChangeEvent
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryDragEvent
import org.bukkit.event.player.*
import ru.kainlight.lighthub.UTILS.*
import ru.kainlight.lighthub.getAudience
import ru.kainlight.lightlibrary.multiMessage

@Suppress("all")
class PlayerListener : Listener {

    @EventHandler
    fun onPlayerConnected(event: PlayerJoinEvent) {
        if (!JOIN_MESSAGE) event.joinMessage = null

        val player = event.player
        player.health = DEFAULT_HEALTH

        SPAWN_LOCATION?.let { if (SPAWN_LOCATION != player.location) player.teleport(it) }

        player.walkSpeed = DEFAULT_SPEED
        if (TOGGLE_FLY && player.hasPermission("lighthub.fly")) {
            player.allowFlight = true
        }
    }

    @EventHandler
    fun onPlayerDisconnectEvent(event: PlayerQuitEvent) {
        if (!QUIT_MESSAGE) event.quitMessage = null

        if (CLEAR_INVENTORY_ON_EXIT) event.player.inventory.clear()
    }

    @EventHandler
    fun onVoid(event: PlayerMoveEvent) {
        if(MIN_Y == null) return

        if (event.player.location.y <= MIN_Y!!) {
            SPAWN_LOCATION?.let { event.player.teleport(it) }
        }
    }

    @EventHandler
    fun onItemMoving(event: InventoryClickEvent) {
        if(!event.whoClicked.hasPermission("lighthub.inv.move")) event.isCancelled = true
    }
    @EventHandler
    fun onItemMoving(event: InventoryDragEvent) {
        if(!event.whoClicked.hasPermission("lighthub.inv.move")) event.isCancelled = true
    }

    @EventHandler(priority = EventPriority.LOWEST)
    fun onChat(event: AsyncPlayerChatEvent) {
        val player = event.player

        if (!player.hasPermission("lighthub.chat")) {
            event.isCancelled = true
            player.getAudience().multiMessage(CANCEL_CHAT_MESSAGE)
        }
    }

    @EventHandler
    fun onCommand(event: PlayerCommandPreprocessEvent) {
        val player = event.player
        val message = event.message
        val command = message.split(" ")[0].replace("/", "")

        if (!player.hasPermission("lighthub.chat.commands")) {
            if (ALLOWED_COMMANDS.isEmpty()) return

            if (!ALLOWED_COMMANDS.contains(command)) {
                event.isCancelled = true
                player.getAudience().multiMessage(CANCEL_CHAT_MESSAGE)
            }
        }
    }

    @EventHandler
    fun onPlayerDeathEvent(event: PlayerDeathEvent) {
        if(!DEATH_MESSAGE) event.deathMessage = null
        event.drops.clear()

        SPAWN_LOCATION?.let { event.entity.player?.teleport(it) ?: return }
    }

    @EventHandler
    fun onDropDisable(event: PlayerDropItemEvent) {
        val player = event.player

        if (!player.hasPermission("lighthub.inv.drop")) event.isCancelled = true
    }

    @EventHandler
    fun onPickup(event: PlayerPickupItemEvent) {
        val player = event.player

        if (!player.hasPermission("lighthub.inv.pickup")) {
            player.inventory.remove(event.item.itemStack)
            event.isCancelled = true
        }
    }

    @EventHandler
    fun onDamagePlayerEvent(event: EntityDamageEvent) {
        if(!DAMAGE) event.isCancelled = true
    }

    @EventHandler
    fun onDamagePlayerEvent(event: EntityDamageByEntityEvent) {
        if(!DAMAGE) event.isCancelled = true
    }

    @EventHandler
    fun onHungerPlayerEvent(event: FoodLevelChangeEvent) {
        if(!HUNGER) event.isCancelled = true
    }

    @EventHandler
    fun onBreakBlocks(event: BlockBreakEvent) {
        if (!event.player.hasPermission("lighthub.block.break")) event.isCancelled = true
    }

    @EventHandler
    fun onPlaceBlocks(event: BlockPlaceEvent) {
        if (!event.player.hasPermission("lighthub.block.place")) event.isCancelled = true
    }


}
