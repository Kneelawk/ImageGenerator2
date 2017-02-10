package org.kneelawk.imagegenerator2.util

import scala.util.Random

object MathUtil {
  val rand = new Random
  def rfloat(min: Float, max: Float) = rand.nextFloat() * (max - min) + min
  
  def rotate(value: Int, amount: Int, min: Int, max: Int): Int = {
    val size = max - min
    if (size > 0) {
      var quot = (value.toDouble + amount.toDouble - min.toDouble) / size.toDouble
      quot -= quot.floor
      return (quot * size + min).toInt
    } else if (size == 0) {
      return min
    } else {
      throw new RuntimeException("Invalid rotate range")
    }
  }

  def rotate(value: Float, amount: Float, min: Float, max: Float): Float = {
    val size = max - min
    if (size > 0) {
      var quot = (value + amount - min) / size
      quot -= quot.floor
      return quot * size + min
    } else if (size == 0) {
      return min
    } else {
      throw new RuntimeException("Invalid rotate range")
    }
  }

  def mod2(value: Float, min: Float, max: Float): Float = {
    val size = max - min
    if (size > 0) {
      var quot = (value - min) / size
      quot -= quot.floor
      return quot * size + min
    } else if (size == 0) {
      return min
    } else {
      throw new RuntimeException("Invalid mod2 range")
    }
  }

  def cap(value: Float, amount: Float, min: Float, max: Float): Float = {
    val nval = value + amount
    if (nval < min) return min
    else if (nval > max) return max
    return nval
  }

  def cap(value: Float, min: Float, max: Float): Float = {
    if (value < min) return min
    else if (value > max) return max
    return value
  }
  
  def average(a: Float, b: Float) = (a + b) / 2
  
  def average(a: Float, b: Float, c: Float) = (a + b + c) / 3
  
  def average(floats: Float*): Float = {
    var v = 0f
    for (f <- floats) {
      v += f
    }
    return v / floats.length
  }
}