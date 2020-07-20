package view.factories

import java.awt._
import java.awt.event.{MouseAdapter, MouseEvent}

import view.utils.{ResourceLoader, ScreenSize}
import javax.swing._
import javax.swing.border.LineBorder

/**
 * Representing a view game factory
 */
trait GameFactory {

  /**
   * Creates a new Center Cell.
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
   * Creates a new Board Panel.
   *
   * @return a new Board Panel.
   */
  def createBoardPanel: JPanel

  /**
   * Creates top or bottom panel with FlowLayout.
   *
   * @return top or bottom panel.
   */
  def createTopBottomPanel: JPanel

  /**
   * Creates right or left panel with GridLayout.
   *
   * @param columns
   *          number of columns.
   * @param rows
   *        number of rows.
   * @return right or left panel.
   */
  def createLeftRightPanel(columns: Int, rows: Int): JPanel

  /**
   * Creates a panel with OverlayLayout.
   *
   * @return a panel with OverlayLayout.
   */
  def createOverlayLayoutPanel: JPanel

  /**
   * Creates a panel with BorderLayout.
   *
   * @return a panel with BorderLayout.
   */
  def createGamePanel: JPanel

  /**
   * Creates a panel with BoxLayout, containing board, left and right panel.
   *
   * @return a panel with BoxLayout, containing board, left and right panel
   */
  def createBoardPlusColumnsPanel: JPanel

  /**
   * Creates a menu button.
   *
   * @return a menu button.
   */
  def createMenuButton(): JButton

  /**
   * Creates a go to first move button.
   *
   * @return a go to first move button
   */
  def createFirstMoveButton(): JButton

  /**
   * Creates a go to previous move button.
   *
   * @return a go to previous move button
   */
  def createPreviousMoveButton(): JButton

  /**
   * Creates a go to next move button.
   *
   * @return a go to next move button.
   */
  def createNextMoveButton(): JButton

  /**
   * Creates a go to last move button.
   *
   * @return a go to last move button
   */
  def createLastMoveButton(): JButton

  /**
    * Creates an undo move button.
    *
    * @return an undo move button
    */
  def createUndoMoveButton(): JButton

  /**
   * Creates a black pawn.
   *
   * @return a label representing a black pawn.
   */
  def createBlackPawn: JLabel

  /**
   * Creates a white pawn.
   *
   * @return a label representing a white pawn.
   */
  def createWhitePawn: JLabel

  /**
   * Creates a white king.
   *
   * @return a label representing a white king.
   */
  def createWhiteKing: JLabel

  /**
   * Creates a lost black pawn.
   *
   * @return a label representing a lost black pawn.
   */
  def createLostBlackPawn: JLabel

  /**
   * Creates a lost white pawn.
   *
   * @return a label representing a lost white pawn.
   */
  def createLostWhitePawn: JLabel

  /**
   * Creates a label for showing player to move and winner.
   *
   * @return a label for showing player to move and winner.
   */
  def createLabelPlayerToMoveWinner: JLabel

  /**
   * Sets the current board size to correctly define the dimension
   * of the cells.
   *
   * @param variantBoardSize
   *        variant board size
   */
  def setVariantBoardSize(variantBoardSize: Int)

  /**
    * Creates a game sub menu panel.
    *
    * @return a game sub menu panel.
    */
  def createGameSubMenuPanel: JPanel

  /**
    * Creates constraints for grid bag layouts.
    */
  def createBagConstraints: GridBagConstraints

}

/**
 * Representing a view game factory
 */
object GameFactory extends GameFactory {

  private val smallerSide: Int = ScreenSize.getSmallerSide
  private val font: Font = FontProvider.hnefataflFont
  private val ge: GraphicsEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment
  ge.registerFont(font)

  private var cellDimension = 0
  private val variantBoardSizeMultiplier: Double = 0.8
  private val menuButtonScale: Int = 7
  private val snapshotButtonScale: Int = 5
  private val basicPawnSizeMultiplier: Double = 1
  private val capturedPawnSizeMultiplier: Double = 0.5

