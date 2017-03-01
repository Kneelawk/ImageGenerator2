package org.kneelawk.imagegenerator2.matrix

import java.awt.Color
import java.awt.Font
import java.awt.Graphics2D
import java.awt.RenderingHints

import scala.collection.Map

import org.kneelawk.imagegenerator2.ImageGenerator
import org.kneelawk.imagegenerator2.util.MathUtil
import org.kneelawk.imagegenerator2.util.StringParsingUtil
import java.awt.image.BufferedImage

object MatrixGenerator extends ImageGenerator {
  import MathUtil._

  val letters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890"
  val font = "Courier"
  val letterHue = 112f / 360f
  val letterSaturation = 0.64f
  val firstSaturation = 0.24f
  val startBrightness = 1f
  def letterBrightness(index: Int, len: Int): Float = {
    (len - index) * startBrightness / len
  }
  def randChar: String =
    letters.charAt(rand.nextInt(letters.length())).toString()

  def name = "Matrix"
  def options = Array(("sparsity", "Sparsity (default: 5000)"))

  def apply(i: BufferedImage, options: Map[String, String], width: Int, height: Int) {
    val g = i.createGraphics()
    import StringParsingUtil.parseInt
    val sparsity = parseInt(options("sparsity"), 5000)

    g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON)
    g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)

    g.setColor(Color.BLACK)
    g.fillRect(0, 0, width, height)

    for (i <- 0 until (width * height / sparsity)) {
      val x = rand.nextInt(width)
      val y = rand.nextInt(height + 80)
      val len = rand.nextInt(18) + 4

      val fontSize = rand.nextInt(9) + 9
      g.setFont(new Font(font, Font.BOLD, fontSize))
      var fontHeight = g.getFontMetrics.getHeight

      g.setColor(Color.getHSBColor(letterHue, firstSaturation, startBrightness))

      g.drawString(randChar, x, y)
      var strLoc = y - fontHeight

      g.setFont(new Font(font, Font.PLAIN, fontSize))
      fontHeight = g.getFontMetrics.getHeight

      for (i <- 0 until len) {
        g.setColor(Color.getHSBColor(letterHue, letterSaturation, letterBrightness(i, len)))

        g.drawString(randChar, x, strLoc)
        strLoc -= fontHeight
      }
    }
  }
}