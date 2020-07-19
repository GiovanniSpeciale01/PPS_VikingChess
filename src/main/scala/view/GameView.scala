package view

import controller.ControllerHnefatafl
import javax.swing.{JFrame, JPanel}
import model.Player
import utils.BoardGame.Board
import utils.Pair
import view.ViewFactory.ViewFactoryImpl

trait GameView {

  /**
    * Gets the dimension according to game variant.
    */
  def getDimension: Int

  /**
    * Switches panel in overlay.
    *
    * @param actualPanel
    *                 panel shown.
    * @param panelToShow
    *                 panel to show.
    */
  def switchOverlay(actualPanel: JPanel, panelToShow: JPanel)

  /**
    * Gets the game menù.
    *
    * @return game menù panel.
    */
  def getInGameMenuPanel: JPanel

  /**
    * Gets the game panel.
    *
    * @return game panel.
    */
  def getGamePanel: JPanel

  /**
    * Gets the menù.
    *
    * @return menù panel.
    */
  def getMenuUtils: Menu

  /**
    * Initializes or restores the game.
    */
  def initOrRestoreGUI()

  /**
    * Makes move.
    *
    * @param fromCoordinate
    *              starting coordinate.
    * @param toCoordinate
    *              arrival coordiante.
    */
  def makeMove(fromCoordinate: Pair[Int], toCoordinate: Pair[Int])

  /**
    * Gets the possible moves from a specifies coordiante.
    *
    * @param coordinate
    *              starting coordinate.
    * @return sequence of possible coordinates.
    */
  def getPossibleMoves(coordinate: Pair[Int]): Seq[Pair[Int]]

  /**
    * Sets the move made by the user.
    *
    * @param playerToMove
    *                 next player to move.
    * @param winner
    *                 possible winner.
    * @param currentBoard
    *                 board updated after the move.
    * @param nBlackCaptured
    *                 total number of black pieces captured.
    * @param nWhiteCaptured
    *                 total number of white pieces captured.
    */
  def updateMove(playerToMove: Player.Value, winner: Player.Value, board: Board, nBlackCaptured: Int, nWhiteCaptured: Int)

  /**
    * Sets the end game.
    *
    * @param winner
    *                 winner of the game.
    * @param kingCoordinate
    *                 king's coordinate in the board.
    */
  def setEndGame(winner: Player.Value, kingCoordinate: Option[Pair[Int]])

  /**
   * Checks if the cell at the specified coordinate is the central cell.
   *
   * @param coordinate
   *                   coordinate of the cell to inspect
   *
   * @return boolean.
   */
  def isCentralCell(coordinate: Pair[Int]): Boolean

  /**
   * Checks if the cell at the specified coordinate is a corner cell.
   *
   * @param coordinate
   *                   coordinate of the cell to inspect
   *
   * @return boolean.
   */
  def isCornerCell(coordinate: Pair[Int]): Boolean
}

object GameView {

  def apply(controller: ControllerHnefatafl): GameView = GameViewImpl(controller)

  case class GameViewImpl(controller: ControllerHnefatafl) extends GameView {

    private var menuPanel, variantsPanel, diffPanel, inGameMenuPanel, playerChoicePanel: JPanel = _
    private var dimension: Int = _
    private var board: Board = _
    private val viewFactory: ViewFactory = new ViewFactoryImpl
    private val menuUtils: Menu = Menu(viewFactory, this)
    private val gameUtils: Game = Game(viewFactory, this)
    private val frame: JFrame = viewFactory.createFrame
    private val overlayPanel: JPanel = viewFactory.createOverlayLayoutPanel
    private var gamePanel: JPanel = viewFactory.createGamePanel

    initMainMenu()
    overlayPanel.add(menuPanel)
    initVariantsMenu()
    overlayPanel.add(variantsPanel)
    initDiffMenu()
    overlayPanel.add(diffPanel)
    initPlayerChoiceMenu()
    overlayPanel.add(playerChoicePanel)
    initInGameMenu()
    overlayPanel.add(inGameMenuPanel)

    frame.add(overlayPanel)
    frame.setVisible(true)

    override def getDimension: Int = dimension

    override def switchOverlay(actualPanel: JPanel, panelToShow: JPanel): Unit = {
      actualPanel.setVisible(false)
      panelToShow.setVisible(true)
    }

    override def getInGameMenuPanel: JPanel = inGameMenuPanel

    override def getGamePanel: JPanel = gamePanel

    override def getMenuUtils: Menu = menuUtils

    override def initOrRestoreGUI(): Unit = {
      if (gamePanel.getComponents.length > 0) {
        gameUtils.restoreGame()
        overlayPanel.remove(gamePanel)
      }
      val newGame: (Board, Player.Value) = controller.newGame(menuUtils.getBoardVariant)
      board = newGame._1.asInstanceOf[Board]
      dimension = board.size
      initGamePanel(board)
      overlayPanel.add(gamePanel)
      showGame()
      gameUtils.getLabelPlayer.setText(newGame._2 + " moves.")
    }

    override def makeMove(fromCoordinate: Pair[Int], toCoordinate: Pair[Int]): Unit = {
      controller.makeMove(fromCoordinate, toCoordinate)
    }

    override def getPossibleMoves(coordinate: Pair[Int]): Seq[Pair[Int]] = {
      controller.getPossibleMoves(coordinate)
    }

    override def updateMove(playerToMove: Player.Value, winner: Player.Value, board: Board, nBlackCaptured: Int, nWhiteCaptured: Int): Unit = {
      gameUtils.updateMove(playerToMove, winner, board, nBlackCaptured, nWhiteCaptured)
    }

    override def setEndGame(winner: Player.Value, kingCoordinate: Option[Pair[Int]]): Unit = {
      gameUtils.setEndGame(winner, kingCoordinate)
    }

    override def isCentralCell(coordinate: Pair[Int]): Boolean = controller.isCentralCell(coordinate)

    override def isCornerCell(coordinate: Pair[Int]): Boolean = controller.isCornerCell(coordinate)

    /**
      * Initializes the main menù.
      */
    private def initMainMenu(): Unit = {
      menuPanel = menuUtils.initMenu
    }

    /**
      * Initializes the variant menù.
      */
    private def initVariantsMenu(): Unit = {
      variantsPanel = menuUtils.initVariantsMenu
    }

    /**
      * Initializes the difficult selection menù.
      */
    private def initDiffMenu(): Unit = {
      diffPanel = menuUtils.initDiffMenu
    }

    /**
      * Initializes the player selection menù.
      */
    private def initPlayerChoiceMenu(): Unit = {
      playerChoicePanel = menuUtils.initPlayerChoiceMenu
    }

    /**
      * Initializes the game menù.
      */
    private def initInGameMenu(): Unit = {
      inGameMenuPanel = menuUtils.initInGameMenu
    }

    /**
      * Initializes the game panel.
      *
      * @param board
      *               board returned from parser.
      */
    private def initGamePanel(board: Board): Unit = {
      gamePanel = gameUtils.initGamePanel(board)
    }

    /**
      * Show the game panel.
      */
    private def showGame(): Unit = {
      playerChoicePanel.setVisible(false)
      gamePanel.setVisible(true)
    }
  }
}
