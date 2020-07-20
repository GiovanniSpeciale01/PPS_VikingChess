package view

import java.awt._
import java.awt.event.{MouseAdapter, MouseEvent}

import javax.swing._
import javax.swing.border.LineBorder

trait GameFactory {

  /**
   * Gets the smaller side of the monitor.
   *
   * @return int size
   */
  def getSmallerSide: Int

  /**
   * Creates a new Center Cell.
   *
   * cell's dimension in percent.
   *
   * @return a new Center Cell.
   */

  def createCenterCell(): Cell

  /**
   * Creates a new Corner Cell.
   *
   * @return a new Corner Cell.
   */
  def createCornerCell(): Cell

  /**
   * Creates a new Pawn Cell.
   *
   * @return a new Pawn Cell.
   */
  def createPawnCell(): Cell

  /**
   * Creates a new Normal Cell.
   *
   * @return a new Normal Cell.
   */
  def createNormalCell(): Cell

  /**
   * Creates a new BoardPanel.
   *
   * @return a new BoardPanel.
   */
  def createBoardPanel: JPanel

  /**
   * Creates top or bottom panel with FlowLayout.
   *
   * @return top or bottom panel.
   *
   */
  def createTopBottomPanel: JPanel

  /**
   * Creates right or left panel with GridLayout.
   *
   * @param columns
   * number of columns.
   * @param rows
   * number of rows.
   * @return right or left panel.
   *
   */
  def createLeftRightPanel(columns: Int, rows: Int): JPanel

  /**
   * Creates a panel with OverlayLayout.
   *
   * @return a panel.
   */
  def createOverlayLayoutPanel: JPanel

  /**
   * Creates a panel with BorderLayout.
   *
   * @return a panel.
   */
  def createGamePanel: JPanel

  /**
   * Creates a panel with BoxLayout, containing board, left and right panel.
   *
   * @return a panel.
   */
  def createBoardPlusColumnsPanel: JPanel

  /**
   * Creates a game button.
   *
   * @return a button.
   */
  def createGameButton(): JButton

  /**
   * Creates a go to first move button.
   *
   * @return
   */
  def createFirstMoveButton(): JButton

  /**
   * Creates a go to previous move button.
   *
   * @return a button.
   */
  def createPreviousMoveButton(): JButton

  /**
   * Creates a go to next move button.
   *
   * @return a button.
   */
  def createNextMoveButton(): JButton

  /**
   * Creates a go to last move button.
   *
   * @return
   */
  def createLastMoveButton(): JButton

  /**
    * Creates a undo move button.
    *
    * @return a button.
    */
  def createUndoMoveButton(): JButton

  /**
   * Creates a pawn black.
   *
   * @return a label.
   */
  def createBlackPawn: JLabel

  /**
   * Creates a pawn white.
   *
   * @return a label.
   */
  def createWhitePawn: JLabel

  /**
   * Creates a king white.
   *
   * @return a label.
   */
  def createWhiteKing: JLabel

  /**
   * Creates a lost pawn black.
   *
   * @return a label.
   */
  def createLostBlackPawn: JLabel

  /**
   * Creates a lost pawn white.
   *
   * @return a label.
   */
  def createLostWhitePawn: JLabel

  /**
   * Creates a label to showing player to move and winner.
   *
   * @return a label.
   */
  def createLabelPlayerToMoveWinner: JLabel

  /**
   * Creates a frame.
   *
   * @return a frame.
   */
  def createFrame: JFrame

  /**
   * Sets the current board size to correctly define the dimension
   * of the cells.
   *
   * @param variantBoardSize
   *              variant board size
   */
  def setVariantBoardSize(variantBoardSize: Int): Unit

  /**
    * Creates a game sub menu panel.
    *
    * @return a panel.
    */
  def createGameSubMenuPanel: JPanel

  /**
    * Creates a constraints for grid bag layouts.
    */
  def createBagConstraints: GridBagConstraints

