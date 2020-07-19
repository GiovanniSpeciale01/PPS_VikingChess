package view

import java.awt.Dimension
import java.awt.event.{ActionEvent, ActionListener}
import javax.swing.{Box, JButton, JPanel}
import model.{GameMode, GameVariant, Player}

trait Menu {

  /**
    * Gets player chosen.
    *
    * @return player
    */
  def getPlayer: Player.Value

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
    * Initializes the game menù.
    *
    * @return panel
    */
  def initInGameMenu: JPanel
}

object Menu {

  def apply(viewFactory: ViewFactory, gameView: GameView): Menu = MenuImpl(viewFactory, gameView)

  case class MenuImpl(viewFactory: ViewFactory, gameView: GameView) extends Menu {

    private val smallerSide = viewFactory.getSmallerSide
    private final val DIMENSION_PANEL: Dimension = new Dimension(smallerSide, smallerSide * 2 / 100)
    private var menuPanel, variantsPanel, diffPanel, playerChoicePanel, inGameMenuPanel: JPanel = _
    private var pvpButton, pveButton, exitButtonMenu, hnefatafl, tawlbwrdd, tablut, brandubh,
      newcomer, amateur, standard, advanced, whiteButton, blackButton, quitGame, returnToMenu,
      returnToGame, restartGame: JButton = _

    private var gameMode: GameMode.Value = _
    private var boardVariant: GameVariant.Val = _
    private var player: Player.Value = _

    override def getPlayer: Player.Value = player

    override def initMenu: JPanel = {
      menuPanel = viewFactory.createMenuPanel("Choose Mode: ")
      pveButton = viewFactory.createMenuButton(" Player Vs Computer")
      pveButton.addActionListener(_ => pveVariant())
      // not enabled for release 0.3
      pveButton.setEnabled(false)
      pvpButton = viewFactory.createMenuButton("Player Vs Player")
      pvpButton.addActionListener(_ => pvpVariant())
      exitButtonMenu = viewFactory.createMenuButton("Exit")
      exitButtonMenu.addActionListener(_ => System.exit(0))
      menuPanel.add(Box.createRigidArea(DIMENSION_PANEL))
      menuPanel.add(pveButton)
      menuPanel.add(pvpButton)
      menuPanel.add(exitButtonMenu)
      menuPanel.add(Box.createVerticalGlue)
      menuPanel
    }

    override def initVariantsMenu: JPanel = {
      variantsPanel = viewFactory.createMenuPanel("Choose Board Variant: ")
      hnefatafl = viewFactory.createMenuButton("Hnefatafl (11 x 11)")
      hnefatafl.addActionListener(chooseListener("Hnefatafl"))
      tawlbwrdd = viewFactory.createMenuButton("Tawlbwrdd (11 x 11)")
      tawlbwrdd.addActionListener(chooseListener("Tawlbwrdd"))
      tablut = viewFactory.createMenuButton("Tablut (9 x 9)")
      tablut.addActionListener(chooseListener("Tablut"))
      brandubh = viewFactory.createMenuButton("Brandubh (7 x 7)")
      brandubh.addActionListener(chooseListener("Brandubh"))

      returnToMenu = viewFactory.createMenuButton("Previous Menu")
      returnToMenu.addActionListener((_: ActionEvent) => gameView.switchOverlay(variantsPanel, menuPanel))

      variantsPanel.add(Box.createRigidArea(DIMENSION_PANEL))
      variantsPanel.add(hnefatafl)
      variantsPanel.add(tawlbwrdd)
      variantsPanel.add(tablut)
      variantsPanel.add(brandubh)
      variantsPanel.add(returnToMenu)
      variantsPanel.add(Box.createVerticalGlue)
      variantsPanel.setVisible(false)
      variantsPanel
    }

    override def initDiffMenu: JPanel = {
      diffPanel = viewFactory.createMenuPanel("Choose Difficulty: ")
      newcomer = viewFactory.createMenuButton("Newcomer")
      newcomer.addActionListener((_: ActionEvent) => gameView.switchOverlay(diffPanel, playerChoicePanel))
      amateur = viewFactory.createMenuButton("Amateur")
      amateur.addActionListener((_: ActionEvent) => gameView.switchOverlay(diffPanel, playerChoicePanel))
      standard = viewFactory.createMenuButton("Standard")
      standard.addActionListener((_: ActionEvent) => gameView.switchOverlay(diffPanel, playerChoicePanel))
      advanced = viewFactory.createMenuButton("Advanced")
      advanced.addActionListener((_: ActionEvent) => gameView.switchOverlay(diffPanel, playerChoicePanel))

      returnToMenu = viewFactory.createMenuButton("Previous Menu")
      returnToMenu.addActionListener((_: ActionEvent) => gameView.switchOverlay(diffPanel, variantsPanel))

      diffPanel.add(Box.createRigidArea(DIMENSION_PANEL))
      diffPanel.add(newcomer)
      diffPanel.add(amateur)
      diffPanel.add(standard)
      diffPanel.add(advanced)
      diffPanel.add(returnToMenu)
      diffPanel.add(Box.createVerticalGlue)
      diffPanel.setVisible(false)
      diffPanel
    }

    override def initPlayerChoiceMenu: JPanel = {
      playerChoicePanel = viewFactory.createMenuPanel("Choose Player: ")
      whiteButton = viewFactory.createMenuButton("White Pawns")
      whiteButton.addActionListener((_: ActionEvent) => whitePlayer())
      blackButton = viewFactory.createMenuButton("Black Pawns")
      blackButton.addActionListener((_: ActionEvent) => blackPlayer())

      returnToMenu = viewFactory.createMenuButton("Previous Menu")
      returnToMenu.addActionListener((_: ActionEvent) => gameView.switchOverlay(playerChoicePanel, variantsPanel))

      playerChoicePanel.add(Box.createRigidArea(DIMENSION_PANEL))
      playerChoicePanel.add(whiteButton)
      playerChoicePanel.add(blackButton)
      playerChoicePanel.add(returnToMenu)
      playerChoicePanel.add(Box.createVerticalGlue)
      playerChoicePanel.setVisible(false)
      playerChoicePanel
    }

    override def getBoardVariant: GameVariant.Val = boardVariant

    override def initInGameMenu: JPanel = {
      inGameMenuPanel = viewFactory.createMenuPanel("Choose Option: ")
      returnToGame = viewFactory.createMenuButton("Return to Game")
      returnToGame.addActionListener((_: ActionEvent) => gameView.switchOverlay(inGameMenuPanel, gameView.getGamePanel))

      restartGame = viewFactory.createMenuButton("Restart Match")
      restartGame.addActionListener((_: ActionEvent) => {gameView.initOrRestoreGUI(); gameView.switchOverlay(inGameMenuPanel, gameView.getGamePanel)})

      returnToMenu = viewFactory.createMenuButton("Leave Match")
      returnToMenu.addActionListener((_: ActionEvent) => gameView.switchOverlay(inGameMenuPanel, menuPanel))
      quitGame = viewFactory.createMenuButton("Quit Game")
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
      * Sets the game mode to PVE
      */
    private def pveVariant(): Unit = {
      gameMode = GameMode.PVE
      showVariant()
    }

    /**
      * Sets the game mode to PVP
      */
    private def pvpVariant(): Unit = {
      gameMode = GameMode.PVP
      showVariant()
    }

    /**
      * Switch from main menù to variant menù
      */
    private def showVariant(): Unit = {
      gameView.switchOverlay(menuPanel, variantsPanel)
    }

    /**
      * Sets the variant chosen from user.
      */
    private def setBoardVariant(variant: String): Unit = {
      if (variant eq GameVariant.Hnefatafl.nameVariant) boardVariant = GameVariant.Hnefatafl
      else if (variant eq GameVariant.Tawlbwrdd.nameVariant) boardVariant = GameVariant.Tawlbwrdd
      else if (variant eq GameVariant.Tablut.nameVariant) boardVariant = GameVariant.Tablut
      else if (variant eq GameVariant.Brandubh.nameVariant) boardVariant = GameVariant.Brandubh
    }

    /**
      * Listner for switch from variant menù in according to the chosen mode.
      *
      * @param text
      *             String of the variant chosen.
      * @return listner
      */
    private def chooseListener(text: String): ActionListener = (_: ActionEvent) => {
      setBoardVariant(text)
      if (gameMode eq GameMode.PVP) gameView.switchOverlay(variantsPanel, playerChoicePanel)
      else gameView.switchOverlay(variantsPanel, diffPanel)
    }

    /**
      * Listner for switch from variant menù in according to the chosen mode.
      */
    private def initOrRestore(): Unit = {
      gameView.initOrRestoreGUI()
    }

    /**
      * Sets the user player to white.
      */
    private def whitePlayer(): Unit = {
      player = Player.White
      initOrRestore()
    }

    /**
      * Sets the user player to black.
      */
    private def blackPlayer(): Unit = {
      player = Player.Black
      initOrRestore()
    }
  }
}
