package com.emerald.setup.config

@JvmInline
value class VersionString(val string: String) {
    init {
        require(Regex("^\\d+\\.\\d+$").matches(string)) { "Invalid Input" }
    }
}
