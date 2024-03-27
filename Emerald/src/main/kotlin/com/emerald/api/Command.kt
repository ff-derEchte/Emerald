package com.emerald.api

@Retention(AnnotationRetention.RUNTIME)
annotation class Command(val name: String = "", val permissions: Array<String> = [])
