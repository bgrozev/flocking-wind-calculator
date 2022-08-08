package net.mustelinae.drift

import kotlinx.browser.window
import kotlinx.coroutines.await
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlin.js.Date
import kotlin.math.floor
import kotlin.math.round

data class Winds(
    val windsAloft: WindsAloft,
    val fetchTime: String,
    val lat: Double,
    val lon: Double
)

@Serializable
class WindsAloft(
    val groundElev: Double,
    //val groundDir: String, // This is sometimes String (RAP) sometimes Int (GFS).
    val altFt: List<Int>,
    val direction: Map<String, Int>,
    val speed: Map<String, Int>,
    @Contextual
    val validtime: Int
) {
    // Usually int, but sometimes "00"?
    fun getValidtime(): Int = validtime.toString().toInt()
}

fun WindsAloft.getValidAtString(): String {
    val now = Date()
    val nowUTC = Date(now.getTime() + now.getTimezoneOffset() * 60000)
    val validTime = Date(nowUTC.getFullYear(), nowUTC.getMonth(), nowUTC.getDate(), getValidtime(), 0, 0)
    val minDiff = round((nowUTC.getTime() - validTime.getTime()) / 60000)

    return when {
        minDiff < 60 && minDiff >= 0 -> "Forecast valid now"
        minDiff < 0 && minDiff > -60 -> {
            var min = -minDiff
            if (min > 5)
                min = round(min / 5) * 5
            "Forecast valid in about $min minute${if (min > 0) "s" else ""}"
        }
        minDiff >= 60 -> {
            val hr = floor(minDiff / 60);
            val min = round((minDiff - hr * 60) / 5) * 5;
            val minStr = if (min < 10) "0$min" else "$min"
            "Forecast valid about $hr:$minStr ago"
        }
        else -> {
            val hr = floor(-minDiff / 60);
            val min = round((-minDiff - hr * 60) / 5) * 5;
            val minStr = if (min < 10) "0$min" else "$min"
            "Forecast valid in about $hr:$minStr"
        }
    }
}


// https://markschulze.net/winds/winds.php?lat=${lat}&lon=${lon}&hourOffset=0&referrer=testing

