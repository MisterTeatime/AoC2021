fun main() {
    fun part1(input: List<String>): Int {

        val swarm = input[0].split(",").map { LanternFish(it.toInt()) }.toMutableList()

        for (i in 1..80) {
            for (j in 0 until swarm.map { it.progress() }.count { it }) swarm.add(LanternFish()) //for progress each fish and add fishes for each rollover occurring
        }

        return swarm.count()
    }

    fun part2(input: List<String>): Long {
        //instead of tracking each fish on its own, we group all fish of same age and track this age with a count of how many fish belong in this group
        var swarm = input[0].split(",").map{it.toInt()}.groupingBy { it }.eachCount().mapKeys { LanternFish(it.key) }.mapValues { it.value.toLong() }

        for (i in 1..256) {
            val progressedSwarm = hashMapOf<LanternFish, Long>() //since we can't alter swarm we're iterating, we need a temp swarm representing the future generation
            for (group in swarm) {
                when {
                    (group.key.progress() && group.value > 0) -> {          //if progress() triggers a rollover
                        progressedSwarm[LanternFish()] = group.value        //add a new group with standard age and initial value same as group (because each fish in there reproduces) to future swarm
                        progressedSwarm[group.key] = group.value            //add the existing group to future swarm
                    }
                    else -> when {
                        (progressedSwarm.containsKey(group.key)) -> progressedSwarm[group.key] = group.value + progressedSwarm[group.key]!! //at some point a new generation merges with an existing group, so we need to add those fish together
                        else -> progressedSwarm[group.key] = group.value
                    }
                }
            }
            swarm = progressedSwarm //the future swarm becomes the new swarm for the next iteration
        }

        return swarm.map { e -> e.value }.sum()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day06_test")
    check(part2(testInput) == 26984457539)

    val input = readInput("Day06")
    println(part1(input))
    println(part2(input))
}
