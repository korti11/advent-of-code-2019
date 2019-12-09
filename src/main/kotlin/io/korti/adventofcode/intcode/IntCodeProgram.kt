package io.korti.adventofcode.intcode

import java.util.*

class IntCodeProgram(private val program: List<Long>, private val headless: Boolean, private val feedbackMode: Boolean) {

    private var mProgram = program.toMutableList()
    private var memory = mutableMapOf<Long, Long>()
    private var pc = 0
    private var rb = 0L
    private var finished = false

    var inputBuffer: Queue<Long> = LinkedList()
    var outputBuffer = 0L

    fun execute() {
        try {
            var instruction: Instruction
            do {
                val (instructionCode, parameterModes) = parseOpCode(mProgram[pc])
                val parameterModesL = parameterModes.toList()
                val parameterCount = parameterCount(instructionCode)
                val parameters = (1..parameterCount).map {
                    Parameter(parameterModesL[it - 1], mProgram[pc + it])
                }
                instruction = Instruction(
                    rb,
                    instructionCode,
                    parameters.toTypedArray()
                )
                pc = execute(instruction)
                if (feedbackMode && instruction.opCode == 4) {
                    break
                }
            } while (instruction.opCode != 99)
            finished = instruction.opCode == 99
        } catch (e: Exception) {
            println("${e.javaClass.name} with ${e.message} at PC: $pc")
            e.printStackTrace()
        }
    }

    fun reset() {
        pc = 0
        mProgram = program.toMutableList()
        inputBuffer.clear()
        finished = false
    }

    fun isFinished() = finished

    fun getValueAtAddress(address: Long): Long {
        return if (address > mProgram.size) {
            memory.getOrDefault(address, 0L)
        } else {
            mProgram[address.toInt()]
        }
    }

    fun writeValueAtAddress(address: Long, value: Long) {
        if(address > mProgram.size) {
            memory[address] = value
        } else {
            mProgram[address.toInt()] = value
        }
    }

    private fun execute(instruction: Instruction): Int {
        return when(instruction.opCode) {
            1 -> {
                val (p1, p2) = instruction.getValues(this)
                val p3 = instruction.getAddress(2)
                writeValueAtAddress(p3, p1 + p2)
                pc + 4
            }
            2 -> {
                val (p1, p2) = instruction.getValues(this)
                val p3 = instruction.getAddress(2)
                writeValueAtAddress(p3, p1 * p2)
                pc + 4
            }
            3 -> {
                val p1 = instruction.parameters[0]
                if (p1.mode == ParameterMode.IMMEDIATE_MODE) {
                    throw RuntimeException("Immediate mode for INTCODE 3 at pos: $pc not supported.")
                }
                val address = instruction.getAddress(0)
                if(headless) {
                    writeValueAtAddress(address, inputBuffer.poll())
                } else {
                    print("Input: ")
                    writeValueAtAddress(address, getInput())
                }
                pc + 2
            }
            4 -> {
                val (p1) = instruction.getValues(this)
                if(headless.not()) {
                    println("Output: $p1")
                }
                outputBuffer = p1
                pc + 2
            }
            5 -> {
                val (p1, p2) = instruction.getValues(this)
                if(p1 != 0L) p2.toInt() else pc + 3
            }
            6 -> {
                val (p1, p2) = instruction.getValues(this)
                if(p1 == 0L) p2.toInt() else pc + 3
            }
            7 -> {
                val (p1, p2) = instruction.getValues(this)
                val p3 = instruction.getAddress(2)
                writeValueAtAddress(p3, if(p1 < p2) 1 else 0)
                pc + 4
            }
            8 -> {
                val (p1, p2) = instruction.getValues(this)
                val p3 = instruction.getAddress(2)
                writeValueAtAddress(p3, if(p1 == p2) 1 else 0)
                pc + 4
            }
            9 -> {
                val (p1) = instruction.getValues(this)
                rb += p1
                pc + 2
            }
            99 -> {
                pc + 1
            }
            else -> {
                throw RuntimeException("Instruction $instruction not supported.")
            }
        }
    }

    private fun getInput(): Long {
        val input = readLine() ?: throw RuntimeException("Input was null.")
        return input.toLong()
    }

    private fun parameterCount(instruction: Int): Int {
        return when {
            arrayOf(1, 2, 7, 8).contains(instruction) -> {
                3
            }
            arrayOf(5, 6).contains(instruction) -> {
                2
            }
            arrayOf(3, 4, 9).contains(instruction) -> {
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

    private fun parseOpCode(opcode: Long): Pair<Int, Triple<ParameterMode, ParameterMode, ParameterMode>> {
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