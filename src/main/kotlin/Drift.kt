package net.mustelinae.drift

import react.FC
import react.Props
import react.dom.html.ReactHTML.br
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.atan
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

val Drift = FC<DriftProps> { props ->
    val i = props.input
    val windDrift = windDrift(props.winds, i.startAltitude, i.endAltitude, i.descentRateMph)
    val canopyDrift = canopyDrift(i.startAltitude, i.endAltitude, i.horizontalSpeedMph, i.descentRateMph, i.jumprunDirection)
    +"Wind drift: ${windDrift.toCardinalString()}"
    br { }
    +"Canopy drift: ${canopyDrift.toCardinalString()}"
    br { }
    +"Combined: ${(windDrift+canopyDrift).toCardinalString()}"
}

external interface DriftProps : Props {
    var winds: WindsAloft
    var input: Input
}

fun canopyDrift(
    highAltitudeKft: Double,
    lowAltitudeKft: Double,
    horizontalSpeedMph: Double,
    descentRateMph: Double,
    jumprun: Double
): MilesVector {
    val deltaAltFt = (highAltitudeKft - lowAltitudeKft) * 1000
    val seconds = deltaAltFt / descentRateMph.mphToFps()
    return drift(seconds, horizontalSpeedMph, jumprun)
}

fun windDrift(winds: WindsAloft, highAltitudeKft: Double, lowAltitudeKft: Double, descentRateMph: Double): MilesVector {
    var drift = MilesVector(0.0, 0.0)

    var prev = lowAltitudeKft*1000
    console.log("start prev=$prev")
    winds.altFt.filter { it >= lowAltitudeKft*1000 && it <= highAltitudeKft*1000 }.forEach {
        val deltaAltFt = (it - prev)
        val seconds = deltaAltFt / descentRateMph.mphToFps()
        val windSpeedKts = winds.speed[it.toString()]!!.toDouble()
        val windDirection = winds.direction[it.toString()]!!.toDouble()
        val singleDrift = drift(seconds, windSpeedKts.ktsToMph(), windDirection + 180)
        console.log("Between $it and $prev: seconds=$seconds windSpeedKts=$windSpeedKts windDirection=$windDirection, windDrift=${singleDrift.toCardinalString()}")
        drift += singleDrift

        prev = it.toDouble()
    }

    return drift
}

fun drift(seconds: Double, speedMph: Double, directionCardinal: Double): MilesVector {
    val driftMiles = seconds * speedMph / 3600
    val directionRad = directionCardinal.cardinalToRad()
    return MilesVector(driftMiles * cos(directionRad), driftMiles * sin(directionRad))
}

typealias MilesVector = Pair<Double, Double>
operator fun MilesVector.plus(other: MilesVector) = MilesVector(first + other.first, second + other.second)
val MilesVector.length: Double
    get() = sqrt(this.first * this.first + this.second * this.second)
val MilesVector.cardinalDirection: Double
    get() = directionRad.radToCardinal()
val MilesVector.directionRad: Double
    get() {
        if (this.first == 0.0) return 90.0.degreeToRad()
        val a = atan(abs(this.second / this.first))
        return when {
            this.first > 0 && this.second > 0 -> a
            this.first <= 0 && this.second > 0 -> PI-a
            this.first <= 0 && this.second <= 0 -> PI+a
            this.first > 0 && this.second <= 0 -> 2*PI-a
            else -> 0.0
        }
    }
fun MilesVector.toCardinalString(): String = "${length.format(2)} miles at ${this.cardinalDirection.toInt()}"
fun Double.format(digits: Int): String = this.asDynamic().toFixed(digits) as String

fun Double.mphToFps() = this * 5280 / 3600
fun Double.ktsToMph() = this * 1.151

fun Double.cardinalToDeg() = ((90 - this%360) + 360) % 360
fun Double.degreeToRad() = this / 180.0 * PI
fun Double.cardinalToRad() = this.cardinalToDeg().degreeToRad()

fun Double.radToDegree() = this * 180.0 / PI
fun Double.degreeToCardinal() = ((-(this%360) + 90) + 360) % 360
fun Double.radToCardinal() = this.radToDegree().degreeToCardinal()
