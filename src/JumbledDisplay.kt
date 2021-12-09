class JumbledDisplay(line: List<String>) {
    private val samples: List<String>
    val digits: List<String>

    private var a = 'a'
    private var b = 'b'
    private var c = 'c'
    private var d = 'd'
    private var e = 'e'
    private var f = 'f'
    private var g = 'g'

    /****
     * 1    2   cf
     * 7    3   acf
     * 4    4   bcdf
     * 2    5   acdeg
     * 3    5   acdfg
     * 5    5   abdfg
     * 0    6   abcefg
     * 6    6   abdefg
     * 9    6   abcdfg
     * 8    7   abcdefg
     */

    private var numberWires = mapOf(
        "abcefg" to "0",
        "cf" to "1",
        "acdeg" to "2",
        "acdfg" to "3",
        "bcdf" to "4",
        "abdfg" to "5",
        "abdefg" to "6",
        "acf" to "7",
        "abcdefg" to "8",
        "abcdfg" to "9"
    )
    val number: String
       get() {
           return digits.joinToString("") { decode(it) }
       }

    init {
        this.samples = line.take(10)
        this.digits = line.takeLast(4)
    }

    private fun decode(input: String): String {
        val decodedInput = input.map {
            when {
                (it == a) -> 'a'
                (it == b) -> 'b'
                (it == c) -> 'c'
                (it == d) -> 'd'
                (it == e) -> 'e'
                (it == f) -> 'f'
                (it == g) -> 'g'
                else -> 'x'
            }
        }.sorted().joinToString("")
       return numberWires[decodedInput]!!
    }

    fun untangle() {
        //find easy digits
        val d1 = samples.first { it.length == 2 }.toCharArray().sorted().joinToString("")
        val d7 = samples.first { it.length == 3 }.toCharArray().sorted().joinToString("")
        val d4 = samples.first { it.length == 4 }.toCharArray().sorted().joinToString("")
        //val d8 = samples.first { it.length == 7 }.toCharArray().sorted().joinToString("") // not really useful

        //wire to a: 7-1 (left: bcdefg)
        a = d7.filterNot { d1.contains(it) }[0]

        //find 9: the one 6-char digit containing 4
        val d9 = samples.first { it.length == 6 && shareSameChars(it, d4)}

        //wire to g: 9-4-a (left:bcdef)
        g = d9.filterNot { it == a || d4.contains(it)}[0]

        //find 3: the one 5-char digit containing 1
        val d3 = samples.first { it.length == 5 && shareSameChars(it, d1)}

        //wire to d: 3-1-a-g (left: bcef)
        d = d3.filterNot { it == a || it == g || d1.contains(it) }[0]

        //wire to b: 4-1-d (left: cef)
        b = d4.filterNot { it == d || d1.contains(it) }[0]

        //find 0: the one 6-char digit, which is not 9 and contains 1
        val d0 = samples.first { it.length == 6 && it != d9 && shareSameChars(it, d1)}

        //wire to e: 0-7-b-g (left: cf)
        e = d0.filterNot { d7.contains(it) || it == b || it == g }[0]

        //find 6: the one 6-char digit, which is not 0 and not 9
        val d6 = samples.first { it.length == 6 && it != d9 && it != d0}

        //wire to f: 6-a-b-d-g-e (left: c)
        f = d6.filterNot { it == a || it == b || it == d || it == e || it == g }[0]

        //wire to c: 1-f
        c = d1.filterNot { it == f }[0]
    }

    private fun shareSameChars(big: String, small: String): Boolean {
        for (ch in small) {
            when {
                (!big.contains(ch)) -> return false
            }
        }
        return true
    }
}