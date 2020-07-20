import model.game.{Coordinate, GameVariant, Move, Player}
import model.prolog.{ParserProlog, PrologSnapshot}
import org.junit.runner.RunWith
import org.scalamock.scalatest.MockFactory
import org.scalatest.junit.JUnitRunner
import org.scalatest.{FunSuite, Matchers}

import scala.collection.mutable.ListBuffer

@RunWith(classOf[JUnitRunner])
class ParserTest extends FunSuite with MockFactory with Matchers {

  test("Tests of a correct horizontal capture of white pawn - Hnefatafl") {
    inSequence {
      ParserProlog.createGame(GameVariant.Hnefatafl.toString.toLowerCase())
      ParserProlog.makeLegitMove(Move(Coordinate(1,4), Coordinate(4,4)))
      ParserProlog.makeLegitMove(Move(Coordinate(4,6), Coordinate(4,5)))
      ParserProlog.makeLegitMove(Move(Coordinate(2,6), Coordinate(4,6)))
    }
    ParserProlog.showPossibleCells(Coordinate(4,5)) shouldBe ListBuffer.empty
  }

  test("Tests of a correct vertical capture of black pawn - Hnefatafl") {
    inSequence {
      ParserProlog.createGame(GameVariant.Hnefatafl.toString.toLowerCase())
      ParserProlog.makeLegitMove(Move(Coordinate(7,1), Coordinate(7,4)))
      ParserProlog.makeLegitMove(Move(Coordinate(8,6), Coordinate(8,4)))
    }
    ParserProlog.showPossibleCells(Coordinate(7,4)) shouldBe ListBuffer.empty
  }

  test("Tests of a uncorrect capture of black pawn - Hnefatafl") {
    inSequence {
      ParserProlog.createGame(GameVariant.Hnefatafl.toString.toLowerCase())
      ParserProlog.makeLegitMove(Move(Coordinate(2,6), Coordinate(3,6)))
      ParserProlog.makeLegitMove(Move(Coordinate(6,4), Coordinate(4,4)))
    }
    val currentGame: Option[PrologSnapshot] = ParserProlog.makeLegitMove(Move(Coordinate(1,5), Coordinate(4,5)))
    currentGame.get.nCaptures shouldBe 0
  }

  test("Tests king captured - Brandubh") {
    inSequence {
      ParserProlog.createGame(GameVariant.Brandubh.toString.toLowerCase())
      ParserProlog.makeLegitMove(Move(Coordinate(1,4), Coordinate(1,5)))
      ParserProlog.makeLegitMove(Move(Coordinate(4,5), Coordinate(7,5)))
      ParserProlog.makeLegitMove(Move(Coordinate(6,4), Coordinate(6,5)))
      ParserProlog.makeLegitMove(Move(Coordinate(4,4), Coordinate(4,5)))
      ParserProlog.makeLegitMove(Move(Coordinate(1,5), Coordinate(3,5)))
      ParserProlog.makeLegitMove(Move(Coordinate(4,3), Coordinate(1,3)))
    }
    val currentGame: Option[PrologSnapshot] = ParserProlog.makeLegitMove(Move(Coordinate(6,5), Coordinate(5,5)))
    currentGame.get.winner shouldBe Player.Black
  }

  test("Tests white wins - Brandubh") {
    inSequence {
    ParserProlog.createGame(GameVariant.Brandubh.toString.toLowerCase())
      ParserProlog.makeLegitMove(Move(Coordinate(4,6), Coordinate(2,6)))
      ParserProlog.makeLegitMove(Move(Coordinate(4,5), Coordinate(1,5)))
      ParserProlog.makeLegitMove(Move(Coordinate(4,7), Coordinate(2,7)))
      ParserProlog.makeLegitMove(Move(Coordinate(4,4), Coordinate(4,7)))
      ParserProlog.makeLegitMove(Move(Coordinate(6,4), Coordinate(6,6)))
    }
    val currentGame: Option[PrologSnapshot] = ParserProlog.makeLegitMove(Move(Coordinate(4,7), Coordinate(7,7)))
    currentGame.get.winner shouldBe Player.White
  }

