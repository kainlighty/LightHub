package ru.kainlight.lighthub.lightlibrary.BUILDERS

import org.bukkit.Color
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta
import ru.kainlight.lighthub.lightlibrary.UTILS.Parser.hexString
import java.util.*
import javax.annotation.Nonnegative

class ItemBuilder() {

    private var itemStack: ItemStack? = null

    private var displayName: String? = null
    private var material: Material? = null
    var itemMeta: ItemMeta? = null

    @Nonnegative private var amount = 1
    var glow = false
    private var color: Color? = null
    @Nonnegative private var damage = 1

    var base64: String? = null
    var skullName: String? = null
    var skullOwner: UUID? = null

    private var data: MutableMap<String, Any> = mutableMapOf()
    private var flags: MutableList<ItemFlag> = mutableListOf()
    private var lore: MutableList<String> = mutableListOf()
    private var enchantments: MutableMap<Enchantment, Int> = mutableMapOf()

    constructor(itemStack: ItemStack) : this() {
        this.itemStack = itemStack
        this.material = itemStack.type
        this.amount = itemStack.amount
        this.itemMeta = itemStack.itemMeta

        if (this.itemMeta!!.hasLore()) {
            this.lore = itemMeta!!.lore!!
        }
        if (this.itemMeta!!.hasEnchants()) {
            this.enchantments = itemMeta!!.enchants
        }
        if (this.itemMeta!!.hasDisplayName()) {
            this.displayName = itemMeta!!.displayName
        }
        if (this.itemMeta!!.itemFlags.isNotEmpty()) {
            this.flags = Arrays.asList(*itemMeta!!.itemFlags.toTypedArray<ItemFlag>())
        }
    }

    constructor(material: Material, @Nonnegative amount: Int = 1) : this() {
        var a = amount
        if (a > 64) a = 64

        this.material = material
        this.amount = a
    }

    constructor(materialName: String, @Nonnegative amount: Int = 1) : this() {
        var a = amount
        if (a > 64) a = 64

        this.material = Material.getMaterial(materialName)
        this.amount = a
    }

    fun amount(@Nonnegative amount: Int = 1): ItemBuilder {
        var a = amount
        if (a > 64) a = 64

        this.amount = a
        return this
    }

    fun glow(): ItemBuilder {
        this.glow = true
        return this
    }

    fun color(color: Color): ItemBuilder {
        if (!this.material!!.name.lowercase(Locale.ROOT).split("_")[0].equals("leather", ignoreCase = true)) {
            throw IllegalStateException("The material must be a leather equipment part.");
        }

        this.color = color
        return this
    }

    fun displayName(displayName: String?): ItemBuilder {
        if (displayName == "" || displayName == null) {
            this.displayName = ""
            return this
        }

        this.displayName = hexString(displayName)
        return this
    }

    fun data(key: String, value: Any): ItemBuilder {
        data[key] = value
        return this
    }

    fun flags(flags: MutableList<ItemFlag>): ItemBuilder {
        this.flags = flags
        return this
    }

    fun flags(vararg flags: ItemFlag): ItemBuilder {
        this.flags = Arrays.asList(*flags)
        return this
    }

    fun flag(flag: ItemFlag): ItemBuilder {
        flags.add(flag)
        return this
    }

    fun defaultFlags(): ItemBuilder {
        this.flags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_DESTROYS, ItemFlag.HIDE_UNBREAKABLE)
        return this
    }

    fun clearLore(): ItemBuilder {
        if (itemMeta !!.lore == null || itemMeta !!.lore !!.isEmpty()) return this
        itemMeta !!.lore !!.clear()
        return this
    }

    fun lore(vararg lines: String?): ItemBuilder {
        val finalList: MutableList<String> = ArrayList()

        for (line in lines) {
            val l = hexString(line !!)
            finalList.add(l)
        }

        this.lore = finalList
        return this
    }

    fun lore(lines: List<String?>): ItemBuilder {
        val finalList: MutableList<String> = ArrayList()

        for (line in lines) {
            val l = hexString(line!!)
            finalList.add(l)
        }

        this.lore = finalList
        return this
    }

    fun appendLine(line: String?): ItemBuilder {
        lore.add(hexString(line!!))
        return this
    }

    fun clearEnchants(): ItemBuilder {
        if (!itemMeta!!.hasEnchants()) return this

        itemMeta !!.enchants.keys.forEach{ itemMeta!!.removeEnchant(it!!) }
        return this
    }

    fun damage(@Nonnegative damage: Int): ItemBuilder {
        require(damage > 0) { "Damage must be at least 1." }

        this.damage = damage
        return this
    }

    fun removeEnchant(enchantment: Enchantment): ItemBuilder {
        itemMeta!!.removeEnchant(enchantment)
        return this
    }

    fun removeEnchant(vararg enchantments: Enchantment): ItemBuilder {
        enchantments.forEach { this.itemMeta!!.removeEnchant(it) }
        return this
    }

    fun enchant(enchantment: Enchantment, @Nonnegative level: Int = 1): ItemBuilder {
        enchantments[enchantment] = level
        return this
    }

    fun enchant(enchantments: MutableList<Enchantment>, level: MutableList<Int>): ItemBuilder {
        require(enchantments.size != level.size) { "The passed parameters must have the same size." }

        for (i in enchantments.indices) {
            val enchantment = enchantments[i]
            val enchantmentLevel = level[i]

            this.enchantments[enchantment] = enchantmentLevel
        }
        return this
    }

    fun enchant(enchantments: MutableList<Enchantment>, @Nonnegative level: Int = 1): ItemBuilder {
        enchantments.forEach { this.enchantments[it] = level }
        return this
    }

    fun build(): ItemStack {
        if (this.itemStack == null) {
            this.itemStack = ItemStack(material!!, this.amount)
        }
        if (this.itemMeta == null) {
            this.itemMeta = itemStack!!.itemMeta
        }
        if (this.displayName != null) {
            itemMeta!!.setDisplayName(this.displayName)
        }
        if (lore.isNotEmpty()) {
            itemMeta!!.lore = this.lore
        }
        if (flags.isNotEmpty()) {
            flags.forEach{ itemMeta!!.addItemFlags(it) }
        }
        if (enchantments.isNotEmpty()) {
            enchantments.forEach { (enchantment: Enchantment?, level: Int?) ->
                itemMeta!!.addEnchant(enchantment, level, true)
            }
        }

        itemStack!!.amount = this.amount

        if (this.glow) {
            itemMeta!!.addEnchant(Enchantment.ARROW_DAMAGE, 1, true)

            if (!itemMeta!!.itemFlags.contains(ItemFlag.HIDE_ENCHANTS)) {
                itemMeta!!.addItemFlags(ItemFlag.HIDE_ENCHANTS)
            }
        }

        itemStack !!.setItemMeta(this.itemMeta)
        return this.itemStack!!
    }

}