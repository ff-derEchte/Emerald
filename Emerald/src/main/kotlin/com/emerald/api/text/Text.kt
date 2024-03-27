package com.emerald.api.text

import net.kyori.adventure.text.Component

class Text(
    private val component: Component
) {
    internal fun toComponent() = component
}


fun Component.toText(): Text = Text(this)

fun text(text: String) = Text(Component.text(text))