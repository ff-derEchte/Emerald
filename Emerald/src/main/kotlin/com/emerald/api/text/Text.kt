package com.emerald.api.text

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.Style


data class Text(
    val text: String,
    val color: Color?,
    val textStyle: TextStyle = TextStyle.Normal,
    val child: Text? = null
) {
    internal fun toComponent(): Component = Component.text(text).apply {
        color?.let { c -> style(Style.style(c.toTextColor()).decorate()) }
        textStyle.toTextDecoration()?.let { style(style().decorate(it)) }
        child?.let { c -> append(c.toComponent()) }
    }

    fun appendChild(child: Text): Text {
        return if (this.child == null) {
            Text(text, color, textStyle, child)
        } else {
            Text(text, color, textStyle, this.child.appendChild(child))
        }
    }
}

fun text(text: String, color: Color? = null, ) = Text(text, color)

fun text(text: String, color: Int) = text(text, color.rgb)
fun text(text: String, color: Long) = text(text, color.argb)

fun Text.bold() = Text(text, color, TextStyle.Bold, child)
fun Text.italic() = Text(text, color, TextStyle.Italic, child)
fun Text.normal() = Text(text, color, TextStyle.Normal, child)
fun Text.underlined() = Text(text, color, TextStyle.Underlined, child)

fun text(texts: List<Text>): Text {
    return when(texts.size) {
        0 -> error("")
        1 -> texts[0]
        else -> texts[0].appendChild(text(texts.slice(1..<texts.size)))
    }
}

fun text(vararg texts: Text) = text(texts.toList())

