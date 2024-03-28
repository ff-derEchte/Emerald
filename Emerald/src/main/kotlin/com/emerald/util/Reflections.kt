package com.emerald.util

import com.emerald.internal.extensions.ExtensionClassLoader
import java.lang.reflect.Field
import java.lang.reflect.Method
import kotlin.reflect.KCallable
import kotlin.reflect.KFunction
import kotlin.reflect.full.*
import kotlin.reflect.jvm.kotlinFunction

internal inline fun<reified A: Annotation> getFunctionsWithAnnotation(classLoader: ExtensionClassLoader): Set<Pair<A, Method>> {
    val functions = mutableSetOf<Pair<A, Method>>()

    val classes = classLoader.loadedClasses.values
    for (clazz in classes) {
        for (method in clazz.methods) {
            if (method.isAnnotationPresent(A::class.java)) {
                functions.add(method.getAnnotation(A::class.java) to method)
            }
        }
    }

    return functions
}

internal inline fun<reified A: Annotation, reified T> getAnnotatedPropertiesWithType(classLoader: ExtensionClassLoader): Set<Field> {
    val fields = mutableSetOf<Field>()

    val classes = classLoader.loadedClasses.values
    for (clazz in classes) {
        val memberFields = clazz.declaredFields.filter { field ->
            field.annotations.any { it is A } && field.type == T::class.java
        }
        fields += memberFields
    }

    return fields
}

internal inline fun<reified A: Annotation> getKFunctionsWithAnnotation(classLoader: ExtensionClassLoader): Set<Pair<A, KCallable<*>>> {
    val functions: MutableSet<Pair<A, KCallable<*>>> = mutableSetOf()

    val classes = classLoader.loadedClasses.values
    for (clazz in classes) {
        for (method in clazz.methods) {
            if (method.isAnnotationPresent(A::class.java)) {
                functions.add(method.getAnnotation(A::class.java) to method.kotlinFunction!!)
            }
        }
    }



    return functions
}