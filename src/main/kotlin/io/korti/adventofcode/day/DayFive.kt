package io.korti.adventofcode.day

private enum class ParameterMode {
    POSITION_MODE,
    IMMEDIATE_MODE
}

private class Parameter(val mode: ParameterMode = ParameterMode.POSITION_MODE, val code: Int)

private class Instruction(val pc: Int, val instruction: Int, vararg val parameters: Parameter) {

    fun execute(program: MutableList<Int>): Int {
        return when(instruction) {
            1 -> {
                val (p1, p2) = getValues(program)
                val p3 = parameters[2]
                program[p3.code] = p1 + p2
                pc + 4
            }
            2 -> {
                val (p1, p2) = getValues(program)
                val p3 = parameters[2]
                program[p3.code] = p1 * p2
                pc + 4
            }
            3 -> {
                val p1 = parameters[0]
                if (p1.mode == ParameterMode.IMMEDIATE_MODE) {
                    throw RuntimeException("Immediate mode for INTCODE 3 at pos: $pc not supported.")
                }
                print("Input: ")
                program[p1.code] = getInput()
                pc + 2
            }
            4 -> {
                val (p1) = getValues(program)
                println("Output: $p1")
                pc + 2
            }
            5 -> {
                val (p1, p2) = getValues(program)
                if(p1 != 0) p2 else pc + 3
            }
            6 -> {
                val (p1, p2) = getValues(program)
                if(p1 == 0) p2 else pc + 3
            }
            7 -> {
                val (p1, p2) = getValues(program)
                val p3 = parameters[2]
                program[p3.code] = if(p1 < p2) 1 else 0
                pc + 4
            }
            8 -> {
                val (p1, p2) = getValues(program)
                val p3 = parameters[2]
                program[p3.code] = if(p1 == p2) 1 else 0
                pc + 4
            }
            99 -> {
                pc + 1
            }
            else -> {
                throw RuntimeException("Instruction $instruction not supported.")
            }
        }
    }

    private fun getInput(): Int {
        val input = readLine() ?: throw RuntimeException("Input was null.")
        return input.toInt()
    }

    private fun getValues(program: List<Int>): Triple<Int, Int, Int> {
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

}

class DayFive : AbstractDay() {

    override fun getDay(): Int {
        return 5
    }

    override fun getSubLevels(): Int {
        return 1
    }

    override fun run(input: List<String>): String {
        val program = input[0].split(",").map { it.toInt() }.toMutableList()
        parseAndExecuteProgram(program)
        return ""
    }

    private fun parseAndExecuteProgram(program: MutableList<Int>) {
        var instruction: Instruction
        var pc = 0
        do {
            val (instructionCode, parameterModes) = parseOpCode(program[pc])
            val parameterModesL = parameterModes.toList()
            val parameterCount = parameterCount(instructionCode)
            val parameters = (1 .. parameterCount).map {
                Parameter(parameterModesL[it - 1], program[pc + it])
            }
            instruction = Instruction(
                pc,
                instructionCode,
                *parameters.toTypedArray()
            )
            pc = instruction.execute(program)
        } while (instruction.instruction != 99)
    }

    private fun parameterCount(instruction: Int): Int {
        return if(arrayOf(1, 2, 7, 8).contains(instruction)) {
            3
        } else if(arrayOf(5, 6).contains(instruction)) {
            2
        } else if (arrayOf(3, 4).contains(instruction)) {
            1
        } else if (instruction == 99) {
            0
        } else {
            throw RuntimeException("Instruction not supported.")
        }
    }

    private fun parseOpCode(opcode: Int): Pair<Int, Triple<ParameterMode, ParameterMode, ParameterMode>> {
        var sOpCode = opcode.toString()
        sOpCode = "0".repeat(5 - sOpCode.length) + sOpCode
        val parModes = sOpCode.substring(0, 3)
        val instruction = sOpCode.substring(3)
        return Pair(
            instruction.toInt(),
            Triple(
                ParameterMode.values()[parModes[2].toString().toInt()],
                ParameterMode.values()[parModes[1].toString().toInt()],
                ParameterMode.values()[parModes[0].toString().toInt()]
            )
        )
    }

}