package ru.kainlight.lighthub.lightlibrary.UTILS

import lombok.Getter
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.TextComponent
import net.kyori.adventure.text.format.TextDecoration
import net.kyori.adventure.text.minimessage.MiniMessage
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer
import java.util.regex.Pattern

@Getter
object Parser {

    private val hexPatten: Pattern = Pattern.compile("#[0-9A-Fa-f]{6}")

    @JvmStatic
    fun hex(input: String): TextComponent {
        var i = input
        val matcher = hexPatten.matcher(i)

        while (matcher.find()) {
            val hexColor = matcher.group()
            if (! hexColor.startsWith("&#")) {
                i = i.replace(hexColor, "&$hexColor")
            }
        }

        return LegacyComponentSerializer.legacyAmpersand().deserialize(i).decoration(TextDecoration.ITALIC, false)
    }

    fun hexString(input: String): String {
        return LegacyComponentSerializer.legacySection().serialize(hex(input))
    }

    fun mini(text: String): Component {
        return MiniMessage.miniMessage().deserialize(text).decoration(TextDecoration.ITALIC, false)
    }

    fun apmersand(text: String): Component {
        return LegacyComponentSerializer.legacyAmpersand().deserialize(text).decoration(TextDecoration.ITALIC, false)
    }

    fun section(text: String): Component {
        return LegacyComponentSerializer.legacySection().deserialize(text).decoration(TextDecoration.ITALIC, false)
    }

    fun char(ch: Char, text: String): Component {
        return LegacyComponentSerializer.legacy(ch).deserialize(text).decoration(TextDecoration.ITALIC, false)
    }
}
