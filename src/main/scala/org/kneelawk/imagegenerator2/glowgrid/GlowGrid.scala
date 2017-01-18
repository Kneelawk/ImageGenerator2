package org.kneelawk.imagegenerator2.glowgrid

import java.awt.Graphics2D

import scala.collection.Map
import scala.util.Random

import org.kneelawk.imagegenerator2.ImageGenerator
import org.kneelawk.imagegenerator2.util.StringParsingUtil
import java.awt.Color
import org.kneelawk.imagegenerator2.util.MathUtil

object GlowGrid extends ImageGenerator {
  val hueOffset = 0.06f
  val briOffset = 0.04f

  def name = "GlowGrid"
  def options = Array(
    ("size", "Square size (default 20)"),
    ("hue", "Hue (default random)"),
    ("bri", "Brightness (default random)"))

  def apply(g: Graphics2D, options: Map[String, String], width: Int, height: Int) {
    import StringParsingUtil._
    import MathUtil._

    val size = parseInt(options("size"), 20)
    val hue = parseFloat(options("hue"), rand.nextFloat())
    val bri = parseFloat(options("bri"), rfloat(0.5f, 1f))

    val gridWidth = math.ceil(width.toDouble / size.toDouble).toInt
    val gridHeight = math.ceil(height.toDouble / size.toDouble).toInt

    val grid = Array.ofDim[Color](gridHeight, gridWidth)

    def drawSquare(x: Int, y: Int, hue: Float, bri: Float) {
      val c = Color.getHSBColor(hue, 1f, bri)
      grid(y)(x) = c
      g.setColor(c)
      g.fillRect(x * size, y * size, size, size)
      g.setColor(Color.getHSBColor(hue, 1f, cap(bri, -0.05f, 0f, 1f)))
      g.drawRect(x * size, y * size, size, size)
    }

    val sHue = rotate(hue, rand.nextFloat() * hueOffset, 0f, 1f)
    val sBri = cap(bri, rand.nextFloat() * briOffset, 0f, 1f)
    drawSquare(0, 0, sHue, sBri)

    for (i <- 1 until gridWidth) {
      // TODO: finish GlowGrid generator
      //      val nHue = rotate(
    }
  }
}