  /**
    * Resets a constraints for x grid parameter.
    */
  def resetXConstraints(limits: GridBagConstraints)

  /**
    * Sets a constraints for x and y grid parameter.
    */
  def setXYConstraints(limits: GridBagConstraints, x: Int, y: Int)

  /**
    * Increments a constraints for x grid parameter.
    */
  def incrementXConstraints(limits: GridBagConstraints)

  /**
    * Increments a constraints for x weight parameter.
    */
  def incrementWeightXConstraints(limits: GridBagConstraints): Unit
}

object GameFactory extends GameFactory {

  private var cellDimension = 0
  private val smallerSide: Int = ScreenSize.getSmallerSide * 9 / 10
  private val f: Font = ResourceLoader.loadFont("/font/NorseBold-2Kge.otf")
  private val ge: GraphicsEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment
  ge.registerFont(f)
  private val centerCellIconPath: String = "/images/iconThrone.png"
  private val cornerCellIconPath: String =  "/images/iconCellWin.png"

  override def getSmallerSide: Int = smallerSide

  override def createCenterCell(): Cell = centerCell()

  override def createCornerCell(): Cell = cornerCell()

  override def createPawnCell(): Cell = pawnCell()

  override def createNormalCell(): Cell = normalCell()

  override def createBoardPanel: JPanel = new BoardPanel()

  override def createTopBottomPanel: JPanel = new TopBottomPanel

  override def createLeftRightPanel(columns: Int, rows: Int): JPanel = new LeftRightPanel(columns, rows)

  override def createOverlayLayoutPanel: JPanel = new OverlayLayoutPanel

  override def createGamePanel: JPanel = new GamePanel

  override def createBoardPlusColumnsPanel: JPanel = new BoardPlusColumns

  override def createWhitePawn: JLabel = whitePawn()

  override def createBlackPawn: JLabel = blackPawn()

  override def createGameButton(): JButton = new GameButton()

  override def createFirstMoveButton(): JButton = firstMoveButton()

  override def createPreviousMoveButton(): JButton = previousMoveButton()

  override def createNextMoveButton(): JButton = nextMoveButton()

  override def createLastMoveButton(): JButton = lastMoveButton()

  override def createUndoMoveButton(): JButton = undoMoveButton()

  override def createWhiteKing: JLabel = kingPawn()

  override def createLostBlackPawn: JLabel = capturedBlackPawn()

  override def createLostWhitePawn: JLabel = capturedWhitePawn()

  override def createLabelPlayerToMoveWinner: JLabel = new LabelPlayer_Winner

  override def createFrame: JFrame = new Frame

  override def setVariantBoardSize(variantBoardSize: Int): Unit = cellDimension = smallerSide / variantBoardSize * 80 / 100

  override def createGameSubMenuPanel: JPanel = new GameSubMenuPanel

  override def createBagConstraints: GridBagConstraints = new GridBagConstraints()

  override def resetXConstraints(limits: GridBagConstraints): Unit = limits.gridx = 0

  override def setXYConstraints(limits: GridBagConstraints, x: Int, y: Int): Unit = {limits.gridx = x; limits.gridy = y}

  override def incrementXConstraints(limits: GridBagConstraints): Unit = limits.gridx += 1

  override def incrementWeightXConstraints(limits: GridBagConstraints): Unit = limits.weightx += 1

  private class BasicCell(private var defaultBackground: Color) extends Cell {
    private var isAPossibleMove: Boolean = false
    private var isLastMove: Boolean = false
    private var isKingEscapedCell: Boolean = false
    private var isSelectedCell: Boolean = false
    private var isKingCapturedCell: Boolean = false

