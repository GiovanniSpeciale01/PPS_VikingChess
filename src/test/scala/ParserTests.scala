import model.{GameRules, GameVariant, ParserProlog, ParserPrologImpl, Player}
import org.scalamock.scalatest.MockFactory
import org.scalatest.{FunSuite, Matchers}
import utils.{Coordinate, Move}

import scala.collection.mutable.ListBuffer

class ParserTests extends FunSuite with MockFactory with Matchers {

  val parser: ParserProlog = ParserPrologImpl(GameRules.theory())

  test("Tests of a correct horizontal capture of white pawn.") {
    inSequence {
      parser.createGame(GameVariant.Hnefatafl.toString().toLowerCase())
      parser.makeLegitMove(Move(Coordinate(1,4), Coordinate(4,4)))
      parser.makeLegitMove(Move(Coordinate(4,6), Coordinate(4,5)))
      parser.makeLegitMove(Move(Coordinate(2,6), Coordinate(4,6)))
    }
    parser.showPossibleCells(Coordinate(4,5)) shouldBe ListBuffer.empty
  }

  test("Tests of a correct vertical capture of black pawn.") {
    inSequence {
      parser.createGame(GameVariant.Hnefatafl.toString().toLowerCase())
      parser.makeLegitMove(Move(Coordinate(7,1), Coordinate(7,4)))
      parser.makeLegitMove(Move(Coordinate(8,6), Coordinate(8,4)))
    }
    parser.showPossibleCells(Coordinate(7,4)) shouldBe ListBuffer.empty
  }

  test("Tests of a uncorrect capture of black pawn.") {
    inSequence {
      parser.createGame(GameVariant.Hnefatafl.toString().toLowerCase())
      parser.makeLegitMove(Move(Coordinate(2,6), Coordinate(3,6)))
      parser.makeLegitMove(Move(Coordinate(6,4), Coordinate(4,4)))
    }
    val currentGame: (_, _, _, Int) = parser.makeLegitMove(Move(Coordinate(1,5), Coordinate(4,5)))
    currentGame._4 shouldBe 0
  }

  test("Tests king captured in Brandubh.") {
    inSequence {
      parser.createGame(GameVariant.Brandubh.toString().toLowerCase())
      parser.makeLegitMove(Move(Coordinate(1,4), Coordinate(1,5)))
      parser.makeLegitMove(Move(Coordinate(4,5), Coordinate(7,5)))
      parser.makeLegitMove(Move(Coordinate(6,4), Coordinate(6,5)))
      parser.makeLegitMove(Move(Coordinate(4,4), Coordinate(4,5)))
      parser.makeLegitMove(Move(Coordinate(1,5), Coordinate(3,5)))
      parser.makeLegitMove(Move(Coordinate(4,3), Coordinate(1,3)))
    }
    val currentGame: (_, Player.Value, _, _) = parser.makeLegitMove(Move(Coordinate(6,5), Coordinate(5,5)))
    currentGame._2 shouldBe Player.Black
  }

  test("Tests white wins in Brandubh.") {
    inSequence {
    parser.createGame(GameVariant.Brandubh.toString().toLowerCase())
      parser.makeLegitMove(Move(Coordinate(4,6), Coordinate(2,6)))
      parser.makeLegitMove(Move(Coordinate(4,5), Coordinate(1,5)))
      parser.makeLegitMove(Move(Coordinate(4,7), Coordinate(2,7)))
      parser.makeLegitMove(Move(Coordinate(4,4), Coordinate(4,7)))
      parser.makeLegitMove(Move(Coordinate(6,4), Coordinate(6,6)))
    }
    val currentGame: (_, Player.Value, _, _) = parser.makeLegitMove(Move(Coordinate(4,7), Coordinate(7,7)))
    currentGame._2 shouldBe Player.White
  }

