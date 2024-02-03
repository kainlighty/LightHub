package ru.kainlight.lighthub.LISTENERS

import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockExplodeEvent
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.event.entity.*
import org.bukkit.event.player.*
import ru.kainlight.lighthub.Main
import ru.kainlight.lighthub.UTILS.*

@Suppress("all")
class PlayerListener(private var plugin: Main) : Listener {

    @EventHandler
    fun onPlayerConnected(event: PlayerJoinEvent) {
        plugin.getMessages().getConfig().getString("join")?.let { event.joinMessage = it }
        val player = event.player
        player.health = DEFAULT_HEALTH

        SPAWN_LOCATION?.let { if(SPAWN_LOCATION != player.location) player.teleport(it) }
        if(TOGGLE_FLY && player.hasPermission("lighthub.fly")) player.allowFlight = true
        player.walkSpeed = DEFAULT_SPEED
    }

    @EventHandler
    fun onPlayerDisconnectEvent(event: PlayerQuitEvent) {
        plugin.getMessages().getConfig().getString("quit")?.let { event.quitMessage = it }

        if(CLEAR_INVENTORY_ON_EXIT) event.player.inventory.clear()
    }

    @EventHandler
    fun onVoid(event: PlayerMoveEvent) {
        if (event.player.isOp) return
        if (event.player.location.y <= MIN_Y) {
            SPAWN_LOCATION?.let { event.player.teleport(it) }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    fun onChat(event: AsyncPlayerChatEvent) {
        val player = event.player

        if (!player.hasPermission("lighthub.chat")) {
            event.isCancelled = true
            CANCEL_CHAT_MESSAGE?.let { if(it.isNotBlank()) LightPlayer.of(player).sendMessage(it) }
        }
    }

    @EventHandler
    fun onCommand(event: PlayerCommandPreprocessEvent) {
        val player = event.player

        if (!player.isOp) {
            if(ALLOWED_COMMANDS.isEmpty()) return
            val command = event.message.split(" ")[0] // .replace("/", "")

            if (!ALLOWED_COMMANDS.contains(command)) {
                event.isCancelled = true;
                CANCEL_CHAT_MESSAGE?.let { if(it.isNotBlank()) LightPlayer.of(player).sendMessage(it) }
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

        if (!player.hasPermission("lighthub.drop")) event.isCancelled = true
    }

    @EventHandler
    fun onDropDisable(event: PlayerPickupItemEvent) {
        val player = event.player

        if (!player.hasPermission("lighthub.pickup")) {
            player.inventory.remove(event.item.itemStack)
            event.isCancelled = true
        }
    }

    @EventHandler
    fun onDamagePlayerEvent(event: EntityDamageEvent) {
        if (event.entity !is Player) return
        event.isCancelled = true
    }

    @EventHandler
    fun onDamagePlayerEvent(event: EntityDamageByEntityEvent) {
        if (event.entity !is Player) return
        event.isCancelled = true
    }

    @EventHandler
    fun onHungerPlayerEvent(event: FoodLevelChangeEvent) {
        event.isCancelled = true
    }

    @EventHandler
    fun onBreakBlocks(event: BlockBreakEvent) {
        val player = event.player

        if (!player.hasPermission("lighthub.block.break")) event.isCancelled = true
    }

    @EventHandler
    fun onPlaceBlocks(event: BlockPlaceEvent) {
        val player = event.player

        if (!player.hasPermission("lighthub.block.place")) event.isCancelled = true
    }

    @EventHandler
    fun onExplosions(event: EntityExplodeEvent) {
        event.isCancelled = true
    }

    @EventHandler
    fun onExplosions(event: BlockExplodeEvent) {
        event.isCancelled = true
    }


}
