package view

import java.awt._
import java.awt.event.{MouseAdapter, MouseEvent}
import java.awt.image.BufferedImage
import java.io.File

import javax.imageio.ImageIO
import javax.swing._

trait MenuFactory {

  /**
   * Creates a new MenuPanel.
   *
   * @param string
   *            text of the menù panel.
   *
   * @return a new MenuPanel.
   */
  def createMenuPanel(string: String): JPanel

  /**
   * Creates a button in main menu.
   *
   * @param text
   *            text of button.
   * @return a button.
   */
  def createMainButton(text: String): JButton

  /**
    * Creates a button in level menu.
    *
    * @param text
    *            text of button.
    * @return a button.
    */
  def createLevelButton(text: String): JButton

  /**
    * Creates a button in variant menu.
    *
    * @param text
    *            text of button.
    * @return a button.
    */
  def createVariantButton(text: String): JButton

  /**
    * Creates a button in player menu.
    *
    * @param text
    *            text of button.
    * @return a button.
    */
  def createPlayerButton(text: String): JButton

  /**
   * Sets the current board size to correctly define the dimension
   * of the cells.
   *
   * @param variantBoardSize
   *              variant board size
   */
  def setVariantBoardSize(variantBoardSize: Int): Unit

  /**
    * Creates a sub menu player panel.
    *
    * @return a panel.
    */
  def createSubMenuPlayerPanel: JPanel

  /**
    * Creates a sub menu variant panel.
    *
    * @return a panel.
    */
  def createSubMenuVariantPanel: JPanel

  /**
    * Creates a sub menu level panel.
    *
    * @return a panel.
    */
  def createSubMenuLevelPanel: JPanel

  /**
    * Creates a label for each different variant.
    *
    * @return a label.
    */
  def createLabelBoardHnefatafl: JLabel
  def createLabelBoardTawlbwrdd: JLabel
  def createLabelBoardTablut: JLabel
  def createLabelBoardBrandubh: JLabel

  /**
    * Creates a label for each different level.
    *
    * @return a label.
    */
  def createLabelNewcomer: JLabel
  def createLabelAmateur: JLabel
  def createLabelStandard: JLabel
  def createLabelAdvanced: JLabel

  /**
    * Creates a label for player white or black.
    *
    * @return a label.
    */
  def createLabelWhitePlayer: JLabel
  def createLabelBlackPlayer: JLabel
}

object MenuFactory extends MenuFactory {

  private var cellDimension = 0
  private val smallerSide: Int = ScreenSize.getSmallerSide * 9 / 10
  private val f: Font = ResourceLoader.loadFont("/font/NorseBold-2Kge.otf")
  private val ge: GraphicsEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment
  ge.registerFont(f)
  private val boardHnefataflIconPath: String =  "/images/iconBoardHnefatafl.png"
  private val boardTawlbwrddIconPath: String =  "/images/iconBoardTawlbwrdd.png"
  private val boardTablutIconPath: String =  "/images/iconBoardTablut.png"
  private val boardBrandubhlIconPath: String =  "/images/iconBoardBrandubh.png"
  private val newcomerIconPath: String =  "/images/iconNewcomer.png"
  private val amateurIconPath: String =  "/images/iconAmateur.png"
  private val standardIconPath: String =  "/images/iconStandard.png"
  private val advancedIconPath: String =  "/images/iconAdvanced.png"
  private val whitePlayerIconPath: String =  "/images/iconWhitePlayer.jpeg"
  private val blackPlayerIconPath: String =  "/images/iconBlackPlayer.jpeg"
  private val HEIGHT_COMPONENT_MENU_DIMENSION: Int = smallerSide * 8 / 100

  override def createMenuPanel(string: String): JPanel = new MenuPanel(string)

  override def createMainButton(s: String): JButton = mainButton(s)

  override def createLevelButton(s: String): JButton = levelButton(s)

  override def createVariantButton(s: String): JButton = variantButton(s)

  override def createPlayerButton(s: String): JButton = playerButton(s)

  override def setVariantBoardSize(variantBoardSize: Int): Unit = cellDimension = smallerSide / variantBoardSize * 80 / 100

  override def createSubMenuPlayerPanel: JPanel = subMenuPlayerPanel()

  override def createSubMenuVariantPanel: JPanel = subMenuVariantPanel()

  override def createSubMenuLevelPanel: JPanel = subMenuLevelPanel()

  override def createLabelBoardHnefatafl: JLabel = new IconLabel(boardHnefataflIconPath)

  override def createLabelBoardTawlbwrdd: JLabel = new IconLabel(boardTawlbwrddIconPath)

  override def createLabelBoardTablut: JLabel = new IconLabel(boardTablutIconPath)

  override def createLabelBoardBrandubh: JLabel = new IconLabel(boardBrandubhlIconPath)

  override def createLabelNewcomer: JLabel = new IconLabel(newcomerIconPath)

  override def createLabelAmateur: JLabel = new IconLabel(amateurIconPath)

  override def createLabelStandard: JLabel = new IconLabel(standardIconPath)

  override def createLabelAdvanced: JLabel = new IconLabel(advancedIconPath)

  override def createLabelWhitePlayer: JLabel = new IconLabel(whitePlayerIconPath)

  override def createLabelBlackPlayer: JLabel = new IconLabel(blackPlayerIconPath)

