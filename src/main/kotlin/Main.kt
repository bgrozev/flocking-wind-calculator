package net.mustelinae.drift

import kotlinx.browser.document
import react.*
import react.dom.render
import react.dom.html.ReactHTML.h1
import react.dom.html.ReactHTML.p

fun main() {
    val container = document.getElementById("root") ?: error("Couldn't find root container!")
    render(App.create(), container)
}

val App = FC<Props> {
    h1 {
        +"Hello"
    }

    var inputState: Input by useState(Input.INITIAL)
    var selectedDropzone: Dropzone by useState(dropzones[0])

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
        onChange = { dropzone ->
            console.log("Dropzone changed to $dropzone" )
            selectedDropzone = dropzone
        }
    }

    Drift {
        winds = getWindsAloft()
        input = inputState
    }

    Winds { winds = getWindsAloft() }
    // Map { }
}

