package io.korti.adventofcode.io

import java.io.BufferedReader
import java.io.FileReader
import java.io.FileWriter


object ReadWriter {

    private const val inputFileTemplate = "data/day%d/day%d-%d.txt"
    private const val outputFileTemplate = "data/day%d/day%d-%ds.txt"

    fun readFile(level: Int, subLevel: Int): List<String> {
        val bufferedReader = BufferedReader(FileReader(inputFileTemplate.format(level, level, subLevel)))
        val input = mutableListOf<String>()

        bufferedReader.useLines {
            it.forEach {
                input.add(it)
            }
        }
        return input
    }

    fun writeFile(level: Int, subLevel: Int, output: String) {
        val fileWriter = FileWriter(outputFileTemplate.format(level, level, subLevel))

        fileWriter.use {
            it.write(output)
        }
    }

}