package org.kneelawk.imagegenerator2.matrix

import java.awt.Graphics2D

import scala.collection.Map

import org.kneelawk.imagegenerator2.ImageGenerator
import java.awt.RenderingHints
import java.awt.Color

object MatrixGenerator extends ImageGenerator {
  def name = "Matrix"
  def options = Array(("sparsity", "Sparsity (default: 5000)", "5000"))

  def apply(g: Graphics2D, options: Map[String, String], width: Int, height: Int) {
    val sparsity = options("sparsity").toInt
    
    g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON)
    g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
    
    g.setColor(Color.BLACK)
    g.fillRect(0, 0, width, height)
    
    for (i <- 0 until (width * height / sparsity)) {
      
    }
  }
}