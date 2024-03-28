package com.emerald.internal.handlers

import com.emerald.api.OnDisable
import com.emerald.api.OnEnable
import com.emerald.internal.extensions.ExtensionClassLoader
import com.emerald.util.getFunctionsWithAnnotation

fun processOnEnableMethods(classLoader: ExtensionClassLoader): () -> Unit {
    val onEnableFunctions = getFunctionsWithAnnotation<OnEnable>(classLoader)

    return {
        onEnableFunctions.forEach { (_, method) -> method.invoke(null) }
    }
}

fun processOnDisableMethods(classLoader: ExtensionClassLoader): () -> Unit {
    val onEnableFunctions = getFunctionsWithAnnotation<OnDisable>(classLoader)

    return {
        onEnableFunctions.forEach { (_, method) -> method.invoke(null) }
    }
}

