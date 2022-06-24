package net.mustelinae.drift

import react.FC
import react.Props
import react.dom.html.ReactHTML

// https://markschulze.net/winds/dropzones.geojson
val dropzones = listOf(
    Dropzone("Chicagoland Skydiving Center", 41.8911147, -89.0802674),
    Dropzone("Skydive Paraclete XP (Raeford)", 35.01910, -79.19060),
    Dropzone("Skydive Phoenix", 33.0531729, -112.1751402),
    Dropzone("Skydive Spaceland Dallas", 33.4492, -96.3783),
    Dropzone("Skydive Spaceland Houston", 29.3569417, -95.4594028),
    Dropzone("Skydive Spaceland San Marcos", 29.7682803, -97.7752789),
    Dropzone("Triangle Skydiving Center", 36.02599, -78.32944),
    Dropzone(Dropzone.CUSTOM_NAME, 0.0, 0.0)
)

fun findDropzone(name: String) = dropzones.find { it.name == name } ?: Dropzone("NONE", -1.0, -1.0)

data class Dropzone(
    val name: String,
    val latitude: Double,
    val longitude: Double
) {
    fun isCustom() = name == CUSTOM_NAME
    companion object {
        const val CUSTOM_NAME = "CUSTOM"
    }
}

external interface DropzonesProps : Props {
    var selected: Dropzone
    var onChange: (Dropzone) -> Unit
}

val DropzonesComponent = FC<DropzonesProps> { props ->
    +"Dropzone: "
    ReactHTML.select {
        onChange = {
            props.onChange(findDropzone(it.target.value))
        }
        dropzones.forEach {
            ReactHTML.option {
                +it.name
                value = it.name
            }
        }
    }
    DoubleInputField {
        name = "Latitude"
        value = props.selected.latitude
        onChange = {
            props.onChange(Dropzone(
                Dropzone.CUSTOM_NAME,
                it,
                props.selected.longitude
            ))
        }
        disabled = !props.selected.isCustom()
    }
    DoubleInputField {
        name = "Longitude"
        value = props.selected.longitude
        onChange = {
            props.onChange(Dropzone(
                Dropzone.CUSTOM_NAME,
                props.selected.latitude,
                it
            ))
        }
        disabled = !props.selected.isCustom()
    }
}