package org.kneelawk.imagegenerator2.driftingyarn

import java.awt.image.BufferedImage

import scala.collection.Map

import org.kneelawk.imagegenerator2.ImageGenerator
import org.kneelawk.imagegenerator2.util.StringParsingUtil
import org.kneelawk.imagegenerator2.util.MathUtil
import org.kneelawk.imagegenerator2.util.HSBColor

object DriftingYarn extends ImageGenerator {
  def name: String = "DriftingYarn"

  def options: Seq[(String, String)] = Array(
    ("hue", "Starting Hue (default random)"),
    ("bri", "Starting Brightness (default random)"),
    ("hueDrift", "Hue Drift Speed (default 0.01)"),
    ("briDrift", "Brightness Drift Speed (default 0.04)"))

  def apply(i: BufferedImage, options: Map[String, String], width: Int, height: Int) {
    import StringParsingUtil._
    import MathUtil._

    val hue = parseFloat(options("hue"), rand.nextFloat())
    val bri = parseFloat(options("bri"), rfloat(0.5f, 1f))
    val hueDrift = parseFloat(options("hueDrift"), 0.01f)
    val briDrift = parseFloat(options("briDrift"), 0.04f)

    val grid = Array.ofDim[HSBColor](height, width)

    def drawSquare(x: Int, y: Int, hue: Float, bri: Float) {
      val c = new HSBColor(hue, 1f, bri)
      grid(y)(x) = c
      val rgb = c.toColor
      i.setRGB(x, y, rgb.getRGB)
    }
    
    drawSquare(0, 0, hue, bri)

    for (i <- 1 until width) {
      val nHue = rotate(grid(0)(i - 1).hue, rand.nextFloat() * hueDrift - hueDrift / 2, 0f, 1f)
      val nBri = cap(grid(0)(i - 1).bri, rand.nextFloat() * briDrift - briDrift / 2, 0f, 1f)
      drawSquare(i, 0, nHue, nBri)
    }

    for (y <- 1 until height) {
      for (x <- 0 until width) {
        val nHue = rotate(
          circularMean(0, 1,
            grid(y - 1)(rotate(x, -1, 0, width)).hue,
            grid(y - 1)(x).hue,
            grid(y - 1)(rotate(x, 1, 0, width)).hue),
          rand.nextFloat() * hueDrift - hueDrift / 2, 0f, 1f)
        val nBri = cap(
          average(
            grid(y - 1)(rotate(x, -1, 0, width)).bri,
            grid(y - 1)(x).bri,
            grid(y - 1)(rotate(x, 1, 0, width)).bri),
          rand.nextFloat() * briDrift - briDrift / 2, 0f, 1f)
        drawSquare(x, y, nHue, nBri)
      }
    }
  }
}