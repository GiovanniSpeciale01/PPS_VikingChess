package view

import java.awt.{Dimension, GridBagConstraints}

import javax.swing._
import model._
import utils.BoardGame.{Board, BoardCell}
import utils.{Coordinate, Move}

import scala.collection.mutable

trait ViewMatch {

  /**
    * Initializes the game panel.
    *
    * @param board
*                 board returned from parser.
    *
    * @return panel
    */
  def initGamePanel(board: Board): JPanel

  /**
    * Restores the game.
    */
  def restoreGame()

  /**
    * Gets the label of the player to move or of the winner.
    *
    * @return label
    */
  def getLabelPlayer: JLabel

  /**
    * Updates view to current snapshot.
    *
    * @param gameSnapshot
    *                 snapshot to update.
    */
  def update(gameSnapshot: GameSnapshot)

  /**
    * Shows the specified snapshot of the game.
    *
    * @param gameSnapshot
    *                 snapshot to show.
    */
  def updateSnapshot(gameSnapshot: GameSnapshot)

  /**
    * Actives/Disables next and last move.
    */
  def activeNextLast()
  def disableNextLast()

  /**
    * Actives/Disables previous and first move.
    */
  def activeFirstPrevious()
  def disableFirstPrevious()

  /**
    * Actives/Disables undo move.
    */
  def activeUndo()
  def disableUndo()

}

object ViewMatch {

  def apply(gameView: ViewHnefatafl): ViewMatch = ViewMatchImpl(gameView)

