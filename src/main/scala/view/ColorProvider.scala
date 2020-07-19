package view

import java.awt.Color

trait ColorProvider {

  def getBlackColor: Color

  def getHighlightColor: Color

  def getSpecialCellColor: Color

  def getPawnCellColor: Color

  def getNormalCellColor: Color

  def getPossibleMovesColor: Color

  def getLightBrownColor: Color

  def getBrownColor: Color

  def getWhiteColor: Color

  def getGoldColor: Color

  def getBlackWinColor: Color

  def getWhiteWinColor: Color

}

object ColorProvider {

  def apply: ColorProvider = ColorProviderImpl()

  case class ColorProviderImpl() extends ColorProvider {

    override def getBlackColor: Color = Color.BLACK

    override def getHighlightColor: Color = Color.LIGHT_GRAY

    override def getSpecialCellColor: Color = new Color(46, 50, 100)

    override def getPawnCellColor: Color = new Color(153, 203, 205)

    override def getNormalCellColor: Color = new Color(83, 143, 159)

    override def getPossibleMovesColor: Color = new Color(41, 71, 79)

    override def getLightBrownColor: Color = new Color(200, 170, 109)

    override def getBrownColor: Color = new Color(114, 73, 51)

    override def getWhiteColor: Color = new Color(255, 250, 240)

    override def getGoldColor: Color = new Color(212, 175, 55)

    override def getBlackWinColor: Color = new Color(203, 50, 52)

    override def getWhiteWinColor: Color = new Color(119, 221, 119)
  }
}
