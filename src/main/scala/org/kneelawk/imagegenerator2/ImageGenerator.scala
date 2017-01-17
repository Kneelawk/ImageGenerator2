package org.kneelawk.imagegenerator2

import java.awt.Graphics2D

trait ImageGenerator {
  def name: String
  def apply(g: Graphics2D, width: Int, height: Int)
}