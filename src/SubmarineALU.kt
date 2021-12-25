import java.util.*

typealias Register = MutableMap<String, Int>

class SubmarineALU(input: List<String>) {
    var register = mutableMapOf(
        "w" to 0,
        "x" to 0,
        "y" to 0,
        "z" to 0
    )

    private val program = input.map { ALUInstruction.parse(it) }

    fun run(input: String) {
        val data = StringBuilder(input)
        program.forEach { it.execute(data, register) }
    }

}

class ALUInstruction(private val operation: ALUOperation, private val parameters: List<String>) {
    fun execute(input: StringBuilder, register: Register) {
        register[parameters[0]] = when (operation) {
            ALUOperation.INP -> input.takeDeleting(1).toInt()
            ALUOperation.ADD -> register[parameters[0]]!! + (parameters[1].toIntOrNull() ?: register[parameters[1]]!!)
            ALUOperation.MUL -> register[parameters[0]]!! * (parameters[1].toIntOrNull() ?: register[parameters[1]]!!)
            ALUOperation.DIV -> register[parameters[0]]!! / (parameters[1].toIntOrNull() ?: register[parameters[1]]!!)
            ALUOperation.MOD -> register[parameters[0]]!! % (parameters[1].toIntOrNull() ?: register[parameters[1]]!!)
            ALUOperation.EQL -> {
                val a = register[parameters[0]]!!
                val b = parameters[1].toIntOrNull() ?: register[parameters[1]]!!
                when (a) {
                    b -> 1
                    else -> 0
                }
            }
        }
    }

    override fun toString(): String {
        return "${operation.toString()} $parameters"
    }
    companion object {
        fun parse(input: String): ALUInstruction {
            val data = input.split(" ")
            val op = ALUOperation.valueOf(data[0].uppercase())
            val paras = data.drop(1)

            return ALUInstruction(ALUOperation.valueOf(data[0].uppercase()), data.drop(1))
        }
    }
}

enum class ALUOperation {
    INP,
    ADD,
    MUL,
    DIV,
    MOD,
    EQL
}