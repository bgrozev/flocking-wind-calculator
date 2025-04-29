package net.mustelinae.drift

import csstype.px
import react.FC
import react.Props
import react.css.css
import react.dom.html.InputType
import react.dom.html.ReactHTML.button
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.input
import react.key

data class Input(
    val startAltitude: Double,
    val endAltitude: Double,
    val descentRateMph: Double,
    val horizontalSpeedMph: Double,
    val jumprunDirection: Double
) {
    companion object {
        val DEFAULT = Input(12.0, 4.0, 21.0, 50.0, -1.0)

        fun fromLocalStorage() = Input(
            startAltitude = LocalStorage.startAltitude,
            endAltitude = LocalStorage.endAltitude,
            descentRateMph = LocalStorage.descentRateMph,
            horizontalSpeedMph = LocalStorage.horizontalSpeedMph,
            jumprunDirection = LocalStorage.jumprunDirection
        )
    }

    fun saveToLocalStorage() {
        LocalStorage.startAltitude = startAltitude
        LocalStorage.endAltitude = endAltitude
        LocalStorage.descentRateMph = descentRateMph
        LocalStorage.horizontalSpeedMph = horizontalSpeedMph
        LocalStorage.jumprunDirection = jumprunDirection
    }
}

external interface InputContainerProps : Props {
    var input: Input
    var onInputChanged: (Input) -> Unit
}

val InputContainer = FC<InputContainerProps> { props ->
    div {
        className = "grid container"
        +"Altitude from:"
        div {
            input {
                type = InputType.number
                className = "inputNumber"
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
            +" kft"
        }
        +"Down to:"
        div {
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
                className = "inputNumber"
            }
            +" kft"
        }
        +"Descent rate:"
        div {
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
                className = "inputNumber"
            }
            +" mph"
        }
        +"Horizontal speed:"
        div {
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
                className = "inputNumber"
            }
            +" mph"
        }
        div {
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
            button {
                +"XRW"
                onClick = {
                    props.onInputChanged(
                        Input(
                            props.input.startAltitude,
                            props.input.endAltitude,
                            40.0,
                            70.0,
                            props.input.jumprunDirection
                        )
                    )
                }
            }
            button {
                +"CRW"
                onClick = {
                    props.onInputChanged(
                        Input(
                            props.input.startAltitude,
                            props.input.endAltitude,
                            15.0,
                            27.0,
                            props.input.jumprunDirection
                        )
                    )
                }
            }
        }
        div {}

        div {
            css { marginTop = 10.px }
            +"Canopy flight direction: "
        }
        div {
            css { marginTop = 10.px }
            input {
                type = InputType.number
                key = "jumprun"
                value = props.input.jumprunDirection.toString()
                onChange = {
                    console.log("jumprun changed to ${it.target.value}")
                    props.onInputChanged(
                        Input(
                            props.input.startAltitude,
                            props.input.endAltitude,
                            props.input.descentRateMph,
                            props.input.horizontalSpeedMph,
                            it.target.value.toDouble()
                        )
                    )
                }
                className = "inputNumber"
            }
            +"˚"
        }
        div {
            button {
                +"N"
                onClick = {
                    props.onInputChanged(
                        Input(
                            props.input.startAltitude,
                            props.input.endAltitude,
                            props.input.descentRateMph,
                            props.input.horizontalSpeedMph,
                            0.0
                        )
                    )
                }
            }
            button {
                +"E"
                onClick = {
                    props.onInputChanged(
                        Input(
                            props.input.startAltitude,
                            props.input.endAltitude,
                            props.input.descentRateMph,
                            props.input.horizontalSpeedMph,
                            90.0
                        )
                    )
                }
            }
            button {
                +"S"
                onClick = {
                    props.onInputChanged(
                        Input(
                            props.input.startAltitude,
                            props.input.endAltitude,
                            props.input.descentRateMph,
                            props.input.horizontalSpeedMph,
                            180.0
                        )
                    )
                }
            }
            button {
                +"W"
                onClick = {
                    props.onInputChanged(
                        Input(
                            props.input.startAltitude,
                            props.input.endAltitude,
                            props.input.descentRateMph,
                            props.input.horizontalSpeedMph,
                            270.0
                        )
                    )
                }
            }
            button {
                +"Into wind"
                onClick = {
                    props.onInputChanged(
                        Input(
                            props.input.startAltitude,
                            props.input.endAltitude,
                            props.input.descentRateMph,
                            props.input.horizontalSpeedMph,
                            -1.0
                        )
                    )
                }
            }
        }
    }
}

    val DoubleInputField = FC<InputFieldProps<Double>> { props ->
    div {
        +props.name
    }
    div {
        input {
            props.className?.let { className = it }
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
    var className: String?
    var value: T
    var onChange: (T) -> Unit
    var disabled: Boolean
}
