package net.mustelinae.drift

import react.FC
import react.Props
import react.dom.html.ReactHTML.b
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.hr
import kotlin.math.*

val Drift = FC<DriftProps> { props ->
    val i = props.input
    val windDrift = windDrift(props.winds.windsAloft, i.startAltitude, i.endAltitude, i.descentRateMph)

    val jumprunDirection = if (i.jumprunDirection >= 0) i.jumprunDirection else (windDrift.cardinalDirection + 180) % 360
    val canopyDrift = canopyDrift(i.startAltitude, i.endAltitude, i.horizontalSpeedMph, i.descentRateMph, jumprunDirection)
    val combined = windDrift + canopyDrift

    div { +"Wind drift:" }
    div { +windDrift.toCardinalString(props.nautical) }
    div { +"Canopy flight:" }
    div { +canopyDrift.toCardinalString(props.nautical) }
    div { +"Combined:" }
    div { +combined.toCardinalString(props.nautical) }

    // canopy flight is always in jumprun direction
    val base = canopyDrift * (1 / canopyDrift.length)
    val proj = combined.proj(base)
    console.log("base=${base.first}, ${base.second}, len=${base.length}")
    console.log("proj=${proj.first}, ${proj.second}, len=${proj.length}")
    console.log("combined=${combined.first}, ${combined.second}, len=${combined.length}")

    val units = if (props.nautical) "nm" else "miles"
    val priorLen = if (props.nautical) proj.length.miToNm() else proj.length
    val prior = canopyDrift.dot(proj) > 0
    val offsetLen = if (props.nautical) (combined - proj).length.miToNm() else (combined - proj).length

    val left = (combined - proj).dot(MilesVector(combined.second, -combined.first)) > 0
    hr {}
    hr {}
    div { +"Forecasted Spot: " }
    div {
        +"Jumprun ${jumprunDirection.toInt()}˚"
    }
    div {}
    div {
        +"${priorLen.absoluteValue.format(2)} $units "
        if (prior) {
            +"prior"
        } else {
            b { +"PAST" }
        }
    }
    if (offsetLen.absoluteValue > 0.05) {
        div {}
        div {
            +"Offset ${offsetLen.absoluteValue.format(2)} $units ${if (left) "left" else "right"} "
        }
    }
}

external interface DriftProps : Props {
    var winds: Winds
    var input: Input
    var nautical: Boolean
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
operator fun MilesVector.minus(other: MilesVector) = MilesVector(first - other.first, second - other.second)
operator fun MilesVector.times(n: Double) = MilesVector(first * n, second * n)
fun MilesVector.proj(base: MilesVector) = base * (this.dot(base) / base.dot(base))
fun MilesVector.dot(other: MilesVector) = this.first * other.first + this.second * other.second
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
fun MilesVector.toCardinalString(nautical: Boolean = false): String {
    val units = if (nautical) "nm" else "miles"
    val l = if (nautical) length.miToNm() else length
    return "${l.format(2)} $units at ${this.cardinalDirection.toInt()}˚"
}
fun Double.format(digits: Int): String = this.asDynamic().toFixed(digits) as String

fun Double.mphToFps() = this * 5280 / 3600
fun Double.ktsToMph() = this * 1.151

fun Double.miToNm() = this / 1.15078

fun Double.cardinalToDeg() = ((90 - this%360) + 360) % 360
fun Double.degreeToRad() = this / 180.0 * PI
fun Double.cardinalToRad() = this.cardinalToDeg().degreeToRad()

fun Double.radToDegree() = this * 180.0 / PI
fun Double.degreeToCardinal() = ((-(this%360) + 90) + 360) % 360
fun Double.radToCardinal() = this.radToDegree().degreeToCardinal()