    setPreferredSize(new Dimension(cellDimension, cellDimension))
    setAlignmentX(Component.CENTER_ALIGNMENT)
    setAlignmentY(Component.CENTER_ALIGNMENT)
    setLayout(new GridLayout())
    setBorder(new LineBorder(Color.BLACK))
    setCursor(new Cursor(Cursor.HAND_CURSOR))
    setOpaque(true)
    resetBackground()
    addMouseListener(new MouseAdapter() {
      override def mouseEntered(e: MouseEvent): Unit =
        setBackground(ColorProvider.getHighlightColor)

      override def mouseExited(e: MouseEvent): Unit = {
        if(isAPossibleMove)
          setPossibleMoveColor()
        else if(isLastMove && !isKingEscapedCell)
          setLastMoveColor()
        else if(isSelectedCell)
          setSelectedCellColor()
        else if(isKingEscapedCell)
          setKingEscapedColor()
        else if(isKingCapturedCell)
          setKingCapturedColor()
        else
          resetBackground()
      }
    })

    override def setAsPossibleMove(): Unit = {
      isAPossibleMove = true
      setPossibleMoveColor()
    }

    override def unsetAsPossibleMove(): Unit = {
      isAPossibleMove = false
      resetBackground()
    }

    override def setAsKingCapturedCell(): Unit = {
      isKingCapturedCell = true
      setKingCapturedColor()
    }

    override def resetKingCell(): Unit = {
      isKingCapturedCell = false
      isKingEscapedCell = false
      resetBackground()
    }

    override def setAsKingEscapedCell(): Unit = {
      isKingEscapedCell = true
      setKingEscapedColor()
    }

    override def setAsLastMoveCell(): Unit = {
      isLastMove = true
      if(!isKingEscapedCell)
        setLastMoveColor()
    }

    override def unsetAsLastMoveCell(): Unit = {
      isLastMove = false
      resetBackground()
    }

    override def setAsSelectedCell(): Unit = {
      isSelectedCell = true
      setSelectedCellColor()
    }

    override def unsetAsSelectedCell(): Unit = {
      isSelectedCell = false
      resetBackground()
    }

    protected def setPossibleMoveColor(): Unit = setBackground(ColorProvider.getPossibleMovesColor)

    protected def setLastMoveColor(): Unit = setBackground(ColorProvider.getLastMoveColor)

    protected def setSelectedCellColor(): Unit = setBackground(ColorProvider.getSelectedCellColor)

    private def setKingEscapedColor(): Unit = setBackground(ColorProvider.getWhiteWinColor)

    private def setKingCapturedColor(): Unit = setBackground(ColorProvider.getBlackWinColor)

    private def resetBackground(): Unit = setBackground(defaultBackground)
  }

  private class IconCell(private var defaultColor: Color, private var iconPath: String) extends BasicCell(defaultColor) {
    private var iconCell = new ImageIcon(ResourceLoader.loadImage(iconPath))
    private var image = iconCell.getImage

    image = image.getScaledInstance(cellDimension * 70 / 100, cellDimension * 70 / 100, Image.SCALE_SMOOTH)
    iconCell = new ImageIcon(image)
    setIcon(iconCell)

    override def setPossibleMoveColor(): Unit = setBackground(ColorProvider.getSpecialCellPossibleMovesColor)

    override def setLastMoveColor(): Unit = setBackground(ColorProvider.getSpecialCellLastMoveColor)

    override def setSelectedCellColor(): Unit = setBackground(ColorProvider.getSpecialCellSelectedCellColor)

  }

  private def pawnCell(): Cell = new BasicCell(ColorProvider.getPawnCellColor)
  private def normalCell(): Cell = new BasicCell(ColorProvider.getNormalCellColor)
  private def centerCell(): Cell = new IconCell(ColorProvider.getSpecialCellColor, centerCellIconPath)
  private def cornerCell(): Cell = new IconCell(ColorProvider.getSpecialCellColor, cornerCellIconPath)

  private class BoardPanel extends JPanel {
    setLayout(new java.awt.GridBagLayout())
    this.setBackground(ColorProvider.getLightBrownColor)
  }

