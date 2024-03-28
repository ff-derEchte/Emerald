package com.emerald.api.text

import net.kyori.adventure.text.format.TextDecoration

enum class TextStyle {
    Bold,
    Italic,
    Underlined,
    Crossed,
    Normal
}

internal fun TextStyle.toTextDecoration() = when(this) {
    TextStyle.Bold -> TextDecoration.BOLD
    TextStyle.Italic -> TextDecoration.ITALIC
    TextStyle.Underlined -> TextDecoration.UNDERLINED
    TextStyle.Crossed -> TextDecoration.STRIKETHROUGH
    TextStyle.Normal -> null
}
