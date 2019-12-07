package io.korti.adventofcode.intcode

import java.util.*

class IntCodeProgram(private val program: List<Int>, private val headless: Boolean, private val feedbackMode: Boolean) {

    private var mProgram = program.toMutableList()
    private var pc = 0
    private var finished = false

    var inputBuffer: Queue<Int> = LinkedList<Int>()
    var outputBuffer = 0

    fun execute() {
        var instruction: Instruction
        do {
            val (instructionCode, parameterModes) = parseOpCode(mProgram[pc])
            val parameterModesL = parameterModes.toList()
            val parameterCount = parameterCount(instructionCode)
            val parameters = (1 .. parameterCount).map {
                Parameter(parameterModesL[it - 1], mProgram[pc + it])
            }
            instruction = Instruction(
                instructionCode,
                parameters.toTypedArray()
            )
            pc = execute(instruction)
            if (feedbackMode && instruction.opCode == 4) {
                break
            }
        } while (instruction.opCode != 99)
        finished = instruction.opCode == 99
    }

    fun reset() {
        pc = 0
        mProgram = program.toMutableList()
        inputBuffer.clear()
        finished = false
    }

    fun isFinished() = finished

    private fun execute(instruction: Instruction): Int {
        return when(instruction.opCode) {
            1 -> {
                val (p1, p2) = instruction.getValues(mProgram)
                val p3 = instruction.parameters[2]
                mProgram[p3.code] = p1 + p2
                pc + 4
            }
            2 -> {
                val (p1, p2) = instruction.getValues(mProgram)
                val p3 = instruction.parameters[2]
                mProgram[p3.code] = p1 * p2
                pc + 4
            }
            3 -> {
                val p1 = instruction.parameters[0]
                if (p1.mode == ParameterMode.IMMEDIATE_MODE) {
                    throw RuntimeException("Immediate mode for INTCODE 3 at pos: $pc not supported.")
                }
                if(headless) {
                    mProgram[p1.code] = inputBuffer.poll()
                } else {
                    print("Input: ")
                    mProgram[p1.code] = getInput()
                }
                pc + 2
            }
            4 -> {
                val (p1) = instruction.getValues(mProgram)
                if(headless.not()) {
                    println("Output: $p1")
                }
                outputBuffer = p1
                pc + 2
            }
            5 -> {
                val (p1, p2) = instruction.getValues(mProgram)
                if(p1 != 0) p2 else pc + 3
            }
            6 -> {
                val (p1, p2) = instruction.getValues(mProgram)
                if(p1 == 0) p2 else pc + 3
            }
            7 -> {
                val (p1, p2) = instruction.getValues(mProgram)
                val p3 = instruction.parameters[2]
                mProgram[p3.code] = if(p1 < p2) 1 else 0
                pc + 4
            }
            8 -> {
                val (p1, p2) = instruction.getValues(mProgram)
                val p3 = instruction.parameters[2]
                mProgram[p3.code] = if(p1 == p2) 1 else 0
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

    private fun parameterCount(instruction: Int): Int {
        return when {
            arrayOf(1, 2, 7, 8).contains(instruction) -> {
                3
            }
            arrayOf(5, 6).contains(instruction) -> {
                2
            }
            arrayOf(3, 4).contains(instruction) -> {
                1
            }
            instruction == 99 -> {
                0
            }
            else -> {
                throw RuntimeException("Instruction $instruction at pc: $pc not supported.")
            }
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