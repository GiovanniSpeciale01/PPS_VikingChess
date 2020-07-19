package view

import java.awt._
import java.awt.event.{MouseAdapter, MouseEvent}
import java.io.File

import javax.imageio.ImageIO
import javax.swing._
import javax.swing.border.LineBorder
import view.ColorProvider.ColorProviderImpl


/**
@author Marco Celli
@author Pasquale Leo
 */

trait ViewFactory {

  /**
    * Gets the smaller side of the monitor.
    *
    * @return int size
    */
  def getSmallerSide: Int

  /**
   * Creates a new Center Cell.
   *
   * @param dimension
   *                cell's dimension in percent.
   *
   * @return a new Center Cell.
   */

  def createCenterCell(dimension: Int): JButton

  /**
   * Creates a new Corner Cell.
   *
   * @param dimension
   *           dimension of the cell.
   *
   * @return a new Corner Cell.
   */
  def createCornerCell(dimension: Int): JButton

  /**
   * Creates a new Pawn Cell.
   *
   * @param dimension
   *            dimension of the cell.
   *
   * @return a new Pawn Cell.
   */
  def createPawnCell(dimension: Int): JButton

  /**
   * Creates a new Normal Cell.
   *
   * @param dimension
   *             dimension of the cell.
   *
   * @return a new Normal Cell.
   */
  def createNormalCell(dimension: Int): JButton

  /**
   * Creates a new BoardPanel.
   *
   * @return a new BoardPanel.
   */
  def createBoardPanel: JPanel

  /**
   * Creates a new MenuPanel.
   *
   * @param string
   *             text of the menù panel.
   *
   * @return a new MenuPanel.
   */
  def createMenuPanel(string: String): JPanel

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
    *                number of columns.
    * @param rows
    *                number of rows.
    *
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
   * Creates a menù button.
   *
   * @param string
   *               name of button.
   *
   * @return a button.
   */
  def createMenuButton(string: String): JButton

  /**
    * Creates a popup menù.
    *
    * @return a popup.
    */
  def createPopUpMenu: JPopupMenu

  /**
    * Creates an item for popup menù.
    *
    * @param text
    *               text of the item.
    *
    * @return an item.
    */
  def createJMenuItem(text: String): JMenuItem

  /**
   * Creates a game button.
   * @param string
   *                name of button.
   *
   * @return a button.
   */
  def createGameButton(string: String): JButton

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
  def createLostBlackPawn : JLabel

  /**
    * Creates a lost pawn white.
    *
    * @return a label.
    */
  def createLostWhitePawn : JLabel

  /**
    * Creates a label to showing player to move and winner.
    *
    * @return a label.
    */
  def createLabelPlayerToMoveWinner : JLabel

  /**
   * Creates a frame.
   *
   * @return a frame.
   */
  def createFrame: JFrame
}

object ViewFactory {

  def apply: ViewFactory = ViewFactoryImpl()

