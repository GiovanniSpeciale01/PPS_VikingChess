package view.factories

import java.awt.Color

/**
 * Object providing view colors.
 */
object ColorProvider {

    def getBlackColor: Color = Color.BLACK

    def getHighlightColor: Color = Color.LIGHT_GRAY

    def getSpecialCellColor: Color = new Color(46, 50, 100)

    def getPawnCellColor: Color = new Color(153, 203, 205)

    def getNormalCellColor: Color = new Color(83, 143, 159)

    def getPossibleMovesColor: Color =  new Color(52, 89, 99)

    def getSpecialCellPossibleMovesColor: Color = new Color(34, 57, 64)

    def getLightBrownColor: Color = Color.decode("#c29b56")

    def getBrownColor: Color = new Color(114, 73, 51)

    def getWhiteColor: Color = new Color(255, 250, 240)

    def getGoldColor: Color = new Color(212, 175, 55)

    def getBlackWinColor: Color = Color.decode("#953e3e")

    def getWhiteWinColor: Color = Color.decode("#5a7a34")

    def getLastMoveColor: Color = Color.decode("#cdd26a")

    def getSpecialCellLastMoveColor: Color = Color.decode("#597502")

    def getSelectedCellColor: Color = Color.decode("#829769")

    def getSpecialCellSelectedCellColor: Color = Color.decode("#646f40")

    def menuButtonHoverColor: Color = Color.decode("#a94413")

}