  private class TopBottomPanel extends JPanel {
    private val WIDTH_DIMENSION = smallerSide
    private val HEIGHT_DIMENSION = smallerSide * 8 / 100
    setLayout(new BoxLayout(this, BoxLayout.X_AXIS))
    setBackground(ColorProvider.getLightBrownColor)
    setPreferredSize(new Dimension(WIDTH_DIMENSION, HEIGHT_DIMENSION))
  }

  private class LeftRightPanel(val columns: Int, val rows: Int) extends JPanel {
    private val WIDTH_DIMENSION = smallerSide * 10 / 100
    private val HEIGHT_DIMENSION = smallerSide * 80 / 100
    private val colorSide = ColorProvider.getLightBrownColor
    setLayout(new GridLayout(rows, columns))
    setBackground(colorSide)
    setPreferredSize(new Dimension(WIDTH_DIMENSION, HEIGHT_DIMENSION))
  }

  private class OverlayLayoutPanel extends JPanel {
    setLayout(new OverlayLayout(this))
    setVisible(true)
  }

  private class GamePanel extends JPanel {
    setBackground(ColorProvider.getLightBrownColor)
  }

  private class BoardPlusColumns extends JPanel {
    setLayout(new BoxLayout(this, BoxLayout.X_AXIS))
  }

  private class GameSubMenuPanel extends JPanel {
    private val PANEL_DIMENSION = new Dimension(smallerSide * 45 / 100, smallerSide * 8 / 100)

    setPreferredSize(PANEL_DIMENSION)
    setMaximumSize(getPreferredSize)
    setOpaque(false)
    setVisible(true)

    val gridBagLayout: GridBagLayout = new java.awt.GridBagLayout()
    this.setLayout(gridBagLayout)
  }

  private class GameButton() extends JButton {
    private var imageIcon = new ImageIcon(ResourceLoader.loadImage("/images/hamburgerMenu.png"))
    private var image = imageIcon.getImage
    image = image.getScaledInstance(smallerSide * 7 / 100, smallerSide * 7 / 100, Image.SCALE_SMOOTH)
    imageIcon = new ImageIcon(image)
    setIcon(imageIcon)
    setCursor(new Cursor(Cursor.HAND_CURSOR))
    setHorizontalAlignment(SwingConstants.CENTER)
    setToolTipText("Game Menu")
    setBorderPainted(false)
    setOpaque(false)
    setContentAreaFilled(false)
  }

  private class SnapshotButton(private val iconPath: String, private val hoverText: String) extends JButton {
    private var imageIcon = new ImageIcon(ResourceLoader.loadImage(iconPath))
    private var image = imageIcon.getImage
    image = image.getScaledInstance(smallerSide * 5 / 100, smallerSide * 5 / 100, Image.SCALE_SMOOTH)
    imageIcon = new ImageIcon(image)
    setIcon(imageIcon)
    setCursor(new Cursor(Cursor.HAND_CURSOR))
    setHorizontalAlignment(SwingConstants.CENTER)
    setEnabled(false)
    setToolTipText(hoverText)
    setBorderPainted(false)
    setOpaque(false)
    setContentAreaFilled(false)
  }

  private val firstMoveButtonPath: String = "/images/iconFirstMove.png"
  private val firstMoveButtonHoverText: String = "Show First Move"
  private def firstMoveButton(): JButton = new SnapshotButton(firstMoveButtonPath, firstMoveButtonHoverText)

  private val previousMoveButtonPath: String = "/images/iconPreviousMove.png"
  private val previousMoveButtonHoverText: String = "Show Previous Move"
  private def previousMoveButton(): JButton = new SnapshotButton(previousMoveButtonPath, previousMoveButtonHoverText)

  private val nextMoveButtonPath: String = "/images/iconNextMove.png"
  private val nextMoveButtonHoverText: String = "Show Next Move"
  private def nextMoveButton(): JButton = new SnapshotButton(nextMoveButtonPath, nextMoveButtonHoverText)