val json = Json {
    ignoreUnknownKeys = true
}
suspend fun getWindsAloft(lat: Double, lon: Double): Winds {
    //val x = window.fetch("https://markschulze.net/winds/winds.php?lat=${lat}&lon=${lon}&hourOffset=0&referrer=testing")
    val x = window.fetch("https://mustelinae.net/winds-aloft?lat=${lat}&lon=${lon}&hourOffset=0")
        .then { it.json() }.then { it }.await()
    val windsAloft: WindsAloft = json.decodeFromString(JSON.stringify(x))
    return Winds(
        windsAloft = windsAloft,
        fetchTime = Date().toTimeString(),
        lat =lat,
        lon = lon
    )
}
fun getSampleWindsAloft(): Winds {
    return Winds(json.decodeFromString(sampleWindsJsonGFS), "now", 0.0, 0.0)
}
val sampleWindsJsonGFS = """
{
  "QFE": 1005.2151,
  "QNH": 1014.70574313,
  "validtime": 16,
  "altFtRaw": [
    152,
    891,
    1645,
    2414,
    3199,
    4825,
    6527,
    8313,
    10190,
    12176,
    14300,
    16567,
    19004,
    21649,
    24536,
    27715,
    31262,
    35306,
    40045,
    46094,
    54464,
    61715,
    68632,
    73289,
    79368,
    88082
  ],
  "directionRaw": {
    "152": 138,
    "891": 152,
    "1645": 165,
    "2414": 175,
    "3199": 187,
    "4825": 211,
    "6527": 281,
    "8313": 313,
    "10190": 323,
    "12176": 324,
    "14300": 353,
    "16567": 329,
    "19004": 313,
    "21649": 308,
    "24536": 297,
    "27715": 313,
    "31262": 307,
    "35306": 300,
    "40045": 302,
    "46094": 288,
    "54464": 303,
    "61715": 93,
    "68632": 119,
    "73289": 110,
    "79368": 83,
    "88082": 82
  },
  "speedRaw": {
    "152": 11,
    "891": 12,
    "1645": 11,
    "2414": 10,
    "3199": 10,
    "4825": 8,
    "6527": 8,
    "8313": 12,
    "10190": 17,
    "12176": 25,
    "14300": 14,
    "16567": 9,
    "19004": 15,
    "21649": 18,
    "24536": 20,
    "27715": 28,
    "31262": 41,
    "35306": 52,
    "40045": 66,
    "46094": 39,
    "54464": 15,
    "61715": 7,
    "68632": 9,
    "73289": 10,
    "79368": 12,
    "88082": 22
  },
  "tempRaw": {
    "152": 31.5,
    "891": 29.4,
    "1645": 27.9,
    "2414": 25.7,
    "3199": 24,
    "4825": 20.7,
    "6527": 17.1,
    "8313": 12.5,
    "10190": 7.6,
    "12176": 5.3,
    "14300": 2,
    "16567": -3.6,
    "19004": -8.3,
    "21649": -13.6,
    "24536": -20.3,
    "27715": -28.5,
    "31262": -36.8,
    "35306": -45.8,
    "40045": -53.7,
    "46094": -54.2,
    "54464": -59.7,
    "61715": -60.3,
    "68632": -56.3,
    "73289": -53.4,
    "79368": -50.7,
    "88082": -45.6
  },
  "altFt": [
    0,
    1000,
    2000,
    3000,
    4000,
    5000,
    6000,
    7000,
    8000,
    9000,
    10000,
    11000,
    12000,
    13000,
    14000,
    15000,
    16000,
    17000,
    18000,
    19000,
    20000,
    21000,
    22000,
    23000,
    24000,
    25000,
    26000,
    27000,
    28000,
    29000,
    30000,
    31000,
    32000,
    33000,
    34000,
    35000,
    36000,
    37000,
    38000,
    39000,
    40000
  ],
  "direction": {
    "0": 135,
    "1000": 153,
    "2000": 169,
    "3000": 184,
    "4000": 199,
    "5000": 218,
    "6000": 259,
    "7000": 289,
    "8000": 307,
    "9000": 317,
    "10000": 322,
    "11000": 323,
    "12000": 324,
    "13000": 336,
    "14000": 349,
    "15000": 346,
    "16000": 335,
    "17000": 326,
    "18000": 320,
    "19000": 313,
    "20000": 311,
    "21000": 309,
    "22000": 307,
    "23000": 303,
    "24000": 299,
    "25000": 300,
    "26000": 304,
    "27000": 309,
    "28000": 312,
    "29000": 311,
    "30000": 309,
    "31000": 308,
    "32000": 306,
    "33000": 304,
    "34000": 302,
    "35000": 300,
    "36000": 300,
    "37000": 300,
    "38000": 301,
    "39000": 301,
    "40000": 301
  },
  "speed": {
    "0": 11,
    "1000": 12,
    "2000": 11,
    "3000": 10,
    "4000": 9,
    "5000": 8,
    "6000": 8,
    "7000": 9,
    "8000": 11,
    "9000": 14,
    "10000": 17,
    "11000": 21,
    "12000": 25,
    "13000": 21,
    "14000": 16,
    "15000": 13,
    "16000": 10,
    "17000": 10,
    "18000": 13,
    "19000": 15,
    "20000": 17,
    "21000": 18,
    "22000": 19,
    "23000": 19,
    "24000": 20,
    "25000": 21,
    "26000": 23,
    "27000": 26,
    "28000": 29,
    "29000": 33,
    "30000": 36,
    "31000": 40,
    "32000": 43,
    "33000": 46,
    "34000": 48,
    "35000": 51,
    "36000": 54,
    "37000": 57,
    "38000": 60,
    "39000": 63,
    "40000": 66
  },
  "temp": {
    "0": 32,
    "1000": 29,
    "2000": 27,
    "3000": 24,
    "4000": 22,
    "5000": 20,
    "6000": 18,
    "7000": 16,
    "8000": 13,
    "9000": 11,
    "10000": 8,
    "11000": 7,
    "12000": 6,
    "13000": 4,
    "14000": 2,
    "15000": 0,
    "16000": -2,
    "17000": -4,
    "18000": -6,
    "19000": -8,
    "20000": -10,
    "21000": -12,
    "22000": -14,
    "23000": -17,
    "24000": -19,
    "25000": -21,
    "26000": -24,
    "27000": -27,
    "28000": -29,
    "29000": -32,
    "30000": -34,
    "31000": -36,
    "32000": -38,
    "33000": -41,
    "34000": -43,
    "35000": -45,
    "36000": -47,
    "37000": -49,
    "38000": -50,
    "39000": -52,
    "40000": -54
  },
  "groundElev": 260,
  "groundTemp": 32,
  "groundDir": 135,
  "groundSpd": 11,
  "altSI": [
    0,
    300,
    600,
    900,
    1200,
    1500,
    1800,
    2100,
    2400,
    2700,
    3000,
    3300,
    3600,
    3900,
    4200,
    4500,
    4800,
    5100,
    5400,
    5700,
    6000,
    6300,
    6600,
    6900,
    7200,
    7500,
    7800,
    8100,
    8400,
    8700,
    9000,
    9300,
    9600,
    9900,
    10200,
    10500,
    10800,
    11100,
    11400,
    11700,
    12000
  ],
  "directionSI": {
    "0": 135,
    "300": 153,
    "600": 169,
    "900": 183,
    "1200": 198,
    "1500": 215,
    "1800": 255,
    "2100": 287,
    "2400": 305,
    "2700": 316,
    "3000": 321,
    "3300": 323,
    "3600": 324,
    "3900": 333,
    "4200": 346,
    "4500": 348,
    "4800": 338,
    "5100": 328,
    "5400": 322,
    "5700": 315,
    "6000": 312,
    "6300": 310,
    "6600": 308,
    "6900": 304,
    "7200": 301,
    "7500": 298,
    "7800": 302,
    "8100": 307,
    "8400": 312,
    "8700": 311,
    "9000": 310,
    "9300": 309,
    "9600": 307,
    "9900": 305,
    "10200": 303,
    "10500": 301,
    "10800": 300,
    "11100": 300,
    "11400": 301,
    "11700": 301,
    "12000": 301
  },
  "speedSI": {
    "0": 20,
    "300": 21,
    "600": 20,
    "900": 19,
    "1200": 17,
    "1500": 15,
    "1800": 15,
    "2100": 16,
    "2400": 20,
    "2700": 24,
    "3000": 30,
    "3300": 37,
    "3600": 44,
    "3900": 41,
    "4200": 31,
    "4500": 24,
    "4800": 20,
    "5100": 18,
    "5400": 22,
    "5700": 27,
    "6000": 30,
    "6300": 32,
    "6600": 34,
    "6900": 35,
    "7200": 36,
    "7500": 37,
    "7800": 41,
    "8100": 46,
    "8400": 50,
    "8700": 57,
    "9000": 64,
    "9300": 71,
    "9600": 78,
    "9900": 82,
    "10200": 87,
    "10500": 92,
    "10800": 96,
    "11100": 102,
    "11400": 108,
    "11700": 113,
    "12000": 119
  },
  "tempSI": {
    "0": 32,
    "300": 29,
    "600": 27,
    "900": 25,
    "1200": 23,
    "1500": 20,
    "1800": 18,
    "2100": 16,
    "2400": 14,
    "2700": 11,
    "3000": 9,
    "3300": 7,
    "3600": 6,
    "3900": 4,
    "4200": 3,
    "4500": 1,
    "4800": -2,
    "5100": -4,
    "5400": -6,
    "5700": -8,
    "6000": -10,
    "6300": -12,
    "6600": -14,
    "6900": -16,
    "7200": -18,
    "7500": -20,
    "7800": -23,
    "8100": -26,
    "8400": -28,
    "8700": -30,
    "9000": -33,
    "9300": -35,
    "9600": -37,
    "9900": -39,
    "10200": -42,
    "10500": -44,
    "10800": -46,
    "11100": -48,
    "11400": -49,
    "11700": -51,
    "12000": -53
  },
  "model": "GFS"
}
    
    """

