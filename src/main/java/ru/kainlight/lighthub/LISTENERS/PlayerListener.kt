package ru.kainlight.lighthub.LISTENERS

import org.bukkit.Bukkit
import org.bukkit.Location
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

class PlayerListener(private var plugin: Main) : Listener {

    @EventHandler
    fun onPlayerConnected(event: PlayerJoinEvent) {
        event.joinMessage = null
        val player = event.player
        player.health = 20.0

        val spawnLocation = this.getSpawnLocation()
        if(player.location != spawnLocation) player.teleport(spawnLocation)

        player.walkSpeed = DEFAULT_SPEED!! / 10.0f
    }

    @EventHandler
    fun onPlayerDisconnectEvent(event: PlayerQuitEvent) {
        plugin.getMessages().getConfig().getString("quit")?.let { event.quitMessage = it }

        if(CLEAR_INVENTORY_ON_EXIT) event.player.inventory.clear()
    }

    @EventHandler
    fun onVoid(event: PlayerMoveEvent) {
        if (event.player.isOp) return
        if (event.player.location.y <= MIN_Y!!) event.player.teleport(this.getSpawnLocation())
    }

    @EventHandler(priority = EventPriority.LOWEST)
    fun onChat(event: AsyncPlayerChatEvent) {
        val player = event.player

        if (!player.hasPermission("lighthub.chat")) {
            event.isCancelled = true
            LightPlayer.of(player).sendMessage(plugin.getMessages().getConfig().getString("chat"))
        }
    }

    @EventHandler
    fun onCommand(event: PlayerCommandPreprocessEvent) {
        val player = event.player

        if (!player.isOp) {
            if(ALLOWED_COMMANDS.isEmpty()) return
            val command = event.message.split(" ")[0].replace("/", "")

            if (!ALLOWED_COMMANDS.contains(command)) {
                event.setCancelled(true);
                LightPlayer.of(player).sendMessage(plugin.getMessages().getConfig().getString("command-blocked"))
            }
        }
    }

    @EventHandler
    fun onPlayerDeathEvent(event: PlayerDeathEvent) {
        event.deathMessage = null
        event.drops.clear()
        event.entity.player?.teleport(this.getSpawnLocation())
    }

    @EventHandler
    fun onDropDisable(event: PlayerDropItemEvent) {
        val player = event.player

        if (!player.hasPermission("lighthub.drop")) {
            event.isCancelled = true
            LightPlayer.of(player).sendMessage(plugin.getMessages().getConfig().getString("drop"))
        }
    }

    @EventHandler
    fun onDropDisable(event: PlayerPickupItemEvent) {
        val player = event.player

        if (!player.hasPermission("lighthub.pickup")) {
            player.inventory.remove(event.item.itemStack)
            event.isCancelled = true
            LightPlayer.of(player).sendMessage(plugin.getMessages().getConfig().getString("drop"))
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

        if (!player.hasPermission("lighthub.block.break")) {
            event.isCancelled = true
            LightPlayer.of(player).sendMessage(plugin.getMessages().getConfig().getString("breaking"))
        }
    }

    @EventHandler
    fun onPlaceBlocks(event: BlockPlaceEvent) {
        val player = event.player

        if (!player.hasPermission("lighthub.block.place")) {
            event.isCancelled = true
            LightPlayer.of(player).sendMessage(plugin.getMessages().getConfig().getString("placing"))
        }
    }

    @EventHandler
    fun onExplosions(event: EntityExplodeEvent) {
        event.isCancelled = true
    }

    @EventHandler
    fun onExplosions(event: BlockExplodeEvent) {
        event.isCancelled = true
    }

    private fun getSpawnLocation(): Location {
        val spawnSection = plugin.getSpawnConfig().getConfig();

        val spawn = Location(
            spawnSection.getString("world")?.let { Bukkit.getWorld(it) },
            spawnSection.getDouble("x"),
            spawnSection.getDouble("y"),
            spawnSection.getDouble("z")
        )

        spawn.yaw = spawnSection.getInt("yaw").toFloat()
        spawn.pitch = spawnSection.getInt("pitch").toFloat()

        return spawn
    }
}
