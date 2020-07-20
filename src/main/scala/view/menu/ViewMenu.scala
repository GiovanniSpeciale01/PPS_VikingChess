package view.menu

import java.awt.event.{ActionEvent, ActionListener}
import java.awt.{Dimension, GridBagConstraints}

import javax.swing._
import model.game.GameMode.GameMode
import model.game.GameVariant.GameVariant
import model.game.Level.Level
import model.game.Player.Player
import model.game.{GameMode, GameVariant, Level, Player}
import view.ViewHnefatafl
import view.factories.MenuFactory
import view.utils.{GridConstraints, ScreenSize}
import view.utils.JPanelAddAll._

/**
 * Represents a hnefatafl game menu view
 */
trait ViewMenu {

  /**
    * Returns selected player.
    *
    * @return selected player
    */
  def getPlayer: Player

  /**
    * Returns the initialized main menu.
    *
    * @return the initialized main menu
    */
  def initMenu: JPanel

  /**
    * Returns the initialized variant selection menu.
    *
    * @return the initialized variant selection menu
    */
  def initVariantsMenu: JPanel

  /**
   * Returns the initialized difficulty selection menu.
   *
   * @return the initialized difficulty selection menu
   */
  def initDiffMenu: JPanel

  /**
   * Returns the initialized player selection menu.
   *
   * @return the initialized player selection menu
   */
  def initPlayerChoiceMenu: JPanel

  /**
    * Returns the selected variant.
    *
    * @return the selected variant
    */
  def getBoardVariant: GameVariant

  /**
   * Returns the selected game mode.
   *
   * @return the selected game mode
   */
  def getGameMode: GameMode

  /**
   * Returns the selected difficulty.
   *
   * @return the selected difficulty
   */
  def getDifficulty: Level

  /**
   * Returns the initialized game menu.
   *
   * @return the initialized game menu
   */
  def initInGameMenu: JPanel
}

/**
 * Represents a hnefatafl game menu view
 */
object ViewMenu {

  def apply(gameView: ViewHnefatafl): ViewMenu = ViewMenuImpl(gameView)