  test("Tests king captured on throne - Brandubh") {
    inSequence {
      ParserProlog.createGame(GameVariant.Brandubh.toString.toLowerCase())
      ParserProlog.makeLegitMove(Move(Coordinate(1,4), Coordinate(1,6)))
      ParserProlog.makeLegitMove(Move(Coordinate(3,4), Coordinate(3,7)))
      ParserProlog.makeLegitMove(Move(Coordinate(2,4), Coordinate(3,4)))
      ParserProlog.makeLegitMove(Move(Coordinate(4,3), Coordinate(7,3)))
      ParserProlog.makeLegitMove(Move(Coordinate(4,2), Coordinate(4,3)))
      ParserProlog.makeLegitMove(Move(Coordinate(5,4), Coordinate(5,1)))
      ParserProlog.makeLegitMove(Move(Coordinate(6,4), Coordinate(5,4)))
      ParserProlog.makeLegitMove(Move(Coordinate(4,5), Coordinate(2,5)))
    }
    val currentGame: Option[PrologSnapshot] = ParserProlog.makeLegitMove(Move(Coordinate(4,6), Coordinate(4,5)))
    currentGame.get.winner shouldBe Player.Black
  }

  test("Tests horizontal king captured - Tablut") {
    inSequence {
      ParserProlog.createGame(GameVariant.Tablut.toString.toLowerCase())
      ParserProlog.makeLegitMove(Move(Coordinate(1,6), Coordinate(2,6)))
      ParserProlog.makeLegitMove(Move(Coordinate(5,7), Coordinate(9,7)))
      ParserProlog.makeLegitMove(Move(Coordinate(2,6), Coordinate(1,6)))
      ParserProlog.makeLegitMove(Move(Coordinate(5,6), Coordinate(8,6)))
      ParserProlog.makeLegitMove(Move(Coordinate(1,6), Coordinate(2,6)))
      ParserProlog.makeLegitMove(Move(Coordinate(5,5), Coordinate(5,7)))
    }
    val currentGame: Option[PrologSnapshot] = ParserProlog.makeLegitMove(Move(Coordinate(2,6), Coordinate(5,6)))
    currentGame.get.winner shouldBe Player.Black
  }

  test("Tests vertical king captured - Tablut") {
    inSequence {
      ParserProlog.createGame(GameVariant.Tablut.toString.toLowerCase())
      ParserProlog.makeLegitMove(Move(Coordinate(4,1), Coordinate(4,3)))
      ParserProlog.makeLegitMove(Move(Coordinate(5,3), Coordinate(9,3)))
      ParserProlog.makeLegitMove(Move(Coordinate(8,5), Coordinate(8,3)))
      ParserProlog.makeLegitMove(Move(Coordinate(5,4), Coordinate(8,4)))
      ParserProlog.makeLegitMove(Move(Coordinate(1,4), Coordinate(1,2)))
      ParserProlog.makeLegitMove(Move(Coordinate(5,5), Coordinate(5,3)))
    }
    val currentGame: Option[PrologSnapshot] = ParserProlog.makeLegitMove(Move(Coordinate(8,3), Coordinate(6,3)))
    currentGame.get.winner shouldBe Player.Black
  }

