package com.emerald.api.text

import net.kyori.adventure.text.format.TextColor

data class Color(val a: UByte, val r: UByte, val g: UByte, val b: UByte)

fun Color.toTextColor() = TextColor.color(r.toInt(), g.toInt(), b.toInt())

val Long.argb: Color
    get() {
        val a = ((this shr 24) and 0xFF).toUByte()
        val r = ((this shr 16) and 0xFF).toUByte()
        val g = ((this shr 8) and 0xFF).toUByte()
        val b = (this and 0xFF).toUByte()
        return Color(a, r, g, b)
    }

val Int.rgb: Color
    get() {
        val r = ((this shr 16) and 0xFF).toUByte()
        val g = ((this shr 8) and 0xFF).toUByte()
        val b = (this and 0xFF).toUByte()
        return Color(255u, r, g, b)
    }