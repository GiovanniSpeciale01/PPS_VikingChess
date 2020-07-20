package model.game

import model.game.Player.Player

/**
  * Defines Enumeration for the game mode.
  */
object GameMode extends Enumeration {
  type GameMode = Value
  val PVP: GameMode.Value = Value("Player vs Player")
  val PVE: GameMode.Value = Value("Player vs Computer")
}

/**
  * Defines Enumeration for the player type.
  */
object Player extends Enumeration {
  type Player = Value
  val White: Value = Value("White")
  val Black: Value = Value("Black")
  val None: Value = Value("None")
  val Draw: Value = Value("Draw")

  /**
   * Class that can map Player value to prolog string.
   *
   * @param player
   *               player value
   */
  case class ParserStringPlayer(player: Player) {
    /**
     * Returns the corresponding prolog player string.
     *
     * @return prolog player string
     */
    def parserString: String = player match {
      case Player.White => "w"
      case Player.Black => "b"
      case Player.None => "n"
      case _ => "d"
    }
  }

  implicit def getParserString(player: Player): ParserStringPlayer = ParserStringPlayer(player)
}

/**
  * Defines Enumeration for the IA difficulty.
  */
object Level extends Enumeration {
  type Level = Value
  val Newcomer: Value = Value("Newcomer")
  val Standard: Value = Value("Standard")
  val Advanced: Value = Value("Advanced")

  /**
   * Class that can map a Level value to IA depth.
   *
   * @param level
   *        level value
   */
  case class LevelDepth(level: Level) {
    /**
     * Returns the corresponding level depth.
     *
     * @return IA depth
     */
    def depth: Int = level match {
      case Level.Newcomer => 1
      case _ => 2
    }
  }

  implicit def depth(level: Level): LevelDepth = LevelDepth(level)
}

/**
  * Defines Enumeration for the game variant.
  */
object GameVariant extends Enumeration {
  type GameVariant = Value
  val Hnefatafl: Value = Value("Hnefatafl")
  val Tawlbwrdd: Value = Value("Tawlbwrdd")
  val Tablut: Value = Value("Tablut")
  val Brandubh: Value = Value("Brandubh")

  /**
   * Class that can map a GameVariant value to the corresponding board size.
   *
   * @param gameVariant
   *        game variant value
   */
  case class GameVariantBoardSize(gameVariant: GameVariant) {
    /**
     * Returns the corresponding board size.
     *
     * @return board size
     */
    def boardSize: Int = gameVariant match {
      case GameVariant.Tablut => 9
      case GameVariant.Brandubh => 7
      case _ => 11
    }
  }

  implicit def getBoardSize(gameVariant: GameVariant): GameVariantBoardSize = GameVariantBoardSize(gameVariant)
}

/**
  * Defines Enumeration for the piece in each cell.
  */
object Piece extends Enumeration {
  type Piece = Value
  val WhitePawn: Value = Value("w")
  val BlackPawn: Value = Value("b")
  val WhiteKing: Value = Value("k")
  val Empty: Value = Value("e")

  /**
   * Class that can map a Piece value to its corresponding Player owner.
   *
   * @param piece
   *        piece value
   */
  case class PieceOwner(piece: Piece) {
    /**
     * Returns the corresponding Player owner.
     *
     * @return piece Player owner
     */
    def pieceOwner: Player = piece match {
      case Piece.WhitePawn => Player.White
      case Piece.WhiteKing => Player.White
      case Piece.BlackPawn => Player.Black
      case Piece.Empty => Player.None
    }
  }

  implicit def getPieceOwner(piece: Piece): PieceOwner = PieceOwner(piece)
}

/**
  * Defines Enumeration for previous, next, first or last snapshot.
  */
object Snapshot extends Enumeration {
  type Snapshot = Value
  val Previous, Next, First, Last = Value
}

/**
 * Defines Enumeration for Maximize or Minimize in Minimax.
 */
object MaxMin extends Enumeration {
  type MaxMin = Value
  val Max, min = Value
}

/**
 * Defines Enumeration for the orthogonal directions.
 */
object OrthogonalDirection extends Enumeration {
  type OrthogonalDirection = Value
  val Up, Right, Down, Left = Value

  /**
   * Implicit to order the orthogonal directions clockwise as up, right, down, left.
   *
   * @return comparing value
   */
  implicit val orthogonalOrdering: Ordering[OrthogonalDirection] = (a: OrthogonalDirection, b: OrthogonalDirection) =>
    a.directionToSort compare b.directionToSort

  /**
   * Class that can map an orthogonal direction value to an int.
   *
   * @param direction
   *        direction value
   */
  case class OrthogonalDirectionValue(direction: OrthogonalDirection) {
    /**
     * Returns an integer for direction sorting.
     *
     * @return integer for sorting
     */
    def directionToSort: Int = direction match {
      case OrthogonalDirection.Up => 1
      case OrthogonalDirection.Right => 2
      case OrthogonalDirection.Down => 3
      case _ => 4
    }
  }

  implicit def directionValue(direction: OrthogonalDirection): OrthogonalDirectionValue = OrthogonalDirectionValue(direction)

  /**
   * Class that can map an orthogonal value to the opposite orthogonal direction value.
   *
   * @param direction
   *        direction value
   */
  case class OrthogonalOpposite(direction: OrthogonalDirection) {
    /**
     * Returns the opposite orthogonal direction.
     *
     * @return the opposite orthogonal direction
     */
    def opposite: OrthogonalDirection = direction match {
      case OrthogonalDirection.Up => OrthogonalDirection.Down
      case OrthogonalDirection.Right => OrthogonalDirection.Left
      case OrthogonalDirection.Down => OrthogonalDirection.Up
      case OrthogonalDirection.Left => OrthogonalDirection.Right
    }
  }

  implicit def orthogonalOpposite(direction: OrthogonalDirection): OrthogonalOpposite = OrthogonalOpposite(direction)
}