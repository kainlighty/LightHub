package ru.kainlight.lighthub.UTILS

import lombok.Getter
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.TextComponent
import net.kyori.adventure.text.format.TextDecoration
import net.kyori.adventure.text.minimessage.MiniMessage
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer
import java.util.regex.Pattern

@Getter
class Parser {

    companion object {
        private val hexPatten: Pattern = Pattern.compile("#[0-9A-Fa-f]{6}")

        fun hex(input: String): TextComponent {
            var input = input
            val matcher = hexPatten.matcher(input)

            while (matcher.find()) {
                val hexColor = matcher.group()
                if (!hexColor.startsWith("&#")) {
                    input = input.replace(hexColor, "&$hexColor")
                }
            }

            return LegacyComponentSerializer.legacyAmpersand().deserialize(input)
        }

        fun hexString(input: String): String {
            return LegacyComponentSerializer.legacySection().serialize(hex(input))
        }

        fun mini(text: String): Component {
            return MiniMessage.miniMessage().deserialize(text).decoration(TextDecoration.ITALIC, false)
        }

        fun apmersand(text: String?): Component {
            return LegacyComponentSerializer.legacyAmpersand().deserialize(text!!)
        }

        fun section(text: String?): Component {
            return LegacyComponentSerializer.legacySection().deserialize(text!!)
        }

        fun Char(ch: Char, text: String?): Component {
            return LegacyComponentSerializer.legacy(ch).deserialize(text!!)
        }
    }
}
