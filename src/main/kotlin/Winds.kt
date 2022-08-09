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
            (0..18).forEach {
                tr {
                    val altitude = props.winds.windsAloft.altFt[it].toString()
                    th { +altitude }
                    th { + "${props.winds.windsAloft.speed[altitude]}" }
                    th { + "${props.winds.windsAloft.direction[altitude]}" }
                }
            }
        }

    }
}

