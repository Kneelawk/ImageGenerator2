package org.kneelawk.imagegenerator2

import java.awt.image.BufferedImage
import java.io.File

import scala.collection.mutable.HashMap
import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.concurrent.duration.Duration
import scala.io.StdIn

import org.kneelawk.imagegenerator2.driftingyarn.DriftingYarn
import org.kneelawk.imagegenerator2.glowgrid.GlowGrid
import org.kneelawk.imagegenerator2.glowgrid2.GlowGrid2
import org.kneelawk.imagegenerator2.matrix.MatrixGenerator
import org.kneelawk.imagegenerator2.reeds.ReedsGenerator

import javax.imageio.ImageIO

object ImageGenerator2 {
  def main(args: Array[String]) {
    val registeredGenerators = Map(
      gen(ReedsGenerator),
      gen(MatrixGenerator),
      gen(GlowGrid),
      gen(GlowGrid2),
      gen(DriftingYarn))

    while (true) {
      val command = StdIn.readLine()
      command match {
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
              opts += opt._1 -> StdIn.readLine(opt._2 + ": ")
            }

            val outDir = new File("output", name)
            if (!outDir.exists()) outDir.mkdirs()

            var offset = 0
            while (new File(name, f"out$offset%04d.png").exists()) offset += 1

            val tasks = for (i <- 0 until number) yield Future {
              val image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB)

              gen(image, opts, width, height)

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
        case _ => {
          println(s"Unknown command: $command")
          println("Commands:")
          println("generate - generates an image")
          println("list - lists the generators")
          println("quit - quits the program")
        }
      }
    }
  }

  def gen(gen: ImageGenerator) = (gen.name, gen)
}