  test("Tests king captured far from throne - Tawlbwrdd") {
    inSequence {
      ParserProlog.createGame(GameVariant.Tawlbwrdd.toString.toLowerCase())
      ParserProlog.makeLegitMove(Move(Coordinate(6,9), Coordinate(1,9)))
      ParserProlog.makeLegitMove(Move(Coordinate(6,8), Coordinate(1,8)))
      ParserProlog.makeLegitMove(Move(Coordinate(1,5), Coordinate(1,4)))
      ParserProlog.makeLegitMove(Move(Coordinate(5,7), Coordinate(3,7)))
      ParserProlog.makeLegitMove(Move(Coordinate(1,4), Coordinate(1,5)))
      ParserProlog.makeLegitMove(Move(Coordinate(6,7), Coordinate(4,7)))
      ParserProlog.makeLegitMove(Move(Coordinate(1,5), Coordinate(1,4)))
      ParserProlog.makeLegitMove(Move(Coordinate(6,6), Coordinate(6,10)))
    }
    val currentGame: Option[PrologSnapshot] = ParserProlog.makeLegitMove(Move(Coordinate(1,9), Coordinate(6,9)))
    currentGame.get.winner shouldBe Player.Black
  }

  test("Tests king not captured on 2 sides next to throne - Brandubh") {
    inSequence {
      ParserProlog.createGame(GameVariant.Brandubh.toString.toLowerCase())
      ParserProlog.makeLegitMove(Move(Coordinate(4,6), Coordinate(1,6)))
      ParserProlog.makeLegitMove(Move(Coordinate(4,5), Coordinate(7,5)))
      ParserProlog.makeLegitMove(Move(Coordinate(6,4), Coordinate(6,5)))
      ParserProlog.makeLegitMove(Move(Coordinate(4,4), Coordinate(4,5)))
      ParserProlog.makeLegitMove(Move(Coordinate(2,4), Coordinate(2,5)))
      ParserProlog.makeLegitMove(Move(Coordinate(7,5), Coordinate(7,6)))
      ParserProlog.makeLegitMove(Move(Coordinate(2,5), Coordinate(3,5)))
      ParserProlog.makeLegitMove(Move(Coordinate(7,6), Coordinate(7,5)))
    }
    val currentGame: Option[PrologSnapshot] = ParserProlog.makeLegitMove(Move(Coordinate(6,5), Coordinate(5,5)))
    currentGame.get.winner shouldBe Player.None
  }

  test("Tests draw 7x7 - Brandubh") {
    inSequence {
      ParserProlog.createGame(GameVariant.Brandubh.toString.toLowerCase())
      ParserProlog.makeLegitMove(Move(Coordinate(4, 6), Coordinate(1, 6)))
      ParserProlog.makeLegitMove(Move(Coordinate(3, 4), Coordinate(3, 7)))
      ParserProlog.makeLegitMove(Move(Coordinate(2, 4), Coordinate(2, 7)))
      ParserProlog.makeLegitMove(Move(Coordinate(5, 4), Coordinate(5, 7)))
      ParserProlog.makeLegitMove(Move(Coordinate(6, 4), Coordinate(6, 7)))
      ParserProlog.makeLegitMove(Move(Coordinate(4, 3), Coordinate(1, 3)))
      ParserProlog.makeLegitMove(Move(Coordinate(4, 2), Coordinate(1, 2)))
      ParserProlog.makeLegitMove(Move(Coordinate(4, 5), Coordinate(7, 5)))
      ParserProlog.makeLegitMove(Move(Coordinate(1, 6), Coordinate(7, 6)))
      ParserProlog.makeLegitMove(Move(Coordinate(4, 4), Coordinate(4, 6)))
      ParserProlog.makeLegitMove(Move(Coordinate(4, 7), Coordinate(3, 7)))
      ParserProlog.makeLegitMove(Move(Coordinate(4, 6), Coordinate(4, 7)))
      ParserProlog.makeLegitMove(Move(Coordinate(7, 6), Coordinate(5, 6)))
      ParserProlog.makeLegitMove(Move(Coordinate(4, 7), Coordinate(4, 6)))
      ParserProlog.makeLegitMove(Move(Coordinate(6, 7), Coordinate(5, 7)))
      ParserProlog.makeLegitMove(Move(Coordinate(4, 6), Coordinate(4, 7)))
    }
    val currentGame: Option[PrologSnapshot] = ParserProlog.makeLegitMove(Move(Coordinate(5,6), Coordinate(4,6)))
    currentGame.get.winner shouldBe Player.Draw
  }

