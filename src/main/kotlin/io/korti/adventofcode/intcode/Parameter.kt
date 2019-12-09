package io.korti.adventofcode.intcode

data class Parameter(val mode: ParameterMode = ParameterMode.POSITION_MODE, val code: Long)