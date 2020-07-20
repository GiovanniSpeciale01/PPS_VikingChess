import model.game.GameSnapshot.GameSnapshotImpl
import model.game.{Coordinate, GameSnapshot, GameVariant, Move, MoveGenerator, Piece, Player}
import model.prolog.ParserProlog
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FunSpec

@RunWith(classOf[JUnitRunner])
class MoveGeneratorTest extends FunSpec {

  describe("In Variant Brandubh ") {
    val game = ParserProlog.createGame(GameVariant.Brandubh.toString.toLowerCase)
    describe("the number of initial possible moves ") {
      val snapshot = GameSnapshotImpl(GameVariant.Brandubh, game.playerToMove, game.winner, game.board, Option.empty, 0, 0)
      it("should be 40.") {
        val initBrandubhPossibleMoves = 40
        assert(MoveGenerator.gamePossibleMoves(snapshot).size == initBrandubhPossibleMoves)
      }
    }
  }

  describe("In Variant Hnefatafl ") {
    val game = ParserProlog.createGame(GameVariant.Hnefatafl.toString.toLowerCase)
    describe("the number of initial possible moves ") {
      val snapshot = GameSnapshotImpl(GameVariant.Hnefatafl, game.playerToMove, game.winner, game.board, Option.empty, 0, 0)
      it("should be 116.") {
        val initHnefataflPossibleMoves = 116
        assert(MoveGenerator.gamePossibleMoves(snapshot).size == initHnefataflPossibleMoves)
      }
    }
  }

  describe("In Variant Brandubh, ") {
    val game = ParserProlog.createGame(GameVariant.Brandubh.toString.toLowerCase)
    val snapshot = GameSnapshotImpl(GameVariant.Brandubh, game.playerToMove, game.winner, game.board, Option.empty, 0, 0)
    describe("defining all the initial possible moves, ") {
      val possibleMove_Brandubh = List(Move(Coordinate(1, 4), Coordinate(1, 5)),
        Move(Coordinate(1, 4), Coordinate(1, 6)), Move(Coordinate(1, 4), Coordinate(1, 3)),
        Move(Coordinate(1, 4), Coordinate(1, 2)), Move(Coordinate(2, 4), Coordinate(2, 5)),
        Move(Coordinate(2, 4), Coordinate(2, 6)), Move(Coordinate(2, 4), Coordinate(2, 7)),
        Move(Coordinate(2, 4), Coordinate(2, 3)), Move(Coordinate(2, 4), Coordinate(2, 2)),
        Move(Coordinate(2, 4), Coordinate(2, 1)), Move(Coordinate(4, 1), Coordinate(3, 1)),
        Move(Coordinate(4, 1), Coordinate(2, 1)), Move(Coordinate(4, 1), Coordinate(5, 1)),
        Move(Coordinate(4, 1), Coordinate(6, 1)), Move(Coordinate(4, 2), Coordinate(3, 2)),
        Move(Coordinate(4, 2), Coordinate(2, 2)), Move(Coordinate(4, 2), Coordinate(1, 2)),
        Move(Coordinate(4, 2), Coordinate(5, 2)), Move(Coordinate(4, 2), Coordinate(6, 2)),
        Move(Coordinate(4, 2), Coordinate(7, 2)), Move(Coordinate(4, 6), Coordinate(3, 6)),
        Move(Coordinate(4, 6), Coordinate(2, 6)), Move(Coordinate(4, 6), Coordinate(1, 6)),
        Move(Coordinate(4, 6), Coordinate(5, 6)), Move(Coordinate(4, 6), Coordinate(6, 6)),
        Move(Coordinate(4, 6), Coordinate(7, 6)), Move(Coordinate(4, 7), Coordinate(3, 7)),
        Move(Coordinate(4, 7), Coordinate(2, 7)), Move(Coordinate(4, 7), Coordinate(5, 7)),
        Move(Coordinate(4, 7), Coordinate(6, 7)), Move(Coordinate(6, 4), Coordinate(6, 5)),
        Move(Coordinate(6, 4), Coordinate(6, 6)), Move(Coordinate(6, 4), Coordinate(6, 7)),
        Move(Coordinate(6, 4), Coordinate(6, 3)), Move(Coordinate(6, 4), Coordinate(6, 2)),
        Move(Coordinate(6, 4), Coordinate(6, 1)), Move(Coordinate(7, 4), Coordinate(7, 5)),
        Move(Coordinate(7, 4), Coordinate(7, 6)), Move(Coordinate(7, 4), Coordinate(7, 3)),
        Move(Coordinate(7, 4), Coordinate(7, 2)))
      it("the generated game possible moves should be equal to them.") {
        MoveGenerator.gamePossibleMoves(snapshot).foreach(move => assert(possibleMove_Brandubh.contains(move)))
      }
    }
  }

