package view

import java.awt.Font
import java.awt.image.BufferedImage
import java.io.InputStream

import javax.imageio.ImageIO

object ResourceLoader {

  def loadImage(imagePath: String): BufferedImage = ImageIO.read(loadResource(imagePath))

  def loadFont(fontPath: String): Font = Font.createFont(Font.TRUETYPE_FONT, loadResource(fontPath))

  private def loadResource(resourcePath: String): InputStream = getClass.getResourceAsStream(resourcePath)

}
