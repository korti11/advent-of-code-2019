package io.korti.adventofcode.day

import io.korti.adventofcode.io.ReadWriter

abstract class AbstractDay {

    abstract fun getDay(): Int

    abstract fun getSubLevels(): Int

    abstract fun run(input: List<String>): String

    fun execute() {
        for (i in 1..getSubLevels()) {
            val input = ReadWriter.readFile(getDay(), i)
            val output = run(input)
            ReadWriter.writeFile(getDay(), i, output)
        }
    }

}