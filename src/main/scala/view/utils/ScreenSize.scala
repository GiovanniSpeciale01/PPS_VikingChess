package view.utils

import java.awt.{Dimension, Toolkit}

/**
 * An object for providing informations about screen size.
 */
object ScreenSize {
  private val screen: Dimension = Toolkit.getDefaultToolkit.getScreenSize

  /**
    * Returns the size of the smaller side of the screen.
    *
    * @return the size of the smaller side of the screen.
    */
  def getSmallerSide: Int = screen match {
    case _ if screen.getHeight < screen.getWidth => screen.getHeight * 9 / 10 toInt
    case _ if screen.getHeight > screen.getWidth => screen.getWidth * 9 / 10 toInt
  }
}
