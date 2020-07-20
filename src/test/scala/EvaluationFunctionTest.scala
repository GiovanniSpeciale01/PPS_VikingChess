import model.game.GameSnapshot.GameSnapshotImpl
import org.junit.runner.RunWith
import org.scalatest.FunSpec
import org.scalatest.junit.JUnitRunner
import model.ia.evaluation_function.{EvaluationFunction, ScoreProvider}
import model.game.{Coordinate, GameSnapshot, GameVariant, Level, Move, MoveGenerator}
import model.prolog.ParserProlog

@RunWith(classOf[JUnitRunner])
class EvaluationFunctionTest extends FunSpec {

  describe("In this session of game Brandubh-Newcomer, ") {
    val game = ParserProlog.createGame(GameVariant.Brandubh.toString.toLowerCase)
    var snapshot: GameSnapshot = GameSnapshotImpl(GameVariant.Brandubh, game.playerToMove, game.winner, game.board, Option.empty, 0, 0)
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(4,6),Coordinate(3,6)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(4,5),Coordinate(1,5)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(4,7),Coordinate(2,7)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(4,4),Coordinate(4,6)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(3,6),Coordinate(1,6)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(4,6),Coordinate(7,6)))
    describe("king is near corner so, ") {
      it("actual score should be 900.") {
        assert(EvaluationFunction(snapshot).score(Level.Newcomer) == ScoreProvider.KingEscapeToCorner)
      }
    }
  }

  describe("In this session of game Brandubh-Newcomer, ") {
    val game = ParserProlog.createGame(GameVariant.Brandubh.toString.toLowerCase)
    var snapshot: GameSnapshot = GameSnapshotImpl(GameVariant.Brandubh, game.playerToMove, game.winner, game.board, Option.empty, 0, 0)
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(4,6),Coordinate(3,6)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(4,5),Coordinate(1,5)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(4,7),Coordinate(2,7)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(4,4),Coordinate(4,7)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(3,6),Coordinate(4,6)))
    describe("king can go to corner in one so, ") {
      it("actual score should be 900.") {
        assert(EvaluationFunction(snapshot).score(Level.Newcomer) == ScoreProvider.KingEscapeToCorner)
      }
    }
  }

  describe("In this session of game Tablut-Standard, ") {
    val game = ParserProlog.createGame(GameVariant.Tablut.toString.toLowerCase)
    var snapshot: GameSnapshot = GameSnapshotImpl(GameVariant.Tablut, game.playerToMove, game.winner, game.board, Option.empty, 0, 0)
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(5,2), Coordinate(1,2)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(5,3), Coordinate(2,3)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(5,1), Coordinate(5,2)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(5,4), Coordinate(2,4)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(5,2), Coordinate(5,4)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(4,5), Coordinate(4,2)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(4,9), Coordinate(4,5)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(5,6), Coordinate(2,6)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(9,6), Coordinate(5,6)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(6,5), Coordinate(6,8)))
    describe("king can be captured in one so, ") {
      it("actual score should be -900.") {
        assert(EvaluationFunction(snapshot).score(Level.Standard) == -ScoreProvider.KingCatchableInOne)
      }
    }
  }

  describe("In this session of game Tablut-Standard, ") {
    val game = ParserProlog.createGame(GameVariant.Tablut.toString.toLowerCase)
    var snapshot: GameSnapshot = GameSnapshotImpl(GameVariant.Tablut, game.playerToMove, game.winner, game.board, Option.empty, 0, 0)
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(5,2), Coordinate(2,2)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(5,3), Coordinate(1,3)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(5,1), Coordinate(5,3)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(5,4), Coordinate(2,4)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(9,4), Coordinate(6,4)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(5,5), Coordinate(5,4)))
    describe("king can be captured in one near to throne so, ") {
      it("actual score should be -900.") {
        assert(EvaluationFunction(snapshot).score(Level.Standard) == -ScoreProvider.KingCatchableInOne)
      }
    }
  }

  describe("In this session of game Brandubh-Standard, ") {
    val game = ParserProlog.createGame(GameVariant.Brandubh.toString.toLowerCase)
    var snapshot: GameSnapshot = GameSnapshotImpl(GameVariant.Brandubh, game.playerToMove, game.winner, game.board, Option.empty, 0, 0)
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(4,2), Coordinate(3,2)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(4,3), Coordinate(2,3)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(6,4), Coordinate(6,2)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(4,4), Coordinate(4,2)))
    describe("king can be captured in one far from throne so, ") {
      it("actual score should be -900.") {
        assert(EvaluationFunction(snapshot).score(Level.Standard) == -ScoreProvider.KingCatchableInOne)
      }
    }
  }

  describe("In this session of game Tawlbwrdd-Standard, ") {
    val game = ParserProlog.createGame(GameVariant.Tawlbwrdd.toString.toLowerCase)
    var snapshot: GameSnapshot = GameSnapshotImpl(GameVariant.Tawlbwrdd, game.playerToMove, game.winner, game.board, Option.empty, 0, 0)
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(9,6), Coordinate(9,2)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(8,6), Coordinate(8,1)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(9,2), Coordinate(9,3)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(7,5), Coordinate(7,3)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(9,3), Coordinate(9,2)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(7,6), Coordinate(7,4)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(9,2), Coordinate(9,3)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(6,6), Coordinate(10,6)))
    describe("king can be captured in one far from throne so, ") {
      it("actual score should be -900.") {
        assert(EvaluationFunction(snapshot).score(Level.Standard) == -ScoreProvider.KingCatchableInOne)
      }
    }
  }

  describe("At the beginning of the game Brandubh-Newcomer, ") {
    val game = ParserProlog.createGame(GameVariant.Brandubh.toString.toLowerCase)
    val snapshot: GameSnapshot = GameSnapshotImpl(GameVariant.Brandubh, game.playerToMove, game.winner, game.board, Option.empty, 0, 0)
    it("actual score should be in Draw (0).") {
      assert(EvaluationFunction(snapshot).score(Level.Newcomer) == ScoreProvider.Draw)
    }
  }

  describe("In this session of game Brandubh-Newcomer, ") {
    val game = ParserProlog.createGame(GameVariant.Brandubh.toString.toLowerCase)
    var snapshot: GameSnapshot = GameSnapshotImpl(GameVariant.Brandubh, game.playerToMove, game.winner, game.board, Option.empty, 0, 0)
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(4,2), Coordinate(5,2)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(4,3), Coordinate(3,3)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(4,6), Coordinate(5,6)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(4,5), Coordinate(3,5)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(5,2), Coordinate(5,3)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(3,3), Coordinate(4,3)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(5,6), Coordinate(5,5)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(4,4), Coordinate(5,4)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(4,7), Coordinate(4,5)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(3,5), Coordinate(2,5)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(4,1), Coordinate(3,1)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(4,3), Coordinate(4,2)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(3,1), Coordinate(3,3)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(4,2), Coordinate(4,3)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(1,4), Coordinate(1,5)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(3,4), Coordinate(3,6)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(4,5), Coordinate(3,5)))
    describe("the blacks are ahead, so ") {
      it("actual score should be < 0.") {
        assert(EvaluationFunction(snapshot).score(Level.Newcomer) < 0)
      }
    }
  }

  describe("In this session of game Hnefatafl-Standard, ") {
    val game = ParserProlog.createGame(GameVariant.Hnefatafl.toString.toLowerCase)
    var snapshot: GameSnapshot = GameSnapshotImpl(GameVariant.Hnefatafl, game.playerToMove, game.winner, game.board, Option.empty, 0, 0)
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(7,1), Coordinate(7,4)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(8,6), Coordinate(8,4)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(5,1), Coordinate(5,4)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(4,6), Coordinate(4,4)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(5,11), Coordinate(5,8)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(4,4), Coordinate(4,8)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(6,2), Coordinate(5,2)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(6,4), Coordinate(3,4)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(5,2), Coordinate(3,2)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(6,5), Coordinate(6,2)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(3,2), Coordinate(5,2)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(6,6), Coordinate(6,3)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(8,1), Coordinate(8,2)))
    describe("the whites owned more rows and columns, so ") {
      it("actual score should be > 0.") {
        assert(EvaluationFunction(snapshot).score(Level.Standard) > 0)
      }
    }
  }

  describe("In this session of game Tawlbwrdd, ") {
    val game = ParserProlog.createGame(GameVariant.Tawlbwrdd.toString.toLowerCase)
    var snapshot: GameSnapshot = GameSnapshotImpl(GameVariant.Tawlbwrdd, game.playerToMove, game.winner, game.board, Option.empty, 0, 0)
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(6,3), Coordinate(4,3)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(6,4), Coordinate(3,4)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(4,3), Coordinate(4,4)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(6,5), Coordinate(6,3)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(2,5), Coordinate(2,4)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(6,3), Coordinate(9,3)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(7,1), Coordinate(9,1)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(6,6), Coordinate(6,2)))
    describe("the king is surrounded by three blacks, so ") {
      it("the relative score should be 25 * 3.") {
        val blackPiecesSurroundingKing = 3
        assert(EvaluationFunction(snapshot).scoreBlackSurroundTheKing == ScoreProvider.BlackNearKing * blackPiecesSurroundingKing)
      }
    }
  }

  describe("In this session of game Hnefatafl, ") {
    val game = ParserProlog.createGame(GameVariant.Hnefatafl.toString.toLowerCase)
    var snapshot = GameSnapshot(GameVariant.Hnefatafl, game.playerToMove, game.winner, game.board, Option.empty, 0, 0)
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(6,2), Coordinate(3,2)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(6,4), Coordinate(2,4)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(3,2), Coordinate(3,4)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(4,6), Coordinate(3,6)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(4,11), Coordinate(4,7)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(3,6), Coordinate(3,7)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(2,6), Coordinate(4,6)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(3,7), Coordinate(3,5)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(4,6), Coordinate(3,6)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(5,5), Coordinate(3,5)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(1,8), Coordinate(3,8)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(5,7), Coordinate(3,7)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(1,6), Coordinate(3,6)))
    describe("the pieces captured (4 whites, 2 blacks), so ") {
      it("the relative score should be Blacks: 4*50 and White: 2*20.") {
        val nCapturedBack = 2
        val nCapturedWhite = 4
        assert(EvaluationFunction(snapshot).scoreCapturedBlack == nCapturedBack * ScoreProvider.BlackCaptured &&
          EvaluationFunction(snapshot).scoreCapturedWhite == nCapturedWhite * ScoreProvider.WhiteCaptured)
      }
    }
  }

  describe("At the beginning of the game Hnefatafl, ") {
    val game = ParserProlog.createGame(GameVariant.Hnefatafl.toString.toLowerCase)
    val snapshot = GameSnapshot(GameVariant.Hnefatafl, game.playerToMove, game.winner, game.board, Option.empty, 0, 0)
    describe("the whites have 4 towers near to throne so, ") {
      it("the relative score should be 12.") {
        val numberOfTowers = 4
        assert(EvaluationFunction(snapshot).scoreTower == ScoreProvider.TowerCoefficient * numberOfTowers)
      }
    }
  }

  describe("At the beginning of the game Tablut, ") {
    val game = ParserProlog.createGame(GameVariant.Tablut.toString.toLowerCase)
    val snapshot = GameSnapshot(GameVariant.Tablut, game.playerToMove, game.winner, game.board, Option.empty, 0, 0)
    describe("the king is on throne so, ") {
      it("the relative score should be 0.") {
        assert(EvaluationFunction(snapshot).scoreKingOnThrone == ScoreProvider.KingOnThrone)
      }
    }
  }

  describe("In this session of game Tablut, ") {
    val game = ParserProlog.createGame(GameVariant.Tablut.toString.toLowerCase)
    var snapshot = GameSnapshot(GameVariant.Tablut, game.playerToMove, game.winner, game.board, Option.empty, 0, 0)
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(5,2), Coordinate(8,2)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(5,3), Coordinate(1,3)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(5,1), Coordinate(5,2)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(5,4), Coordinate(2,4)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(8,2), Coordinate(8,4)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(5,5), Coordinate(5,3)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(8,4), Coordinate(8,3)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(5,3), Coordinate(6,3)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(5,2), Coordinate(5,3)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(6,3), Coordinate(6,2)))
    describe("the king is far from throne so, ") {
      it("the relative score should be > 0.") {
        val quadraticDistanceFromKingToThrone = 10
        assert(EvaluationFunction(snapshot).scoreKingOnThrone ==
          ScoreProvider.KingDistanceToCornerDividend / quadraticDistanceFromKingToThrone)
      }
    }
  }

  describe("In this session of game Tawlbwrdd, ") {
    val game = ParserProlog.createGame(GameVariant.Tawlbwrdd.toString.toLowerCase)
    var snapshot = GameSnapshot(GameVariant.Tawlbwrdd, game.playerToMove, game.winner, game.board, Option.empty, 0, 0)
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(6,3), Coordinate(4,3)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(4,6), Coordinate(4,8)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(3,6), Coordinate(3,4)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(8,6), Coordinate(8,4)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(7,2), Coordinate(8,2)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(6,4), Coordinate(6,3)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(9,6), Coordinate(9,3)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(5,7), Coordinate(4,7)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(10,5), Coordinate(10,4)))
    describe("the 14 blacks have closed a right barricade and 4 a wrong cordon so, ") {
      it("the relative score should be 14 * 5 + 80 - 50.") {
        val blackPawnsInCordon = 14 + 4
        assert(EvaluationFunction(snapshot).scoreBlackCordon ==
          blackPawnsInCordon * ScoreProvider.PawnInCordon + ScoreProvider.RightCordon - ScoreProvider.WrongCordon)
      }
    }
  }

  describe("In this session of game Tawlbwrdd, ") {
    val game = ParserProlog.createGame(GameVariant.Tawlbwrdd.toString.toLowerCase)
    var snapshot = GameSnapshot(GameVariant.Tawlbwrdd, game.playerToMove, game.winner, game.board, Option.empty, 0, 0)
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(6,3), Coordinate(4,3)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(4,6), Coordinate(4,5)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(3,6), Coordinate(3,4)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(6,4), Coordinate(5,4)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(7,2), Coordinate(8,2)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(7,5), Coordinate(7,4)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(9,6), Coordinate(9,3)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(8,6), Coordinate(8,4)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(10,5), Coordinate(10,4)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(7,7), Coordinate(8,7)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(10,7), Coordinate(10,8)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(6,8), Coordinate(8,8)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(6,9), Coordinate(9,9)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(6,7), Coordinate(6,8)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(7,10), Coordinate(8,10)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(5,7), Coordinate(4,7)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(2,7), Coordinate(2,8)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(8,7), Coordinate(6,7)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(5,11), Coordinate(3,11)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(4,7), Coordinate(4,8)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(3,11), Coordinate(3,9)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(6,8), Coordinate(5,8)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(1,7), Coordinate(2,7)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(4,8), Coordinate(4,7)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(2,8), Coordinate(3,8)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(5,8), Coordinate(6,8)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(3,9), Coordinate(4,9)))
    describe("22 blacks have closed an inner cordon with 13 whites inside so, ") {
      it("the relative score should be 22 * 5 + 80 + 13 * 10.") {
        val blackPawnsInCordon: Int = 22
        val whitePawnsInCordon: Int = 13
        assert(EvaluationFunction(snapshot).scoreBlackCordon ==
          blackPawnsInCordon * ScoreProvider.PawnInCordon + ScoreProvider.RightCordon +
          + whitePawnsInCordon * ScoreProvider.WhiteInsideCordon)
      }
    }
  }

  describe("In this session of game Tawlbwrdd, ") {
    val game = ParserProlog.createGame(GameVariant.Tawlbwrdd.toString.toLowerCase)
    var snapshot = GameSnapshot(GameVariant.Tawlbwrdd, game.playerToMove, game.winner, game.board, Option.empty, 0, 0)
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(6,3), Coordinate(4,3)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(4,6), Coordinate(4,4)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(3,6), Coordinate(3,4)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(6,4), Coordinate(5,4)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(9,6), Coordinate(9,4)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(7,5), Coordinate(7,4)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(6,1), Coordinate(6,3)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(6,5), Coordinate(6,4)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(7,2), Coordinate(8,2)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(7,4), Coordinate(8,4)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(7,1), Coordinate(7,3)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(7,7), Coordinate(8,7)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(5,2), Coordinate(5,3)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(5,5), Coordinate(4,5)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(2,5), Coordinate(3,5)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(5,6), Coordinate(4,6)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(1,6), Coordinate(3,6)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(6,8), Coordinate(4,8)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(2,7), Coordinate(3,7)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(5,7), Coordinate(4,7)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(6,9), Coordinate(4,9)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(6,6), Coordinate(6,5)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(8,2), Coordinate(8,3)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(7,6), Coordinate(7,7)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(1,7), Coordinate(1,8)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(4,8), Coordinate(5,8)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(1,8), Coordinate(4,8)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(5,8), Coordinate(5,7)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(6,11), Coordinate(6,8)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(8,7), Coordinate(9,7)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(7,10), Coordinate(7,8)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(6,5), Coordinate(6,7)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(5,10), Coordinate(5,8)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(8,6), Coordinate(8,5)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(7,11), Coordinate(9,11)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(8,5), Coordinate(8,7)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(9,11), Coordinate(9,8)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(8,4), Coordinate(8,6)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(4,9), Coordinate(8,9)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(8,6), Coordinate(7,6)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(11,6), Coordinate(10,6)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(6,4), Coordinate(6,5)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(11,5), Coordinate(11,2)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(5,4), Coordinate(7,4)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(11,7), Coordinate(11,10)))
    describe("19 blacks have closed an inner cordon with 12 whites inside so, ") {
      it("the relative score should be 19 * 5 + 80 + 12 * 10.") {
        val blackPawnsInCordon: Int = 19
        val whitePawnsInCordon: Int = 12
        assert(EvaluationFunction(snapshot).scoreBlackCordon ==
          blackPawnsInCordon * ScoreProvider.PawnInCordon + ScoreProvider.RightCordon +
          + whitePawnsInCordon * ScoreProvider.WhiteInsideCordon)
      }
    }
  }

  describe("In this session of game Hnefatafl, ") {
    val game = ParserProlog.createGame(GameVariant.Hnefatafl.toString.toLowerCase)
    var snapshot = GameSnapshot(GameVariant.Hnefatafl, game.playerToMove, game.winner, game.board, Option.empty, 0, 0)
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(6,2), Coordinate(3,2)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(6,4), Coordinate(6,2)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(1,4), Coordinate(3,4)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(5,5), Coordinate(5,3)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(1,5), Coordinate(1,3)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(6,5), Coordinate(6,3)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(1,3), Coordinate(4,3)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(4,6), Coordinate(4,7)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(11,4), Coordinate(7,4)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(7,5), Coordinate(5,5)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(7,1), Coordinate(7,2)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(8,6), Coordinate(8,8)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(8,1), Coordinate(8,3)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(4,7), Coordinate(4,9)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(3,4), Coordinate(6,4)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(5,6), Coordinate(4,6)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(2,6), Coordinate(2,4)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(5,5), Coordinate(3,5)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(2,4), Coordinate(5,4)))
    describe("10 blacks have closed an inner cordon with 0 whites inside so, ") {
      it("the relative score should be 10 * 5 - 50.") {
        val blackPawnsInCordon: Int = 10
        assert(EvaluationFunction(snapshot).scoreBlackCordon ==
          blackPawnsInCordon * ScoreProvider.PawnInCordon - ScoreProvider.WrongCordon)
      }
    }
  }

  describe("In this session of game Hnefatafl, ") {
    val game = ParserProlog.createGame(GameVariant.Hnefatafl.toString.toLowerCase)
    var snapshot = GameSnapshot(GameVariant.Hnefatafl, game.playerToMove, game.winner, game.board, Option.empty, 0, 0)
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(6,2), Coordinate(2,2)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(4,6), Coordinate(4,3)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(4,1), Coordinate(3,1)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(5,5), Coordinate(3,5)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(1,4), Coordinate(1,3)))
    describe("3 blacks have closed a diagonal barricade so, ") {
      it("the relative score should be 3 * 5 + 80.") {
        val blackPawnsInCordon: Int = 3
        assert(EvaluationFunction(snapshot).scoreBlackCordon ==
          blackPawnsInCordon * ScoreProvider.PawnInCordon + ScoreProvider.RightCordon)
      }
    }
  }

  describe("In this session of game Tablut, ") {
    val game = ParserProlog.createGame(GameVariant.Tablut.toString.toLowerCase)
    var snapshot = GameSnapshot(GameVariant.Tablut, game.playerToMove, game.winner, game.board, Option.empty, 0, 0)
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(6,1), Coordinate(6,3)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(5,4), Coordinate(4,4)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(4,1), Coordinate(4,3)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(3,5), Coordinate(3,4)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(5,2), Coordinate(5,3)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(3,4), Coordinate(3,5)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(9,4), Coordinate(9,3)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(7,5), Coordinate(7,6)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(8,5), Coordinate(8,3)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(6,5), Coordinate(6,6)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(9,5), Coordinate(7,5)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(5,5), Coordinate(6,5)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(7,5), Coordinate(7,3)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(4,4), Coordinate(3,4)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(2,5), Coordinate(2,3)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(3,4), Coordinate(2,4)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(1,4), Coordinate(1,3)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(3,5), Coordinate(3,4)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(5,1), Coordinate(3,1)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(4,5), Coordinate(4,4)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(3,1), Coordinate(3,3)))
    describe("9 blacks have closed a vertical barricade so, ") {
      it("the relative score should be 9 * 5 + 80.") {
        val blackPawnsInCordon: Int = 9
        assert(EvaluationFunction(snapshot).scoreBlackCordon ==
          blackPawnsInCordon * ScoreProvider.PawnInCordon + ScoreProvider.RightCordon)
      }
    }
  }

  describe("In this session of game Tawlbwrdd, ") {
    val game = ParserProlog.createGame(GameVariant.Tawlbwrdd.toString.toLowerCase)
    var snapshot = GameSnapshot(GameVariant.Tawlbwrdd, game.playerToMove, game.winner, game.board, Option.empty, 0, 0)
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(6,3), Coordinate(4,3)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(4,6), Coordinate(4,4)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(6,9), Coordinate(4,9)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(5,5), Coordinate(5,4)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(2,5), Coordinate(4,5)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(5,6), Coordinate(5,5)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(3,6), Coordinate(4,6)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(5,7), Coordinate(5,6)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(2,7), Coordinate(4,7)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(5,4), Coordinate(5,3)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(1,5), Coordinate(1,4)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(6,8), Coordinate(6,9)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(1,7), Coordinate(1,8)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(6,4), Coordinate(8,4)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(1,4), Coordinate(4,4)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(5,5), Coordinate(5,4)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(1,8), Coordinate(4,8)))
    describe("13 blacks have closed a horizontal barricade and 4 blacks a wrong circle cordon so, ") {
      it("the relative score should be 13 * 5 + 80 + 4 * 5 - 50.") {
        val blackPawnsInCordon: Int = 13 + 4
        assert(EvaluationFunction(snapshot).scoreBlackCordon ==
          blackPawnsInCordon * ScoreProvider.PawnInCordon + ScoreProvider.RightCordon - ScoreProvider.WrongCordon)
      }
    }
  }

  describe("In this session of game Tawlbwrdd, ") {
    val game = ParserProlog.createGame(GameVariant.Tawlbwrdd.toString.toLowerCase)
    var snapshot = GameSnapshot(GameVariant.Tawlbwrdd, game.playerToMove, game.winner, game.board, Option.empty, 0, 0)
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(1,5), Coordinate(1,2)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(4,6), Coordinate(4,11)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(2,5), Coordinate(2,1)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(4,11), Coordinate(2,11)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(7,1), Coordinate(10,1)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(6,8), Coordinate(1,8)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(7,2), Coordinate(11,2)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(1,8), Coordinate(1,10)))
    describe("the blacks have 4 wrong barricade while the whites 2 wrong barricade so, ") {
      it("the relative negative score should be 4 * 50 for blacks and 2 * 50 for whites.") {
        val wrongBarricade = EvaluationFunction(snapshot).scoreWrongBarricade
        val wrongBarricadeBlacks = 4
        val wrongBarricadeWhites = 2
        assert(wrongBarricade.blackScore == wrongBarricadeBlacks * ScoreProvider.WrongBarricade &&
          wrongBarricade.whiteScore == wrongBarricadeWhites * ScoreProvider.WrongBarricade)
      }
    }
  }

  describe("In this session of game Tablut, ") {
    val game = ParserProlog.createGame(GameVariant.Tablut.toString.toLowerCase)
    var snapshot = GameSnapshot(GameVariant.Tablut, game.playerToMove, game.winner, game.board, Option.empty, 0, 0)
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(2,5), Coordinate(2,3)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(3,5), Coordinate(3,9)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(1,5), Coordinate(2,5)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(4,5), Coordinate(4,8)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(1,6), Coordinate(2,6)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(5,5), Coordinate(4,5)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(2,6), Coordinate(1,6)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(4,5), Coordinate(4,4)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(1,6), Coordinate(2,6)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(4,4), Coordinate(3,4)))
    describe("2 blacks are in king's diagonal so, ") {
      it("the relative score should be 2 * 15.") {
        val blacksOnKingsDiagonals = 2
        assert(EvaluationFunction(snapshot).scoreBlackOnKingDiagonal ==
          ScoreProvider.BlackOnDiagonalKing * blacksOnKingsDiagonals)
      }
    }
  }

  describe("In this session of game Hnefatafl, ") {
    val game = ParserProlog.createGame(GameVariant.Hnefatafl.toString.toLowerCase)
    var snapshot = GameSnapshot(GameVariant.Hnefatafl, game.playerToMove, game.winner, game.board, Option.empty, 0, 0)
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(2,6), Coordinate(2,5)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(4,6), Coordinate(3,6)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(2,5), Coordinate(3,5)))
    describe("last black moved is catchable in one so, ") {
      it("the relative score for whites should be 100.") {
        assert(EvaluationFunction(snapshot).scoreLastPawnMovedCatchableInOne == ScoreProvider.LastBlackMovedCatchableInOne)
      }
    }
  }

  describe("In this session of game Brandubh, ") {
    val game = ParserProlog.createGame(GameVariant.Brandubh.toString.toLowerCase)
    var snapshot = GameSnapshot(GameVariant.Brandubh, game.playerToMove, game.winner, game.board, Option.empty, 0, 0)
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(4,2), Coordinate(5,2)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(4,3), Coordinate(2,3)))
    describe("last white moved is catchable in one so, ") {
      it("the relative score for blacks should be 150.") {
        assert(EvaluationFunction(snapshot).scoreLastPawnMovedCatchableInOne == ScoreProvider.LastWhiteMovedCatchableInOne)
      }
    }
  }

  describe("In this session of game Brandubh, ") {
    val game = ParserProlog.createGame(GameVariant.Brandubh.toString.toLowerCase)
    var snapshot = GameSnapshot(GameVariant.Brandubh, game.playerToMove, game.winner, game.board, Option.empty, 0, 0)
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(4,6), Coordinate(1,6)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(4,5), Coordinate(6,5)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(4,7), Coordinate(5,7)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(3,4), Coordinate(3,7)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(5,7), Coordinate(5,5)))
    describe("last black moved is catchable in one so, ") {
      it("the relative score for whites should be 100.") {
        assert(EvaluationFunction(snapshot).scoreLastPawnMovedCatchableInOne == ScoreProvider.LastBlackMovedCatchableInOne)
      }
    }
  }

  describe("In this session of game Brandubh, ") {
    val game = ParserProlog.createGame(GameVariant.Brandubh.toString.toLowerCase)
    var snapshot = GameSnapshot(GameVariant.Brandubh, game.playerToMove, game.winner, game.board, Option.empty, 0, 0)
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(4,7), Coordinate(2,7)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(3,4), Coordinate(3,7)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(2,4), Coordinate(2,6)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(4,5), Coordinate(1,5)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(2,6), Coordinate(2,3)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(3,7), Coordinate(4,7)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(4,6), Coordinate(4,5)))
    describe("last black moved is catchable in one by still king so, ") {
      it("the relative score for whites should be 100.") {
        assert(EvaluationFunction(snapshot).scoreLastPawnMovedCatchableInOne == ScoreProvider.LastBlackMovedCatchableInOne)
      }
    }
  }

  describe("In this session of game Hnefatafl, ") {
    val game = ParserProlog.createGame(GameVariant.Hnefatafl.toString.toLowerCase)
    var snapshot = GameSnapshot(GameVariant.Hnefatafl, game.playerToMove, game.winner, game.board, Option.empty, 0, 0)
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(4,1), Coordinate(4,5)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(4,6), Coordinate(4,7)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(4,11), Coordinate(4,8)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(5,7), Coordinate(5,8)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(2,6), Coordinate(4,6)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(8,6), Coordinate(8,10)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(4,8), Coordinate(4,11)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(5,8), Coordinate(5,10)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(4,11), Coordinate(2,11)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(5,6), Coordinate(5,7)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(4,6), Coordinate(4,10)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(5,7), Coordinate(5,9)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(4,10), Coordinate(1,10)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(5,9), Coordinate(9,9)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(8,1), Coordinate(8,2)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(7,6), Coordinate(8,6)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(10,6), Coordinate(10,4)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(8,6), Coordinate(8,7)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(11,4), Coordinate(11,3)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(8,7), Coordinate(8,9)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(11,3), Coordinate(9,3)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(9,9), Coordinate(9,10)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(11,8), Coordinate(10,8)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(7,7), Coordinate(9,7)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(10,8), Coordinate(10,7)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(9,7), Coordinate(9,9)))
    describe("the score for all difficulty ") {
      it("should be the same for all.") {
        assert(EvaluationFunction(snapshot).score(Level.Newcomer) == EvaluationFunction(snapshot).score(Level.Standard) &&
          EvaluationFunction(snapshot).score(Level.Newcomer) == EvaluationFunction(snapshot).score(Level.Advanced))
      }
    }
  }

  describe("In this session of game Tablut, ") {
    val game = ParserProlog.createGame(GameVariant.Tablut.toString.toLowerCase)
    var snapshot = GameSnapshot(GameVariant.Tablut, game.playerToMove, game.winner, game.board, Option.empty, 0, 0)
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(5,2), Coordinate(3,2)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(3,5), Coordinate(3,7)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(2,5), Coordinate(2,3)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(4,5), Coordinate(4,7)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(6,9), Coordinate(6,7)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(7,5), Coordinate(7,7)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(8,5), Coordinate(8,3)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(5,5), Coordinate(2,5)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(9,6), Coordinate(7,6)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(7,7), Coordinate(6,7)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(7,6), Coordinate(7,2)))
    describe("king can escape to a corner so, ") {
      it("the score for all difficulty should be 900.") {
        assert(EvaluationFunction(snapshot).score(Level.Newcomer) == ScoreProvider.KingEscapeNearCorner &&
          EvaluationFunction(snapshot).score(Level.Standard) == ScoreProvider.KingEscapeNearCorner &&
          EvaluationFunction(snapshot).score(Level.Advanced) == ScoreProvider.KingEscapeNearCorner)
      }
    }
  }

  describe("In this session of game Tawlbwrdd, ") {
    val game = ParserProlog.createGame(GameVariant.Tawlbwrdd.toString.toLowerCase)
    var snapshot = GameSnapshot(GameVariant.Tawlbwrdd, game.playerToMove, game.winner, game.board, Option.empty, 0, 0)
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(6,9), Coordinate(4,9)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(4,6), Coordinate(4,5)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(3,6), Coordinate(3,8)))
    describe("8 blacks have closed a diagonal barricade and 8 blacks 2 wrong circle so, ") {
      it("the relative score should be (8 * 5 + 80) + (8 * 5 - 50 * 2).") {
        val blackPawnsInCordon = 8 + 4 + 4
        assert(EvaluationFunction(snapshot).scoreBlackCordon ==
          blackPawnsInCordon * ScoreProvider.PawnInCordon
          + ScoreProvider.RightCordon
          - ScoreProvider.WrongCordon - ScoreProvider.WrongCordon)
      }
    }
  }
}