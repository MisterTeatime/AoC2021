fun main() {
    fun part1(input: List<String>): Int {

        val activeCubes: MutableSet<Point3D> = mutableSetOf()
        val instructions = BootInstruction.parseInstructions(input)

        for (inst in instructions) {

            for (x in inst.xRange) for (y in inst.yRange) for (z in inst.zRange) {
                when (inst.action) {
                    BootAction.ON -> activeCubes.add(Point3D(x, y, z))
                    BootAction.OFF -> activeCubes.remove(Point3D(x, y, z))
                    BootAction.NOP -> {}
                }
            }
        }

        return activeCubes.size
    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day22_test")
    check(part1(testInput) == 39)

    val input = readInput("Day22")
    println(part1(input))
    println(part2(input))
}

data class BootInstruction(val action: BootAction, val xRange: Set<Int>, val yRange: Set<Int>, val zRange: Set<Int>) {
    companion object {
        val validRange = -50..50

        private fun parseInstruction(input: String): BootInstruction {
            val action = when (input.split(" ")[0]) {
                "on" -> BootAction.ON
                "off" -> BootAction.OFF
                else -> BootAction.NOP
            }

            val ranges = input.split(" ")[1].split(",")
            val (xstart, xstop) = ranges.filter { it[0] == 'x' }[0].drop(2).split("..").map { it.toInt() }
            val (ystart, ystop) = ranges.filter { it[0] == 'y' }[0].drop(2).split("..").map { it.toInt() }
            val (zstart, zstop) = ranges.filter { it[0] == 'z' }[0].drop(2).split("..").map { it.toInt() }

            val xrange = validRange intersect IntRange(xstart, xstop)
            val yrange = validRange intersect IntRange(ystart, ystop)
            val zrange = validRange intersect IntRange(zstart, zstop)

            return BootInstruction(action = action, xRange = xrange, yRange = yrange, zRange = zrange)
        }

        fun parseInstructions(input: List<String>): List<BootInstruction> = input.map { parseInstruction(it) }
    }
}

enum class BootAction {
    ON,
    OFF,
    NOP
}