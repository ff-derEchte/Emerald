package com.emerald.util

import com.emerald.setup.Plugin
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.runBlocking
import org.bukkit.Bukkit

suspend inline fun<T> sync(crossinline task: () -> T): T {
    return if (Bukkit.isPrimaryThread()) {
        task()
    } else {
        val job = CompletableDeferred<T>()
        Bukkit.getScheduler().callSyncMethod(Plugin.instance) {
            job.complete(task())
        }
        job.await()
    }
}

inline fun<T> syncBlocking(crossinline task: () -> T): T = runBlocking { sync(task) }