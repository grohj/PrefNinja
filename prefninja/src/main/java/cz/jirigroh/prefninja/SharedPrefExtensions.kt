package cz.jirigroh.prefninja

import android.content.SharedPreferences
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

inline fun <reified T> SharedPreferences.getDynamic(key: String, defaultValue: T): Any? {
    return when (T::class) {
        Long::class -> getLong(key, defaultValue as Long)
        String::class -> getString(key, defaultValue as String?)
        Int::class -> getInt(key, defaultValue as Int)
        Boolean::class -> getBoolean(key, defaultValue as Boolean)
        Float::class -> getFloat(key, defaultValue as Float)
        //more to come
        else -> throw IllegalArgumentException("${T::class.simpleName} class is not supported.")
    }
}


inline fun <reified T> SharedPreferences.Editor.putDynamic(key: String, value: T) {

    when(T::class) {
        Long::class -> putLong(key, value as Long)
        String::class -> putString(key, value as String?)
        Int::class -> putInt(key, value as Int)
        Boolean::class -> putBoolean(key, value as Boolean)
        Float::class -> putFloat(key, value as Float)
        //more to come
        else -> throw IllegalArgumentException("${T::class.simpleName} class not supported.")
    }
    apply()
}

inline fun <reified T : Any> SharedPreferences.preferenceProperty(
    key: String,
    defaultValue: T
): ReadWriteProperty<Any?, T> {
    return object : ReadWriteProperty<Any?, T> {
        override fun getValue(thisRef: Any?, property: KProperty<*>): T {
            return getDynamic(key, defaultValue) as T
        }

        override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
            edit().putDynamic(key, value)
        }
    }
}

inline fun <reified T> SharedPreferences.preferenceProperty(
    key: String
): ReadWriteProperty<Any?, T?> {
    return object : ReadWriteProperty<Any?, T?> {
        override fun getValue(thisRef: Any?, property: KProperty<*>): T? {
            return getDynamic<T?>(property.name, null) as T?
        }

        override fun setValue(thisRef: Any?, property: KProperty<*>, value: T?) {
            edit().putDynamic(property.name, value)
        }
    }
}

inline fun <reified T> SharedPreferences.preferenceProperty(): ReadWriteProperty<Any?, T?> {
    return object : ReadWriteProperty<Any?, T?> {
        override fun getValue(thisRef: Any?, property: KProperty<*>): T? {
            return getDynamic<T?>(property.name, null) as T?
        }

        override fun setValue(thisRef: Any?, property: KProperty<*>, value: T?) {
            edit().putDynamic(property.name, value)
        }
    }
}