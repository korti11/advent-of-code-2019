package io.korti.adventofcode.intcode

data class Instruction(val opCode: Int, val parameters: Array<Parameter>) {

    fun getValues(program: List<Int>): Triple<Int, Int, Int> {
        return Triple(
            getValue(parameters.elementAtOrNull(0), program),
            getValue(parameters.elementAtOrNull(1), program),
            getValue(parameters.elementAtOrNull(2), program)
        )
    }

    private fun getValue(parameter: Parameter?, program: List<Int>): Int {
        return when {
            parameter == null -> {
                -1
            }
            parameter.mode == ParameterMode.POSITION_MODE -> {
                program[parameter.code]
            }
            else -> {
                parameter.code
            }
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Instruction

        if (opCode != other.opCode) return false
        if (!parameters.contentEquals(other.parameters)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = opCode
        result = 31 * result + parameters.contentHashCode()
        return result
    }
}