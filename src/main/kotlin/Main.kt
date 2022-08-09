package net.mustelinae.drift

import kotlinx.browser.document
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import react.*
import react.dom.html.ReactHTML.a
import react.dom.html.ReactHTML.br
import react.dom.html.ReactHTML.button
import react.dom.html.ReactHTML.div
import react.dom.render
import react.dom.html.ReactHTML.h2
import react.dom.html.ReactHTML.hr
import react.dom.html.ReactHTML.p
import react.dom.html.ReactHTML.small

fun main() {
    val container = document.getElementById("root") ?: return
    render(App.create(), container)
    GlobalScope.launch { fetchWinds() }
}

suspend fun fetchWinds() {
    getDropzone()?.let {
        console.log("Fetching winds for $it")
        val w = getWindsAloft(it.first.latitude, it.first.longitude, it.second)
        console.log("Fetched winds, setting")
        onWindsChanged(w)
    } ?: run {
        console.log("Not fetching winds, no dropzone selected.")
        onWindsChanged(null)
    }
}
var onWindsChanged: (Winds?) -> Unit = {
    console.log("Default onWindsChanged called.")
}
fun subscribeToWinds(f: (Winds?) -> Unit) {
    onWindsChanged = f
}
var getDropzone: () -> Pair<Dropzone, Int>? = { null }

val App = FC<Props> {
    h2 {
        +"Flocking Wind Calculator"
    }
    p {
        className = "poweredby"
        +"Powered by "
        a {
            href = "https://www.markschulze.net/winds/"
            +"Winds Aloft by Mark Schulze"
        }
    }

    var inputState: Input by useState(Input.fromLocalStorage())
    var selectedDropzone: Dropzone by useState(LocalStorage.dropzone)
    var selectedHourOffset: Int by useState(LocalStorage.hourOffset.toInt())
    var windsState: Winds? by useState(null)
    var showWinds: Boolean by useState(LocalStorage.showWinds)
    getDropzone = { Pair(selectedDropzone, selectedHourOffset) }
    useEffect {
        subscribeToWinds { w ->
            console.log("Got new winds $w")
            windsState = w
        }
    }

    InputContainer {
        input = inputState
        onInputChanged = { newInput ->
            inputState = newInput
            newInput.saveToLocalStorage()
            console.log("Input changed to $newInput")
        }
    }

    p { }
    DropzonesComponent {
        selected = selectedDropzone
        hourOffset = selectedHourOffset
        onChange = { dropzone, hourOffset ->
            console.log("Dropzone changed to $dropzone (hourOffset=$hourOffset)" )
            val changed = selectedDropzone != dropzone || selectedHourOffset != hourOffset
            selectedDropzone = dropzone
            selectedHourOffset = hourOffset
            if (changed) {
                LocalStorage.dropzone = dropzone
                LocalStorage.hourOffset = hourOffset.toDouble()
                windsState = null
                GlobalScope.launch { fetchWinds() }
            }
        }
    }

    p { }
    hr { }
    val w = windsState
    if (w == null) {
        +"Fetching winds..."
    }
    else {
        val dz = dropzones.find { it.latitude == w.lat && it.longitude == w.lon }
        div {
            className = "grid container"
            div { +"WindsAloft for:" }
            div { +(dz?.name ?: "lat=${w.lat}, lon=${w.lon}") }
            div { +"Fetched at:" }
            div { +w.fetchTime }
            div { +"Forcast valid:" }
            div { +w.windsAloft.getValidAtString(selectedHourOffset) } }
        br { }
        div {
            className = "grid container"
            Drift {
                winds = w
                input = inputState
            }
        }
        hr { }
        button {
            +if (showWinds) "Hide winds" else "Show winds"
            onClick = {
                LocalStorage.showWinds = !showWinds
                showWinds = !showWinds
            }
        }
        if (showWinds) {
            WindsContainer { winds = w }
        }
    }

    p {
        small { +"For questions or feedback email boris at mustelinae.net" }
    }
    // Map { }
}

