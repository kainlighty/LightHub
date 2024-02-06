package ru.kainlight.lighthub.LISTENERS

import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockExplodeEvent
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.event.entity.*
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryDragEvent
import org.bukkit.event.inventory.InventoryMoveItemEvent
import org.bukkit.event.player.*
import ru.kainlight.lighthub.COMMANDS.FlyCommand
import ru.kainlight.lighthub.Main
import ru.kainlight.lighthub.UTILS.*

@Suppress("all")
class PlayerListener(private var plugin: Main) : Listener {

    @EventHandler
    fun onPlayerConnected(event: PlayerJoinEvent) {
        if (!JOIN_MESSAGE_ENABLED) event.joinMessage = null

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
        if (!QUIT_MESSAGE_ENABLED) event.quitMessage = null

        val player = event.player
        if (CLEAR_INVENTORY_ON_EXIT && !player.isOp) player.inventory.clear()
    }

    @EventHandler
    fun onVoid(event: PlayerMoveEvent) {
        if (event.player.location.y <= MIN_Y) {
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
            CANCEL_CHAT_MESSAGE?.let { if (it.isNotBlank()) LightPlayer.of(player).sendMessage(it) }
        }
    }

    @EventHandler
    fun onCommand(event: PlayerCommandPreprocessEvent) {
        val player = event.player
        val message = event.message
        val command = message.split(" ")[0].replace("/", "")

        if (!player.hasPermission("lighthub.command")) {
            if (ALLOWED_COMMANDS.isEmpty()) return

            if (!ALLOWED_COMMANDS.contains(command)) {
                event.isCancelled = true;
                CANCEL_CHAT_MESSAGE?.let { if (it.isNotBlank()) LightPlayer.of(player).sendMessage(it) }
            }
        }
    }

    @EventHandler
    fun onPlayerDeathEvent(event: PlayerDeathEvent) {
        event.deathMessage = null
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
        if(!event.entity.hasPermission("lighthub.damage")) event.isCancelled = true
    }

    @EventHandler
    fun onDamagePlayerEvent(event: EntityDamageByEntityEvent) {
        if(!event.entity.hasPermission("lighthub.damage")) event.isCancelled = true
    }

    @EventHandler
    fun onHungerPlayerEvent(event: FoodLevelChangeEvent) {
        event.isCancelled = true
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
