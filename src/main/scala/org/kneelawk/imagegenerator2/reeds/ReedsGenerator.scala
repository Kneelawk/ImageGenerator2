package org.kneelawk.imagegenerator2.reeds

import org.kneelawk.imagegenerator2.ImageGenerator
import java.awt.Graphics2D
import java.awt.RenderingHints
import java.awt.GradientPaint
import java.awt.Color
import scala.util.Random

object ReedsGenerator extends ImageGenerator {
  val SHAPE = Array((0, 0), (20, 0), (20, 20), (10, 30), (0, 20))
  
  val rand = new Random
  def rfloat(min: Float, max: Float) = rand.nextFloat() * (max - min) + min

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

  def cap(value: Float, amount: Float, min: Float, max: Float): Float = {
    val nval = value + amount
    if (nval < min) return min
    else if (nval > max) return max
    return nval
  }

  def name = "Reeds"
  def apply(g: Graphics2D, width: Int, height: Int) {
    g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

    val bg1Hue = rand.nextFloat()
    val bg1Sat = 1f
    val bg1Bri = rfloat(0.6f, 1f)
    val bg1 = Color.getHSBColor(bg1Hue, bg1Sat, bg1Bri)

    val bg2Hue = rotate(bg1Hue, if (rand.nextBoolean()) rfloat(0.15f, 0.25f) else rfloat(-0.25f, -0.15f), 0f, 1f)
    val bg2Sat = bg1Sat
    val bg2Bri = cap(bg1Bri, rfloat(-0.2f, 0.2f), 0f, 1f)
    val bg2 = Color.getHSBColor(bg2Hue, bg2Sat, bg2Bri)

    g.setPaint(new GradientPaint(width / 2, 0, bg1, width / 2, height, bg2))
    g.fillRect(0, 0, width, height)

    val locs = (0 until (width * height / 1000) map (i => (rfloat(-20, width).toInt, rfloat(-30, height).toInt))).sortWith(_._2 < _._2)

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

      g.fillPolygon(SHAPE.map(_._1 + x), SHAPE.map(_._2 + y), SHAPE.length)
      g.fillRect(x + 9, y + 28, 2, height - (y + 28))
    }
  }
}