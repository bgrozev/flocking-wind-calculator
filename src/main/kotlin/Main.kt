package net.mustelinae.drift

import kotlinx.browser.document
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import react.*
import react.dom.html.ReactHTML.br
import react.dom.render
import react.dom.html.ReactHTML.h1
import react.dom.html.ReactHTML.p

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
    h1 {
        +"Flocking Wind Calculator"
    }

    var inputState: Input by useState(Input.INITIAL)
    var selectedDropzone: Dropzone by useState(dropzones[0])
    var selectedHourOffset: Int by useState(0)
    var windsState: Winds? by useState(null)
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
                windsState = null
                GlobalScope.launch { fetchWinds() }
            }
        }
    }

    p { }
    val w = windsState
    if (w == null) {
        +"Fetching winds..."
    }
    else {
        val dz = dropzones.find { it.latitude == w.lat && it.longitude == w.lon }
        +"WindsAloft for ${dz?.name ?: "lat=${w.lat}, lon=${w.lon}"} fetched at ${w.fetchTime}"
        br { }
        +w.windsAloft.getValidAtString()
        br { }
        br { }
        Drift {
            winds = w
            input = inputState
        }

        WindsContainer { winds = w }
    }
    // Map { }
}