  private val centerCellIconPath: String = "/images/iconThrone.png"
  private val cornerCellIconPath: String = "/images/iconCellWin.png"
  private val menuButtonPath: String = "/images/hamburgerMenu.png"
  private val menuButtonHoverPath: String = "/images/hamburgerMenuHover.png"
  private val menuButtonHoverText: String = "Menu"
  private val firstMoveButtonPath: String = "/images/iconFirstMove.png"
  private val firstMoveButtonHoverPath: String = "/images/iconFirstMoveHover.png"
  private val firstMoveButtonHoverText: String = "Show First Move"
  private val previousMoveButtonPath: String = "/images/iconPreviousMove.png"
  private val previousMoveButtonHoverPath: String = "/images/iconPreviousMoveHover.png"
  private val previousMoveButtonHoverText: String = "Show Previous Move"
  private val nextMoveButtonPath: String = "/images/iconNextMove.png"
  private val nextMoveButtonHoverPath: String = "/images/iconNextMoveHover.png"
  private val nextMoveButtonHoverText: String = "Show Next Move"
  private val lastMoveButtonPath: String = "/images/iconLastMove.png"
  private val lastMoveButtonHoverPath: String = "/images/iconLastMoveHover.png"
  private val lastMoveButtonHoverText: String = "Show Last Move"
  private val undoMoveButtonPath: String = "/images/iconUndoMove.png"
  private val undoMoveButtonHoverPath: String = "/images/iconUndoMoveHover.png"
  private val undoMoveButtonHoverText: String = "Go Back"

  /**
   * @inheritdoc
   */
  override def createCenterCell(): Cell = centerCell()

  /**
   * @inheritdoc
   */
  override def createCornerCell(): Cell = cornerCell()

  /**
   * @inheritdoc
   */
  override def createPawnCell(): Cell = pawnCell()

  /**
   * @inheritdoc
   */
  override def createNormalCell(): Cell = normalCell()

  /**
   * @inheritdoc
   */
  override def createBoardPanel: JPanel = new BoardPanel()

  /**
   * @inheritdoc
   */
  override def createTopBottomPanel: JPanel = new TopBottomPanel

  /**
   * @inheritdoc
   */
  override def createLeftRightPanel(columns: Int, rows: Int): JPanel = new LeftRightPanel(columns, rows)

  /**
   * @inheritdoc
   */
  override def createOverlayLayoutPanel: JPanel = new OverlayLayoutPanel

  /**
   * @inheritdoc
   */
  override def createGamePanel: JPanel = new GamePanel

  /**
   * @inheritdoc
   */
  override def createBoardPlusColumnsPanel: JPanel = new BoardPlusColumns

  /**
   * @inheritdoc
   */
  override def createWhitePawn: JLabel = whitePawn()

  /**
   * @inheritdoc
   */
  override def createBlackPawn: JLabel = blackPawn()

  /**
   * @inheritdoc
   */
  override def createMenuButton(): JButton = menuButton()

  /**
   * @inheritdoc
   */
  override def createFirstMoveButton(): JButton = firstMoveButton()

  /**
   * @inheritdoc
   */
  override def createPreviousMoveButton(): JButton = previousMoveButton()

  /**
   * @inheritdoc
   */
  override def createNextMoveButton(): JButton = nextMoveButton()

  /**
   * @inheritdoc
   */
  override def createLastMoveButton(): JButton = lastMoveButton()

  /**
   * @inheritdoc
   */
  override def createUndoMoveButton(): JButton = undoMoveButton()

  /**
   * @inheritdoc
   */
  override def createWhiteKing: JLabel = kingPawn()

  /**
   * @inheritdoc
   */
  override def createLostBlackPawn: JLabel = capturedBlackPawn()

  /**
   * @inheritdoc
   */
  override def createLostWhitePawn: JLabel = capturedWhitePawn()

  /**
   * @inheritdoc
   */
  override def createLabelPlayerToMoveWinner: JLabel = new LabelPlayer_Winner

  /**
   * @inheritdoc
   */
  override def setVariantBoardSize(variantBoardSize: Int): Unit =
    cellDimension = (smallerSide / variantBoardSize * variantBoardSizeMultiplier).toInt

  /**
   * @inheritdoc
   */
  override def createGameSubMenuPanel: JPanel = new GameSubMenuPanel

  /**
   * @inheritdoc
   */
  override def createBagConstraints: GridBagConstraints = new GridBagConstraints()

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