  test("Tests king captured on throne in Brandubh.") {
    inSequence {
      parser.createGame(GameVariant.Brandubh.toString().toLowerCase())
      parser.makeLegitMove(Move(Coordinate(1,4), Coordinate(1,6)))
      parser.makeLegitMove(Move(Coordinate(3,4), Coordinate(3,7)))
      parser.makeLegitMove(Move(Coordinate(2,4), Coordinate(3,4)))
      parser.makeLegitMove(Move(Coordinate(4,3), Coordinate(7,3)))
      parser.makeLegitMove(Move(Coordinate(4,2), Coordinate(4,3)))
      parser.makeLegitMove(Move(Coordinate(5,4), Coordinate(5,1)))
      parser.makeLegitMove(Move(Coordinate(6,4), Coordinate(5,4)))
      parser.makeLegitMove(Move(Coordinate(4,5), Coordinate(2,5)))
    }
    val currentGame: (_, Player.Value, _, _) = parser.makeLegitMove(Move(Coordinate(4,6), Coordinate(4,5)))
    currentGame._2 shouldBe Player.Black
  }

  test("Tests horizontal king captured in Tablut.") {
    inSequence {
      parser.createGame(GameVariant.Tablut.toString().toLowerCase())
      parser.makeLegitMove(Move(Coordinate(1,6), Coordinate(2,6)))
      parser.makeLegitMove(Move(Coordinate(5,7), Coordinate(9,7)))
      parser.makeLegitMove(Move(Coordinate(2,6), Coordinate(1,6)))
      parser.makeLegitMove(Move(Coordinate(5,6), Coordinate(8,6)))
      parser.makeLegitMove(Move(Coordinate(1,6), Coordinate(2,6)))
      parser.makeLegitMove(Move(Coordinate(5,5), Coordinate(5,7)))
    }
    val currentGame: (_, Player.Value, _, _) = parser.makeLegitMove(Move(Coordinate(2,6), Coordinate(5,6)))
    currentGame._2 shouldBe Player.Black
  }

  test("Tests vertical king captured in Tablut.") {
    inSequence {
      parser.createGame(GameVariant.Tablut.toString().toLowerCase())
      parser.makeLegitMove(Move(Coordinate(4,1), Coordinate(4,3)))
      parser.makeLegitMove(Move(Coordinate(5,3), Coordinate(9,3)))
      parser.makeLegitMove(Move(Coordinate(8,5), Coordinate(8,3)))
      parser.makeLegitMove(Move(Coordinate(5,4), Coordinate(8,4)))
      parser.makeLegitMove(Move(Coordinate(1,4), Coordinate(1,2)))
      parser.makeLegitMove(Move(Coordinate(5,5), Coordinate(5,3)))
    }
    val currentGame: (_, Player.Value, _, _) = parser.makeLegitMove(Move(Coordinate(8,3), Coordinate(6,3)))
    currentGame._2 shouldBe Player.Black
  }

  test("Tests king captured far from throne in Tawlbwrdd.") {
    inSequence {
      parser.createGame(GameVariant.Tawlbwrdd.toString().toLowerCase())
      parser.makeLegitMove(Move(Coordinate(6,9), Coordinate(1,9)))
      parser.makeLegitMove(Move(Coordinate(6,8), Coordinate(1,8)))
      parser.makeLegitMove(Move(Coordinate(1,5), Coordinate(1,4)))
      parser.makeLegitMove(Move(Coordinate(5,7), Coordinate(3,7)))
      parser.makeLegitMove(Move(Coordinate(1,4), Coordinate(1,5)))
      parser.makeLegitMove(Move(Coordinate(6,7), Coordinate(4,7)))
      parser.makeLegitMove(Move(Coordinate(1,5), Coordinate(1,4)))
      parser.makeLegitMove(Move(Coordinate(6,6), Coordinate(6,10)))
    }
    val currentGame: (_, Player.Value, _, _) = parser.makeLegitMove(Move(Coordinate(1,9), Coordinate(6,9)))
    currentGame._2 shouldBe Player.Black
  }

  test("Tests king not captured on 2 sides next to throne in Brandubh") {
    inSequence {
      parser.createGame(GameVariant.Brandubh.toString().toLowerCase())
      parser.makeLegitMove(Move(Coordinate(4,6), Coordinate(1,6)))
      parser.makeLegitMove(Move(Coordinate(4,5), Coordinate(7,5)))
      parser.makeLegitMove(Move(Coordinate(6,4), Coordinate(6,5)))
      parser.makeLegitMove(Move(Coordinate(4,4), Coordinate(4,5)))
      parser.makeLegitMove(Move(Coordinate(2,4), Coordinate(2,5)))
      parser.makeLegitMove(Move(Coordinate(7,5), Coordinate(7,6)))
      parser.makeLegitMove(Move(Coordinate(2,5), Coordinate(3,5)))
      parser.makeLegitMove(Move(Coordinate(7,6), Coordinate(7,5)))
    }
    val currentGame: (_, Player.Value, _, _) = parser.makeLegitMove(Move(Coordinate(6,5), Coordinate(5,5)))
    currentGame._2 shouldBe Player.None
  }

