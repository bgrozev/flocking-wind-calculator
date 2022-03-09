package net.mustelinae.drift

import react.FC
import react.Props
import react.dom.html.InputType
import react.dom.html.ReactHTML
import react.dom.html.ReactHTML.button
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.input
import react.dom.html.ReactHTML.p
import react.key

data class Input(
    val startAltitude: Double,
    val endAltitude: Double,
    val descentRateMph: Double,
    val horizontalSpeedMph: Double,
    val jumprunDirection: Double
) {
    companion object {
        val INITIAL = Input(12.0, 4.0, 21.0, 50.0, 180.0)
    }
}

external interface InputContainerProps : Props {
    var input: Input
    var onInputChanged: (Input) -> Unit
}

val InputContainer = FC<InputContainerProps> { props ->
    div {
        +"Altitude from "
        input {
            type = InputType.number
            key = "start-altitude"
            value = props.input.startAltitude.toString()
            onChange = {
                console.log("Start altitude changed to ${it.target.value}")
                props.onInputChanged(
                    Input(
                        it.target.value.toDouble(),
                        props.input.endAltitude,
                        props.input.descentRateMph,
                        props.input.horizontalSpeedMph,
                        props.input.jumprunDirection
                    )
                )
            }
        }
        +" kft down to "
        input {
            type = InputType.number
            key = "end-altitude"
            value = props.input.endAltitude.toString()
            onChange = {
                console.log("End altitude changed to ${it.target.value}")
                props.onInputChanged(
                    Input(
                        props.input.startAltitude,
                        it.target.value.toDouble(),
                        props.input.descentRateMph,
                        props.input.horizontalSpeedMph,
                        props.input.jumprunDirection
                    )
                )
            }
        }
        + "kft."
    }

    +"Descent rate "
    input {
        type = InputType.number
        key = "descent-rate"
        value = props.input.descentRateMph.toString()
        onChange = {
            console.log("descent rate changed to ${it.target.value}")
            props.onInputChanged(
                Input(
                    props.input.startAltitude,
                    props.input.endAltitude,
                    it.target.value.toDouble(),
                    props.input.horizontalSpeedMph,
                    props.input.jumprunDirection
                )
            )
        }
    }
    +" mph, horizontal speed "
    input {
        type = InputType.number
        key = "horizontal-speed"
        value = props.input.horizontalSpeedMph.toString()
        onChange = {
            console.log("horizontal speed changed to ${it.target.value}")
            props.onInputChanged(
                Input(
                    props.input.startAltitude,
                    props.input.endAltitude,
                    props.input.descentRateMph,
                    it.target.value.toDouble(),
                    props.input.jumprunDirection
                )
            )
        }
    }
    +" mph. "
    button {
        +"Flow"
        onClick = {
            props.onInputChanged(
                Input(
                    props.input.startAltitude,
                    props.input.endAltitude,
                    21.0,
                    50.0,
                    props.input.jumprunDirection
                )
            )
        }
    }
    button {
        +"Float"
        onClick = {
            props.onInputChanged(
                Input(
                    props.input.startAltitude,
                    props.input.endAltitude,
                    17.0,
                    40.0,
                    props.input.jumprunDirection
                )
            )
        }
    }

    DoubleInputField {
        name = "Jumprun "
        value = props.input.jumprunDirection
        onChange = {
            props.onInputChanged(
                Input(
                    props.input.startAltitude,
                    props.input.endAltitude,
                    props.input.descentRateMph,
                    props.input.horizontalSpeedMph,
                    it
                )
            )
        }
    }
}

val DoubleInputField = FC<InputFieldProps<Double>> { props ->
    ReactHTML.div {
        +props.name
        ReactHTML.input {
            type = InputType.number
            key = props.name
            value = props.value.toString()
            onChange = {
                console.log("${props.name} changed to ${it.target.value}")
                props.onChange(it.target.value.toDouble())
            }
            disabled = props.disabled
        }
    }
}

external interface InputFieldProps<T> : Props {
    var name: String
    var value: T
    var onChange: (T) -> Unit
    var disabled: Boolean
}
