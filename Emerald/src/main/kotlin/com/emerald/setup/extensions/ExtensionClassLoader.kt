package com.emerald.setup.extensions

import java.io.IOException
import java.util.jar.JarEntry
import java.util.jar.JarFile


class ExtensionClassLoader(
    jarPath: String,
    private val parent: ClassLoader
) : ClassLoader() {

    internal val loadedClasses: MutableMap<String, Class<*>> = mutableMapOf()

    init {
        loadClassesFromJar(jarPath)
    }

    private fun loadClassesFromJar(jarFilePath: String) {
        try {
            val jarFile = JarFile(jarFilePath)
            jarFile.stream().forEach { entry: JarEntry ->
                if (entry.name.endsWith(".class")) {
                    val className = entry.name.replace("/", ".").removeSuffix(".class")

                    val classBytes: ByteArray = jarFile.getInputStream(entry).readAllBytes()
                    val clazz = defineClass(className, classBytes, 0, classBytes.size)
                    loadedClasses[className] = clazz
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    override fun loadClass(name: String?): Class<*> = loadedClasses[name] ?: parent.loadClass(name)

    override fun findClass(name: String?): Class<*> = loadedClasses[name] ?: parent.loadClass(name)
}