  test("Tests draw in Brandubh.") {
    inSequence {
      parser.createGame(GameVariant.Brandubh.toString().toLowerCase())
      parser.makeLegitMove(Move(Coordinate(4, 6), Coordinate(1, 6)))
      parser.makeLegitMove(Move(Coordinate(3, 4), Coordinate(3, 7)))
      parser.makeLegitMove(Move(Coordinate(2, 4), Coordinate(2, 7)))
      parser.makeLegitMove(Move(Coordinate(5, 4), Coordinate(5, 7)))
      parser.makeLegitMove(Move(Coordinate(6, 4), Coordinate(6, 7)))
      parser.makeLegitMove(Move(Coordinate(4, 3), Coordinate(1, 3)))
      parser.makeLegitMove(Move(Coordinate(4, 2), Coordinate(1, 2)))
      parser.makeLegitMove(Move(Coordinate(4, 5), Coordinate(7, 5)))
      parser.makeLegitMove(Move(Coordinate(1, 6), Coordinate(7, 6)))
      parser.makeLegitMove(Move(Coordinate(4, 4), Coordinate(4, 6)))
      parser.makeLegitMove(Move(Coordinate(4, 7), Coordinate(3, 7)))
      parser.makeLegitMove(Move(Coordinate(4, 6), Coordinate(4, 7)))
      parser.makeLegitMove(Move(Coordinate(7, 6), Coordinate(5, 6)))
      parser.makeLegitMove(Move(Coordinate(4, 7), Coordinate(4, 6)))
      parser.makeLegitMove(Move(Coordinate(6, 7), Coordinate(5, 7)))
      parser.makeLegitMove(Move(Coordinate(4, 6), Coordinate(4, 7)))
    }
    val currentGame: (_, Player.Value, _, _) = parser.makeLegitMove(Move(Coordinate(5,6), Coordinate(4,6)))
    currentGame._2 shouldBe Player.Draw
  }

  test("Tests 1 deep clone."){
    var copyParser: ParserProlog = null

    parser.createGame(GameVariant.Brandubh.toString().toLowerCase())
    parser.makeLegitMove(Move(Coordinate(1,4), Coordinate(1,5)))
    parser.makeLegitMove(Move(Coordinate(4,5), Coordinate(7,5)))
    parser.makeLegitMove(Move(Coordinate(6,4), Coordinate(6,5)))
    copyParser = parser.copy()
    parser.makeLegitMove(Move(Coordinate(4,4), Coordinate(4,5)))
    parser.makeLegitMove(Move(Coordinate(1,5), Coordinate(3,5)))
    parser.makeLegitMove(Move(Coordinate(4,3), Coordinate(1,3)))


    assert(!parser.equals(copyParser))
  }

  test("Tests 2 deep clone."){
    var copyParser: ParserProlog = null
    inSequence {
      parser.createGame(GameVariant.Brandubh.toString().toLowerCase())
      parser.makeLegitMove(Move(Coordinate(1,4), Coordinate(1,5)))
      parser.makeLegitMove(Move(Coordinate(4,5), Coordinate(7,5)))
      parser.makeLegitMove(Move(Coordinate(6,4), Coordinate(6,5)))
    }
    copyParser = parser.copy()
    assert(parser.equals(copyParser))
  }

  test("Tests 3 deep clone.") {
    var copyParser: ParserProlog = null
    inSequence {
      parser.createGame(GameVariant.Brandubh.toString().toLowerCase())
      copyParser = parser.copy()
      parser.makeLegitMove(Move(Coordinate(1,4), Coordinate(1,3)))
    }
    assert(parser.showPossibleCells(Coordinate(1, 4)).isEmpty)
    assert(copyParser.showPossibleCells(Coordinate(1, 4)).nonEmpty)
  }



}
