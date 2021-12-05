fun main() {
    fun part1(input: List<String>): Int {
        return input.map { line -> line.split(" -> ")} //turns each line description into two point coordinate pairs
            .map { line -> Line(line)} //turns each pair into a Line
            .filter { line -> line.isHorizontal() || line.isVertical()} //discards Lines that are not horizontal or vertical
            .flatMap { l -> l.getAllIntegerPoints()} //turns all lines into their integer points and returns a huge list with all points
            .groupingBy { it } //groups by points
            .eachCount() //applies count to each group
            .filter { it.value > 1 } //keeps all groups with more than one occurrence
            .count() //count of left groups
    }

    fun part2(input: List<String>): Int {
        return input.map { line -> line.split(" -> ")} //turns each line description into two point coordinate pairs
            .map { line -> Line(line)} //turns each pair into a Line
            //.filter { line -> line.isHorizontal() || line.isVertical()} //discards Lines that are not horizontal or vertical
            .flatMap { l -> l.getAllIntegerPoints()} //turns all lines into their integer points and returns a huge list with all points
            .groupingBy { it } //groups by points
            .eachCount() //applies count to each group
            .filter { it.value > 1 } //keeps all groups with more than one occurrence
            .count() //count of left groups
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day05_test")
    check(part2(testInput) == 12)

    val input = readInput("Day05")
    println(part1(input))
    println(part2(input))
}
