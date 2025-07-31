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
    Dropzone("Århus Faldskærm Club", 56.313, 10.615),
    Dropzone("Chicagoland Skydiving Center", 41.891, -89.080),
    Dropzone("Cleveland Skydiving Center", 41.352, -81.099),
    Dropzone("Dropzone Denmark", 56.184, 9.031),
    Dropzone("HLF Denmark", 56.396, 8.442),
    Dropzone("Jump Georgia Skydiving", 32.650, -81.598),
    Dropzone("Jumptown", 42.568, -72.283),
    Dropzone("Kolding Faldskærmsklub", 55.437, 9.327),
    Dropzone("Skydive Alabama", 34.267, -86.863),
    Dropzone("Skydive Arizona", 32.805, -111.584),
    Dropzone("Skydive Atlanta", 32.953, -84.262),
    Dropzone("Skydive City (ZHills)", 28.220, -82.151),
    Dropzone("Skydive Deland", 29.062, -81.286),
    Dropzone("Skydive Elsinore", 33.631, -117.296),
    Dropzone("Skydive Empuriabrava", 42.259, 3.109),
    Dropzone("Skydive Grand Haven", 43.035, -86.200),
    Dropzone("Skydive Kapowsin", 47.242, -123.142),
    Dropzone("Skydive Langar", 52.890,-0.909),
    Dropzone("Skydive Moab", 38.759, -109.745),
    Dropzone("Skydive Paraclete XP (Raeford)", 35.019, -79.191),
    Dropzone("Skydive Phoenix", 33.053, -112.175),
    Dropzone("Skydive Sibson", 52.561, -0.397),
    Dropzone("Skydive Snohomish", 47.907, -122.101),
    Dropzone("Skydive Spaceland Atlanta", 33.977, -85.166),
    Dropzone("Skydive Spaceland Dallas", 33.449, -96.378),
    Dropzone("Skydive Spaceland Houston", 29.357, -95.459),
    Dropzone("Skydive Spaceland San Marcos", 29.768, -97.775),
    Dropzone("Skydive Spain", 37.296,-6.162),
    Dropzone("Skydive Suffolk", 36.679,-76.610),
    Dropzone("Skydive Tennessee", 35.381, -86.240),
    Dropzone("Skydive Utah", 40.619, -112.407),
    Dropzone("Skydive Voss", 60.640, 6.482),
    Dropzone("Start Skydiving", 39.530, -84.398),
    Dropzone("Texas Skydiving", 30.417, -96.968),
    Dropzone("Triangle Skydiving Center", 36.026, -78.329),
    Dropzone("West Jump Denmark", 56.551, 9.168),
    Dropzone("Wisconsin Skydiving Center", 42.962, -88.818),
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