  private class MenuPanel(string: String) extends JPanel {

    private val menuLabel = new JLabel()

    private val chooseLabel = new JLabel()

    private val image = ResourceLoader.loadImage("/images/Cornice.png")

    private val imageScaled = image.getScaledInstance(smallerSide, smallerSide * 98 / 100, Image.SCALE_DEFAULT)

    private val img = new ImageIcon(ResourceLoader.loadImage("/images/logo.png"))

    setLayout(new BoxLayout(this, BoxLayout.Y_AXIS))
    menuLabel.setPreferredSize(new Dimension(smallerSide, smallerSide * 25 / 100))
    menuLabel.setAlignmentX(Component.CENTER_ALIGNMENT)
    menuLabel.setIcon(img)
    chooseLabel.setPreferredSize(new Dimension(smallerSide, smallerSide * 6 / 100))
    chooseLabel.setAlignmentX(Component.CENTER_ALIGNMENT)
    chooseLabel.setFont(new Font(f.getFontName, Font.BOLD, smallerSide * 6 / 100))
    chooseLabel.setText(string)
    chooseLabel.setForeground(ColorProvider.getWhiteColor)
    add(Box.createRigidArea(new Dimension(smallerSide, smallerSide * 5 / 100)))
    add(menuLabel)
    add(Box.createRigidArea(new Dimension(smallerSide, smallerSide * 1 / 100)))
    add(chooseLabel)
    add(Box.createRigidArea(new Dimension(smallerSide, smallerSide * 1 / 100)))


    override protected def paintComponent(g: Graphics): Unit = {
      super.paintComponent(g)
      g.drawImage(imageScaled, 0, 0, null)
    }
  }

  abstract private class EmptyButton(val s: String) extends JButton {
    setCursor(new Cursor(Cursor.HAND_CURSOR))
    setHorizontalAlignment(SwingConstants.CENTER)
    setText(s)
  }

  private class MenuButton(s: String, sizePercentageWidth: Int, sizePercentageHeight: Int) extends EmptyButton(s) {

    private val FONT_DIMENSION = smallerSide * 5 / 100
    private val BUTTON_DIMENSION = new Dimension(sizePercentageWidth, sizePercentageHeight)

    setPreferredSize(BUTTON_DIMENSION)
    setMaximumSize(getPreferredSize)
    setAlignmentX(Component.CENTER_ALIGNMENT)

    setOpaque(false)
    setContentAreaFilled(false)
    setBorderPainted(false)

    setFont(new Font(f.getFontName, Font.BOLD, FONT_DIMENSION))
    setForeground(ColorProvider.getWhiteColor)
    setBorderPainted(false)

    addMouseListener(new MouseAdapter() {
      override def mouseEntered(e: MouseEvent): Unit = {
        setForeground(ColorProvider.getNormalCellColor)
      }

      override def mouseExited(e: MouseEvent): Unit = {
        setForeground(ColorProvider.getWhiteColor)
      }
    })
  }

  private def mainButton(text: String): JButton = new MenuButton(text, smallerSide * 60 / 100, HEIGHT_COMPONENT_MENU_DIMENSION)
  private def levelButton(text: String): JButton = new MenuButton(text, smallerSide * 25 / 100, HEIGHT_COMPONENT_MENU_DIMENSION)
  private def variantButton(text: String): JButton = new MenuButton(text, smallerSide * 40 / 100, HEIGHT_COMPONENT_MENU_DIMENSION)
  private def playerButton(text: String): JButton = new MenuButton(text, smallerSide * 29 / 100, HEIGHT_COMPONENT_MENU_DIMENSION)

  private class IconLabel(pathIcon: String) extends JLabel {
    private val ICON_DIMENSION: Int = smallerSide * 5 / 100
    this.setPreferredSize(new Dimension(ICON_DIMENSION, ICON_DIMENSION))
    this.setMaximumSize(getPreferredSize)
    val img: BufferedImage = ResourceLoader.loadImage(pathIcon)
    val dimg: Image = img.getScaledInstance(ICON_DIMENSION, ICON_DIMENSION, Image.SCALE_SMOOTH)
    val imageIcon = new ImageIcon(dimg)
    this.setIcon(imageIcon)
  }

  private class SubMenuPanel(sizePercentageWidth: Int, sizePercentageHeight: Int) extends JPanel {
    private val SUB_PANEL_DIMENSION = new Dimension(sizePercentageWidth, sizePercentageHeight)

    setPreferredSize(SUB_PANEL_DIMENSION)
    setMaximumSize(getPreferredSize)
    setAlignmentX(Component.CENTER_ALIGNMENT)
    setOpaque(false)
    setVisible(true)
    val gridBagLayout: GridBagLayout = new java.awt.GridBagLayout()
    setLayout(gridBagLayout)

  }

  private def subMenuPlayerPanel(): JPanel = new SubMenuPanel(smallerSide * 40/100, HEIGHT_COMPONENT_MENU_DIMENSION)
  private def subMenuVariantPanel(): JPanel = new SubMenuPanel(smallerSide * 70/100, HEIGHT_COMPONENT_MENU_DIMENSION)
  private def subMenuLevelPanel(): JPanel = new SubMenuPanel(smallerSide * 50/100, HEIGHT_COMPONENT_MENU_DIMENSION)
}