package ru.kainlight.lighthub.lightlibrary.CONFIGS

import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.configuration.file.YamlConfiguration
import ru.kainlight.lighthub.Main
import java.io.File
import java.io.IOException

@Suppress("all")
class LightConfig(private val plugin: Main, private val subdirectory: String?, private val fileName: String) {
    private var configFile: File? = null
    private var fileConfiguration: FileConfiguration? = null

    constructor(plugin: Main, fileName: String) : this(plugin, null, fileName)

    init {
        saveDefaultConfig()
    }

    fun saveDefaultConfig() {
        if (configFile == null) {
            if (subdirectory == null) {
                configFile = File(plugin.dataFolder, fileName)
            } else {
                val subdirectoryFile = File(plugin.dataFolder, subdirectory)
                if (!subdirectoryFile.exists()) {
                    subdirectoryFile.mkdir()
                }
                configFile = File(subdirectoryFile, fileName)
            }
        }

        if (!configFile!!.exists()) {
            if (subdirectory != null) {
                plugin.saveResource(subdirectory + File.separator + fileName, false)
            } else {
                plugin.saveResource(fileName, false)
            }
        }
    }

    fun reloadConfig() {
        if (configFile == null) {
            configFile = if (subdirectory != null) File(plugin.dataFolder.toString() + File.separator + subdirectory, fileName)
            else File(plugin.dataFolder, fileName)
        }
        fileConfiguration = YamlConfiguration.loadConfiguration(configFile!!)
    }

    fun getConfig(): FileConfiguration {
        if (fileConfiguration == null) {
            reloadConfig();
        }
        return fileConfiguration!!;
    }

    fun saveConfig() {
        if (fileConfiguration == null || configFile == null) return

        try {
            getConfig().save(configFile!!)
        } catch (e: IOException) {
            plugin.logger.warning("Could not save config to $configFile")
        }
    }
}