  test("Tests draw 11x11 - Hnefatafl") {
    inSequence {
      ParserProlog.createGame(GameVariant.Hnefatafl.toString.toLowerCase())
      ParserProlog.makeLegitMove(Move(Coordinate(6,2), Coordinate(3,2)))
      ParserProlog.makeLegitMove(Move(Coordinate(4,6), Coordinate(4,2)))
      ParserProlog.makeLegitMove(Move(Coordinate(5,1), Coordinate(5,2)))
      ParserProlog.makeLegitMove(Move(Coordinate(6,4), Coordinate(2,4)))
      ParserProlog.makeLegitMove(Move(Coordinate(3,2), Coordinate(3,4)))
      ParserProlog.makeLegitMove(Move(Coordinate(5,7), Coordinate(2,7)))
      ParserProlog.makeLegitMove(Move(Coordinate(3,4), Coordinate(3,7)))
      ParserProlog.makeLegitMove(Move(Coordinate(6,8), Coordinate(2,8)))
      ParserProlog.makeLegitMove(Move(Coordinate(3,7), Coordinate(3,8)))
      ParserProlog.makeLegitMove(Move(Coordinate(5,6), Coordinate(5,10)))
      ParserProlog.makeLegitMove(Move(Coordinate(4,11), Coordinate(4,10)))
      ParserProlog.makeLegitMove(Move(Coordinate(6,5), Coordinate(6,2)))
      ParserProlog.makeLegitMove(Move(Coordinate(7,1), Coordinate(7,2)))
      ParserProlog.makeLegitMove(Move(Coordinate(8,6), Coordinate(8,2)))
      ParserProlog.makeLegitMove(Move(Coordinate(8,11), Coordinate(8,3)))
      ParserProlog.makeLegitMove(Move(Coordinate(5,5), Coordinate(5,10)))
      ParserProlog.makeLegitMove(Move(Coordinate(5,2), Coordinate(5,9)))
      ParserProlog.makeLegitMove(Move(Coordinate(7,7), Coordinate(7,10)))
      ParserProlog.makeLegitMove(Move(Coordinate(8,3), Coordinate(8,10)))
      ParserProlog.makeLegitMove(Move(Coordinate(7,6), Coordinate(7,10)))
      ParserProlog.makeLegitMove(Move(Coordinate(5,9), Coordinate(7,9)))
      ParserProlog.makeLegitMove(Move(Coordinate(7,5), Coordinate(7,3)))
      ParserProlog.makeLegitMove(Move(Coordinate(7,9), Coordinate(7,4)))
      ParserProlog.makeLegitMove(Move(Coordinate(6,7), Coordinate(2,7)))
      ParserProlog.makeLegitMove(Move(Coordinate(11,7), Coordinate(3,7)))
      ParserProlog.makeLegitMove(Move(Coordinate(6,6), Coordinate(9,6)))
      ParserProlog.makeLegitMove(Move(Coordinate(8,10), Coordinate(8,11)))
      ParserProlog.makeLegitMove(Move(Coordinate(9,6), Coordinate(9,11)))
      ParserProlog.makeLegitMove(Move(Coordinate(10,6), Coordinate(10,11)))
      ParserProlog.makeLegitMove(Move(Coordinate(9,11), Coordinate(9,10)))
      ParserProlog.makeLegitMove(Move(Coordinate(11,8), Coordinate(9,8)))
      ParserProlog.makeLegitMove(Move(Coordinate(9,10), Coordinate(9,11)))
      ParserProlog.makeLegitMove(Move(Coordinate(9,8), Coordinate(9,9)))
      ParserProlog.makeLegitMove(Move(Coordinate(9,11), Coordinate(9,10)))
      ParserProlog.makeLegitMove(Move(Coordinate(11,6), Coordinate(10,6)))
      ParserProlog.makeLegitMove(Move(Coordinate(9,10), Coordinate(9,11)))
      ParserProlog.makeLegitMove(Move(Coordinate(10,6), Coordinate(10,10)))
      ParserProlog.makeLegitMove(Move(Coordinate(9,11), Coordinate(10,11)))
      ParserProlog.makeLegitMove(Move(Coordinate(9,9), Coordinate(9,10)))
      ParserProlog.makeLegitMove(Move(Coordinate(10,11), Coordinate(9,11)))
    }

    val currentGame: Option[PrologSnapshot] = ParserProlog.makeLegitMove(Move(Coordinate(10,10), Coordinate(10,11)))
    currentGame.get.winner shouldBe Player.Draw
  }

