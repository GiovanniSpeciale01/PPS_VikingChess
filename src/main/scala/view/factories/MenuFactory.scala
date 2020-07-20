package view.factories

import java.awt._
import java.awt.event.{MouseAdapter, MouseEvent}
import java.awt.image.BufferedImage

import javax.swing._
import view.utils.{ResourceLoader, ScreenSize}

trait MenuFactory {

  /**
   * Creates a viking chess game frame.
   *
   * @return a viking chess game frame.
   */
  def createFrame: JFrame

  /**
   * Creates a new Menu Panel.
   *
   * @param string
   *         text of the menu panel.
   *
   * @return a new Menu Panel.
   */
  def createMenuPanel(string: String): JPanel

  /**
   * Creates a button for main menu.
   *
   * @param text
   *         text of button.
   * @return a button for main menu
   */
  def createMainButton(text: String): JButton

  /**
    * Creates a button for level selection menu.
    *
    * @param text
    *         text of button.
    * @return a button for level selection menu
    */
  def createLevelButton(text: String): JButton

  /**
    * Creates a button for variant selection menu.
    *
    * @param text
    *         text of button.
    * @return a button for variant selection menu.
    */
  def createVariantButton(text: String): JButton

  /**
    * Creates a button for player selection menu.
    *
    * @param text
    *         text of button.
    * @return a button for player selection menu.
    */
  def createPlayerButton(text: String): JButton

  /**
    * Creates a sub menu player panel.
    *
    * @return a sub menu player panel.
    */
  def createSubMenuPlayerPanel: JPanel

  /**
    * Creates a sub menu variant panel.
    *
    * @return a sub menu variant panel.
    */
  def createSubMenuVariantPanel: JPanel

  /**
    * Creates a sub menu level panel.
    *
    * @return a sub menu level panel.
    */
  def createSubMenuLevelPanel: JPanel

  /**
    * Creates a label for hnefatafl variant selection.
    *
    * @return a label for hnefatafl variant selection.
    */
  def createLabelBoardHnefatafl: JLabel

  /**
   * Creates a label for tawlbwrdd variant selection.
   *
   * @return a label for tawlbwrdd variant selection.
   */
  def createLabelBoardTawlbwrdd: JLabel

  /**
   * Creates a label for tablut variant selection.
   *
   * @return a label for tablut variant selection.
   */
  def createLabelBoardTablut: JLabel

  /**
   * Creates a label for brandubh variant selection.
   *
   * @return a label for brandubh variant selection.
   */
  def createLabelBoardBrandubh: JLabel

  /**
    * Creates a label for newcomer ia level selection.
    *
    * @return a label for newcomer ia level selection.
    */
  def createLabelNewcomer: JLabel

  /**
   * Creates a label for standard ia level selection.
   *
   * @return a label for standard ia level selection.
   */
  def createLabelStandard: JLabel

  /**
   * Creates a label for advanced ia level selection.
   *
   * @return a label for advanced ia level selection.
   */
  def createLabelAdvanced: JLabel

  /**
    * Creates a label for white player selection.
    *
    * @return a label for white player selection.
    */
  def createLabelWhitePlayer: JLabel

  /**
   * Creates a label for black player selection.
   *
   * @return a label for black player selection.
   */
  def createLabelBlackPlayer: JLabel
}

/**
 * Representing a view menu factory
 */
object MenuFactory extends MenuFactory {

  private val smallerSide: Int = ScreenSize.getSmallerSide

  private val FRAME_TITLE = "Viking Chess - Hnefatafl"
  private val HEIGHT_MENU_COMPONENT_DIMENSION: Int = smallerSide * 8 / 100

  private val font: Font = FontProvider.hnefataflFont
  private val ge: GraphicsEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment
  ge.registerFont(font)

  private val iconAppPath: String = "/images/iconApp.png"
  private val panelBackgroundPath: String = "/images/Cornice.png"
  private val logoPath: String = "/images/logo.png"
  private val boardHnefataflIconPath: String = "/images/iconBoardHnefatafl.png"
  private val boardTawlbwrddIconPath: String = "/images/iconBoardTawlbwrdd.png"
  private val boardTablutIconPath: String = "/images/iconBoardTablut.png"
  private val boardBrandubhIconPath: String = "/images/iconBoardBrandubh.png"
  private val newcomerIconPath: String = "/images/iconNewcomer.png"
  private val standardIconPath: String = "/images/iconStandard.png"
  private val advancedIconPath: String = "/images/iconAdvanced.png"
  private val whitePlayerIconPath: String = "/images/iconWhitePlayer.png"
  private val blackPlayerIconPath: String = "/images/iconBlackPlayer.png"

  /**
   * @inheritdoc
   */
  override def createFrame: JFrame = new Frame

  /**
   * @inheritdoc
   */
  override def createMenuPanel(string: String): JPanel = new MenuPanel(string)

  /**
   * @inheritdoc
   */
  override def createMainButton(s: String): JButton = mainButton(s)

  /**
   * @inheritdoc
   */
  override def createLevelButton(s: String): JButton = levelButton(s)

  /**
   * @inheritdoc
   */
  override def createVariantButton(s: String): JButton = variantButton(s)

  /**
   * @inheritdoc
   */
  override def createPlayerButton(s: String): JButton = playerButton(s)

