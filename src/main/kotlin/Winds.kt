package net.mustelinae.drift

import csstype.FontWeight
import react.FC
import react.Props
import react.css.css
import react.dom.html.ReactHTML.table
import react.dom.html.ReactHTML.tbody
import react.dom.html.ReactHTML.th
import react.dom.html.ReactHTML.thead
import react.dom.html.ReactHTML.tr

external interface WindsProps : Props {
    var winds: Winds
}

val WindsContainer = FC<WindsProps> { props ->
    table {
        className = "windsTable"
        thead {
            tr {
                th { +"Altitude (ft)" }
                th { +"Speed (kts)" }
                th { +"Direction" }
            }
        }
        tbody {
            (0..40).forEach {
                tr {
                    val altitude = props.winds.windsAloft.altFt[it].toString()
                    val speed = props.winds.windsAloft.speed[altitude]
                    val direction = props.winds.windsAloft.direction[altitude]
                    th { +altitude }
                    th { + (speed?.toString() ?: "n/a") }
                    th { + (direction?.toString() ?: "n/a") }
                }
            }
        }

    }
}

