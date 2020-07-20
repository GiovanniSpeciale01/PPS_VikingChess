package view

import java.awt.{Dimension, GridBagConstraints}
import java.awt.event.{ActionEvent, ActionListener}

import javax.swing._
import model.{GameMode, GameVariant, Level, Player}

trait Menu {

  /**
    * Gets player chosen.
    *
    * @return player
    */
  def getPlayer: Player.Val

  /**
    * Initializes the main menù.
    *
    * @return panel
    */
  def initMenu: JPanel

  /**
    * Initializes the variant menù.
    *
    * @return panel
    */
  def initVariantsMenu: JPanel

  /**
    * Initializes the difficult selection menù.
    *
    * @return panel
    */
  def initDiffMenu: JPanel

  /**
    * Initializes the player selection menù.
    *
    * @return panel
    */
  def initPlayerChoiceMenu: JPanel

  /**
    * Gets the variant chosen.
    *
    * @return variant
    */
  def getBoardVariant: GameVariant.Val

  /**
    * Gets the game mode chosen.
    *
    * @return game mode
    */
  def getGameMode: GameMode.Value

  /**
    * Gets the level of difficult chosen.
    *
    * @return level
    */
  def getDifficult: Level.Val

  /**
    * Initializes the game menù.
    *
    * @return panel
    */
  def initInGameMenu: JPanel
}

object Menu {

  def apply(gameView: ViewHnefatafl): Menu = MenuImpl(gameView)

