package view.game

import java.awt.event.ActionListener
import java.awt.{Dimension, GridBagConstraints}

import javax.swing._
import model.game.BoardGame.{Board, BoardCell}
import model.game.Piece.Piece
import model.game.Player.Player
import model.game.Snapshot.Snapshot
import model.game._
import view.ViewHnefatafl
import view.factories.{Cell, ColorProvider, GameFactory, MenuFactory}
import view.utils.{GridConstraints, ScreenSize}
import view.utils.JPanelAddAll._

import scala.collection.mutable

/**
 * Represents a hnefatafl game view
 */
trait ViewGame {

  /**
    * Returns the initialized game panel.
    *
    * @return the initialized game panel
    */
  def initGamePanel(): JPanel

  /**
    * Restores the game.
    */
  def restoreGame()

  /**
    * Returns player's to move or winner's label.
    *
    * @return player's to move or winner's label
    */
  def getLabelPlayer: JLabel

  /**
    * Shows the specified snapshot of the game.
    *
    * @param gameSnapshot
    *         snapshot to show.
    */
  def update(gameSnapshot: GameSnapshot)

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
 * Represents a hnefatafl game view
 */
object ViewGame {

  def apply(gameView: ViewHnefatafl): ViewGame = ViewMatchImpl(gameView)

