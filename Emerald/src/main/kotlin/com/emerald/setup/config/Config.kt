package com.emerald.setup.config

import com.emerald.api.Config
import com.emerald.setup.extensions.ExtensionClassLoader
import com.emerald.util.getAnnotatedPropertiesWithType

private typealias ConfigImpl = com.emerald.api.config.Config

fun loadConfig(classLoader: ExtensionClassLoader): ConfigImpl {
    val configFields = getAnnotatedPropertiesWithType<Config, ConfigImpl>(classLoader)
    if (configFields.size != 1) {
        error("Must have exactly 1 config function but had ${configFields.size}")
    }

    val configField = configFields.first()

    configField.isAccessible = true
    return when(val config = configField.get(null)) {
        is ConfigImpl -> config
        else -> error("Config provider must return config")
    }
}