  private val lastMoveButtonPath: String = "/images/iconLastMove.png"
  private val lastMoveButtonHoverText: String = "Show Last Move"
  private def lastMoveButton(): JButton = new SnapshotButton(lastMoveButtonPath, lastMoveButtonHoverText)

  private val undoMoveButtonPath: String = "/images/iconUndoMove.png"
  private val undoMoveButtonHoverText: String = "Turn Back"
  private def undoMoveButton(): JButton = new SnapshotButton(undoMoveButtonPath, undoMoveButtonHoverText)

  private class Pawn(private val internalColor: Color, private val externalColor: Color,
              private val sizeMultiplier: Double) extends JLabel {
    private val EXTERNAL_ROUNDRECT_MULTIPLIER: Int = (cellDimension * 8 / 10 * sizeMultiplier).toInt
    private val INTERNAL_ROUNDRECT_MULTIPLIER: Int = (cellDimension * 7 / 10 * sizeMultiplier).toInt
    private val ARC_DIMENSION: Int = 10

    setOpaque(false)
    setVisible(true)

    override def paintComponent(g: Graphics): Unit = {
      super.paintComponent(g)
      val X_CENTRE = getWidth / 2
      val Y_CENTRE = getHeight / 2
      val radius1 = EXTERNAL_ROUNDRECT_MULTIPLIER * Math.sqrt(2).toInt / 2
      val radius2 = INTERNAL_ROUNDRECT_MULTIPLIER * Math.sqrt(2).toInt / 2
      g.setColor(externalColor)
      g.fillRoundRect(X_CENTRE - radius1, Y_CENTRE - radius1, EXTERNAL_ROUNDRECT_MULTIPLIER, EXTERNAL_ROUNDRECT_MULTIPLIER, ARC_DIMENSION, ARC_DIMENSION)
      g.setColor(internalColor)
      g.fillRoundRect(X_CENTRE - radius2, Y_CENTRE - radius2, INTERNAL_ROUNDRECT_MULTIPLIER, INTERNAL_ROUNDRECT_MULTIPLIER, ARC_DIMENSION, ARC_DIMENSION)
    }
  }

  private val basicPawnSizeMultiplier: Double = 1
  private val capturedPawnSizeMultiplier: Double = 0.5
  private def whitePawn(): JLabel = new Pawn(ColorProvider.getWhiteColor, ColorProvider.getBlackColor, basicPawnSizeMultiplier)
  private def blackPawn(): JLabel = new Pawn(ColorProvider.getBlackColor, ColorProvider.getWhiteColor, basicPawnSizeMultiplier)
  private def kingPawn(): JLabel = new Pawn(ColorProvider.getWhiteColor, ColorProvider.getGoldColor, basicPawnSizeMultiplier)
  private def capturedWhitePawn(): JLabel = new Pawn(ColorProvider.getWhiteColor, ColorProvider.getBlackColor, capturedPawnSizeMultiplier)
  private def capturedBlackPawn(): JLabel = new Pawn(ColorProvider.getBlackColor, ColorProvider.getWhiteColor, capturedPawnSizeMultiplier)

  private class LabelPlayer_Winner extends JLabel {
    private val DIMENSION_FONT: Int = smallerSide * 3 / 100
    setFont(new Font(f.getFontName, Font.BOLD, DIMENSION_FONT))
    setForeground(ColorProvider.getPossibleMovesColor)
  }

  private class Frame extends JFrame {
    private val FRAME_TITLE = "Viking Chess - Hnefatafl"
    private val iconApp = new ImageIcon(ResourceLoader.loadImage("/images/iconApp.png"))

    setTitle(FRAME_TITLE)
    setIconImage(iconApp.getImage)
    setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE)
    setResizable(false)
    setSize(smallerSide, smallerSide)
    setLocationRelativeTo(null)
  }
}