  case class MenuImpl(view: ViewHnefatafl) extends Menu {

    private final val DIMENSION_PANEL: Dimension =
      new Dimension(GameFactory.getSmallerSide, GameFactory.getSmallerSide * 2 / 100)
    private var menuPanel, variantsPanel, diffPanel, playerChoicePanel, inGameMenuPanel,
                panelVariantHnefatafl, panelVariantTawlbwrdd, panelVariantTablut, panelVariantBrandubh,
                panelLevelNewcomer, panelLevelAmateur, panelLevelStandard, panelLevelAdvanced,
                panelChoseWhite, panelChoseBlack: JPanel = _
    private var pvpButton, pveButton, exitButtonMenu, hnefatafl, tawlbwrdd, tablut, brandubh,
      newcomer, amateur, standard, advanced, whiteButton, blackButton, quitGame, returnToMenu,
      returnToGame, restartGame: JButton = _

    private var labelHnefatafl, labelTawlbwrdd, labelTablut, labelBrandubh, labelNewcomer, labelAmateur,
                labelStandard, labelAdvanced, labelWhiteChose, labelBlackChose: JLabel = _

    private var gameMode: GameMode.Value = _
    private var boardVariant: GameVariant.Val = _
    private var levelIA: Level.Val = _
    private var player: Player.Val = _

    private var limits: GridBagConstraints = _

    override def getPlayer: Player.Val = player

    override def initMenu: JPanel = {
      menuPanel = MenuFactory.createMenuPanel("Choose Mode: ")
      pveButton = MenuFactory.createMainButton(" Player Vs Computer")
      pveButton.addActionListener(chooseGameModeListener("PVE"))
      pvpButton = MenuFactory.createMainButton("Player Vs Player")
      pvpButton.addActionListener(chooseGameModeListener("PVP"))
      exitButtonMenu = MenuFactory.createMainButton("Exit")
      exitButtonMenu.addActionListener(_ => System.exit(0))
      menuPanel.add(Box.createRigidArea(DIMENSION_PANEL))
      menuPanel.add(pveButton)
      menuPanel.add(pvpButton)
      menuPanel.add(exitButtonMenu)
      menuPanel.add(Box.createVerticalGlue)
      limits = GameFactory.createBagConstraints
      limits.fill = GridBagConstraints.NONE
      limits.anchor = GridBagConstraints.LINE_START
      menuPanel
    }

    override def initVariantsMenu: JPanel = {
      variantsPanel = MenuFactory.createMenuPanel("Choose Board Variant: ")

      panelVariantHnefatafl = MenuFactory.createSubMenuVariantPanel
      panelVariantTawlbwrdd = MenuFactory.createSubMenuVariantPanel
      panelVariantTablut = MenuFactory.createSubMenuVariantPanel
      panelVariantBrandubh = MenuFactory.createSubMenuVariantPanel

      labelHnefatafl = MenuFactory.createLabelBoardHnefatafl
      GameFactory.resetXConstraints(limits)
      panelVariantHnefatafl.add(labelHnefatafl, limits)
      hnefatafl = MenuFactory.createVariantButton("Hnefatafl (11 x 11)")
      hnefatafl.addActionListener(chooseVariantListener("Hnefatafl"))
      GameFactory.incrementXConstraints(limits)
      panelVariantHnefatafl.add(hnefatafl, limits)

      labelTawlbwrdd = MenuFactory.createLabelBoardTawlbwrdd
      GameFactory.resetXConstraints(limits)
      panelVariantTawlbwrdd.add(labelTawlbwrdd, limits)
      tawlbwrdd = MenuFactory.createVariantButton("Tawlbwrdd (11 x 11)")
      tawlbwrdd.addActionListener(chooseVariantListener("Tawlbwrdd"))
      GameFactory.incrementXConstraints(limits)
      panelVariantTawlbwrdd.add(tawlbwrdd, limits)

      labelTablut = MenuFactory.createLabelBoardTablut
      GameFactory.resetXConstraints(limits)
      panelVariantTablut.add(labelTablut, limits)
      tablut = MenuFactory.createVariantButton("Tablut (9 x 9)")
      tablut.addActionListener(chooseVariantListener("Tablut"))
      GameFactory.incrementXConstraints(limits)
      panelVariantTablut.add(tablut, limits)

      labelBrandubh = MenuFactory.createLabelBoardBrandubh
      GameFactory.resetXConstraints(limits)
      panelVariantBrandubh.add(labelBrandubh, limits)
      brandubh = MenuFactory.createVariantButton("Brandubh (7 x 7)")
      brandubh.addActionListener(chooseVariantListener("Brandubh"))
      GameFactory.incrementXConstraints(limits)
      panelVariantBrandubh.add(brandubh, limits)

      returnToMenu = MenuFactory.createMainButton("Previous Menu")
      returnToMenu.addActionListener((_: ActionEvent) => view.switchOverlay(variantsPanel, menuPanel))

      variantsPanel.add(Box.createRigidArea(DIMENSION_PANEL))
      variantsPanel.add(panelVariantHnefatafl)
      variantsPanel.add(panelVariantTawlbwrdd)
      variantsPanel.add(panelVariantTablut)
      variantsPanel.add(panelVariantBrandubh)
      variantsPanel.add(returnToMenu)
      variantsPanel.add(Box.createVerticalGlue)

      variantsPanel.setVisible(false)
      variantsPanel
    }

    override def initDiffMenu: JPanel = {
      diffPanel = MenuFactory.createMenuPanel("Choose Difficulty: ")

      panelLevelNewcomer = MenuFactory.createSubMenuLevelPanel
      panelLevelAmateur = MenuFactory.createSubMenuLevelPanel
      panelLevelStandard = MenuFactory.createSubMenuLevelPanel
      panelLevelAdvanced = MenuFactory.createSubMenuLevelPanel

      labelNewcomer = MenuFactory.createLabelNewcomer
      GameFactory.resetXConstraints(limits)
      panelLevelNewcomer.add(labelNewcomer, limits)
      newcomer = MenuFactory.createLevelButton("Newcomer")
      newcomer.addActionListener(chooseLevelIAListener("Newcomer"))
      GameFactory.incrementXConstraints(limits)
      panelLevelNewcomer.add(newcomer, limits)

      labelAmateur = MenuFactory.createLabelAmateur
      GameFactory.resetXConstraints(limits)
      panelLevelAmateur.add(labelAmateur, limits)
      amateur = MenuFactory.createLevelButton("Amateur")
      amateur.addActionListener(chooseLevelIAListener("Amateur"))
      GameFactory.incrementXConstraints(limits)
      panelLevelAmateur.add(amateur, limits)

      labelStandard = MenuFactory.createLabelStandard
      GameFactory.resetXConstraints(limits)
      panelLevelStandard.add(labelStandard, limits)
      standard = MenuFactory.createLevelButton("Standard")
      standard.addActionListener(chooseLevelIAListener("Standard"))
      GameFactory.incrementXConstraints(limits)
      panelLevelStandard.add(standard, limits)
      standard.setEnabled(false)

      labelAdvanced = MenuFactory.createLabelAdvanced
      GameFactory.resetXConstraints(limits)
      panelLevelAdvanced.add(labelAdvanced, limits)
      advanced = MenuFactory.createLevelButton("Advanced")
      advanced.addActionListener(chooseLevelIAListener("Advanced"))
      GameFactory.incrementXConstraints(limits)
      panelLevelAdvanced.add(advanced, limits)
      advanced.setEnabled(false)

      returnToMenu = MenuFactory.createMainButton("Previous Menu")
      returnToMenu.addActionListener((_: ActionEvent) => view.switchOverlay(diffPanel, variantsPanel))

      diffPanel.add(Box.createRigidArea(DIMENSION_PANEL))
      diffPanel.add(panelLevelNewcomer)
      diffPanel.add(panelLevelAmateur)
      diffPanel.add(panelLevelStandard)
      diffPanel.add(panelLevelAdvanced)
      diffPanel.add(returnToMenu)
      diffPanel.add(Box.createVerticalGlue)
      diffPanel.setVisible(false)
      diffPanel
    }

    override def initPlayerChoiceMenu: JPanel = {
      playerChoicePanel = MenuFactory.createMenuPanel("Choose Player: ")

      panelChoseWhite = MenuFactory.createSubMenuPlayerPanel
      panelChoseBlack = MenuFactory.createSubMenuPlayerPanel

      labelWhiteChose = MenuFactory.createLabelWhitePlayer
      GameFactory.resetXConstraints(limits)
      panelChoseWhite.add(labelWhiteChose,limits)
      whiteButton = MenuFactory.createPlayerButton("White Pawns")
      whiteButton.addActionListener(setPlayer("White"))
      GameFactory.incrementXConstraints(limits)
      panelChoseWhite.add(whiteButton,limits)

      labelBlackChose = MenuFactory.createLabelBlackPlayer
      GameFactory.resetXConstraints(limits)
      panelChoseBlack.add(labelBlackChose,limits)
      blackButton = MenuFactory.createPlayerButton("Black Pawns")
      blackButton.addActionListener(setPlayer("Black"))
      GameFactory.incrementXConstraints(limits)
      panelChoseBlack.add(blackButton,limits)

      returnToMenu = MenuFactory.createMainButton("Previous Menu")
      returnToMenu.addActionListener((_: ActionEvent) => view.switchOverlay(playerChoicePanel, variantsPanel))

      playerChoicePanel.add(Box.createRigidArea(DIMENSION_PANEL))
      playerChoicePanel.add(panelChoseWhite)
      playerChoicePanel.add(panelChoseBlack)
      playerChoicePanel.add(returnToMenu)
      playerChoicePanel.add(Box.createVerticalGlue)
      playerChoicePanel.setVisible(false)
      playerChoicePanel
    }

    override def getBoardVariant: GameVariant.Val = boardVariant

    override def getGameMode: GameMode.Value = gameMode

    override def getDifficult: Level.Val = levelIA

    override def initInGameMenu: JPanel = {
      inGameMenuPanel = MenuFactory.createMenuPanel("Choose Option: ")
      returnToGame = MenuFactory.createMainButton("Return to Game")
      returnToGame.addActionListener((_: ActionEvent) => view.switchOverlay(inGameMenuPanel, view.getGamePanel))

      restartGame = MenuFactory.createMainButton("Restart Match")
      restartGame.addActionListener((_: ActionEvent) => {view.initOrRestoreGUI(player); view.switchOverlay(inGameMenuPanel, view.getGamePanel)})

      returnToMenu = MenuFactory.createMainButton("Leave Match")
      returnToMenu.addActionListener((_: ActionEvent) => view.switchOverlay(inGameMenuPanel, menuPanel))
      quitGame = MenuFactory.createMainButton("Quit Game")
      quitGame.addActionListener((_: ActionEvent) => System.exit(0))
      inGameMenuPanel.add(Box.createRigidArea(DIMENSION_PANEL))
      inGameMenuPanel.add(returnToGame)
      inGameMenuPanel.add(restartGame)
      inGameMenuPanel.add(returnToMenu)
      inGameMenuPanel.add(quitGame)
      inGameMenuPanel.add(Box.createVerticalGlue)
      inGameMenuPanel.setVisible(false)
      inGameMenuPanel
    }

    /**
      * Sets the game mode.
      *
      * @param gameModeString
      *               game mode chosen by the user.
      */
    private def setGameMode(gameModeString: String): Unit = gameModeString match {
      case "PVP" => gameMode = GameMode.PVP
      case "PVE" => gameMode = GameMode.PVE
    }

    /**
      * Listens for switch from main menù to variant menù.
      *
      * @param gameMode
      *             String of the game mode chosen.
      * @return listner
      */
    private def chooseGameModeListener(gameMode: String): ActionListener = (_: ActionEvent) => {
      setGameMode(gameMode)
      view.switchOverlay(menuPanel, variantsPanel)
    }

    /**
      * Sets the variant chosen from user.
      *
      * @param variant
      *              variant chosen from user.
      */
    private def setBoardVariant(variant: String): Unit = variant match {
      case "Hnefatafl" => boardVariant = GameVariant.Hnefatafl
      case "Tawlbwrdd" => boardVariant = GameVariant.Tawlbwrdd
      case "Tablut" => boardVariant = GameVariant.Tablut
      case "Brandubh" => boardVariant = GameVariant.Brandubh
    }

    /**
      * Listens for switch from variant menù in according to the chosen mode.
      *
      * @param variant
      *             String of the variant chosen.
      * @return listner
      */
    private def chooseVariantListener(variant: String): ActionListener = (_: ActionEvent) => {
      setBoardVariant(variant)
      gameMode match {
        case GameMode.PVP => view.switchOverlay(variantsPanel, playerChoicePanel)
        case GameMode.PVE => view.switchOverlay(variantsPanel, diffPanel)
      }
    }

    /**
      * Sets the difficult chosen from user.
      */
    private def setLevelIA(level: String): Unit = level match {
      case "Newcomer" => levelIA = Level.Newcomer
      case "Amateur" => levelIA = Level.Amateur
      case "Standard" => levelIA = Level.Standard
      case "Advanced" => levelIA = Level.Advanced
    }

    /**
      * Listens for switch from difficult menù.
      *
      * @param level
      *             String of the level IA chosen.
      * @return listner
      */
    private def chooseLevelIAListener(level: String): ActionListener = (_: ActionEvent) => {
      setLevelIA(level)
      view.switchOverlay(diffPanel, playerChoicePanel)
    }

    /**
      * Sets the user player.
      */
    private def setPlayer(playerString: String): ActionListener = (_: ActionEvent) => playerString match {
      case "White" => player = Player.White; initOrRestore()
      case "Black" => player = Player.Black; initOrRestore()
    }

    /**
      * Initializes or restores the game by calling game view.
      */
    private def initOrRestore(): Unit = {
      view.initOrRestoreGUI(player)
    }
  }
}