  case class ViewFactoryImpl() extends ViewFactory {

    private val colorProvider = new ColorProviderImpl
    private var cellDimension = 0
    var smallerSide: Int = ScreenSize.getSmallerSide * 9 / 10
    val f: Font = ResourceLoader.loadFont("/font/NorseBold-2Kge.otf")
    val ge: GraphicsEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment
    ge.registerFont(f)

    override def getSmallerSide: Int = smallerSide

    override def createCenterCell(dimension: Int): JButton = new CenterCell(dimension)

    override def createCornerCell(dimension: Int): JButton = new CornerCell(dimension)

    override def createPawnCell(dimension: Int): JButton = new PawnCell(dimension)

    override def createNormalCell(dimension: Int): JButton = new NormalCell(dimension)

    override def createBoardPanel: JPanel = new BoardPanel()

    override def createMenuPanel(string: String): JPanel = new MenuPanel(string)

    override def createTopBottomPanel: JPanel = new TopBottomPanel

    override def createLeftRightPanel(columns: Int, rows: Int): JPanel = new LeftRightPanel(columns, rows)

    override def createOverlayLayoutPanel: JPanel = new OverlayLayoutPanel

    override def createGamePanel: JPanel = new GamePanel

    override def createBoardPlusColumnsPanel: JPanel = new BoardPlusColumns

    override def createMenuButton(s: String): JButton = new MenuButton(s)

    override def createPopUpMenu: JPopupMenu = new JPopupMenu

    override def createJMenuItem(text: String): JMenuItem = new MenuItem(text)

    override def createGameButton(s: String): JButton = new GameButton(s)

    override def createWhitePawn: JLabel = new WhitePawn

    override def createBlackPawn: JLabel = new BlackPawn

    override def createWhiteKing: JLabel = new KingPawn

    override def createLostBlackPawn : JLabel = new LostBlackPawn

    override def createLostWhitePawn : JLabel = new LostWhitePawn

    override def createLabelPlayerToMoveWinner: JLabel = new LabelPlayer_Winner

    override def createFrame: JFrame = new Frame

    abstract private class Cell(dimension: Int) extends JButton {
      cellDimension = smallerSide / dimension * 80 / 100

      var colorCell: Color = _

      var color: Color = _

      setPreferredSize(new Dimension(cellDimension, cellDimension))
      setAlignmentX(Component.CENTER_ALIGNMENT)
      setAlignmentY(Component.CENTER_ALIGNMENT)
      setLayout(new GridLayout())
      setBorder(new LineBorder(Color.BLACK))
      setCursor(new Cursor(Cursor.HAND_CURSOR))
      setOpaque(true)
      addMouseListener(new MouseAdapter() {
        override def mouseEntered(e: MouseEvent): Unit = {
          if( !getBackground.equals(colorProvider.getWhiteWinColor) && !getBackground.equals(colorProvider.getBlackWinColor)){
            color = getBackground
            setBackground(Color.LIGHT_GRAY)
          }
        }
        override def mouseExited(e: MouseEvent): Unit = {
          if(!getBackground.equals(colorProvider.getWhiteWinColor) && !getBackground.equals(colorProvider.getBlackWinColor)){
              if (getComponents.length > 0) {
                setBackground(colorCell)
              }
              else {
                setBackground(color)
              }
          }
        }
      })
    }

    private class SpecialCell(dimension: Int) extends Cell(dimension) {

      private val colorCellWin = colorProvider.getSpecialCellColor
      colorCell = colorCellWin
      setBackground(colorCellWin)

    }

    private class CenterCell(dimension: Int) extends SpecialCell(dimension){

      private var iconCell = new ImageIcon(ResourceLoader.loadImage("/images/iconThrone.png"))
      private var image = iconCell.getImage

      image = image.getScaledInstance(cellDimension * 70/100, cellDimension * 70/100, Image.SCALE_SMOOTH)

      iconCell = new ImageIcon(image)

      setIcon(iconCell)

    }

    private class CornerCell(dimension: Int) extends SpecialCell(dimension){

      private var iconCell = new ImageIcon(ResourceLoader.loadImage("/images/iconCellWin.png"))
      private var image = iconCell.getImage

      image = image.getScaledInstance(cellDimension * 70/100, cellDimension * 70/100, Image.SCALE_SMOOTH)

      iconCell = new ImageIcon(image)

      setIcon(iconCell)

    }

    private class PawnCell(dimension: Int) extends Cell(dimension) {
      private val colorCellPawn = colorProvider.getPawnCellColor
      colorCell = colorCellPawn
      setBackground(colorCellPawn)
    }

    private class NormalCell(dimension: Int) extends Cell(dimension) {
      private val colorNormalCell = colorProvider.getNormalCellColor
      colorCell = colorNormalCell
      setBackground(colorNormalCell)
    }

    private class BoardPanel extends JPanel {
      this.setBackground(colorProvider.getLightBrownColor)
    }

    private class MenuPanel(string: String) extends JPanel {

      private val menuLabel = new JLabel()

      private val chooseLabel = new JLabel()

      private val image = ResourceLoader.loadImage("/images/Cornice.png")

      private val imageScaled = image.getScaledInstance(smallerSide, smallerSide * 98/100, Image.SCALE_DEFAULT)

      private val img = new ImageIcon(ResourceLoader.loadImage("/images/logo.png"))

      setLayout(new BoxLayout(this, BoxLayout.Y_AXIS))
      menuLabel.setPreferredSize(new Dimension(smallerSide, smallerSide * 25/ 100))
      menuLabel.setAlignmentX(Component.CENTER_ALIGNMENT)
      menuLabel.setIcon(img)
      chooseLabel.setPreferredSize(new Dimension(smallerSide, smallerSide * 6/ 100))
      chooseLabel.setAlignmentX(Component.CENTER_ALIGNMENT)
      chooseLabel.setFont(new Font(f.getFontName, Font.BOLD, smallerSide * 6/100))
      chooseLabel.setText(string)
      chooseLabel.setForeground(colorProvider.getWhiteColor)
      add(Box.createRigidArea(new Dimension(smallerSide, smallerSide * 5/100)))
      add(menuLabel)
      add(Box.createRigidArea(new Dimension(smallerSide, smallerSide * 1/100)))
      add(chooseLabel)
      add(Box.createRigidArea(new Dimension(smallerSide, smallerSide * 1/100)))



      override protected def paintComponent(g: Graphics): Unit = {
        super.paintComponent(g)
        g.drawImage(imageScaled, 0, 0, null)
      }
    }

    private class TopBottomPanel extends JPanel {
      private val WIDTH_DIMENSION = smallerSide
      private val HEIGHT_DIMENSION = smallerSide * 8 / 100

      setBackground(colorProvider.getLightBrownColor)
      setPreferredSize(new Dimension(WIDTH_DIMENSION, HEIGHT_DIMENSION))
      setAlignmentX(Component.RIGHT_ALIGNMENT)

    }

    private class LeftRightPanel(val columns: Int, val rows: Int) extends JPanel {
      private val WIDTH_DIMENSION = smallerSide * 10 / 100
      private val HEIGHT_DIMENSION = smallerSide * 80 / 100
      private val colorSide = colorProvider.getLightBrownColor
      setLayout(new GridLayout(rows, columns))
      setBackground(colorSide)
      setPreferredSize(new Dimension(WIDTH_DIMENSION, HEIGHT_DIMENSION))
    }

    private class OverlayLayoutPanel extends JPanel {
      setLayout(new OverlayLayout(this))
      setVisible(true)
    }

    private class GamePanel extends JPanel {
      setBackground(colorProvider.getLightBrownColor)
    }

    private class BoardPlusColumns extends JPanel {
      setLayout(new BoxLayout(this, BoxLayout.X_AXIS))
    }

    abstract private class EmptyButton(val s: String) extends JButton {
      setCursor(new Cursor(Cursor.HAND_CURSOR))
      setHorizontalAlignment(SwingConstants.CENTER)
      setText(s)
    }

    private class MenuButton(s: String) extends EmptyButton(s) {

      private val FONT_DIMENSION = smallerSide * 5 / 100
      private val BUTTON_DIMENSION = new Dimension(smallerSide * 60/100, smallerSide * 8/100)

      setPreferredSize(BUTTON_DIMENSION)
      setMaximumSize(getPreferredSize)
      setAlignmentX(Component.CENTER_ALIGNMENT)

      setOpaque(false)
      setContentAreaFilled(false)
      setBorderPainted(false)

      setFont(new Font(f.getFontName, Font.BOLD, FONT_DIMENSION))
      setForeground(colorProvider.getWhiteColor)
      setBorderPainted(false)

      addMouseListener(new MouseAdapter() {
        override def mouseEntered(e: MouseEvent): Unit = {
          setForeground(colorProvider.getNormalCellColor)
        }
        override def mouseExited(e: MouseEvent): Unit = {
          setForeground(colorProvider.getWhiteColor)
        }
      })
    }

    private class GameButton(s: String) extends EmptyButton(s) {
      private var imageIcon = new ImageIcon(ResourceLoader.loadImage("/images/hamburgerMenu.png"))
      private var image = imageIcon.getImage
      image = image.getScaledInstance(smallerSide * 7/ 100, smallerSide * 7/100, Image.SCALE_SMOOTH)
      imageIcon = new ImageIcon(image)
      setIcon(imageIcon)
      setBorderPainted(false)
      setOpaque(false)
      setContentAreaFilled(false)

    }

    private class PopUpMenu extends JPopupMenu {

      private val FONT_DIMENSION = smallerSide * 3 / 100
      setCursor(new Cursor(Cursor.HAND_CURSOR))
      setFont(new Font(f.getFontName, Font.PLAIN, FONT_DIMENSION))

    }

    private class MenuItem(text: String) extends JMenuItem {

      private val colorSide = colorProvider.getBrownColor
      private val FONT_DIMENSION = smallerSide * 3 / 100

      setText(text)
      setFont(new Font(f.getFontName, Font.PLAIN, FONT_DIMENSION))
      setForeground(Color.WHITE)
      setBackground(colorSide)
      setOpaque(true)

    }

    abstract private class Pawn extends JLabel {

      protected var externalColor: Color = _
      protected var internalColor: Color = _
      protected var namePawn: String = _

      protected var EXTERNAL_ROUNDRECT_DIMENSION: Int= cellDimension * 8 / 10
      protected var INTERNAL_ROUNDRECT_DIMENSION: Int = cellDimension * 7 / 10

      setOpaque(false)
      setVisible(true)


      override def paintComponent(g: Graphics): Unit = {
        super.paintComponent(g)
        val X_CENTRE = getWidth / 2
        val Y_CENTRE = getHeight / 2
        val radius1 = EXTERNAL_ROUNDRECT_DIMENSION * Math.sqrt(2).toInt / 2
        val radius2 = INTERNAL_ROUNDRECT_DIMENSION * Math.sqrt(2).toInt / 2
        g.setColor(externalColor)
        g.fillRoundRect(X_CENTRE - radius1, Y_CENTRE - radius1, EXTERNAL_ROUNDRECT_DIMENSION, EXTERNAL_ROUNDRECT_DIMENSION, 10, 10)
        g.setColor(internalColor)
        g.fillRoundRect(X_CENTRE - radius2, Y_CENTRE - radius2, INTERNAL_ROUNDRECT_DIMENSION, INTERNAL_ROUNDRECT_DIMENSION, 10, 10)
      }

    }

    private class WhitePawn extends Pawn {
      private val white = colorProvider.getWhiteColor
      private val black = colorProvider.getBlackColor
      namePawn = "white"
      externalColor = black
      internalColor = white
    }

    private class BlackPawn extends Pawn {
      private val white = colorProvider.getWhiteColor
      private val black = colorProvider.getBlackColor
      namePawn = "black"
      externalColor = white
      internalColor = black
    }

    private class KingPawn extends Pawn {
      private val gold = colorProvider.getGoldColor
      private val white = colorProvider.getWhiteColor
      namePawn = "king"
      externalColor = gold
      internalColor = white

    }

    private class LostWhitePawn extends WhitePawn {
      EXTERNAL_ROUNDRECT_DIMENSION = EXTERNAL_ROUNDRECT_DIMENSION / 2
      INTERNAL_ROUNDRECT_DIMENSION = INTERNAL_ROUNDRECT_DIMENSION / 2
    }

    private class LostBlackPawn extends BlackPawn {
      EXTERNAL_ROUNDRECT_DIMENSION = EXTERNAL_ROUNDRECT_DIMENSION / 2
      INTERNAL_ROUNDRECT_DIMENSION = INTERNAL_ROUNDRECT_DIMENSION / 2
    }

    private class LabelPlayer_Winner extends JLabel {
      private val DIMENSION_FONT: Int = smallerSide * 7 / 100
      setFont(new Font(f.getFontName, Font.BOLD, DIMENSION_FONT))
      setForeground(colorProvider.getPossibleMovesColor)
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
}
