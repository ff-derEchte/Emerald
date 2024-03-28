package com.emerald.api.config

import com.emerald.internal.config.ConfigBuilderImpl
import com.emerald.internal.config.VersionString

data class Config(
    val name: String,
    val version: VersionString
)

fun config(closure: ConfigBuilder.() -> Unit) = ConfigBuilderImpl()
    .apply { closure() }
    .toConfig()


interface ConfigBuilder {
    var name: String?
    var version: String?
}