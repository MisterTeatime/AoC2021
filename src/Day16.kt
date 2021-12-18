fun main() {
    fun part1(input: List<String>): Int {
        val test = parsePacket(StringBuilder(toBinaryString(input[0])))

        return input.size
    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day16_test")
    check(part1(testInput) == 5)

    val input = readInput("Day16")
    println(part1(input))
    println(part2(input))
}

fun parsePacket(input: StringBuilder): Packet {
    val type = input.drop(3).take(3).toString().toInt(2)
    val parsedPacket = when (type) {
        4 -> parseLiteralPacket(input)
        else -> parseOperatorPacket(input)
    }

    return parsedPacket
}

fun parseLiteralPacket(input: StringBuilder): Packet {
    val version = input.takeDeleting(3)
    val type = input.takeDeleting(3)
    val bitGroups = mutableListOf<String>()

    while (true) {
        if (!input.startsWith("1")) break
        val oneGroup = input.takeDeleting(5)
        bitGroups.add(oneGroup)
    }

    check(input.startsWith("0"))
    val zeroGroup = input.takeDeleting(5)
    bitGroups.add(zeroGroup)
    val number = bitGroups.joinToString("") { it.drop(1) }.toLong(2)
    return Packet(PacketHeader(version.toInt(2), type.toInt(2)), LiteralPacketBody(number))
}

fun parseOperatorPacket(input: StringBuilder): Packet {
    val version = input.takeDeleting(3)
    val type = input.takeDeleting(3)
    val lengthType = input.takeDeleting(1).toInt(2)
    val subPackets = mutableListOf<Packet>()

    if (lengthType == 0) {
        val bitsSubPackets = input.takeDeleting(15).toInt(2)
        val remainingLength = input.length

        while (remainingLength - input.length < bitsSubPackets) {
            subPackets.add(parsePacket(input))
        }
    } else if (lengthType == 1) {
        val subPacketNum = input.takeDeleting(11).toInt(2)

        repeat(subPacketNum) {
            subPackets.add(parsePacket(input))
        }
    }

    return Packet(PacketHeader(version.toInt(2), type.toInt(2)), OperatorPacketBody(subPackets))
}

data class Packet(val header: PacketHeader, val body: PacketBody) {
    fun versionSum(): Long {
        val thisSum = header.packetVersion
        return thisSum + body.versionSum()
    }

    override fun toString(): String {
        return "[$header($body)]"
    }
}

sealed class PacketBody {
    abstract fun eval(): Long
    abstract fun versionSum(): Long
}

data class PacketHeader(val packetVersion: Int, val typeId: Int) {
    override fun toString(): String {
        return "version=$packetVersion, type=$typeId"
    }
}

data class LiteralPacketBody(val number: Long): PacketBody() {
    override fun eval(): Long {
        return number
    }

    override fun versionSum(): Long {
        return 0L
    }

    override fun toString(): String {
        return "num=$number"
    }
}

data class OperatorPacketBody(val packets: List<Packet>): PacketBody() {
    override fun versionSum(): Long {
        return packets.sumOf { it.versionSum() }
    }

    override fun toString(): String {
        return "OP($packets)"
    }

    override fun eval(): Long {
        return 0L
    }
}

fun StringBuilder.takeDeleting(len: Int): String {
    val res = this.take(len)
    this.delete(0, len)
    return res.toString()
}

fun toBinaryString(input: String) = input.map { hexChar ->
    val hexx = hexChar.digitToInt(16)
    hexx.toString(2).padStart(4, '0')
}.joinToString("")