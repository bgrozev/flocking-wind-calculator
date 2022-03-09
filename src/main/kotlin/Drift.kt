package net.mustelinae.drift

import react.FC
import react.Props

val Drift = FC<DriftProps> {
}

external interface DriftProps : Props {
    var winds: WindsAloft
    var input: Input
}

fun windDrift(winds: WindsAloft, highAltitude: Double, lowAltitude: Double, descentRateMph: Double): Offset {
    var drift = Offset(0.0, 0.0)

    var prev = lowAltitude
    winds.altFt.filter { it >= lowAltitude && it <= highAltitude }.forEach {
        val seconds = (it - prev) / descentRateMph.mphToFps()
        val windSpeedKts = winds.speed[it.toString()]!!.toDouble()
        val windDirection = winds.direction[it.toString()]!!.toDouble()
        drift += windDriftSingle(seconds, windSpeedKts, windDirection)

        prev = it.toDouble()
    }

    return drift
}

fun windDriftSingle(seconds: Double, windSpeedKts: Double, windDirection: Double): Offset {
    return Offset(0.0, 0.0)
}
typealias Offset = Pair<Double, Double>
operator fun Offset.plus(other: Offset) = Offset(first + other.first, second + other.second)

fun Double.mphToFps() = this * 5280 / 3600