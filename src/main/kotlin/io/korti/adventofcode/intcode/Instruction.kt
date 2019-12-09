package io.korti.adventofcode.intcode

data class Instruction(val rb: Long, val opCode: Int, val parameters: Array<Parameter>) {

    fun getValues(program: IntCodeProgram): Triple<Long, Long, Long> {
        return Triple(
            getValue(parameters.elementAtOrNull(0), program),
            getValue(parameters.elementAtOrNull(1), program),
            getValue(parameters.elementAtOrNull(2), program)
        )
    }

    fun getAddress(parameterId: Int): Long {
        val parameter = parameters.getOrNull(parameterId)
        return when {
            parameter == null -> {
                -1
            }
            parameter.mode == ParameterMode.POSITION_MODE -> {
                parameter.code
            }
            parameter.mode == ParameterMode.RELATIVE_MODE -> {
                rb + parameter.code
            }
            else -> {
                throw RuntimeException("Can't get an address for IMMEDIATE_MODE")
            }
        }
    }

    private fun getValue(parameter: Parameter?, program: IntCodeProgram): Long {
        return when {
            parameter == null -> {
                -1
            }
            parameter.mode == ParameterMode.POSITION_MODE -> {
                program.getValueAtAddress(parameter.code)
            }
            parameter.mode == ParameterMode.RELATIVE_MODE -> {
                program.getValueAtAddress(rb + parameter.code)
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