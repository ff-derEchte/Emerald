package com.emerald.util

import com.emerald.api.player.BukkitPlayer
import com.emerald.setup.extensions.ExtensionClassLoader
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import java.lang.reflect.Method

internal inline fun<reified A: Annotation> getFunctionsWithAnnotation(classLoader: ExtensionClassLoader): Set<Pair<A, Method>> {
    val functions = mutableSetOf<Pair<A, Method>>()

    val classes = classLoader.loadedClasses.values
    for (clazz in classes) {
        for (method in clazz.methods) {
            if (method.isAnnotationPresent(A::class.java)) {
                functions.add(method.getAnnotation(A::class.java) to method)
            }
        }
    }

    return functions
}

fun foo(player: BukkitPlayer) {
}