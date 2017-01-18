package org.kneelawk.imagegenerator2.util

object StringParsingUtil {
  def parseInt(s: String, default: Int): Int = {
    try {
      s.toInt
    } catch {
      case _: NumberFormatException => default
    }
  }

  def parseFloat(s: String, default: Float): Float = {
    try {
      s.toFloat
    } catch {
      case _: NumberFormatException => default
    }
  }

  def parseDouble(s: String, default: Double): Double = {
    try {
      s.toDouble
    } catch {
      case _: NumberFormatException => default
    }
  }
}