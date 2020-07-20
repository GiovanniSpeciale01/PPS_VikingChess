package view

import controller.ControllerHnefatafl
import javax.swing.{JFrame, JPanel}
import model.game.Snapshot.Snapshot
import view.utils.JPanelAddAll._
import model.game.{Coordinate, GameSnapshot, Move}
import view.factories.{GameFactory, MenuFactory}
import view.game.ViewGame
import view.menu.ViewMenu

/**
 * Represents the hnefatafl game main viewer which shows the right overlay and
 * communicates with the controller on behalf of the other components of the view.
 */
trait ViewHnefatafl {
  /**
    * Calls the controller to get the board dimension according to game variant.
    *
    * @return board dimension
    */
  def getDimension: Int

  /**
    * Switches panel in overlay.
    *
    * @param actualPanel
    *         panel shown.
    * @param panelToShow
    *         panel to show.
    */
  def switchOverlay(actualPanel: JPanel, panelToShow: JPanel)

  /**
    * Returns the in game menu.
    *
    * @return in game menu.
    */
  def getInGameMenuPanel: JPanel

  /**
    * Returns the game panel.
    *
    * @return game panel.
    */
  def getGamePanel: JPanel

  /**
    * Returns the main menu panel.
    *
    * @return main menu panel.
    */
  def getMainMenu: ViewMenu

  /**
    * Resets the game.
    */
  def resetGUI()

  /**
    * Calls the controller to make a move.
    *
    * @param move
    *         move to make
    */
  def makeMove(move: Move)

  /**
    * Calls the controller for getting the possible moves from a specified coordinate.
    *
    * @param coordinate
    *          starting coordinate.
    * @return sequence of possible coordinates.
    */
  def getPossibleMoves(coordinate: Coordinate): Seq[Coordinate]

  /**
    * Updates the board in view.
    *
    * @param gameSnapshot
    *         snapshot to show.
    */
  def update(gameSnapshot: GameSnapshot)

  /**
   * Calls the controller to check if the cell at the specified coordinate is the central cell.
   *
   * @param coordinate
   *          coordinate of the cell to inspect
   *
   * @return if the cell at the specified coordinate is the central cell.
   */
  def isCentralCell(coordinate: Coordinate): Boolean

  /**
   * Calls the controller to check if the cell at the specified coordinate is a corner cell.
   *
   * @param coordinate
   *         coordinate of the cell to inspect
   *
   * @return if the cell at the specified coordinate is a corner cell.
   */
  def isCornerCell(coordinate: Coordinate): Boolean

  /**
    * Calls the controller to check if the cell at the specified coordinate is a initial pawn cell.
    *
    * @param coordinate
    *         coordinate of the cell to inspect
    *
    * @return if the cell at the specified coordinate is a initial pawn cell.
    */
  def isPawnCell(coordinate: Coordinate): Boolean

  /**
    * Calls the controller to get the king coordinate in the current board.
    *
    * @return king coordinate.
    */
  def findKing(): Coordinate

  /**
    * Calls the controller to switch to a previous or later state of the game.
    *
    * @param snapshotToShow
    *         indicates snapshot to show.
    */
  def changeSnapshot(snapshotToShow: Snapshot)

  /**
   * Calls the controller to undo the last move.
   */
  def undoMove()

  /**
    * Enables next and last move buttons.
    */
  def activeNextLast()

  /**
   * Disables next and last move buttons.
   */
  def disableNextLast()

  /**
    * Enables previous and first move buttons.
    */
  def activeFirstPrevious()

  /**
   * Disables previous and first move buttons.
   */
  def disableFirstPrevious()

  /**
    * Enables undo move button.
    */
  def activeUndo()

  /**
   * Disables undo move button.
   */
  def disableUndo()
}

/**
 * Represents the hnefatafl game main viewer which shows the right overlay and
 * communicates with the controller on behalf of the other components of the view.
 */
object ViewHnefatafl {

  def apply(): ViewHnefatafl = ViewHnefataflImpl()

