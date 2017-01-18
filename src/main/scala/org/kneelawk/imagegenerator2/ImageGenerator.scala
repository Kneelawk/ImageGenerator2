package org.kneelawk.imagegenerator2

import java.awt.Graphics2D
import scala.collection.Map
import scala.annotation.Annotation

trait ImageGenerator {
  def options: Seq[(String, String)]
  def name: String
  def apply(g: Graphics2D, options: Map[String, String], width: Int, height: Int)
}