  case class ViewMenuImpl(view: ViewHnefatafl) extends ViewMenu {

    private val smallerSide = ScreenSize.getSmallerSide

    private val DIMENSION_PANEL: Dimension = new Dimension(smallerSide, smallerSide * 2 / 100)
    private var menuPanel, variantsPanel, diffPanel, playerChoicePanel, inGameMenuPanel,
                panelVariantHnefatafl, panelVariantTawlbwrdd, panelVariantTablut, panelVariantBrandubh,
                panelLevelNewcomer, panelLevelStandard, panelLevelAdvanced,
                panelChoseWhite, panelChoseBlack: JPanel = _
    private var pvpButton, pveButton, exitButtonMenu, quitGame, returnToMenu,
      returnToGame, restartGame: JButton = _

    private var gameMode: GameMode = _
    private var boardVariant: GameVariant = _
    private var levelIA: Level = _
    private var chosenPlayer: Player = _

    private val chooseModeString: String = "Choose Mode: "
    private val exitString: String = "Exit"
    private val chooseVariantString: String = "Choose Game Variant: "
    private val previousMenuString: String = "Previous Menu"
    private val chooseLevelString: String = "Choose Difficulty: "
    private val choosePlayerString: String = "Choose Player: "
    private val chooseOptionString: String = "Choose Option: "
    private val returnToGameString: String = "Return to Game"
    private val restartMatchString: String = "Restart Match"
    private val leaveMatchString: String = "Leave Match"
    private val quitGameString: String = "Quit Game"
    private val pawnButtonString: String = " pawns"

    /**
     * @inheritdoc
     */
    override def getPlayer: Player = chosenPlayer

    /**
     * @inheritdoc
     */
    override def initMenu: JPanel = {
      menuPanel = MenuFactory.createMenuPanel(chooseModeString)
      pveButton = MenuFactory.createMainButton(GameMode.PVE.toString)
      pveButton.addActionListener(selectGameModeListener(GameMode.PVE.toString))
      pvpButton = MenuFactory.createMainButton(GameMode.PVP.toString)
      pvpButton.addActionListener(selectGameModeListener(GameMode.PVP.toString))
      exitButtonMenu = MenuFactory.createMainButton(exitString)
      exitButtonMenu.addActionListener(_ => System.exit(0))
      menuPanel.addAll(Box.createRigidArea(DIMENSION_PANEL))(pveButton)(pvpButton)(exitButtonMenu)(Box.createVerticalGlue)
      menuPanel
    }

    /**
     * @inheritdoc
     */
    private def initMenuButton(p: JPanel, l: JLabel, b: JButton, al: ActionListener): Unit = {
      GridConstraints.setAnchor(GridBagConstraints.LINE_START)
      GridConstraints.resetXConstraints()
      p.add(l, GridConstraints.getLimits)
      b.addActionListener(al)
      GridConstraints.incrementXConstraints()
      p.add(b, GridConstraints.getLimits)
    }

    /**
     * @inheritdoc
     */
    override def initVariantsMenu: JPanel = {
      variantsPanel = MenuFactory.createMenuPanel(chooseVariantString)

      panelVariantHnefatafl = MenuFactory.createSubMenuVariantPanel
      panelVariantTawlbwrdd = MenuFactory.createSubMenuVariantPanel
      panelVariantTablut = MenuFactory.createSubMenuVariantPanel
      panelVariantBrandubh = MenuFactory.createSubMenuVariantPanel

      initMenuButton(panelVariantHnefatafl, MenuFactory.createLabelBoardHnefatafl,
        MenuFactory.createVariantButton(variantButtonString(GameVariant.Hnefatafl)),
        selectVariantListener(GameVariant.Hnefatafl))
      initMenuButton(panelVariantTawlbwrdd, MenuFactory.createLabelBoardTawlbwrdd,
        MenuFactory.createVariantButton(variantButtonString(GameVariant.Tawlbwrdd)),
        selectVariantListener(GameVariant.Tawlbwrdd))
      initMenuButton(panelVariantTablut, MenuFactory.createLabelBoardTablut,
        MenuFactory.createVariantButton(variantButtonString(GameVariant.Tablut)),
        selectVariantListener(GameVariant.Tablut))
      initMenuButton(panelVariantBrandubh, MenuFactory.createLabelBoardBrandubh,
        MenuFactory.createVariantButton(variantButtonString(GameVariant.Brandubh)),
        selectVariantListener(GameVariant.Brandubh))

      initPreviousMenuButton(variantsPanel, menuPanel)

      variantsPanel.addAll(Box.createRigidArea(DIMENSION_PANEL))(panelVariantHnefatafl)(panelVariantTawlbwrdd)(
        panelVariantTablut)(panelVariantBrandubh)(returnToMenu)(Box.createVerticalGlue)

      variantsPanel.setVisible(false)
      variantsPanel
    }

    private def variantButtonString(variant: GameVariant): String =
      variant.toString + " (" + variant.boardSize + "x" + variant.boardSize + ")"

    /**
     * @inheritdoc
     */
    override def initDiffMenu: JPanel = {
      diffPanel = MenuFactory.createMenuPanel(chooseLevelString)

      panelLevelNewcomer = MenuFactory.createSubMenuLevelPanel
      panelLevelStandard = MenuFactory.createSubMenuLevelPanel
      panelLevelAdvanced = MenuFactory.createSubMenuLevelPanel

      initMenuButton(panelLevelNewcomer, MenuFactory.createLabelNewcomer,
        MenuFactory.createLevelButton(Level.Newcomer.toString), selectLevelIAListener(Level.Newcomer))
      initMenuButton(panelLevelStandard, MenuFactory.createLabelStandard,
        MenuFactory.createLevelButton(Level.Standard.toString), selectLevelIAListener(Level.Standard))
      initMenuButton(panelLevelAdvanced, MenuFactory.createLabelAdvanced,
        MenuFactory.createLevelButton(Level.Advanced.toString), selectLevelIAListener(Level.Advanced))

      initPreviousMenuButton(diffPanel, variantsPanel)

      diffPanel.addAll(Box.createRigidArea(DIMENSION_PANEL))(panelLevelNewcomer)(panelLevelStandard)(
        panelLevelAdvanced)(returnToMenu)(Box.createVerticalGlue)
      diffPanel.setVisible(false)
      diffPanel
    }

    /**
     * @inheritdoc
     */
    override def initPlayerChoiceMenu: JPanel = {
      playerChoicePanel = MenuFactory.createMenuPanel(choosePlayerString)

      panelChoseWhite = MenuFactory.createSubMenuPlayerPanel
      panelChoseBlack = MenuFactory.createSubMenuPlayerPanel

      initMenuButton(panelChoseWhite, MenuFactory.createLabelWhitePlayer,
        MenuFactory.createPlayerButton(playerButtonString(Player.White)), selectPlayerListener(Player.White))
      initMenuButton(panelChoseBlack, MenuFactory.createLabelBlackPlayer,
        MenuFactory.createPlayerButton(playerButtonString(Player.Black)), selectPlayerListener(Player.Black))

      initPreviousMenuButton(playerChoicePanel, diffPanel)

      playerChoicePanel.addAll(Box.createRigidArea(DIMENSION_PANEL))(panelChoseWhite)(panelChoseBlack)(returnToMenu)(
        Box.createVerticalGlue)
      playerChoicePanel.setVisible(false)
      playerChoicePanel
    }

    private def playerButtonString(player: Player): String = player.toString + pawnButtonString

    private def initPreviousMenuButton(previousPanel: JPanel, nextPanel: JPanel): Unit =
      initGoBackButton(previousPanel, nextPanel, previousMenuString)

    private def initGoBackButton(previousPanel: JPanel, nextPanel: JPanel, text: String): Unit = {
      returnToMenu = MenuFactory.createMainButton(text)
      returnToMenu.addActionListener((_: ActionEvent) => view.switchOverlay(previousPanel, nextPanel))
    }

    /**
     * @inheritdoc
     */
    override def getBoardVariant: GameVariant = boardVariant

    /**
     * @inheritdoc
     */
    override def getGameMode: GameMode = gameMode

    /**
     * @inheritdoc
     */
    override def getDifficulty: Level = levelIA

    /**
     * @inheritdoc
     */
    override def initInGameMenu: JPanel = {
      inGameMenuPanel = MenuFactory.createMenuPanel(chooseOptionString)
      returnToGame = MenuFactory.createMainButton(returnToGameString)
      returnToGame.addActionListener((_: ActionEvent) => view.switchOverlay(inGameMenuPanel, view.getGamePanel))

      restartGame = MenuFactory.createMainButton(restartMatchString)
      restartGame.addActionListener((_: ActionEvent) => {view.resetGUI(); view.switchOverlay(inGameMenuPanel, view.getGamePanel)})

      initGoBackButton(inGameMenuPanel, menuPanel, leaveMatchString)

      quitGame = MenuFactory.createMainButton(quitGameString)
      quitGame.addActionListener((_: ActionEvent) => System.exit(0))
      inGameMenuPanel.addAll(Box.createRigidArea(DIMENSION_PANEL))(returnToGame)(restartGame)(returnToMenu)(quitGame)(
        Box.createVerticalGlue)
      inGameMenuPanel.setVisible(false)
      inGameMenuPanel
    }

    private def selectGameModeListener(gameModeString: String): ActionListener =
      (_: ActionEvent) => {
        gameMode = GameMode.withName(gameModeString)
        view.switchOverlay(menuPanel, variantsPanel)
      }

    private def selectVariantListener(variant: GameVariant): ActionListener = (_: ActionEvent) => {
      boardVariant = variant
      gameMode match {
        case GameMode.PVP => chosenPlayer = Player.None; view.resetGUI()
        case GameMode.PVE => view.switchOverlay(variantsPanel, diffPanel)
      }
    }

    private def selectLevelIAListener(level: Level): ActionListener =
      (_: ActionEvent) => {
        levelIA = level
        view.switchOverlay(diffPanel, playerChoicePanel)
      }

    private def selectPlayerListener(player: Player): ActionListener =
      (_: ActionEvent) => {
        chosenPlayer = player
        view.resetGUI()
      }
  }
}
