package net.mustelinae.drift

import kotlinx.browser.window
import kotlinx.coroutines.await
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlin.js.Date

data class Winds(
    val windsAloft: WindsAloft,
    val fetchTime: String,
    val lat: Double,
    val lon: Double
)

@Serializable
class WindsAloft(
    val groundElev: Double,
    val groundDir: String,
    val altFt: List<Int>,
    val direction: Map<String, Int>,
    val speed: Map<String, Int>
)

// https://markschulze.net/winds/winds.php?lat=${lat}&lon=${lon}&hourOffset=0&referrer=testing

val json = Json {
    ignoreUnknownKeys = true
}
suspend fun getWindsAloft(lat: Double, lon: Double): Winds {
    //val x = window.fetch("https://markschulze.net/winds/winds.php?lat=${lat}&lon=${lon}&hourOffset=0&referrer=testing")
    val x = window.fetch("https://mustelinae.net/winds-aloft?lat=${lat}&lon=${lon}&hourOffset=0")
        .then { it.json() }.then { it }.await()
    val j: WindsAloft = json.decodeFromString(JSON.stringify(x))
    return Winds(
        j,
        Date().toTimeString(),
        lat,
        lon
    )
}
fun getSampleWindsAloft(): Winds {
    return Winds(json.decodeFromString(sampleWindsJson), "now", 0.0, 0.0)
}
val sampleWindsJson = """
    {"validtime":"22","groundElev":36,"groundTemp":20,"groundDir":"143","groundSpd":"7","QFE":1014.5,"QNH":1015.81760958,"altFtRaw":[0,95,266,430,545,951,1483,2119,2621,2841,3647,4559,4973,5602,6783,8121,9630,10270,11332,13251,15364,17551,18919,19690,21730,23685,24370,25561,27359,29077,30707,31039,32252,33728,35050,35149,36510,37845,39226,40692,42250,43909,45664,45694,47639,49797,52208,53700,54901,57735,60460,60637,63071,65780,67434,68716,71917,75447,79432,84063,89675,97019],"directionRaw":{"0":"143","95":"159","266":"164","430":"173","545":"179","951":"185","1483":"186","2119":"187","2621":"188","2841":"188","3647":"190","4559":"193","4973":"195","5602":"198","6783":"192","8121":"182","9630":"189","10270":"199","11332":"215","13251":"267","15364":"303","17551":"328","18919":"302","19690":"288","21730":"297","23685":"321","24370":"321","25561":"320","27359":"315","29077":"316","30707":"316","31039":"317","32252":"319","33728":"325","35050":"329","35149":"329","36510":"331","37845":"328","39226":"322","40692":"315","42250":"312","43909":"314","45664":"308","45694":"308","47639":"302","49797":"302","52208":"301","53700":"299","54901":"297","57735":"296","60460":"295","60637":"295","63071":"296","65780":"282","67434":"274","68716":"267","71917":"297","75447":"316","79432":"321","84063":"317","89675":"317","97019":"298"},"speedRaw":{"0":"7","95":"12","266":"13","430":"15","545":"16","951":"19","1483":"20","2119":"21","2621":"22","2841":"22","3647":"24","4559":"26","4973":"24","5602":"20","6783":"15","8121":"15","9630":"14","10270":"13","11332":"12","13251":"10","15364":"10","17551":"13","18919":"12","19690":"11","21730":"16","23685":"27","24370":"30","25561":"34","27359":"39","29077":"44","30707":"51","31039":"52","32252":"57","33728":"62","35050":"65","35149":"65","36510":"65","37845":"63","39226":"62","40692":"66","42250":"69","43909":"62","45664":"54","45694":"54","47639":"56","49797":"54","52208":"45","53700":"40","54901":"36","57735":"26","60460":"17","60637":"17","63071":"12","65780":"8","67434":"9","68716":"9","71917":"11","75447":"8","79432":"5","84063":"6","89675":"9","97019":"22"},"tempRaw":{"0":20,"95":21.3,"266":22.1,"430":22.2,"545":22.2,"951":21.3,"1483":19.7,"2119":17.8,"2621":16.3,"2841":15.7,"3647":13.2,"4559":12.3,"4973":12.5,"5602":12.7,"6783":12,"8121":10.1,"9630":7.6,"10270":6.2,"11332":4,"13251":0.2,"15364":-3.6,"17551":-8.8,"18919":-12.4,"19690":-14.4,"21730":-19.4,"23685":-23.3,"24370":-24.7,"25561":-27.1,"27359":-30.5,"29077":-34.4,"30707":-38.3,"31039":-39.1,"32252":-42.1,"33728":-45.5,"35050":-48.5,"35149":-48.7,"36510":-51.5,"37845":-53.6,"39226":-55.1,"40692":-56.9,"42250":-60.2,"43909":-63.7,"45664":-66.2,"45694":-66.2,"47639":-66,"49797":-66.8,"52208":-67.4,"53700":-67.1,"54901":-66.8,"57735":-67.1,"60460":-67.4,"60637":-67.3,"63071":-66.4,"65780":-63.9,"67434":-63.1,"68716":-62.5,"71917":-60.9,"75447":-60,"79432":-58.2,"84063":-55.8,"89675":-50.3,"97019":-45.1},"altFt":[0,1000,2000,3000,4000,5000,6000,7000,8000,9000,10000,11000,12000,13000,14000,15000,16000,17000,18000,19000,20000,21000,22000,23000,24000,25000,26000,27000,28000,29000,30000,31000,32000,33000,34000,35000,36000,37000,38000,39000,40000],"direction":{"0":143,"1000":185,"2000":187,"3000":188,"4000":191,"5000":195,"6000":196,"7000":190,"8000":183,"9000":186,"10000":195,"11000":210,"12000":233,"13000":260,"14000":280,"15000":297,"16000":310,"17000":322,"18000":319,"19000":301,"20000":289,"21000":294,"22000":300,"23000":313,"24000":321,"25000":320,"26000":319,"27000":316,"28000":315,"29000":316,"30000":316,"31000":317,"32000":319,"33000":322,"34000":326,"35000":329,"36000":330,"37000":330,"38000":327,"39000":323,"40000":318},"speed":{"0":7,"1000":19,"2000":21,"3000":22,"4000":25,"5000":24,"6000":18,"7000":15,"8000":15,"9000":14,"10000":13,"11000":12,"12000":11,"13000":10,"14000":10,"15000":10,"16000":11,"17000":12,"18000":13,"19000":12,"20000":12,"21000":14,"22000":18,"23000":23,"24000":28,"25000":32,"26000":35,"27000":38,"28000":41,"29000":44,"30000":48,"31000":52,"32000":56,"33000":60,"34000":63,"35000":65,"36000":65,"37000":64,"38000":63,"39000":62,"40000":64},"temp":{"0":20,"1000":21,"2000":18,"3000":15,"4000":13,"5000":13,"6000":12,"7000":12,"8000":10,"9000":9,"10000":7,"11000":5,"12000":3,"13000":1,"14000":-1,"15000":-3,"16000":-5,"17000":-7,"18000":-10,"19000":-13,"20000":-15,"21000":-18,"22000":-20,"23000":-22,"24000":-24,"25000":-26,"26000":-28,"27000":-30,"28000":-32,"29000":-34,"30000":-37,"31000":-39,"32000":-41,"33000":-44,"34000":-46,"35000":-48,"36000":-50,"37000":-52,"38000":-54,"39000":-55,"40000":-56},"altSI":[0,300,600,900,1200,1500,1800,2100,2400,2700,3000,3300,3600,3900,4200,4500,4800,5100,5400,5700,6000,6300,6600,6900,7200,7500,7800,8100,8400,8700,9000,9300,9600,9900,10200,10500,10800,11100,11400,11700,12000],"directionSI":{"0":143,"300":185,"600":187,"900":188,"1200":191,"1500":195,"1800":196,"2100":191,"2400":184,"2700":185,"3000":192,"3300":207,"3600":228,"3900":255,"4200":276,"4500":293,"4800":307,"5100":319,"5400":325,"5700":306,"6000":288,"6300":292,"6600":297,"6900":308,"7200":320,"7500":321,"7800":320,"8100":317,"8400":315,"8700":316,"9000":316,"9300":316,"9600":318,"9900":320,"10200":324,"10500":327,"10800":329,"11100":331,"11400":329,"11700":326,"12000":321},"speedSI":{"0":13,"300":35,"600":38,"900":41,"1200":46,"1500":45,"1800":35,"2100":28,"2400":28,"2700":27,"3000":25,"3300":23,"3600":21,"3900":19,"4200":19,"4500":19,"4800":19,"5100":22,"5400":24,"5700":23,"6000":20,"6300":25,"6600":29,"6900":39,"7200":49,"7500":57,"7800":63,"8100":68,"8400":73,"8700":79,"9000":85,"9300":93,"9600":100,"9900":107,"10200":113,"10500":118,"10800":120,"11100":120,"11400":118,"11700":116,"12000":116},"tempSI":{"0":20,"300":21,"600":18,"900":15,"1200":13,"1500":12,"1800":13,"2100":12,"2400":10,"2700":9,"3000":7,"3300":5,"3600":3,"3900":1,"4200":-1,"4500":-3,"4800":-5,"5100":-7,"5400":-9,"5700":-12,"6000":-14,"6300":-17,"6600":-19,"6900":-21,"7200":-23,"7500":-25,"7800":-27,"8100":-29,"8400":-31,"8700":-33,"9000":-35,"9300":-38,"9600":-40,"9900":-43,"10200":-45,"10500":-47,"10800":-49,"11100":-51,"11400":-53,"11700":-54,"12000":-55},"model":"RAP"}
    """


val sampleWindsJson2 = """
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

