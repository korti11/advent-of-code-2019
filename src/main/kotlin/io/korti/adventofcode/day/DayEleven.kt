package io.korti.adventofcode.day

import io.korti.adventofcode.intcode.IntCodeProgram

class DayEleven : AbstractDay() {
    override fun getDay(): Int {
        return 11
    }

    override fun getSubLevels(): Int {
        return 1
    }

    override fun run(input: List<String>): String {
        val intCode = input[0].split(",").map { it.toLong() }
        return PrintRobot(intCode).startPrinting().toString()
    }

}

class PrintRobot(intCode: List<Long>) {

    private val brain = IntCodeProgram(intCode, headless = true, feedbackMode = true)
    private val paintedPanels = HashMap<Pair<Int, Int>, Long>()
    private var x = 0
    private var y = 0
    private var dir = 0

    fun startPrinting(): Int {
        paintedPanels[Pair(0,0)] = 1
        do {
            val curPos = Pair(x, y)

            if(paintedPanels.contains(curPos)) {
                //println("Double position at ($x, $y)")
                brain.inputBuffer.add(paintedPanels[curPos])
            }
            else {
                brain.inputBuffer.add(0)
            }

            brain.execute()
            if (brain.isFinished()) {
                break
            }
            val color = brain.outputBuffer.poll()
            if (color != null) paintedPanels[curPos] = color
            //println("Paint at the position ($x, $y) with the color $color")

            brain.execute()
            val direction = brain.outputBuffer.poll()
            if (direction != null) move(direction)
            //println("Move in direction $direction")
            //println("Painted panels: ${paintedPanels.size}")
        } while (brain.isFinished().not())
        printPanel(paintedPanels)
        return paintedPanels.size
    }

    private fun printPanel(panel: HashMap<Pair<Int, Int>, Long>) {
        val minX = panel.keys.minBy { it.first }?.first
        val maxX = panel.keys.maxBy { it.first }?.first
        val minY = panel.keys.minBy { it.second }?.second
        val maxY = panel.keys.maxBy { it.second }?.second

        if (minX != null && maxX != null && minY != null && maxY != null) {
            for (y in minY..maxY) {
                for (x in minX..maxX) {
                    val color = panel[Pair(x, y)]
                    if (color == 1L) {
                        print('#')
                    } else {
                        print('.')
                    }
                }
                println()
            }
        }
    }

    private fun move(direction: Long) {
        dir += if(direction == 0L) -1 else 1
        if(dir < 0) dir = 3
        if(dir > 3) dir = 0

        when(dir) {
            0 -> y--
            1 -> x++
            2 -> y++
            3 -> x--
        }
    }

}