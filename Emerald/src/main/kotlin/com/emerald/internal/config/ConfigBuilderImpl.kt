package com.emerald.internal.config

import com.emerald.api.config.Config
import com.emerald.api.config.ConfigBuilder

internal class ConfigBuilderImpl : ConfigBuilder {
    override var name: String? = null
    override var version: String? = null

    fun toConfig(): Config = Config(name!!, VersionString(version!!))
}