val sampleWindsJsonRAP = """
    {
      "validtime": "15",
      "groundElev": 587,
      "groundTemp": 4.1,
      "groundDir": "216",
      "groundSpd": "8",
      "QFE": 997.7,
      "QNH": 1019.11388609,
      "altFtRaw": [
        0,
        89,
        246,
        502,
        882,
        1384,
        1991,
        2047,
        2686,
        3464,
        4353,
        4353,
        5366,
        6514,
        7816,
        9286,
        9568,
        10952,
        12828,
        14888,
        17027,
        18161,
        19119,
        21123,
        23042,
        23603,
        24885,
        26647,
        28329,
        29930,
        30225,
        31462,
        32928,
        34237,
        34342,
        35700,
        37028,
        38402,
        39885,
        41479,
        43194,
        45008,
        45038,
        47032,
        49210,
        51624,
        53107,
        54304,
        57121,
        59860,
        60034,
        62494,
        65207,
        66850,
        68126,
        71304,
        74794,
        78694,
        83174,
        88544,
        95471
      ],
      "directionRaw": {
        "0": "216",
        "89": "217",
        "246": "218",
        "502": "224",
        "882": "243",
        "1384": "263",
        "1991": "275",
        "2047": "275",
        "2686": "279",
        "3464": "276",
        "4353": "271",
        "5366": "264",
        "6514": "254",
        "7816": "249",
        "9286": "249",
        "9568": "249",
        "10952": "248",
        "12828": "251",
        "14888": "257",
        "17027": "258",
        "18161": "257",
        "19119": "256",
        "21123": "253",
        "23042": "251",
        "23603": "251",
        "24885": "251",
        "26647": "252",
        "28329": "254",
        "29930": "257",
        "30225": "257",
        "31462": "259",
        "32928": "260",
        "34237": "261",
        "34342": "261",
        "35700": "262",
        "37028": "260",
        "38402": "260",
        "39885": "263",
        "41479": "264",
        "43194": "262",
        "45008": "258",
        "45038": "258",
        "47032": "258",
        "49210": "259",
        "51624": "262",
        "53107": "265",
        "54304": "268",
        "57121": "271",
        "59860": "268",
        "60034": "268",
        "62494": "264",
        "65207": "266",
        "66850": "269",
        "68126": "271",
        "71304": "270",
        "74794": "279",
        "78694": "283",
        "83174": "272",
        "88544": "264",
        "95471": "261"
      },
      "speedRaw": {
        "0": "8",
        "89": "10",
        "246": "12",
        "502": "16",
        "882": "21",
        "1384": "27",
        "1991": "33",
        "2047": "34",
        "2686": "40",
        "3464": "47",
        "4353": "53",
        "5366": "52",
        "6514": "52",
        "7816": "61",
        "9286": "73",
        "9568": "74",
        "10952": "80",
        "12828": "83",
        "14888": "86",
        "17027": "88",
        "18161": "89",
        "19119": "89",
        "21123": "89",
        "23042": "89",
        "23603": "89",
        "24885": "90",
        "26647": "94",
        "28329": "98",
        "29930": "104",
        "30225": "105",
        "31462": "111",
        "32928": "117",
        "34237": "120",
        "34342": "120",
        "35700": "122",
        "37028": "122",
        "38402": "126",
        "39885": "128",
        "41479": "122",
        "43194": "112",
        "45008": "106",
        "45038": "106",
        "47032": "105",
        "49210": "101",
        "51624": "93",
        "53107": "86",
        "54304": "80",
        "57121": "58",
        "59860": "40",
        "60034": "39",
        "62494": "31",
        "65207": "27",
        "66850": "28",
        "68126": "28",
        "71304": "29",
        "74794": "30",
        "78694": "33",
        "83174": "36",
        "88544": "40",
        "95471": "53"
      },
      "tempRaw": {
        "0": 4.1,
        "89": 3.7,
        "246": 3.2,
        "502": 3.3,
        "882": 6.1,
        "1384": 8.7,
        "1991": 9.7,
        "2047": 9.7,
        "2686": 9.3,
        "3464": 8.8,
        "4353": 8.1,
        "5366": 6.6,
        "6514": 5.2,
        "7816": 4.2,
        "9286": 2.4,
        "9568": 2,
        "10952": 0.3,
        "12828": -2.5,
        "14888": -6.5,
        "17027": -11.2,
        "18161": -13.5,
        "19119": -15.5,
        "21123": -19.9,
        "23042": -24.2,
        "23603": -25.5,
        "24885": -28.5,
        "26647": -32.7,
        "28329": -36.7,
        "29930": -39.9,
        "30225": -40.4,
        "31462": -42.3,
        "32928": -44.9,
        "34237": -48.1,
        "34342": -48.3,
        "35700": -51.8,
        "37028": -54.9,
        "38402": -54.7,
        "39885": -53.2,
        "41479": -53.7,
        "43194": -56.1,
        "45008": -59.4,
        "45038": -59.4,
        "47032": -62.8,
        "49210": -65.7,
        "51624": -67.8,
        "53107": -68.2,
        "54304": -68.5,
        "57121": -67.3,
        "59860": -65.6,
        "60034": -65.5,
        "62494": -64.4,
        "65207": -65.2,
        "66850": -64.4,
        "68126": -63.8,
        "71304": -62.1,
        "74794": -63.1,
        "78694": -64.3,
        "83174": -63.5,
        "88544": -61.5,
        "95471": -59.3
      },
      "altFt": [
        0,
        1000,
        2000,
        3000,
        4000,
        5000,
        6000,
        7000,
        8000,
        9000,
        10000,
        11000,
        12000,
        13000,
        14000,
        15000,
        16000,
        17000,
        18000,
        19000,
        20000,
        21000,
        22000,
        23000,
        24000,
        25000,
        26000,
        27000,
        28000,
        29000,
        30000,
        31000,
        32000,
        33000,
        34000,
        35000,
        36000,
        37000,
        38000,
        39000,
        40000
      ],
      "direction": {
        "0": 216,
        "1000": 248,
        "2000": 275,
        "3000": 278,
        "4000": 273,
        "5000": 267,
        "6000": 258,
        "7000": 252,
        "8000": 249,
        "9000": 249,
        "10000": 249,
        "11000": 248,
        "12000": 250,
        "13000": 252,
        "14000": 254,
        "15000": 257,
        "16000": 258,
        "17000": 258,
        "18000": 257,
        "19000": 256,
        "20000": 255,
        "21000": 253,
        "22000": 252,
        "23000": 251,
        "24000": 251,
        "25000": 251,
        "26000": 252,
        "27000": 252,
        "28000": 254,
        "29000": 255,
        "30000": 257,
        "31000": 258,
        "32000": 259,
        "33000": 260,
        "34000": 261,
        "35000": 261,
        "36000": 262,
        "37000": 260,
        "38000": 260,
        "39000": 261,
        "40000": 263
      },
      "speed": {
        "0": 8,
        "1000": 22,
        "2000": 33,
        "3000": 43,
        "4000": 51,
        "5000": 52,
        "6000": 52,
        "7000": 55,
        "8000": 63,
        "9000": 71,
        "10000": 76,
        "11000": 80,
        "12000": 82,
        "13000": 83,
        "14000": 85,
        "15000": 86,
        "16000": 87,
        "17000": 88,
        "18000": 89,
        "19000": 89,
        "20000": 89,
        "21000": 89,
        "22000": 89,
        "23000": 89,
        "24000": 89,
        "25000": 90,
        "26000": 93,
        "27000": 95,
        "28000": 97,
        "29000": 101,
        "30000": 104,
        "31000": 109,
        "32000": 113,
        "33000": 117,
        "34000": 119,
        "35000": 121,
        "36000": 122,
        "37000": 122,
        "38000": 125,
        "39000": 127,
        "40000": 128
      },
      "temp": {
        "0": 4,
        "1000": 7,
        "2000": 10,
        "3000": 9,
        "4000": 8,
        "5000": 7,
        "6000": 6,
        "7000": 5,
        "8000": 4,
        "9000": 3,
        "10000": 1,
        "11000": 0,
        "12000": -1,
        "13000": -3,
        "14000": -5,
        "15000": -7,
        "16000": -9,
        "17000": -11,
        "18000": -13,
        "19000": -15,
        "20000": -17,
        "21000": -20,
        "22000": -22,
        "23000": -24,
        "24000": -26,
        "25000": -29,
        "26000": -31,
        "27000": -34,
        "28000": -36,
        "29000": -38,
        "30000": -40,
        "31000": -42,
        "32000": -43,
        "33000": -45,
        "34000": -48,
        "35000": -50,
        "36000": -53,
        "37000": -55,
        "38000": -55,
        "39000": -54,
        "40000": -53
      },
      "altSI": [
        0,
        300,
        600,
        900,
        1200,
        1500,
        1800,
        2100,
        2400,
        2700,
        3000,
        3300,
        3600,
        3900,
        4200,
        4500,
        4800,
        5100,
        5400,
        5700,
        6000,
        6300,
        6600,
        6900,
        7200,
        7500,
        7800,
        8100,
        8400,
        8700,
        9000,
        9300,
        9600,
        9900,
        10200,
        10500,
        10800,
        11100,
        11400,
        11700,
        12000
      ],
      "directionSI": {
        "0": 216,
        "300": 247,
        "600": 275,
        "900": 278,
        "1200": 273,
        "1500": 267,
        "1800": 259,
        "2100": 253,
        "2400": 249,
        "2700": 249,
        "3000": 249,
        "3300": 248,
        "3600": 249,
        "3900": 251,
        "4200": 254,
        "4500": 257,
        "4800": 257,
        "5100": 258,
        "5400": 257,
        "5700": 256,
        "6000": 255,
        "6300": 254,
        "6600": 252,
        "6900": 251,
        "7200": 251,
        "7500": 251,
        "7800": 251,
        "8100": 252,
        "8400": 253,
        "8700": 254,
        "9000": 256,
        "9300": 257,
        "9600": 259,
        "9900": 260,
        "10200": 260,
        "10500": 261,
        "10800": 262,
        "11100": 261,
        "11400": 260,
        "11700": 260,
        "12000": 262
      },
      "speedSI": {
        "0": 15,
        "300": 41,
        "600": 61,
        "900": 79,
        "1200": 93,
        "1500": 97,
        "1800": 96,
        "2100": 101,
        "2400": 114,
        "2700": 129,
        "3000": 139,
        "3300": 147,
        "3600": 151,
        "3900": 154,
        "4200": 156,
        "4500": 159,
        "4800": 161,
        "5100": 162,
        "5400": 164,
        "5700": 165,
        "6000": 165,
        "6300": 165,
        "6600": 165,
        "6900": 165,
        "7200": 165,
        "7500": 166,
        "7800": 170,
        "8100": 174,
        "8400": 178,
        "8700": 183,
        "9000": 190,
        "9300": 197,
        "9600": 206,
        "9900": 213,
        "10200": 219,
        "10500": 223,
        "10800": 225,
        "11100": 226,
        "11400": 228,
        "11700": 233,
        "12000": 236
      },
      "tempSI": {
        "0": 4,
        "300": 7,
        "600": 10,
        "900": 9,
        "1200": 8,
        "1500": 7,
        "1800": 6,
        "2100": 5,
        "2400": 4,
        "2700": 3,
        "3000": 2,
        "3300": 0,
        "3600": -1,
        "3900": -2,
        "4200": -4,
        "4500": -6,
        "4800": -8,
        "5100": -11,
        "5400": -13,
        "5700": -15,
        "6000": -17,
        "6300": -19,
        "6600": -21,
        "6900": -23,
        "7200": -26,
        "7500": -28,
        "7800": -30,
        "8100": -33,
        "8400": -35,
        "8700": -37,
        "9000": -39,
        "9300": -41,
        "9600": -42,
        "9900": -44,
        "10200": -46,
        "10500": -49,
        "10800": -51,
        "11100": -53,
        "11400": -55,
        "11700": -55,
        "12000": -54
      },
      "model": "RAP"
    }
""".trimIndent()