  case class ViewMatchImpl(view: ViewHnefatafl) extends ViewGame {

    private val smallerSide = ScreenSize.getSmallerSide

    private val HEIGHT_DIMENSION = smallerSide * 8 / 100
    private val SMALL_WIDTH_DIMENSION = smallerSide * 11 / 100
    private val MEDIUM_WIDTH_DIMENSION = smallerSide * 20 / 100
    private val BIG_WIDTH_DIMENSION = smallerSide * 30 / 100
    private val BETWEEN_COMPONENTS_DIMENSION = smallerSide * 53 / 100
    private val WIDTH_BETWEEN_LABELS_DIMENSION = smallerSide * 6 / 100

    private var gamePanel, northPanel, subNorthPanel, southPanel, subSouthPanel, boardPanel, boardPlusColumns,
        leftPanel, rightPanel: JPanel = _

    private var menuButton, firstMoveButton, nextMoveButton, previousMoveButton, lastMoveButton, undoMoveButton: JButton = _

    private var playerOrWinnerLabel: JLabel = _
    private val playerWhiteLabel: JLabel = MenuFactory.createLabelWhitePlayer
    private val playerBlackLabel: JLabel = MenuFactory.createLabelBlackPlayer

    private val cells: mutable.HashMap[Coordinate, Cell] = mutable.HashMap.empty
    private var possibleMoves: Seq[Coordinate] = Seq.empty
    private var selectedCell: Option[Coordinate] = Option.empty
    private var lastMoveCells: Option[(Cell, Cell)] = Option.empty
    private var kingCoordinate: Option[Coordinate] = Option.empty

    private val winString: String = " has won"
    private val movesString: String = " moves"

    /**
     * @inheritdoc
     */
    override def initGamePanel(): JPanel = {
      GameFactory.setVariantBoardSize(this.view.getDimension)

      initNorthPanel()
      initSouthPanel()
      initLeftRightPanel()
      initBoard()

      gamePanel = GameFactory.createGamePanel
      gamePanel.add(northPanel)

      boardPlusColumns = GameFactory.createBoardPlusColumnsPanel
      boardPlusColumns.addAll(leftPanel)(boardPanel)(rightPanel)

      gamePanel.addAll(boardPlusColumns)(southPanel)
      gamePanel
    }

    /**
     * @inheritdoc
     */
    override def restoreGame(): Unit = {
      cells.clear()
      gamePanel.removeAll()
    }

    /**
     * @inheritdoc
     */
    override def getLabelPlayer: JLabel = playerOrWinnerLabel

    /**
     * @inheritdoc
     */
    override def update(gameSnapshot: GameSnapshot): Unit = {
      addLostPawns(gameSnapshot.getNumberCapturedBlacks, gameSnapshot.getNumberCapturedWhites)
      drawPawns(gameSnapshot.getBoard)

      possibleMoves.foreach(c => cells(c).unsetAsPossibleMove())
      deselectCell()

      if(kingCoordinate.nonEmpty)
        cells(kingCoordinate.get).resetKingCell()

      if(!gameSnapshot.getWinner.equals(Player.None))
        kingCoordinate = Option(view.findKing())

      setStatusGame(gameSnapshot.getWinner, gameSnapshot.getPlayerToMove)
      resetLastMoveCells()
      if(gameSnapshot.getLastMove.nonEmpty)
        highlightLastMove(gameSnapshot.getLastMove.get)

      boardPanel.repaint()
      boardPanel.validate()
      gamePanel.validate()
    }

    /**
     * @inheritdoc
     */
    override def activeNextLast(): Unit = {
      nextMoveButton.setEnabled(true)
      lastMoveButton.setEnabled(true)
      undoMoveButton.setEnabled(false)
    }

    /**
     * @inheritdoc
     */
    override def disableNextLast(): Unit = {
      nextMoveButton.setEnabled(false)
      lastMoveButton.setEnabled(false)
      undoMoveButton.setEnabled(true)
    }

    /**
     * @inheritdoc
     */
    override def activeFirstPrevious(): Unit = {
      previousMoveButton.setEnabled(true)
      firstMoveButton.setEnabled(true)
    }

    /**
     * @inheritdoc
     */
    override def disableFirstPrevious(): Unit = {
      previousMoveButton.setEnabled(false)
      firstMoveButton.setEnabled(false)
    }

    /**
     * @inheritdoc
     */
    override def activeUndo(): Unit = undoMoveButton.setEnabled(true)

    /**
     * @inheritdoc
     */
    override def disableUndo(): Unit = undoMoveButton.setEnabled(false)

    private def setStatusGame(winner: Player, playerToMove: Player): Unit = winner match {
      case Player.White =>
        playerOrWinnerLabel.setForeground(ColorProvider.getWhiteColor)
        playerOrWinnerLabel.setText(winText(Player.White))
        playerWhiteLabel.setVisible(true)
        playerBlackLabel.setVisible(false)
        cells(kingCoordinate.get).setAsKingEscapedCell()
        resetListeners()

      case Player.Black =>
        playerOrWinnerLabel.setForeground(ColorProvider.getBlackColor)
        playerOrWinnerLabel.setText(winText(Player.Black))
        playerWhiteLabel.setVisible(false)
        playerBlackLabel.setVisible(true)
        cells(kingCoordinate.get).setAsKingCapturedCell()
        resetListeners()

      case Player.Draw =>
        playerOrWinnerLabel.setText(Player.Draw.toString)
        resetListeners()

      case _ => switchPlayerLabel(playerToMove)
    }

    private def winText(player: Player): String = player.toString + winString

    private def resetListeners(): Unit = cells.values.foreach(c => c.getActionListeners.foreach(c.removeActionListener))

    private def switchPlayerLabel(playerToMove: Player): Unit = {
      playerOrWinnerLabel.setText(playerToMove + movesString)
      if(playerToMove.equals(Player.White)) {
        playerBlackLabel.setVisible(false)
        playerWhiteLabel.setVisible(true)
      } else {
        playerBlackLabel.setVisible(true)
        playerWhiteLabel.setVisible(false)
      }
    }

    private def highlightLastMove(lastMove: Move): Unit = {
      lastMoveCells = Option(cells(lastMove.from), cells(lastMove.to))
      lastMoveCells.get._1.setAsLastMoveCell()
      lastMoveCells.get._2.setAsLastMoveCell()
    }

    private def resetLastMoveCells(): Unit = {
      if(lastMoveCells.nonEmpty) {
        lastMoveCells.get._1.unsetAsLastMoveCell()
        lastMoveCells.get._2.unsetAsLastMoveCell()
      }
    }

    private def initBoard(): Unit = {
      boardPanel = GameFactory.createBoardPanel
      GridConstraints.setAnchor(GridBagConstraints.LINE_START)
      for (i <- 1 to view.getDimension) {
        for (j <- 1 to view.getDimension) {
          val coordinate: Coordinate = Coordinate(i, j)
          val cell: Cell = setTypeCell(coordinate)
          cell.addActionListener(_ => actionCell(cell))
          GridConstraints.setXYConstraints(j, i)
          cells += coordinate -> cell
          boardPanel.add(cell, GridConstraints.getLimits)
        }
      }
    }

    private def initNorthPanel(): Unit = {
      northPanel = GameFactory.createTopBottomPanel
      subNorthPanel = GameFactory.createGameSubMenuPanel

      GridConstraints.setAnchor(GridBagConstraints.LINE_START)

      GridConstraints.resetXConstraints()
      playerBlackLabel.setVisible(true)
      playerWhiteLabel.setVisible(false)
      subNorthPanel.add(playerBlackLabel, GridConstraints.getLimits)
      subNorthPanel.add(playerWhiteLabel, GridConstraints.getLimits)

      subNorthPanel.add(Box.createRigidArea(new Dimension(WIDTH_BETWEEN_LABELS_DIMENSION, 0)),GridConstraints.getLimits)

      playerOrWinnerLabel = GameFactory.createLabelPlayerToMoveWinner
      GridConstraints.incrementXConstraints()
      subNorthPanel.add(playerOrWinnerLabel, GridConstraints.getLimits)

      menuButton = GameFactory.createMenuButton()
      menuButton.addActionListener(_ => view.switchOverlay(gamePanel, view.getInGameMenuPanel))

      northPanel.addAll(Box.createRigidArea(new Dimension(SMALL_WIDTH_DIMENSION, HEIGHT_DIMENSION)))(subNorthPanel)(
        Box.createRigidArea(new Dimension(BETWEEN_COMPONENTS_DIMENSION, HEIGHT_DIMENSION)))(
        menuButton)(Box.createRigidArea(new Dimension(MEDIUM_WIDTH_DIMENSION, HEIGHT_DIMENSION)))
    }

    private def initSouthPanel(): Unit = {
      southPanel = GameFactory.createTopBottomPanel
      southPanel.add(Box.createRigidArea(new Dimension(BIG_WIDTH_DIMENSION, HEIGHT_DIMENSION)))
      subSouthPanel = GameFactory.createGameSubMenuPanel

      GridConstraints.resetXConstraints()
      GridConstraints.incrementWeightXConstraints()
      GridConstraints.setAnchor(GridBagConstraints.CENTER)

      firstMoveButton = GameFactory.createFirstMoveButton()
      initSouthPanelButton(firstMoveButton, _ => changeSnapshot(Snapshot.First))
      previousMoveButton = GameFactory.createPreviousMoveButton()
      initSouthPanelButton(previousMoveButton, _ => changeSnapshot(Snapshot.Previous))
      nextMoveButton = GameFactory.createNextMoveButton()
      initSouthPanelButton(nextMoveButton, _ => changeSnapshot(Snapshot.Next))
      lastMoveButton = GameFactory.createLastMoveButton()
      initSouthPanelButton(lastMoveButton, _ => changeSnapshot(Snapshot.Last))
      undoMoveButton = GameFactory.createUndoMoveButton()
      initSouthPanelButton(undoMoveButton, _ => undoMove())

      southPanel.add(subSouthPanel)
      southPanel.add(Box.createRigidArea(new Dimension(SMALL_WIDTH_DIMENSION, HEIGHT_DIMENSION)))
    }

    private def initSouthPanelButton(b: JButton, al: ActionListener): Unit = {
      b.addActionListener(al)
      subSouthPanel.add(b, GridConstraints.getLimits)
      GridConstraints.incrementXConstraints()
    }

    private def initLeftRightPanel(): Unit = {
      leftPanel = GameFactory.createLeftRightPanel(1, view.getDimension)
      rightPanel = GameFactory.createLeftRightPanel(1, view.getDimension)
    }

    private def actionCell(cell: JButton): Unit = {
      if(cell.getComponents.length > 0 && possibleMoves.isEmpty)
        actionSelectCell(cell)
      else if(possibleMoves.nonEmpty && !possibleMoves.contains(getCoordinate(cell))) {
        actionDeselectCell()
        actionSelectCell(cell)
      } else if(possibleMoves.contains(getCoordinate(cell)) && selectedCell.isDefined)
        actionMovePawn(cell)
      else
        actionDeselectCell()
    }

    private def actionSelectCell(cell: JButton): Unit = {
      selectedCell = Option(getCoordinate(cell))
      moveRequest(getCoordinate(cell))
    }

    private def actionDeselectCell(): Unit = {
      possibleMoves.foreach(c => cells(c).unsetAsPossibleMove())
      deselectCell()
    }

    private def actionMovePawn(cell: JButton): Unit = view.makeMove(Move(selectedCell.get, getCoordinate(cell)))

    private def addLostPawns(nBlackCaptured: Int, nWhiteCaptured: Int): Unit = {
      drawLostPawns(Player.Black, nBlackCaptured)
      drawLostPawns(Player.White, nWhiteCaptured)
    }

    private def drawLostPawns(player: Player, length: Int): Unit = {
      val panel: JPanel = if (Player.Black eq player) leftPanel else rightPanel
      panel.removeAll()
      for (_ <- 0 until length) panel.add(createLostPawn(player))
      panel.repaint()
    }

    private def createLostPawn(player: Player): JLabel = player match {
      case Player.Black => GameFactory.createLostBlackPawn
      case _ => GameFactory.createLostWhitePawn
    }

    private def getCoordinate(cell: JButton): Coordinate = cells.filter(v => v._2.equals(cell)).head._1

    private def moveRequest(coordinate: Coordinate): Unit = {
      possibleMoves = view.getPossibleMoves(coordinate)
      possibleMoves.foreach(c => cells(c).setAsPossibleMove())
      if(possibleMoves.nonEmpty)
        cells(selectedCell.get).setAsSelectedCell()
    }

    private def deselectCell(): Unit = {
      if(selectedCell.nonEmpty)
        cells(selectedCell.get).unsetAsSelectedCell()
      selectedCell = Option.empty
      possibleMoves = Seq.empty
    }

    private def drawPawns(board: Board): Unit = {
      board.rows.foreach(_.foreach(c => {
        val button: JButton = cells(c.getCoordinate)
        if (button.getComponentCount > 0)
          button.removeAll()
        pawnChoice(c)
      }))
    }

    private def pawnChoice(cell: BoardCell): Unit = {
      val piece: Piece = cell.getPiece
      val button: JButton = cells(cell.getCoordinate)
      piece match {
        case Piece.WhitePawn => button.add(GameFactory.createWhitePawn)
        case Piece.BlackPawn => button.add(GameFactory.createBlackPawn)
        case Piece.WhiteKing => button.add(GameFactory.createWhiteKing)
        case _ =>
      }
    }

    private def setTypeCell(cell: Coordinate): Cell = cell match {
      case coordinate if view.isCornerCell(coordinate) => GameFactory.createCornerCell()
      case coordinate if view.isCentralCell(coordinate) => GameFactory.createCenterCell()
      case coordinate if view.isPawnCell(coordinate) => GameFactory.createPawnCell()
      case _ => GameFactory.createNormalCell()
    }

    private def changeSnapshot(snapshotToShow: Snapshot): Unit = view.changeSnapshot(snapshotToShow)

    private def undoMove(): Unit = {
      cells.values.foreach(c => if(c.getActionListeners.length == 0) c.addActionListener(_ => actionCell(c)))
      view.undoMove()
    }
  }
}
