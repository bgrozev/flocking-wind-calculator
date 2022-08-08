package net.mustelinae.drift

import kotlinx.browser.window
import kotlin.properties.Delegates
import kotlin.properties.ReadWriteProperty
import net.mustelinae.drift.Input.Companion.DEFAULT

class LocalStorage {
    companion object {
        var dropzone: Dropzone = window.localStorage.getItem("dropzone")?.let { lsDropzone ->
            dropzones.find { it.name == lsDropzone }
        } ?: dropzones[0]
            set(value) {
                field = value
                window.localStorage.setItem("dropzone", value.name)
            }

        var startAltitude: Double by localStorage("startAltitude", DEFAULT.startAltitude)
        var endAltitude: Double by localStorage("endAltitude", DEFAULT.endAltitude)
        var descentRateMph: Double by localStorage("descentRateMph", DEFAULT.descentRateMph)
        var horizontalSpeedMph: Double by localStorage("horizontalSpeedMph", DEFAULT.horizontalSpeedMph)
        var jumprunDirection: Double by localStorage("jumprunDirection", DEFAULT.jumprunDirection)
    }
}

inline fun localStorage(key: String, defaultValue: Double): ReadWriteProperty<Any?, Double> =
    Delegates.observable(window.localStorage.getItem(key)?.toDouble() ?: defaultValue) { property, oldValue, newValue ->
        if (oldValue != newValue) {
            window.localStorage.setItem(key, newValue.toString())
        }
    }
