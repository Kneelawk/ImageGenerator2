package org.kneelawk.imagegenerator2.reeds

import java.awt.Color
import java.awt.GradientPaint
import java.awt.Graphics2D
import java.awt.RenderingHints

import scala.collection.Map

import org.kneelawk.imagegenerator2.ImageGenerator
import org.kneelawk.imagegenerator2.util.MathUtil
import org.kneelawk.imagegenerator2.util.StringParsingUtil

object ReedsGenerator extends ImageGenerator {
  val SHAPE = Array((0, 0), (2, 0), (2, 2), (1, 3), (0, 2))

  def options = Array(
    ("sparsity", "Sparsity (default 1000)"),
    ("leafScale", "Leaf Scale (default 10)"),
    ("topHue", "Top Hue (default random)"),
    ("topBri", "Top Brightness (default random)"),
    ("bottomHue", "Bottom Hye (default random)"),
    ("bottomBri", "Bottom Brightness (default random)"))

  def name = "Reeds"
  def apply(g: Graphics2D, options: Map[String, String], width: Int, height: Int) {
    import Math._
    import MathUtil._
    import StringParsingUtil._

    g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

    val sparsity = parseInt(options("sparsity"), 1000)
    
    val leafScale = parseInt(options("leafScale"), 10)

    val bg1Hue = parseFloat(options("topHue"), rand.nextFloat())
    val bg1Sat = 1f
    val bg1Bri = parseFloat(options("topBri"), rfloat(0.6f, 1f))
    val bg1 = Color.getHSBColor(bg1Hue, bg1Sat, bg1Bri)

    val bg2Hue = parseFloat(options("bottomHue"), rotate(bg1Hue, if (rand.nextBoolean()) rfloat(0.15f, 0.25f) else rfloat(-0.25f, -0.15f), 0f, 1f))
    val bg2Sat = bg1Sat
    val bg2Bri = parseFloat(options("bottomBri"), cap(bg1Bri, rfloat(-0.2f, 0.2f), 0f, 1f))
    val bg2 = Color.getHSBColor(bg2Hue, bg2Sat, bg2Bri)

    g.setPaint(new GradientPaint(width / 2, 0, bg1, width / 2, height, bg2))
    g.fillRect(0, 0, width, height)

    val locs = (0 until (width * height / sparsity) map (i => (rfloat(-20, width).toInt, rfloat(-30, height).toInt))).sortWith(_._2 < _._2)

    for (loc <- locs) {
      val sbg1Hue = rotate(bg1Hue, if (rand.nextBoolean()) rfloat(0.02f, 0.08f) else rfloat(-0.08f, -0.02f), 0f, 1f)
      val sbg1Sat = bg1Sat
      val sbg1Bri = rotate(bg1Bri, rfloat(-0.2f, 0.2f), 0f, 1f)
      val sbg1 = Color.getHSBColor(sbg1Hue, sbg1Sat, sbg1Bri)

      val sbg2Hue = rotate(bg2Hue, if (rand.nextBoolean()) rfloat(0.02f, 0.08f) else rfloat(-0.08f, -0.02f), 0f, 1f)
      val sbg2Sat = bg2Sat
      val sbg2Bri = rotate(bg2Bri, rfloat(-0.2f, 0.2f), 0f, 1f)
      val sbg2 = Color.getHSBColor(sbg2Hue, sbg2Sat, sbg2Bri)

      g.setPaint(new GradientPaint(width / 2, 0, sbg1, width / 2, height, sbg2))

      val x = loc._1
      val y = loc._2

      g.fillPolygon(SHAPE.map(_._1 * leafScale + x), SHAPE.map(_._2 * leafScale + y), SHAPE.length)
      g.fillRect(x + max(leafScale - 1, 0), y + max(leafScale - 2, 0), 2, height - y)
    }
  }
}