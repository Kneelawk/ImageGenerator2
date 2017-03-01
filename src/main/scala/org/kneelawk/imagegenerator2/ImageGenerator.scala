package org.kneelawk.imagegenerator2

import java.awt.Graphics2D
import scala.collection.Map
import scala.annotation.Annotation
import java.awt.image.BufferedImage

trait ImageGenerator {
  def options: Seq[(String, String)]
  def name: String
  def apply(i: BufferedImage, options: Map[String, String], width: Int, height: Int)
}