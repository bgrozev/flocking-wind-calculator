package net.mustelinae.drift

import kotlinx.browser.window
import kotlin.properties.Delegates
import kotlin.properties.ReadWriteProperty
import net.mustelinae.drift.Input.Companion.DEFAULT

class LocalStorage {
    companion object {
        var startAltitude: Double by localStorage("startAltitude", DEFAULT.startAltitude)
        var endAltitude: Double by localStorage("endAltitude", DEFAULT.endAltitude)
        var descentRateMph: Double by localStorage("descentRateMph", DEFAULT.descentRateMph)
        var horizontalSpeedMph: Double by localStorage("horizontalSpeedMph", DEFAULT.horizontalSpeedMph)
        var jumprunDirection: Double by localStorage("jumprunDirection", DEFAULT.jumprunDirection)

        var hourOffset: Double by localStorage("hourOffset", 0.0)
        var showWinds: Boolean by localStorage("showWinds", false)

        var latitude: Double by localStorage("latitude", 0.0)
        var longitude: Double by localStorage("longitude", 0.0)

        var dropzone: Dropzone = window.localStorage.getItem("dropzone")?.let { lsDropzone ->
            if (lsDropzone == Dropzone.CUSTOM_NAME) {
                Dropzone(Dropzone.CUSTOM_NAME, latitude, longitude)
            } else {
                dropzones.find { it.name == lsDropzone }
            }
        } ?: dropzones[0]
            set(value) {
                field = value
                window.localStorage.setItem("dropzone", value.name)
                latitude = dropzone.latitude
                longitude = dropzone.longitude
            }

        var nautical: Boolean by localStorage("nautical", false)
    }
}

fun <T> localStorage(key: String, defaultValue: T, toT: (String) -> T): ReadWriteProperty<Any?, T> =
    Delegates.observable(window.localStorage.getItem(key)?.let { toT(it) } ?: defaultValue) { _, oldValue, newValue ->
        if (oldValue != newValue) {
            window.localStorage.setItem(key, newValue.toString())
        }
    }

fun localStorage(key: String, defaultValue: Double) = localStorage(key, defaultValue) { it.toDouble() }
fun localStorage(key: String, defaultValue: Boolean) = localStorage(key, defaultValue) { it.toBoolean() }
