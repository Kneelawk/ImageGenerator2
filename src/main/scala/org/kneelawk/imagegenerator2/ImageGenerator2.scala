package org.kneelawk.imagegenerator2

import java.awt.image.BufferedImage
import java.io.File

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.io.StdIn

import org.kneelawk.imagegenerator2.reeds.ReedsGenerator
import javax.imageio.ImageIO
import scala.concurrent.Await
import scala.concurrent.duration.Duration
import scala.collection.mutable.HashMap

object ImageGenerator2 {
  def main(args: Array[String]) {
    val registeredGenerators = Map(
      gen(ReedsGenerator))

    while (true) {
      StdIn.readLine() match {
        case "generate" => {
          val name = StdIn.readLine("Image batch name: ")
          val genName = StdIn.readLine("Generator name: ")
          val number = StdIn.readLine("Number of images: ").toInt
          val width = StdIn.readLine("Width: ").toInt
          val height = StdIn.readLine("Height: ").toInt

          if (registeredGenerators.contains(genName)) {
            val gen = registeredGenerators(genName)

            val opts = new HashMap[String, String]
            for (opt <- gen.options) {
              val read = StdIn.readLine(opt._2 + ": ")
              opts += opt._1 -> (if ("" == read || null == read) opt._3 else read)
            }

            val outDir = new File("output", name)
            if (!outDir.exists()) outDir.mkdirs()

            var offset = 0
            while (new File(name, f"out$offset%04d.png").exists()) offset += 1

            val tasks = for (i <- 0 until number) yield Future {
              val image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB)
              val g = image.createGraphics()

              gen(g, opts, width, height)

              ImageIO.write(image, "png", new File(outDir, f"out${offset + i}%04d.png"))

              println(s"Finished $i")
            }

            println("All threads started.")

            Await.result(Future.sequence(tasks), Duration.Inf)

            println("All done.")
          } else {
            println(s"No generator with name: $genName")
          }
        }
        case "list" => {
          println("Generators:")
          registeredGenerators.keys.foreach { x => println(x) }
        }
        case "quit" => return
      }
    }
  }

  def gen(gen: ImageGenerator) = (gen.name, gen)
}