  case class ViewHnefataflImpl() extends ViewHnefatafl {

    private var menuPanel, variantsPanel, difficultyPanel, inGameMenuPanel, playerChoicePanel: JPanel = _
    private var dimension: Int = _
    private val viewMainMenu: ViewMenu = ViewMenu(this)
    private val viewGame: ViewGame = ViewGame(this)
    private val frame: JFrame = MenuFactory.createFrame
    private val overlayPanel: JPanel = GameFactory.createOverlayLayoutPanel
    private var gamePanel: JPanel = GameFactory.createGamePanel

    menuPanel = viewMainMenu.initMenu
    variantsPanel = viewMainMenu.initVariantsMenu
    difficultyPanel = viewMainMenu.initDiffMenu
    playerChoicePanel = viewMainMenu.initPlayerChoiceMenu
    inGameMenuPanel = viewMainMenu.initInGameMenu

    overlayPanel.addAll(menuPanel)(variantsPanel)(difficultyPanel)(playerChoicePanel)(inGameMenuPanel)

    frame.add(overlayPanel)
    frame.setVisible(true)

    /**
     * @inheritdoc
     */
    override def getDimension: Int = ControllerHnefatafl.getDimension

    /**
     * @inheritdoc
     */
    override def switchOverlay(actualPanel: JPanel, panelToShow: JPanel): Unit = {
      actualPanel.setVisible(false)
      panelToShow.setVisible(true)
    }

    /**
     * @inheritdoc
     */
    override def getInGameMenuPanel: JPanel = inGameMenuPanel

    /**
     * @inheritdoc
     */
    override def getGamePanel: JPanel = gamePanel

    /**
     * @inheritdoc
     */
    override def getMainMenu: ViewMenu = viewMainMenu

    /**
     * @inheritdoc
     */
    override def resetGUI(): Unit = {
      if (gamePanel.getComponents.length > 0) {
        viewGame.restoreGame()
        overlayPanel.remove(gamePanel)
      }
      val newGame: GameSnapshot = ControllerHnefatafl.newGame(viewMainMenu.getBoardVariant, viewMainMenu.getGameMode, viewMainMenu.getDifficulty, viewMainMenu.getPlayer)
      dimension = newGame.getBoard.size
      gamePanel = viewGame.initGamePanel()
      overlayPanel.add(gamePanel)
      viewGame.update(newGame)
      showGame()
      ControllerHnefatafl.startGame()
    }

    /**
     * @inheritdoc
     */
    override def makeMove(move: Move): Unit = ControllerHnefatafl.makeMove(move)

    /**
     * @inheritdoc
     */
    override def getPossibleMoves(coordinate: Coordinate): Seq[Coordinate] = ControllerHnefatafl.getPossibleMoves(coordinate)

    /**
     * @inheritdoc
     */
    override def update(gameSnapshot: GameSnapshot): Unit = viewGame.update(gameSnapshot)

    /**
     * @inheritdoc
     */
    override def isCentralCell(coordinate: Coordinate): Boolean = ControllerHnefatafl.isCentralCell(coordinate)

    /**
     * @inheritdoc
     */
    override def isCornerCell(coordinate: Coordinate): Boolean = ControllerHnefatafl.isCornerCell(coordinate)

    /**
     * @inheritdoc
     */
    override def isPawnCell(coordinate: Coordinate): Boolean = ControllerHnefatafl.isPawnCell(coordinate)

    /**
     * @inheritdoc
     */
    override def findKing(): Coordinate = ControllerHnefatafl.findKing()

    /**
     * @inheritdoc
     */
    override def changeSnapshot(snapshotToShow: Snapshot): Unit = ControllerHnefatafl.changeSnapshot(snapshotToShow)

    /**
     * @inheritdoc
     */
    override def undoMove(): Unit = ControllerHnefatafl.undoMove()

    /**
     * @inheritdoc
     */
    override def disableNextLast(): Unit = viewGame.disableNextLast()

    /**
     * @inheritdoc
     */
    override def disableFirstPrevious(): Unit = viewGame.disableFirstPrevious()

    /**
     * @inheritdoc
     */
    override def activeUndo(): Unit = viewGame.activeUndo()

    /**
     * @inheritdoc
     */
    override def disableUndo(): Unit = viewGame.disableUndo()

    /**
     * @inheritdoc
     */
    override def activeNextLast(): Unit = viewGame.activeNextLast()

    /**
     * @inheritdoc
     */
    override def activeFirstPrevious(): Unit = viewGame.activeFirstPrevious()

    private def showGame(): Unit = {
      playerChoicePanel.setVisible(false)
      variantsPanel.setVisible(false)
      gamePanel.setVisible(true)
    }
  }
}
