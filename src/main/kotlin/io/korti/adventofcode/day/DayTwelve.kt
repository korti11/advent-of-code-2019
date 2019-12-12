package io.korti.adventofcode.day

import io.korti.adventofcode.math.Axis
import io.korti.adventofcode.math.Vec3
import java.lang.Math.pow
import java.text.DecimalFormat
import java.util.*
import kotlin.math.abs
import kotlin.math.min
import kotlin.math.pow

typealias Planet = Pair<Vec3, Vec3>

class DayTwelve : AbstractDay() {
    override fun getDay(): Int {
        return 12
    }

    override fun getSubLevels(): Int {
        return 1
    }

    override fun run(input: List<String>): String {
        val original = input.map { it.replace("<", "").replace(">", "").replace(" ", "").split(",") }.map { vec ->
            vec.map { it.split("=")[1] }.let { Vec3(it[0].toInt(), it[1].toInt(), it[2].toInt()) }
        }.map { Planet(it, Vec3(0, 0, 0)) }
        var vectors = original.toMutableList().toList()
        val steps = arrayOf(0L, 0L, 0L)

        /*for(time in 1..1000) {
            vectors = calcVel(vectors)
            vectors = updatePositions(vectors)
        }*/
        for ((index, axis) in Axis.values().withIndex()) {
            println("Start simulating axis: $axis")
            do {
                vectors = calcVel(vectors, axis)
                vectors = updatePositions(vectors)
                steps[index] += 1L
            } while (check(vectors, original, axis).not())
            println("Finished simulating axis: $axis with ${steps[index]}")
        }

        val factors = steps.map { primeFactors(it) }.map { it.entries }.flatten()
            .groupBy { it.key }.map { Pair(it.key, it.value.map { entry -> entry.value }) }
            .map { Pair(it.first, it.second.max()) }
//        return vectors.map { Pair(it.first.sum(), it.second.sum()) }
//            .map { it.first * it.second }.sum().toString()
        return factors.let {
            var lcm = 1.0
            for((num, count) in it) {
                if(count != null) {
                    lcm *= num.toDouble().pow(count.toDouble())
                }
            }
            lcm.toLong()
        }.toString()
    }

    private fun check(vectors: List<Planet>, original: List<Planet>, axis: Axis): Boolean {
        val axisVectors = vectors.map { Pair(it.first.get(axis), it.second.get(axis)) }
        val axisOriginal = original.map { Pair(it.first.get(axis), it.second.get(axis)) }
        return axisVectors.containsAll(axisOriginal)
    }

    fun primeFactors(n: Long): Map<Long, Long> {
        if (n < 2) return emptyMap()

        val primeFactors = mutableMapOf<Long, Long>()
        var remainder = n
        var i = 2L
        while (i <= remainder / i) {
            while (remainder % i == 0L) {
                primeFactors[i] = primeFactors.getOrDefault(i, 0) + 1
                remainder /= i
            }

            i++
        }

        if (remainder > 1)
            primeFactors[remainder] = primeFactors.getOrDefault(remainder, 0) + 1

        return primeFactors
    }

    private fun gcd(n1: Long, n2: Long, n3: Long): Long {
        val n12 = n1 * n2
        val n13 = n1 * n3
        val n23 = n2 * n3
        var i = 1L
        var gcd = 1L

        val min = min(n12, min(n13, n23))

        println("Start calculation gcd for $n12, $n13 and $n23")
        while(i <= min) {
            if(n12 % i == 0L && n13 % i == 0L && n23 % i == 0L) {
                gcd = i
            }
            i++
        }
        println("Finished calculating gcd $gcd")

        return gcd
    }

    private fun Vec3.sum(): Int {
        return abs(this.x.toInt()) + abs(this.y.toInt()) + abs(this.z.toInt())
    }

    private fun calcVel(vectors: List<Planet>, axis: Axis): List<Planet> {
        val newVectors = mutableListOf<Planet>()

        for (planet1 in vectors) {
            var vel = planet1.second
            for (planet2 in vectors) {
                if (planet1 == planet2) {
                    continue
                }
                vel = when (axis) {
                    Axis.X -> vel.move(Axis.X, planet2.first.compareX(planet1.first))
                    Axis.Y ->  vel.move(Axis.Y, planet2.first.compareY(planet1.first))
                    Axis.Z -> vel.move(Axis.Z, planet2.first.compareZ(planet1.first))
                }
            }
            newVectors.add(Planet(planet1.first, vel))
        }

        return newVectors
    }

    private fun updatePositions(vectors: List<Planet>): List<Planet> {
        val newVectors = mutableListOf<Planet>()

        for(planet in vectors) {
            newVectors.add(Planet(planet.first + planet.second, planet.second))
        }

        return newVectors
    }
}