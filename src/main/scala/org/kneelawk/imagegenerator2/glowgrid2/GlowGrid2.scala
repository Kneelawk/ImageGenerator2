package org.kneelawk.imagegenerator2.glowgrid2

import java.awt.Graphics2D

import scala.collection.Map
import scala.util.Random

import org.kneelawk.imagegenerator2.ImageGenerator
import org.kneelawk.imagegenerator2.util.StringParsingUtil
import java.awt.Color
import org.kneelawk.imagegenerator2.util.MathUtil
import org.kneelawk.imagegenerator2.util.HSBColor

object GlowGrid2 extends ImageGenerator {
  val hueOffset = 0.04f
  val briOffset = 0.06f

  def name = "GlowGrid2"
  def options = Array(
    ("size", "Square size (default 20)"),
    ("hue", "Hue (default random)"),
    ("bri", "Brightness (default random)"))

  def apply(g: Graphics2D, options: Map[String, String], width: Int, height: Int) {
    import StringParsingUtil._
    import MathUtil._

    val size = Math.max(parseInt(options("size"), 20), 5)
    val hue = parseFloat(options("hue"), rand.nextFloat())
    val bri = parseFloat(options("bri"), rfloat(0.5f, 1f))

    val gridWidth = math.ceil(width.toDouble / size.toDouble).toInt
    val gridHeight = math.ceil(height.toDouble / size.toDouble).toInt

    val grid = Array.ofDim[HSBColor](gridHeight, gridWidth)

    def drawSquare(x: Int, y: Int, hue: Float, bri: Float) {
      val c = new HSBColor(hue, 1f, bri)
      grid(y)(x) = c
      g.setColor(c.toColor)
      g.fillRect(x * size, y * size, size, size)
      g.setColor(Color.getHSBColor(hue, 1f, cap(bri, -0.1f, 0f, 1f)))
      g.drawRoundRect(x * size + 2, y * size + 2, size - 5, size - 5, size / 10, size / 10)
    }

    val sHue = rotate(hue, rand.nextFloat() * hueOffset - hueOffset / 2, 0f, 1f)
    val sBri = cap(bri, rand.nextFloat() * briOffset - briOffset / 2, 0f, 1f)
    drawSquare(0, 0, sHue, sBri)

    for (i <- 1 until gridWidth) {
      val nHue = rotate(grid(0)(i - 1).hue, rand.nextFloat() * hueOffset - hueOffset / 2, 0f, 1f)
      val nBri = cap(grid(0)(i - 1).bri, rand.nextFloat() * briOffset - briOffset / 2, 0f, 1f)
      drawSquare(i, 0, nHue, nBri)
    }

    for (y <- 1 until gridHeight) {
      for (x <- 0 until gridWidth) {
        val nHue = rotate(
          circularMean(0, 1,
            grid(y - 1)(rotate(x, -1, 0, gridWidth)).hue,
            grid(y - 1)(x).hue,
            grid(y - 1)(rotate(x, 1, 0, gridWidth)).hue),
          rand.nextFloat() * hueOffset - hueOffset / 2, 0f, 1f)
        val nBri = cap(
          average(
            grid(y - 1)(rotate(x, -1, 0, gridWidth)).bri,
            grid(y - 1)(x).bri,
            grid(y - 1)(rotate(x, 1, 0, gridWidth)).bri),
          rand.nextFloat() * briOffset - briOffset / 2, 0f, 1f)
        drawSquare(x, y, nHue, nBri)
      }
    }
  }
}