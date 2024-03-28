package com.emerald.internal.config

@JvmInline
value class VersionString(val string: String) {
    init {
        require(Regex("^\\d+\\.\\d+$").matches(string)) { "Invalid Input" }
    }
}
