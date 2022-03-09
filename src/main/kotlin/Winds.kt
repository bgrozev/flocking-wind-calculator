package net.mustelinae.drift

import react.FC
import react.Props
import react.dom.html.ReactHTML.p
import react.dom.html.ReactHTML.table
import react.dom.html.ReactHTML.tbody
import react.dom.html.ReactHTML.th
import react.dom.html.ReactHTML.thead
import react.dom.html.ReactHTML.tr

external interface WindsProps : Props {
    var winds: WindsAloft
}

val Winds = FC<WindsProps> { props ->
    p { +"Winds: " }
    table {
        thead {
            tr {
                th { +"Altitude (ft)" }
                th { +"Speed (kts)" }
                th { +"Direction" }
            }
        }
        tbody {
            (0..18).forEach {
                tr {
                    val altitude = props.winds.altFt[it].toString()
                    th { +altitude }
                    th { + "${props.winds.speed[altitude]}" }
                    th { + "${props.winds.direction[altitude]}" }
                }
            }
        }

    }
}

