package org.kneelawk.imagegenerator2.util

import java.awt.Color

case class HSBColor(hue: Float, sat: Float, bri: Float) {
  def toColor = Color.getHSBColor(hue, sat, bri)
}