  /**
   * @inheritdoc
   */
  override def createSubMenuPlayerPanel: JPanel = subMenuPlayerPanel()

  /**
   * @inheritdoc
   */
  override def createSubMenuVariantPanel: JPanel = subMenuVariantPanel()

  /**
   * @inheritdoc
   */
  override def createSubMenuLevelPanel: JPanel = subMenuLevelPanel()

  /**
   * @inheritdoc
   */
  override def createLabelBoardHnefatafl: JLabel = new IconLabel(boardHnefataflIconPath)

  /**
   * @inheritdoc
   */
  override def createLabelBoardTawlbwrdd: JLabel = new IconLabel(boardTawlbwrddIconPath)

  /**
   * @inheritdoc
   */
  override def createLabelBoardTablut: JLabel = new IconLabel(boardTablutIconPath)

  /**
   * @inheritdoc
   */
  override def createLabelBoardBrandubh: JLabel = new IconLabel(boardBrandubhIconPath)

  /**
   * @inheritdoc
   */
  override def createLabelNewcomer: JLabel = new IconLabel(newcomerIconPath)

  /**
   * @inheritdoc
   */
  override def createLabelStandard: JLabel = new IconLabel(standardIconPath)

  /**
   * @inheritdoc
   */
  override def createLabelAdvanced: JLabel = new IconLabel(advancedIconPath)

  /**
   * @inheritdoc
   */
  override def createLabelWhitePlayer: JLabel = new IconLabel(whitePlayerIconPath)

  /**
   * @inheritdoc
   */
  override def createLabelBlackPlayer: JLabel = new IconLabel(blackPlayerIconPath)

  private class Frame extends JFrame {
    private val iconApp = new ImageIcon(ResourceLoader.loadImage(iconAppPath))

    setTitle(FRAME_TITLE)
    setIconImage(iconApp.getImage)
    setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE)
    setResizable(false)
    setSize(smallerSide, smallerSide)
    setLocationRelativeTo(null)
  }

  private class MenuPanel(string: String) extends JPanel {
    private val menuLabel = new JLabel()
    private val chooseLabel = new JLabel()
    private val image = ResourceLoader.loadImage(panelBackgroundPath)
    private val imageScaled = image.getScaledInstance(smallerSide, smallerSide * 98 / 100, Image.SCALE_DEFAULT)
    private val img = new ImageIcon(ResourceLoader.loadImage(logoPath))

    setLayout(new BoxLayout(this, BoxLayout.Y_AXIS))
    menuLabel.setPreferredSize(new Dimension(smallerSide, smallerSide * 30 / 100))
    menuLabel.setAlignmentX(Component.CENTER_ALIGNMENT)
    menuLabel.setIcon(img)
    chooseLabel.setPreferredSize(new Dimension(smallerSide, smallerSide * 6 / 100))
    chooseLabel.setAlignmentX(Component.CENTER_ALIGNMENT)
    chooseLabel.setFont(new Font(font.getFontName, Font.BOLD, smallerSide * 6 / 100))
    chooseLabel.setText(string)
    chooseLabel.setForeground(ColorProvider.getWhiteColor)
    add(Box.createRigidArea(new Dimension(smallerSide, smallerSide * 10 / 100)))
    add(menuLabel)
    add(Box.createRigidArea(new Dimension(smallerSide, smallerSide * 3 / 100)))
    add(chooseLabel)
    add(Box.createRigidArea(new Dimension(smallerSide, smallerSide * 1 / 100)))

    override protected def paintComponent(g: Graphics): Unit = {
      super.paintComponent(g)
      g.drawImage(imageScaled, 0, 0,null)
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

    setFont(new Font(font.getFontName, Font.BOLD, FONT_DIMENSION))
    setForeground(ColorProvider.getWhiteColor)
    setBorderPainted(false)

    addMouseListener(new MouseAdapter() {
      override def mouseEntered(e: MouseEvent): Unit = {
        setForeground(ColorProvider.menuButtonHoverColor)
      }

      override def mouseExited(e: MouseEvent): Unit = {
        setForeground(ColorProvider.getWhiteColor)
      }
    })
  }

  private def mainButton(text: String): JButton = new MenuButton(text, smallerSide * 60 / 100, HEIGHT_MENU_COMPONENT_DIMENSION)

  private def levelButton(text: String): JButton = new MenuButton(text, smallerSide * 25 / 100, HEIGHT_MENU_COMPONENT_DIMENSION)

  private def variantButton(text: String): JButton = new MenuButton(text, smallerSide * 40 / 100, HEIGHT_MENU_COMPONENT_DIMENSION)

  private def playerButton(text: String): JButton = new MenuButton(text, smallerSide * 29 / 100, HEIGHT_MENU_COMPONENT_DIMENSION)

  private class IconLabel(pathIcon: String) extends JLabel {
    private val ICON_DIMENSION: Int = smallerSide * 6 / 100
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

  private def subMenuPlayerPanel(): JPanel = new SubMenuPanel(smallerSide * 40/100, HEIGHT_MENU_COMPONENT_DIMENSION)

  private def subMenuVariantPanel(): JPanel = new SubMenuPanel(smallerSide * 70/100, HEIGHT_MENU_COMPONENT_DIMENSION)

  private def subMenuLevelPanel(): JPanel = new SubMenuPanel(smallerSide * 50/100, HEIGHT_MENU_COMPONENT_DIMENSION)

}