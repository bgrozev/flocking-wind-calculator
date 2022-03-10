import net.mustelinae.drift.MilesVector
import net.mustelinae.drift.cardinalDirection
import net.mustelinae.drift.cardinalToDeg
import net.mustelinae.drift.degreeToCardinal
import kotlin.math.sqrt
import kotlin.test.Test
import kotlin.test.assertEquals

class DriftTest  {
    @Test
    fun testDirectionConversions() {
        listOf(
            Pair(0.0, 90.0),
            Pair(30.0, 60.0),
            Pair(45.0, 45.0),
            Pair(90.0, 0.0),
            Pair(135.0, 315.0),
            Pair(180.0, 270.0),
            Pair(270.0, 180.0),
            Pair(315.0, 135.0),
            Pair(359.0, 91.0),
        ).forEach {
            assertEquals(it.second, it.first.cardinalToDeg())
            assertEquals(it.first, it.second.degreeToCardinal())
        }

        assertEquals(315.0.cardinalToDeg(), (-45.0).cardinalToDeg())
        assertEquals(315.0.degreeToCardinal(), (-45.0).degreeToCardinal())

        assertEquals(90.0, 720.0.cardinalToDeg())
        assertEquals(90.0, 720.0.degreeToCardinal())
    }

    @Test
    fun testVectorDirection() {
        val sqrt3 = sqrt(3.0)
        assertEquals(45.0, MilesVector(1.0, 1.0).cardinalDirection)
        assertEquals(30.0, MilesVector(1.0, sqrt3).cardinalDirection, 0.001)
        assertEquals(315.0, MilesVector(-1.0, 1.0).cardinalDirection, 0.001)
        assertEquals(330.0, MilesVector(-1.0, sqrt3).cardinalDirection, 0.001)
        assertEquals(225.0, MilesVector(-1.0, -1.0).cardinalDirection, 0.001)
        assertEquals(210.0, MilesVector(-1.0, -sqrt3).cardinalDirection, 0.001)
        assertEquals(135.0, MilesVector(1.0, -1.0).cardinalDirection, 0.001)
        assertEquals(150.0, MilesVector(1.0, -sqrt3).cardinalDirection, 0.001)

        assertEquals(90.0, MilesVector(1.0, 0.0).cardinalDirection, 0.001)
        assertEquals(0.0, MilesVector(0.0, 1.0).cardinalDirection, 0.001)
    }
}