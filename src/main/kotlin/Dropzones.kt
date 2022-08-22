package net.mustelinae.drift

import react.FC
import react.Props
import react.dom.html.InputType
import react.dom.html.ReactHTML
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.input
import react.key

// https://markschulze.net/winds/dropzones.geojson
val dropzones = listOf(
    Dropzone("Chicagoland Skydiving Center", 41.891, -89.080),
    Dropzone("Cleveland Skydiving Center", 41.352, -81.099),
    Dropzone("Jumptown", 42.568, -72.283),
    Dropzone("Skydive Arizona", 32.805, -111.584),
    Dropzone("Skydive Empuriabrava", 42.259, 3.109),
    Dropzone("Skydive Paraclete XP (Raeford)", 35.019, -79.191),
    Dropzone("Skydive Phoenix", 33.053, -112.175),
    Dropzone("Skydive Spaceland Atlanta", 33.977, -85.166),
    Dropzone("Skydive Spaceland Dallas", 33.449, -96.378),
    Dropzone("Skydive Spaceland Houston", 29.357, -95.459),
    Dropzone("Skydive Spaceland San Marcos", 29.768, -97.775),
    Dropzone("Triangle Skydiving Center", 36.026, -78.329),
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
    var hourOffset: Int
    var onChange: (Dropzone, Int) -> Unit
}

val DropzonesComponent = FC<DropzonesProps> { props ->
    div {
        className = "grid container"
        +"Dropzone:"
        ReactHTML.select {
            onChange = {
                props.onChange(findDropzone(it.target.value), props.hourOffset)
            }
            dropzones.forEach {
                ReactHTML.option {
                    +it.name
                    value = it.name
                }
            }
            defaultValue = LocalStorage.dropzone.name
        }
        DoubleInputField {
            name = "Latitude:"
            className = "inputLatLon"
            value = props.selected.latitude
            onChange = {
                props.onChange(
                    Dropzone(
                        Dropzone.CUSTOM_NAME,
                        it,
                        props.selected.longitude
                    ),
                    props.hourOffset
                )
            }
            disabled = !props.selected.isCustom()
        }
        DoubleInputField {
            name = "Longitude:"
            className = "inputLatLon"
            value = props.selected.longitude
            onChange = {
                props.onChange(
                    Dropzone(
                        Dropzone.CUSTOM_NAME,
                        props.selected.latitude,
                        it
                    ),
                    props.hourOffset
                )
            }
            disabled = !props.selected.isCustom()
        }
        +"Hour offset "
            input {
                name = "Hour offset"
                className = "inputNumber"
                type = InputType.number
                key = "hourOffset"
                value = props.hourOffset.toString()
                onChange = {
                    console.log("hourOffset changed to ${it.target.value}")
                    props.onChange(
                        props.selected,
                        it.target.value.toInt()
                    )
                }
            }
    }
}