  test("Tests draw in 9x9 - Tablut") {
    inSequence {
      ParserProlog.createGame(GameVariant.Tablut.toString.toLowerCase())
      ParserProlog.makeLegitMove(Move(Coordinate(5,2), Coordinate(7,2)))
      ParserProlog.makeLegitMove(Move(Coordinate(5,3), Coordinate(1,3)))
      ParserProlog.makeLegitMove(Move(Coordinate(7,2), Coordinate(1,2)))
      ParserProlog.makeLegitMove(Move(Coordinate(5,4), Coordinate(2,4)))
      ParserProlog.makeLegitMove(Move(Coordinate(1,2), Coordinate(2,2)))
      ParserProlog.makeLegitMove(Move(Coordinate(5,7), Coordinate(1,7)))
      ParserProlog.makeLegitMove(Move(Coordinate(5,8), Coordinate(1,8)))
      ParserProlog.makeLegitMove(Move(Coordinate(3,5), Coordinate(3,6)))
      ParserProlog.makeLegitMove(Move(Coordinate(2,2), Coordinate(2,3)))
      ParserProlog.makeLegitMove(Move(Coordinate(3,6), Coordinate(3,9)))
      ParserProlog.makeLegitMove(Move(Coordinate(2,5), Coordinate(2,9)))
      ParserProlog.makeLegitMove(Move(Coordinate(5,6), Coordinate(5,8)))
      ParserProlog.makeLegitMove(Move(Coordinate(6,9), Coordinate(6,8)))
      ParserProlog.makeLegitMove(Move(Coordinate(4,5), Coordinate(4,2)))
      ParserProlog.makeLegitMove(Move(Coordinate(1,8), Coordinate(4,8)))
      ParserProlog.makeLegitMove(Move(Coordinate(5,5), Coordinate(2,5)))
      ParserProlog.makeLegitMove(Move(Coordinate(4,8), Coordinate(4,3)))
      ParserProlog.makeLegitMove(Move(Coordinate(6,5), Coordinate(6,7)))
      ParserProlog.makeLegitMove(Move(Coordinate(1,6), Coordinate(6,6)))
      ParserProlog.makeLegitMove(Move(Coordinate(7,5), Coordinate(6,5)))
      ParserProlog.makeLegitMove(Move(Coordinate(6,1), Coordinate(6,4)))
      ParserProlog.makeLegitMove(Move(Coordinate(2,5), Coordinate(2,6)))
      ParserProlog.makeLegitMove(Move(Coordinate(9,6), Coordinate(9,7)))
      ParserProlog.makeLegitMove(Move(Coordinate(2,6), Coordinate(3,6)))
      ParserProlog.makeLegitMove(Move(Coordinate(9,7), Coordinate(1,7)))
      ParserProlog.makeLegitMove(Move(Coordinate(3,6), Coordinate(1,6)))
    }
    val currentGame: Option[PrologSnapshot]= ParserProlog.makeLegitMove(Move(Coordinate(6,6), Coordinate(2,6)))

    currentGame.get.winner shouldBe Player.Draw
  }

}