  case class ViewMatchImpl(view: ViewHnefatafl) extends ViewMatch {

    private val HEIGHT_DIMENSION = GameFactory.getSmallerSide * 8 / 100
    private val SMALL_WIDTH_DIMENSION = GameFactory.getSmallerSide * 10 / 100
    private val MEDIUM_WIDTH_DIMENSION = GameFactory.getSmallerSide * 20 / 100
    private val BIG_WIDTH_DIMENSION = GameFactory.getSmallerSide * 30 / 100
    private val BETWEEN_COMPONENTS_DIMENSION = GameFactory.getSmallerSide * 55 / 100

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

    private var limits: GridBagConstraints = _

    override def initGamePanel(initialBoard: Board): JPanel = {
      limits = GameFactory.createBagConstraints
      limits.fill = GridBagConstraints.NONE
      limits.anchor = GridBagConstraints.LINE_START

      GameFactory.setVariantBoardSize(this.view.getDimension)

      initNorthPanel()
      initSouthPanel()
      initLeftRightPanel()
      initBoard()
      drawPawns(initialBoard)

      gamePanel = GameFactory.createGamePanel
      gamePanel.add(northPanel)

      boardPlusColumns = GameFactory.createBoardPlusColumnsPanel
      boardPlusColumns.add(leftPanel)
      boardPlusColumns.add(boardPanel)
      boardPlusColumns.add(rightPanel)

      gamePanel.add(boardPlusColumns)
      gamePanel.add(southPanel)
      gamePanel
    }

    override def restoreGame(): Unit = {
      cells.clear()
      gamePanel.removeAll()
    }

    override def getLabelPlayer: JLabel = playerOrWinnerLabel

    override def update(gameSnapshot: GameSnapshot): Unit = {
      updateSnapshot(gameSnapshot)

      //makeMoveIA(gameSnapshot)
    }

    override def updateSnapshot(gameSnapshot: GameSnapshot): Unit = {
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

    override def activeNextLast(): Unit = {
      nextMoveButton.setEnabled(true)
      lastMoveButton.setEnabled(true)
      undoMoveButton.setEnabled(false)
    }

    override def disableNextLast(): Unit = {
      nextMoveButton.setEnabled(false)
      lastMoveButton.setEnabled(false)
      undoMoveButton.setEnabled(true)
    }

    override def activeFirstPrevious(): Unit = {
      previousMoveButton.setEnabled(true)
      firstMoveButton.setEnabled(true)
    }

    override def disableFirstPrevious(): Unit = {
      previousMoveButton.setEnabled(false)
      firstMoveButton.setEnabled(false)
    }

    override def activeUndo(): Unit = undoMoveButton.setEnabled(true)

    override def disableUndo(): Unit = undoMoveButton.setEnabled(false)

    /**
      * Sets the end game.
      *
      * @param winner
      *                 winner of the game.
      */
    private def setStatusGame(winner: Player.Val, playerToMove: Player.Val): Unit = winner match {
      case Player.White =>
        playerOrWinnerLabel.setForeground(ColorProvider.getWhiteColor)
        playerOrWinnerLabel.setText("White has Won!")
        playerWhiteLabel.setVisible(true)
        playerBlackLabel.setVisible(false)
        cells(kingCoordinate.get).setAsKingEscapedCell()
        resetListeners()

      case Player.Black =>
        playerOrWinnerLabel.setForeground(ColorProvider.getBlackColor)
        playerOrWinnerLabel.setText("Black has Won!")
        playerWhiteLabel.setVisible(false)
        playerBlackLabel.setVisible(true)
        cells(kingCoordinate.get).setAsKingCapturedCell()
        resetListeners()

      case Player.Draw =>
        playerOrWinnerLabel.setText("Draw!")
        resetListeners()

      case _ => switchPlayerLabel(playerToMove)
    }

    /**
      * Resets all listeners for each cell button.
      */
    private def resetListeners(): Unit = cells.values.foreach(c => c.getActionListeners.foreach(c.removeActionListener))

    /**
      * Switches the player label showed.
      */
    private def switchPlayerLabel(playerToMove: Player.Val): Unit = {
      playerOrWinnerLabel.setText(playerToMove + " moves")
      if(playerToMove.equals(Player.White)) {
        playerBlackLabel.setVisible(false)
        playerWhiteLabel.setVisible(true)
      } else {
        playerBlackLabel.setVisible(true)
        playerWhiteLabel.setVisible(false)
      }
    }

    /**
      * Highlights the cells where the last move occured.
      *
      * @param lastMove
      *                 last move fromCoordinate and toCoordinate
      */
    private def highlightLastMove(lastMove: Move): Unit = {
      lastMoveCells = Option(cells(lastMove.from), cells(lastMove.to))
      lastMoveCells.get._1.setAsLastMoveCell()
      lastMoveCells.get._2.setAsLastMoveCell()
    }

    /**
      * Resets the cells of the last move.
      */
    private def resetLastMoveCells(): Unit = {
      if(lastMoveCells.nonEmpty) {
        lastMoveCells.get._1.unsetAsLastMoveCell()
        lastMoveCells.get._2.unsetAsLastMoveCell()
      }
    }

    /**
      * Initializes the board panel.
      */
    private def initBoard(): Unit = {
      boardPanel = GameFactory.createBoardPanel
      for (i <- 1 to view.getDimension) {
        for (j <- 1 to view.getDimension) {
          val coordinate: Coordinate = Coordinate(i, j)
          val cell: Cell = setTypeCell(coordinate)
          cell.addActionListener(_ => actionCell(cell))
          GameFactory.setXYConstraints(limits,j,i)
          cells += coordinate -> cell
          boardPanel.add(cell, limits)
        }
      }
    }

    /**
      * Initializes the north panel.
      */
    private def initNorthPanel(): Unit = {
      northPanel = GameFactory.createTopBottomPanel
      northPanel.add(Box.createRigidArea(new Dimension(SMALL_WIDTH_DIMENSION, HEIGHT_DIMENSION)))
      subNorthPanel = GameFactory.createGameSubMenuPanel

      GameFactory.resetXConstraints(limits)
      playerBlackLabel.setVisible(true)
      playerWhiteLabel.setVisible(false)
      subNorthPanel.add(playerBlackLabel, limits)
      subNorthPanel.add(playerWhiteLabel, limits)

      playerOrWinnerLabel = GameFactory.createLabelPlayerToMoveWinner
      GameFactory.incrementXConstraints(limits)
      subNorthPanel.add(playerOrWinnerLabel, limits)
      northPanel.add(subNorthPanel)

      northPanel.add(Box.createRigidArea(new Dimension(BETWEEN_COMPONENTS_DIMENSION, HEIGHT_DIMENSION)))
      menuButton = GameFactory.createGameButton()
      menuButton.addActionListener(_ => view.switchOverlay(gamePanel, view.getInGameMenuPanel))
      northPanel.add(menuButton)

      northPanel.add(Box.createRigidArea(new Dimension(MEDIUM_WIDTH_DIMENSION, HEIGHT_DIMENSION)))
    }

    /**
      * Initializes the south panel.
      */
    private def initSouthPanel(): Unit = {
      southPanel = GameFactory.createTopBottomPanel
      southPanel.add(Box.createRigidArea(new Dimension(BIG_WIDTH_DIMENSION, HEIGHT_DIMENSION)))
      subSouthPanel = GameFactory.createGameSubMenuPanel

      GameFactory.resetXConstraints(limits)
      GameFactory.incrementWeightXConstraints(limits)
      limits.anchor = GridBagConstraints.CENTER
      firstMoveButton = GameFactory.createFirstMoveButton()
      firstMoveButton.addActionListener(_ => changeSnapshot(Snapshot.First))
      subSouthPanel.add(firstMoveButton, limits)

      previousMoveButton = GameFactory.createPreviousMoveButton()
      previousMoveButton.addActionListener(_ => changeSnapshot(Snapshot.Previous))
      GameFactory.incrementXConstraints(limits)
      subSouthPanel.add(previousMoveButton, limits)

      nextMoveButton = GameFactory.createNextMoveButton()
      nextMoveButton.addActionListener(_ => changeSnapshot(Snapshot.Next))
      GameFactory.incrementXConstraints(limits)
      subSouthPanel.add(nextMoveButton, limits)

      lastMoveButton = GameFactory.createLastMoveButton()
      lastMoveButton.addActionListener(_ => changeSnapshot(Snapshot.Last))
      GameFactory.incrementXConstraints(limits)
      subSouthPanel.add(lastMoveButton, limits)

      undoMoveButton = GameFactory.createUndoMoveButton()
      undoMoveButton.addActionListener(_ => undoMove())
      GameFactory.incrementXConstraints(limits)
      subSouthPanel.add(undoMoveButton, limits)
      southPanel.add(subSouthPanel)
      southPanel.add(Box.createRigidArea(new Dimension(SMALL_WIDTH_DIMENSION, HEIGHT_DIMENSION)))
    }

    /**
      * Initializes the panels of captures.
      */
    private def initLeftRightPanel(): Unit = {

      leftPanel = GameFactory.createLeftRightPanel(1, view.getDimension)
      rightPanel = GameFactory.createLeftRightPanel(1, view.getDimension)
    }

    /**
      * Defines the action to show the possible moves from a specifies cell.
      *
      * @param cell
      *               cell where possible moves begin.
      */
    private def actionCell(cell: JButton): Unit = {
      if(cell.getComponents.length > 0 && possibleMoves.isEmpty)
        actionSelectCell(cell)
      else if(possibleMoves.nonEmpty && !possibleMoves.contains(getCoordinate(cell))) {
        actionDeselectCell()
        actionSelectCell(cell)
      }
      else if(possibleMoves.contains(getCoordinate(cell)) && selectedCell.isDefined)
        actionMovePawn(cell)
      else
        actionDeselectCell()
    }

    /**
      * Defines the selection of a cell.
      *
      * @param cell
      *               starting cell.
      */
    private def actionSelectCell(cell: JButton): Unit = {
      selectedCell = Option(getCoordinate(cell))
      moveRequest(getCoordinate(cell))
    }

    /**
      * Defines the deselection of a cell.
      */
    private def actionDeselectCell(): Unit = {
      possibleMoves.foreach(c => cells(c).unsetAsPossibleMove())
      deselectCell()
    }

    /**
      * Defines the movement action from a cell to another cell.
      *
      * @param cell
      *               arrival cell.
      */
    private def actionMovePawn(cell: JButton): Unit = {
      view.makeMove(Move(selectedCell.get, getCoordinate(cell)))
    }

    /**
      * Updates the number of captured pieces white or black.
      *
      * @param nBlackCaptured
      *                 number of captured pieces black.
      * @param nWhiteCaptured
      *                 number of captured pieces white.
      */
    private def addLostPawns(nBlackCaptured: Int, nWhiteCaptured: Int): Unit = {
      drawLostPawns(Player.Black, nBlackCaptured)
      drawLostPawns(Player.White, nWhiteCaptured)
    }

    /**
      * Draws the number of captured pieces white or black.
      *
      * @param player
      *                 player to move.
      * @param length
      *                 number of captured pieces.
      */
    private def drawLostPawns(player: Player.Val, length: Int): Unit = {
      val panel: JPanel = if (Player.Black eq player) leftPanel else rightPanel
      panel.removeAll()
      for (_ <- 0 until length) {
        panel.add(createLostPawn(player))
      }
      panel.repaint()
    }

    /**
      * Adds the captured piece white or black.
      *
      * @return label
      */
    private def createLostPawn(player: Player.Val): JLabel = player match {
      case Player.Black => GameFactory.createLostBlackPawn
      case _ => GameFactory.createLostWhitePawn
    }

    /**
      * Gets the coordinate of a specifies cell.
      *
      * @param cell
      *               specifies cell.
      *
      * @return pair of coordinate
      */
    private def getCoordinate(cell: JButton): Coordinate = {
      cells.filter(v => v._2.equals(cell)).head._1
    }

    /**
      * Requires for a move.
      *
      * @param coordinate
      *               specifies coordinate.
      */
    private def moveRequest(coordinate: Coordinate): Unit = {
      possibleMoves = view.getPossibleMoves(coordinate)
      possibleMoves.foreach(c => cells(c).setAsPossibleMove())
      if(possibleMoves.nonEmpty)
        cells(selectedCell.get).setAsSelectedCell()
    }

    /**
      * Empties the selected cell and its possible moves.
      */
    private def deselectCell(): Unit = {
      if(selectedCell.nonEmpty)
        cells(selectedCell.get).unsetAsSelectedCell()
      selectedCell = Option.empty
      possibleMoves = Seq.empty
    }

    /**
      * Sets pieces in the board.
      *
      * @param board
      *             board.
      */
    private def drawPawns(board: Board): Unit = {
      board.rows.foreach(_.foreach(c => {
        val button: JButton = cells(c.getCoordinate)
        if (button.getComponentCount > 0) button.removeAll()
        pawnChoice(c)
      }))
    }

    /**
      * Sets the type of piece.
      *
      * @param cell
      *             cell to be set.
      */
    private def pawnChoice(cell: BoardCell): Unit = {
      val piece: Piece.Val = cell.getPiece
      val button: JButton = cells(cell.getCoordinate)
      piece match {
        case Piece.WhitePawn => button.add(GameFactory.createWhitePawn)
        case Piece.BlackPawn => button.add(GameFactory.createBlackPawn)
        case Piece.WhiteKing => button.add(GameFactory.createWhiteKing)
        case _ =>
      }
    }

    /**
     * Sets the type of cell.
     *
     * @param cell
     *             cell to be set.
     */
    private def setTypeCell(cell: Coordinate): Cell = cell match {
      case coordinate if view.isCornerCell(coordinate) => GameFactory.createCornerCell()
      case coordinate if view.isCentralCell(coordinate) => GameFactory.createCenterCell()
      case coordinate if view.isPawnCell(coordinate) => GameFactory.createPawnCell()
      case _ => GameFactory.createNormalCell()

    }

    /**
      * Show next, previous, first or last move.
      *
      * @param snapshotToShow
      *             snapshot to show.
      */
    private def changeSnapshot(snapshotToShow: Snapshot.Value): Unit = view.changeSnapshot(snapshotToShow)

    /**
      * Delete last move and reset winnerStatus.
      */
    private def undoMove(): Unit = {
      cells.values.foreach(c => if(c.getActionListeners.length == 0) c.addActionListener(_ => actionCell(c)))
      view.undoMove()
    }
  }
}
