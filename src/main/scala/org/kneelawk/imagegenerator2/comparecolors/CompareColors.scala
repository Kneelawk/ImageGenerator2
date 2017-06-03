package org.kneelawk.imagegenerator2.compareColors

import java.awt.image.BufferedImage

import scala.collection.Map

import org.kneelawk.imagegenerator2.ImageGenerator
import org.kneelawk.imagegenerator2.util.MathUtil
import javax.imageio.ImageIO
import java.io.File

object CompareColors extends ImageGenerator {
  def name: String = "CompareColors"

  def options: Seq[(String, String)] = Array(
    ("path", "Path to starting image"))

  def apply(i: BufferedImage, options: Map[String, String], width: Int, height: Int): Unit = {
    import MathUtil._
    val input = ImageIO.read(new File(options("path")))

    if (input.getWidth != width || input.getHeight != height) throw new IllegalArgumentException("Input and Output image sizes must mach")
    
    for (y <- 0 until height; x <- 0 until width) {
      val r = input.getRGB(mod2(x + 1, 0, width), y)
      val l = input.getRGB(mod2(x - 1, 0, width), y)
      val d = input.getRGB(x, mod2(y + 1, 0, height))
      val u = input.getRGB(x, mod2(y - 1, 0, height))
      val c = input.getRGB(x, y)
      
      if (r != c || l != c || d != c || u != c) {
        i.setRGB(x, y, 0xFFFFFFFF)
      } else {
        i.setRGB(x, y, 0x00000000)
      }
    }
  }
}