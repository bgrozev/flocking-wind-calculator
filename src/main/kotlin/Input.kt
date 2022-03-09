package net.mustelinae.drift

import react.FC
import react.Props
import react.dom.html.InputType
import react.dom.html.ReactHTML
import react.dom.html.ReactHTML.button
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
    DoubleInputField {
        name = "Start Altitude (kft)"
        value = props.input.startAltitude
        onChange = {
            props.onInputChanged(
                Input(
                    it,
                    props.input.endAltitude,
                    props.input.descentRateMph,
                    props.input.horizontalSpeedMph,
                    props.input.jumprunDirection
                )
            )
        }
    }

    DoubleInputField {
        name = "End Altitude (kft)"
        value = props.input.endAltitude
        onChange = {
            props.onInputChanged(
                Input(
                    props.input.startAltitude,
                    it,
                    props.input.descentRateMph,
                    props.input.horizontalSpeedMph,
                    props.input.jumprunDirection
                )
            )
        }
    }

    p { }
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
        name = "Descent Rate (mph)"
        value = props.input.descentRateMph
        onChange = {
            props.onInputChanged(
                Input(
                    props.input.startAltitude,
                    props.input.endAltitude,
                    it,
                    props.input.horizontalSpeedMph,
                    props.input.jumprunDirection
                )
            )
        }
    }
    DoubleInputField {
        name = "Horizontal speed (mph)"
        value = props.input.horizontalSpeedMph
        onChange = {
            props.onInputChanged(
                Input(
                    props.input.startAltitude,
                    props.input.endAltitude,
                    props.input.descentRateMph,
                    it,
                    props.input.jumprunDirection
                )
            )
        }
    }

    p {}
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
