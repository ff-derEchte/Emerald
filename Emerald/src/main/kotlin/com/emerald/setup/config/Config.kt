package com.emerald.setup.config

import com.emerald.api.Config
import com.emerald.setup.extensions.ExtensionClassLoader
import com.emerald.util.getFunctionsWithAnnotation

private typealias ConfigImpl = com.emerald.api.config.Config

fun loadConfig(classLoader: ExtensionClassLoader): ConfigImpl {
    val configFunction = getFunctionsWithAnnotation<Config>(classLoader)

    if (configFunction.size != 1) {
        error("Must have exactly 1 config function")
    }

    val (_, configMethod) = configFunction.first()

    if (configMethod.returnType != ConfigImpl::class.java) {
        error("Config provider must return config")
    }

    return when(val config = configMethod.invoke(null)) {
        is ConfigImpl -> config
        else -> error("Config provider must return config")
    }
}