    private val IMG_DIMENSION = cellDimension * 70 / 100

    private var iconCell = new ImageIcon(ResourceLoader.loadImage(iconPath))
    private var image = iconCell.getImage

    image = image.getScaledInstance(IMG_DIMENSION, IMG_DIMENSION, Image.SCALE_SMOOTH)
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

  private class GameButton(private val iconPath: String, private val hoverIconPath: String, private val hoverText: String, private val scale: Int) extends JButton {
    private var imageIcon = new ImageIcon(ResourceLoader.loadImage(iconPath))
    private var hoverImageIcon = new ImageIcon(ResourceLoader.loadImage(hoverIconPath))

    private var image = imageIcon.getImage
    scaleImage()
    imageIcon = new ImageIcon(image)
    image = hoverImageIcon.getImage
    scaleImage()
    hoverImageIcon = new ImageIcon(image)
    setNormalIcon()
    setCursor(new Cursor(Cursor.HAND_CURSOR))
    setHorizontalAlignment(SwingConstants.CENTER)
    setToolTipText(hoverText)
    setBorderPainted(false)
    setOpaque(false)
    setContentAreaFilled(false)

    addMouseListener(new MouseAdapter() {
      override def mouseEntered(evt: MouseEvent): Unit = if(isEnabled) setHoverIcon()
      override def mouseExited(evt: MouseEvent): Unit = setNormalIcon()
    })

    private def scaleImage(): Unit = image = {
      val dimension = smallerSide * scale / 100
      image.getScaledInstance(dimension, dimension, Image.SCALE_SMOOTH)
    }

    private def setNormalIcon(): Unit = setIcon(imageIcon)

    private def setHoverIcon(): Unit = setIcon(hoverImageIcon)
  }

  private class SnapshotButton(private val iconPath: String, private val hoverIconPath: String, private val hoverText: String, private val scale: Int)
    extends GameButton(iconPath: String, hoverIconPath: String, hoverText: String, scale: Int) {
    setEnabled(false)
  }

  private def menuButton(): JButton =
    new GameButton(menuButtonPath, menuButtonHoverPath, menuButtonHoverText, menuButtonScale)

  private def firstMoveButton(): JButton =
    new SnapshotButton(firstMoveButtonPath, firstMoveButtonHoverPath, firstMoveButtonHoverText, snapshotButtonScale)

  private def previousMoveButton(): JButton =
    new SnapshotButton(previousMoveButtonPath, previousMoveButtonHoverPath, previousMoveButtonHoverText, snapshotButtonScale)

  private def nextMoveButton(): JButton =
    new SnapshotButton(nextMoveButtonPath, nextMoveButtonHoverPath, nextMoveButtonHoverText, snapshotButtonScale)

  private def lastMoveButton(): JButton =
    new SnapshotButton(lastMoveButtonPath, lastMoveButtonHoverPath, lastMoveButtonHoverText, snapshotButtonScale)

  private def undoMoveButton(): JButton =
    new SnapshotButton(undoMoveButtonPath, undoMoveButtonHoverPath, undoMoveButtonHoverText, snapshotButtonScale)

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

  private def whitePawn(): JLabel = new Pawn(ColorProvider.getWhiteColor, ColorProvider.getBlackColor, basicPawnSizeMultiplier)
  private def blackPawn(): JLabel = new Pawn(ColorProvider.getBlackColor, ColorProvider.getWhiteColor, basicPawnSizeMultiplier)
  private def kingPawn(): JLabel = new Pawn(ColorProvider.getWhiteColor, ColorProvider.getGoldColor, basicPawnSizeMultiplier)
  private def capturedWhitePawn(): JLabel = new Pawn(ColorProvider.getWhiteColor, ColorProvider.getBlackColor, capturedPawnSizeMultiplier)
  private def capturedBlackPawn(): JLabel = new Pawn(ColorProvider.getBlackColor, ColorProvider.getWhiteColor, capturedPawnSizeMultiplier)

  private class LabelPlayer_Winner extends JLabel {
    private val DIMENSION_FONT: Int = smallerSide * 3 / 100
    setFont(new Font(font.getFontName, Font.BOLD, DIMENSION_FONT))
    setForeground(ColorProvider.getPossibleMovesColor)
  }

}