  describe("In Variant Hnefatafl, ") {
    val game = ParserProlog.createGame(GameVariant.Hnefatafl.toString.toLowerCase)
    var snapshot: GameSnapshot = GameSnapshotImpl(GameVariant.Hnefatafl, game.playerToMove, game.winner, game.board, Option.empty, 0, 0)
    describe("moving a black pawn from (1,4) to (4,4), ") {
      snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(1, 4), Coordinate(4, 4)))
      it("in cell (4,4) there should be a black pawn.") {
        assert(snapshot.getBoard.getCell(Coordinate(4, 4)).getPiece.equals(Piece.BlackPawn))
      }
    }
  }

  describe("In this session of game Tablut, ") {
    val game = ParserProlog.createGame(GameVariant.Tablut.toString.toLowerCase)
    var snapshot: GameSnapshot = GameSnapshotImpl(GameVariant.Tablut, game.playerToMove, game.winner, game.board, Option.empty, 0, 0)
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(9,6), Coordinate(6,6)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(5,3), Coordinate(6,3)))
    describe("moving a black in (1,6), "){
      snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(1,6), Coordinate(4,6)))
      it("a white pawn should be captured ") {
        assert(snapshot.getNumberCapturedWhites == 1)
      }
      it("and your cell should be empty.") {
        assert(snapshot.getBoard.getCell(Coordinate(5,6)).getPiece.equals(Piece.Empty))
      }
    }
  }

  describe("In this session of game Tablut, ") {
    val game = ParserProlog.createGame(GameVariant.Tablut.toString.toLowerCase)
    var snapshot: GameSnapshot = GameSnapshotImpl(GameVariant.Tablut, game.playerToMove, game.winner, game.board, Option.empty, 0, 0)
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(1,6), Coordinate(4,6)))
    describe("moving a white in (3,6), "){
      snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(3,5), Coordinate(3,6)))
      it("a white pawn should be captured ") {
        assert(snapshot.getNumberCapturedBlacks == 1)
      }
      it("and your cell should be empty.") {
        assert(snapshot.getBoard.getCell(Coordinate(4,6)).getPiece.equals(Piece.Empty))
      }
    }
  }

  describe("In this session of game Tablut, ") {
    val game = ParserProlog.createGame(GameVariant.Tablut.toString.toLowerCase)
    var snapshot: GameSnapshot = GameSnapshotImpl(GameVariant.Tablut, game.playerToMove, game.winner, game.board, Option.empty, 0, 0)
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(9,4), Coordinate(6,4)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(7,5), Coordinate(7,6)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(4,9), Coordinate(4,6)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(5,7), Coordinate(2,7)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(8,5), Coordinate(8,6)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(6,5), Coordinate(7,5)))
    describe("moving a black in (4,6), "){
      snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(6,9), Coordinate(6,6)))
      it("two white pawns should be captured ") {
        assert(snapshot.getNumberCapturedWhites == 2)
      }
      it("and your cells should be empty.") {
        assert(snapshot.getBoard.getCell(Coordinate(5,6)).getPiece.equals(Piece.Empty) &&
          snapshot.getBoard.getCell(Coordinate(7,6)).getPiece.equals(Piece.Empty))
      }
    }
  }

  describe("In this session of game Tablut, ") {
    val game = ParserProlog.createGame(GameVariant.Tablut.toString.toLowerCase)
    var snapshot: GameSnapshot = GameSnapshotImpl(GameVariant.Tablut, game.playerToMove, game.winner, game.board, Option.empty, 0, 0)
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(9,4), Coordinate(6,4)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(7,5), Coordinate(7,6)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(4,9), Coordinate(4,6)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(5,7), Coordinate(5,2)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(8,5), Coordinate(8,6)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(5,3), Coordinate(4,3)))
    describe("moving a black in (6,6), "){
      snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(6,9), Coordinate(6,6)))
      it("three white pawns should be captured ") {
        assert(snapshot.getNumberCapturedWhites == 3)
      }
      it("and your cells should be empty.") {
        assert(snapshot.getBoard.getCell(Coordinate(5,6)).getPiece.equals(Piece.Empty) &&
          snapshot.getBoard.getCell(Coordinate(7,6)).getPiece.equals(Piece.Empty) &&
          snapshot.getBoard.getCell(Coordinate(6,5)).getPiece.equals(Piece.Empty))
      }
    }
  }

  describe("In this session of game Tawlbwrdd, ") {
    val game = ParserProlog.createGame(GameVariant.Tawlbwrdd.toString.toLowerCase)
    var snapshot: GameSnapshot = GameSnapshotImpl(GameVariant.Tawlbwrdd, game.playerToMove, game.winner, game.board, Option.empty, 0, 0)
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(6,3), Coordinate(4,3)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(6,4), Coordinate(4,4)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(2,7), Coordinate(4,7)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(6,5), Coordinate(6,3)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(7,2), Coordinate(6,2)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(6,3), Coordinate(10,3)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(6,2), Coordinate(6,5)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(6,8), Coordinate(5,8)))
    describe("moving a black in (4,5), "){
      snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(2,5), Coordinate(4,5)))
      it("three white pawns should be captured ") {
        assert(snapshot.getNumberCapturedWhites == 3)
      }
      it("and your cells should be empty.") {
        assert(snapshot.getBoard.getCell(Coordinate(4,4)).getPiece.equals(Piece.Empty) &&
          snapshot.getBoard.getCell(Coordinate(4,6)).getPiece.equals(Piece.Empty) &&
          snapshot.getBoard.getCell(Coordinate(5,5)).getPiece.equals(Piece.Empty))
      }
    }
  }

  describe("Check White Winner in Hnefatafl: ") {
    val game = ParserProlog.createGame(GameVariant.Hnefatafl.toString.toLowerCase)
    var snapshot: GameSnapshot = GameSnapshotImpl(GameVariant.Hnefatafl, game.playerToMove, game.winner, game.board, Option.empty, 0, 0)
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(1,4), Coordinate(1,3)))
    describe("moving the king in (1,1), "){
      snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(6,6), Coordinate(1,1)))
      it("the winner should be player White.") {
        assert(snapshot.getWinner.equals(Player.White))
      }
    }
  }

  describe("Check White Winner in Hnefatafl: ") {
    val game = ParserProlog.createGame(GameVariant.Hnefatafl.toString.toLowerCase)
    var snapshot: GameSnapshot = GameSnapshotImpl(GameVariant.Hnefatafl, game.playerToMove, game.winner, game.board, Option.empty, 0, 0)
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(1,4), Coordinate(1,3)))
    describe("moving the king in (1,11), "){
      snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(6,6), Coordinate(1,11)))
      it("the winner should be player White.") {
        assert(snapshot.getWinner.equals(Player.White))
      }
    }
  }

  describe("Check White Winner in Hnefatafl: ") {
    val game = ParserProlog.createGame(GameVariant.Hnefatafl.toString.toLowerCase)
    var snapshot: GameSnapshot = GameSnapshotImpl(GameVariant.Hnefatafl, game.playerToMove, game.winner, game.board, Option.empty, 0, 0)
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(1,4), Coordinate(1,3)))
    describe("moving the king in (11,11), "){
      snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(6,6), Coordinate(11,11)))
      it("the winner should be player White.") {
        assert(snapshot.getWinner.equals(Player.White))
      }
    }
  }

  describe("Check White Winner in Hnefatafl: ") {
    val game = ParserProlog.createGame(GameVariant.Hnefatafl.toString.toLowerCase)
    var snapshot: GameSnapshot = GameSnapshotImpl(GameVariant.Hnefatafl, game.playerToMove, game.winner, game.board, Option.empty, 0, 0)
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(1,4), Coordinate(1,3)))
    describe("moving the king in (11,1), "){
      snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(6,6), Coordinate(11,1)))
      it("the winner should be player White.") {
        assert(snapshot.getWinner.equals(Player.White))
      }
    }
  }

  describe("In this session of game Hnefatafl, ") {
    val game = ParserProlog.createGame(GameVariant.Hnefatafl.toString.toLowerCase)
    var snapshot: GameSnapshot = GameSnapshotImpl(GameVariant.Hnefatafl, game.playerToMove, game.winner, game.board, Option.empty, 0, 0)
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(4,1), Coordinate(4,2)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(5,5), Coordinate(2,5)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(6,2), Coordinate(6,3)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(6,4), Coordinate(10,4)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(8,1), Coordinate(8,4)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(7,5), Coordinate(10,5)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(6,3), Coordinate(6,4)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(6,5), Coordinate(3,5)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(8,4), Coordinate(7,4)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(6,6), Coordinate(6,5)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(7,4), Coordinate(7,5)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(2,5), Coordinate(2,4)))
    describe("moving the black in (5,5), "){
      snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(5,1), Coordinate(5,5)))
      describe(" the king is captured near to throne and "){
        it("the winner should be player Black ") {
          assert(snapshot.getWinner.equals(Player.Black))
        }
      }
    }
  }

  describe("In this session of game Tawlbwrdd, ") {
    val game = ParserProlog.createGame(GameVariant.Tawlbwrdd.toString.toLowerCase)
    var snapshot: GameSnapshot = GameSnapshotImpl(GameVariant.Tawlbwrdd, game.playerToMove, game.winner, game.board, Option.empty, 0, 0)
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(6,3), Coordinate(3,3)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(6,4), Coordinate(2,4)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(3,6), Coordinate(3,4)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(6,5), Coordinate(6,4)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(5,2), Coordinate(4,2)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(6,4), Coordinate(4,4)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(3,3), Coordinate(4,3)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(6,6), Coordinate(6,2)))
    describe("moving the black in (5,2), "){
      snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(4,2), Coordinate(5,2)))
      describe(" the king is not captured far from throne and "){
        it("the winner should be None ") {
          assert(snapshot.getWinner.equals(Player.None))
        }
      }
    }
  }

  describe("In this session of game Hnefatafl, ") {
    val game = ParserProlog.createGame(GameVariant.Hnefatafl.toString.toLowerCase)
    var snapshot: GameSnapshot = GameSnapshotImpl(GameVariant.Hnefatafl, game.playerToMove, game.winner, game.board, Option.empty, 0, 0)
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(4,1), Coordinate(2,1)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(5,5), Coordinate(2,5)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(5,1), Coordinate(3,1)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(6,4), Coordinate(2,4)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(6,2), Coordinate(2,2)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(6,5), Coordinate(3,5)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(6,1), Coordinate(5,1)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(6,6), Coordinate(6,1)))
    describe("moving the black in (6,2), "){
      snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(3,2), Coordinate(6,2)))
      describe(" the king is not captured on edge and "){
        it("the winner should be None ") {
          assert(snapshot.getWinner.equals(Player.None))
        }
      }
    }
  }

  describe("In this session of game Hnefatafl, ") {
    val game = ParserProlog.createGame(GameVariant.Hnefatafl.toString.toLowerCase)
    var snapshot: GameSnapshot = GameSnapshotImpl(GameVariant.Hnefatafl, game.playerToMove, game.winner, game.board, Option.empty, 0, 0)
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(1,4), Coordinate(1,3)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(4,6), Coordinate(4,8)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(4,1), Coordinate(4,2)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(5,5), Coordinate(2,5)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(5,1), Coordinate(3,1)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(6,4), Coordinate(1,4)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(2,6), Coordinate(3,6)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(6,5), Coordinate(3,5)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(6,2), Coordinate(6,5)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(5,6), Coordinate(5,1)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(3,6), Coordinate(5,6)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(6,8), Coordinate(10,8)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(6,10), Coordinate(6,9)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(5,7), Coordinate(2,7)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(10,6), Coordinate(9,6)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(6,7), Coordinate(3,7)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(6,9), Coordinate(6,7)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(8,6), Coordinate(8,10)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(8,11), Coordinate(9,11)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(7,7), Coordinate(7,10)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(8,1), Coordinate(9,1)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(7,5), Coordinate(7,2)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(11,4), Coordinate(10,4)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(7,6), Coordinate(7,3)))
    describe("moving the black in (7,6), "){
      snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(9,6), Coordinate(7,6)))
      it("the king should be captured on throne."){
        assert(snapshot.getBoard.getCell(Coordinate(6,6)).getPiece.equals(Piece.WhiteKing))
      }
      it("winner should be player Black.") {
        assert(snapshot.getWinner.equals(Player.Black))
      }
    }
  }

  describe("In this session of game Tablut, ") {
    val game = ParserProlog.createGame(GameVariant.Tablut.toString.toLowerCase)
    var snapshot: GameSnapshot = GameSnapshotImpl(GameVariant.Tablut, game.playerToMove, game.winner, game.board, Option.empty, 0, 0)
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(1,4), Coordinate(3,4)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(2,4), Coordinate(4,3)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(4,1), Coordinate(3,9)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(5,3), Coordinate(4,5)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(6,9), Coordinate(7,9)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(3,5), Coordinate(3,8)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(5,9), Coordinate(4,9)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(5,6), Coordinate(3,6)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(9,6), Coordinate(9,7)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(4,5), Coordinate(4,7)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(2,5), Coordinate(4,5)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(6,5), Coordinate(6,9)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(6,1), Coordinate(6,5)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(5,7), Coordinate(8,7)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(5,8), Coordinate(5,6)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(4,7), Coordinate(1,7)))
    describe("moving the black in (5,4), "){
      snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(5,1), Coordinate(5,4)))
      it("the king should be captured on throne."){
        assert(snapshot.getBoard.getCell(Coordinate(5,5)).getPiece.equals(Piece.WhiteKing))
      }
      it("winner should be player Black.") {
        assert(snapshot.getWinner.equals(Player.Black))
      }
    }
  }

  describe("In this session of game Brandubh, ") {
    val game = ParserProlog.createGame(GameVariant.Brandubh.toString.toLowerCase)
    var snapshot: GameSnapshot = GameSnapshotImpl(GameVariant.Brandubh, game.playerToMove, game.winner, game.board, Option.empty, 0, 0)
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(1,4), Coordinate(3,4)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(5,4), Coordinate(5,7)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(2,4), Coordinate(4,3)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(4,5), Coordinate(1,5)))
    describe("moving the black in (4,5), "){
      snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(4,1), Coordinate(4,5)))
      describe("the king is not captured on throne and "){
        it("winner should be None.") {
          assert(snapshot.getWinner.equals(Player.None))
        }
      }
    }
  }

  describe("In this session of game Tablut, ") {
    val game = ParserProlog.createGame(GameVariant.Tablut.toString.toLowerCase)
    var snapshot: GameSnapshot = GameSnapshotImpl(GameVariant.Tablut, game.playerToMove, game.winner, game.board, Option.empty, 0, 0)
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(1,4), Coordinate(1,3)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(5,5), Coordinate(2,3)))
    describe("moving the black in (3,3), "){
      snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(1,5), Coordinate(3,3)))
      it("the king should be captured far from throne."){
        assert(snapshot.getBoard.getCell(Coordinate(2,3)).getPiece.equals(Piece.WhiteKing))
      }
      it("winner should be player Black.") {
        assert(snapshot.getWinner.equals(Player.Black))
      }
    }
  }

  describe("In this session of game Tablut, ") {
    val game = ParserProlog.createGame(GameVariant.Tablut.toString.toLowerCase)
    var snapshot: GameSnapshot = GameSnapshotImpl(GameVariant.Tablut, game.playerToMove, game.winner, game.board, Option.empty, 0, 0)
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(6, 9), Coordinate(8, 9)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(5, 5), Coordinate(8, 8)))
    describe("moving the black in (8,7), "){
      snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(8, 5), Coordinate(8, 7)))
      it("the king should be captured far from throne."){
        assert(snapshot.getBoard.getCell(Coordinate(8,8)).getPiece.equals(Piece.WhiteKing))
      }
      it("winner should be player Black.") {
        assert(snapshot.getWinner.equals(Player.Black))
      }
    }
  }

  describe("In this session of game Tablut, ") {
    val game = ParserProlog.createGame(GameVariant.Tablut.toString.toLowerCase)
    var snapshot: GameSnapshot = GameSnapshotImpl(GameVariant.Tablut, game.playerToMove, game.winner, game.board, Option.empty, 0, 0)
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(6,9), Coordinate(7,9)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(5,5), Coordinate(6,9)))
    describe("moving the black in (6,8), "){
      snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(5,8), Coordinate(6,8)))
      describe("the sneaky king is not captured and "){
        it("winner should be player None.") {
          assert(snapshot.getWinner.equals(Player.None))
        }
      }
    }
  }

  describe("In this session of game Tawlbwrdd, ") {
    val game = ParserProlog.createGame(GameVariant.Tawlbwrdd.toString.toLowerCase)
    var snapshot: GameSnapshot = GameSnapshotImpl(GameVariant.Tawlbwrdd, game.playerToMove, game.winner, game.board, Option.empty, 0, 0)
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(6,9), Coordinate(7,9)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(6,6), Coordinate(6,10)))
    describe("moving the black in (8,9), "){
      snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(7,9), Coordinate(8,9)))
      describe("the sneaky king is not captured and "){
        it("winner should be player None.") {
          assert(snapshot.getWinner.equals(Player.None))
        }
      }
    }
  }

  describe("In this session of game Brandubh, ") {
    val game = ParserProlog.createGame(GameVariant.Brandubh.toString.toLowerCase)
    var snapshot: GameSnapshot = GameSnapshotImpl(GameVariant.Brandubh, game.playerToMove, game.winner, game.board, Option.empty, 0, 0)
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(1,4), Coordinate(3,3)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(4,4), Coordinate(4,3)))
    describe("moving the black in (5,3), "){
      snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(2,4), Coordinate(5,3)))
      it("the king should be captured near to throne."){
        assert(snapshot.getBoard.getCell(Coordinate(4,3)).getPiece.equals(Piece.WhiteKing))
      }
      it("winner should be player Black.") {
        assert(snapshot.getWinner.equals(Player.Black))
      }
    }
  }

  describe("In this session of game Brandubh, ") {
    val game = ParserProlog.createGame(GameVariant.Brandubh.toString.toLowerCase)
    var snapshot: GameSnapshot = GameSnapshotImpl(GameVariant.Brandubh, game.playerToMove, game.winner, game.board, Option.empty, 0, 0)
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(4,6), Coordinate(1,6)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(4,5), Coordinate(7,5)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(6,4), Coordinate(6,5)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(4,4), Coordinate(4,5)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(2,4), Coordinate(2,5)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(7,5), Coordinate(7,6)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(2,5), Coordinate(3,5)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(7,6), Coordinate(7,5)))
    describe("moving the black in (5,5), "){
      snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(6,5), Coordinate(5,5)))
      describe("the king is not captured near to throne and "){
        it("winner should be player None.") {
          assert(snapshot.getWinner.equals(Player.None))
        }
      }
    }
  }

  describe("In this session of game Brandubh, ") {
    val game = ParserProlog.createGame(GameVariant.Brandubh.toString.toLowerCase)
    var snapshot: GameSnapshot = GameSnapshotImpl(GameVariant.Brandubh, game.playerToMove, game.winner, game.board, Option.empty, 0, 0)
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(4,6), Coordinate(1,6)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(3,4), Coordinate(3,7)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(2,4), Coordinate(2,7)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(5,4), Coordinate(5,7)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(6,4), Coordinate(6,7)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(4,3), Coordinate(1,3)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(4,2), Coordinate(1,2)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(4,5), Coordinate(7,5)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(1,6), Coordinate(7,6)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(4,4), Coordinate(4,6)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(4,7), Coordinate(3,7)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(4,6), Coordinate(4,7)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(7,6), Coordinate(5,6)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(4,7), Coordinate(4,6)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(6,7), Coordinate(5,7)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(4,6), Coordinate(4,7)))
    describe("moving the black in (4,6), "){
      snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(5,6), Coordinate(4,6)))
      describe("the whites have not possible moves and "){
        it("the game should end with a Draw.") {
          assert(snapshot.getWinner.equals(Player.Draw))
        }
      }
    }
  }

  describe("In this session of game Tablut, ") {
    val game = ParserProlog.createGame(GameVariant.Tablut.toString.toLowerCase)
    var snapshot: GameSnapshot = GameSnapshotImpl(GameVariant.Tablut, game.playerToMove, game.winner, game.board, Option.empty, 0, 0)
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(5,2), Coordinate(7,2)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(5,3), Coordinate(1,3)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(7,2), Coordinate(1,2)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(5,4), Coordinate(2,4)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(1,2), Coordinate(2,2)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(5,7), Coordinate(1,7)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(5,8), Coordinate(1,8)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(3,5), Coordinate(3,6)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(2,2), Coordinate(2,3)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(3,6), Coordinate(3,9)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(2,5), Coordinate(2,9)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(5,6), Coordinate(5,8)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(6,9), Coordinate(6,8)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(4,5), Coordinate(4,2)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(1,8), Coordinate(4,8)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(5,5), Coordinate(2,5)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(4,8), Coordinate(4,3)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(6,5), Coordinate(6,7)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(1,6), Coordinate(6,6)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(7,5), Coordinate(6,5)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(6,1), Coordinate(6,4)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(2,5), Coordinate(2,6)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(9,6), Coordinate(9,7)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(2,6), Coordinate(3,6)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(9,7), Coordinate(1,7)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(3,6), Coordinate(1,6)))
    describe("moving the black in (2,6), "){
      snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(6,6), Coordinate(2,6)))
      describe("the whites have not possible moves and "){
        it("the game should end with a Draw.") {
          assert(snapshot.getWinner.equals(Player.Draw))
        }
      }
    }
  }

  describe("In this session of game Hnefatafl, ") {
    val game = ParserProlog.createGame(GameVariant.Hnefatafl.toString.toLowerCase)
    var snapshot: GameSnapshot = GameSnapshotImpl(GameVariant.Hnefatafl, game.playerToMove, game.winner, game.board, Option.empty, 0, 0)
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(6,2), Coordinate(3,2)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(4,6), Coordinate(4,2)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(5,1), Coordinate(5,2)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(6,4), Coordinate(2,4)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(3,2), Coordinate(3,4)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(5,7), Coordinate(2,7)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(3,4), Coordinate(3,7)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(6,8), Coordinate(2,8)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(3,7), Coordinate(3,8)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(5,6), Coordinate(5,10)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(4,11), Coordinate(4,10)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(6,5), Coordinate(6,2)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(7,1), Coordinate(7,2)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(8,6), Coordinate(8,2)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(8,11), Coordinate(8,3)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(5,5), Coordinate(5,10)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(5,2), Coordinate(5,9)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(7,7), Coordinate(7,10)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(8,3), Coordinate(8,10)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(7,6), Coordinate(7,10)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(5,9), Coordinate(7,9)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(7,5), Coordinate(7,3)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(7,9), Coordinate(7,4)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(6,7), Coordinate(2,7)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(11,7), Coordinate(3,7)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(6,6), Coordinate(9,6)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(8,10), Coordinate(8,11)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(9,6), Coordinate(9,11)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(10,6), Coordinate(10,11)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(9,11), Coordinate(9,10)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(11,8), Coordinate(9,8)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(9,10), Coordinate(9,11)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(9,8), Coordinate(9,9)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(9,11), Coordinate(9,10)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(11,6), Coordinate(10,6)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(9,10), Coordinate(9,11)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(10,6), Coordinate(10,10)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(9,11), Coordinate(10,11)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(9,9), Coordinate(9,10)))
    snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(10,11), Coordinate(9,11)))
    describe("moving the black in (2,6), "){
      snapshot = MoveGenerator.makeMove(snapshot, Move(Coordinate(10,10), Coordinate(10,11)))
      describe("the whites have not possible moves and "){
        it("the game should end with a Draw.") {
          assert(snapshot.getWinner.equals(Player.Draw))
        }
      }
    }
  }
}