import model.{GameRules, GameVariant, ParserProlog, ParserPrologImpl, Player, TheoryGame}
import org.scalamock.scalatest.MockFactory
import org.scalatest.{FunSuite, Matchers}
import utils.Pair.PairImpl

import scala.collection.mutable.ListBuffer

/**
  *   @author Luca Nannini
  *   @author Giovanni Maria Speciale
  */

class ParserTests extends FunSuite with MockFactory with Matchers {

  val parser: ParserProlog = ParserPrologImpl(GameRules.theory())

  test("Tests of a correct horizontal capture of white pawn.") {
    inSequence {
      parser.createGame(GameVariant.Hnefatafl.toString().toLowerCase())
      parser.makeMove(PairImpl(1,4), PairImpl(4,4))
      parser.makeMove(PairImpl(4,6), PairImpl(4,5))
      parser.makeMove(PairImpl(2,6), PairImpl(4,6))
    }
    parser.showPossibleCells(PairImpl(4,5)) shouldBe ListBuffer.empty
  }

  test("Tests of a correct vertical capture of black pawn.") {
    inSequence {
      parser.createGame(GameVariant.Hnefatafl.toString().toLowerCase())
      parser.makeMove(PairImpl(7,1), PairImpl(7,4))
      parser.makeMove(PairImpl(8,6), PairImpl(8,4))
    }
    parser.showPossibleCells(PairImpl(7,4)) shouldBe ListBuffer.empty
  }

  test("Tests of a uncorrect capture of black pawn.") {
    inSequence {
      parser.createGame(GameVariant.Hnefatafl.toString().toLowerCase())
      parser.makeMove(PairImpl(2,6), PairImpl(3,6))
      parser.makeMove(PairImpl(6,4), PairImpl(4,4))
    }
    val currentGame: (_, _, _, Int) = parser.makeMove(PairImpl(1,5), PairImpl(4,5))
    currentGame._4 shouldBe 0
  }

  test("Tests king captured in Brandubh.") {
    inSequence {
      parser.createGame(GameVariant.Brandubh.toString().toLowerCase())
      parser.makeMove(PairImpl(1,4), PairImpl(1,5))
      parser.makeMove(PairImpl(4,5), PairImpl(7,5))
      parser.makeMove(PairImpl(6,4), PairImpl(6,5))
      parser.makeMove(PairImpl(4,4), PairImpl(4,5))
      parser.makeMove(PairImpl(1,5), PairImpl(3,5))
      parser.makeMove(PairImpl(4,3), PairImpl(1,3))
    }
    val currentGame: (_, Player.Value, _, _) = parser.makeMove(PairImpl(6,5), PairImpl(5,5))
    currentGame._2 shouldBe Player.Black
  }

  test("Tests white wins in Brandubh.") {
    inSequence {
    parser.createGame(GameVariant.Brandubh.toString().toLowerCase())
      parser.makeMove(PairImpl(4,6), PairImpl(2,6))
      parser.makeMove(PairImpl(4,5), PairImpl(1,5))
      parser.makeMove(PairImpl(4,7), PairImpl(2,7))
      parser.makeMove(PairImpl(4,4), PairImpl(4,7))
      parser.makeMove(PairImpl(6,4), PairImpl(6,6))
    }
    val currentGame: (_, Player.Value, _, _) = parser.makeMove(PairImpl(4,7), PairImpl(7,7))
    currentGame._2 shouldBe Player.White
  }

  test("Tests king captured on throne in Brandubh.") {
    inSequence {
      parser.createGame(GameVariant.Brandubh.toString().toLowerCase())
      parser.makeMove(PairImpl(1,4), PairImpl(1,6))
      parser.makeMove(PairImpl(3,4), PairImpl(3,7))
      parser.makeMove(PairImpl(2,4), PairImpl(3,4))
      parser.makeMove(PairImpl(4,3), PairImpl(7,3))
      parser.makeMove(PairImpl(4,2), PairImpl(4,3))
      parser.makeMove(PairImpl(5,4), PairImpl(5,1))
      parser.makeMove(PairImpl(6,4), PairImpl(5,4))
      parser.makeMove(PairImpl(4,5), PairImpl(2,5))
    }
    val currentGame: (_, Player.Value, _, _) = parser.makeMove(PairImpl(4,6), PairImpl(4,5))
    currentGame._2 shouldBe Player.Black
  }

  test("Tests horizontal king captured in Tablut.") {
    inSequence {
      parser.createGame(GameVariant.Tablut.toString().toLowerCase())
      parser.makeMove(PairImpl(1,6), PairImpl(2,6))
      parser.makeMove(PairImpl(5,7), PairImpl(9,7))
      parser.makeMove(PairImpl(2,6), PairImpl(1,6))
      parser.makeMove(PairImpl(5,6), PairImpl(8,6))
      parser.makeMove(PairImpl(1,6), PairImpl(2,6))
      parser.makeMove(PairImpl(5,5), PairImpl(5,7))
    }
    val currentGame: (_, Player.Value, _, _) = parser.makeMove(PairImpl(2,6), PairImpl(5,6))
    currentGame._2 shouldBe Player.Black
  }

  test("Tests vertical king captured in Tablut.") {
    inSequence {
      parser.createGame(GameVariant.Tablut.toString().toLowerCase())
      parser.makeMove(PairImpl(4,1), PairImpl(4,3))
      parser.makeMove(PairImpl(5,3), PairImpl(9,3))
      parser.makeMove(PairImpl(8,5), PairImpl(8,3))
      parser.makeMove(PairImpl(5,4), PairImpl(8,4))
      parser.makeMove(PairImpl(1,4), PairImpl(1,2))
      parser.makeMove(PairImpl(5,5), PairImpl(5,3))
    }
    val currentGame: (_, Player.Value, _, _) = parser.makeMove(PairImpl(8,3), PairImpl(6,3))
    currentGame._2 shouldBe Player.Black
  }

  test("Tests king captured far from throne in Tawlbwrdd.") {
    inSequence {
      parser.createGame(GameVariant.Tawlbwrdd.toString().toLowerCase())
      parser.makeMove(PairImpl(6,9), PairImpl(1,9))
      parser.makeMove(PairImpl(6,8), PairImpl(1,8))
      parser.makeMove(PairImpl(1,5), PairImpl(1,4))
      parser.makeMove(PairImpl(5,7), PairImpl(3,7))
      parser.makeMove(PairImpl(1,4), PairImpl(1,5))
      parser.makeMove(PairImpl(6,7), PairImpl(4,7))
      parser.makeMove(PairImpl(1,5), PairImpl(1,4))
      parser.makeMove(PairImpl(6,6), PairImpl(6,10))
    }
    val currentGame: (_, Player.Value, _, _) = parser.makeMove(PairImpl(1,9), PairImpl(6,9))
    currentGame._2 shouldBe Player.Black
  }

  test("Tests draw in Brandubh.") {
    inSequence {
      parser.createGame(GameVariant.Brandubh.toString().toLowerCase())
      parser.makeMove(PairImpl(4, 6), PairImpl(1, 6))
      parser.makeMove(PairImpl(3, 4), PairImpl(3, 7))
      parser.makeMove(PairImpl(2, 4), PairImpl(2, 7))
      parser.makeMove(PairImpl(5, 4), PairImpl(5, 7))
      parser.makeMove(PairImpl(6, 4), PairImpl(6, 7))
      parser.makeMove(PairImpl(4, 3), PairImpl(1, 3))
      parser.makeMove(PairImpl(4, 2), PairImpl(1, 2))
      parser.makeMove(PairImpl(4, 5), PairImpl(7, 5))
      parser.makeMove(PairImpl(1, 6), PairImpl(7, 6))
      parser.makeMove(PairImpl(4, 4), PairImpl(4, 6))
      parser.makeMove(PairImpl(4, 7), PairImpl(3, 7))
      parser.makeMove(PairImpl(4, 6), PairImpl(4, 7))
      parser.makeMove(PairImpl(7, 6), PairImpl(5, 6))
      parser.makeMove(PairImpl(4, 7), PairImpl(4, 6))
      parser.makeMove(PairImpl(6, 7), PairImpl(5, 7))
      parser.makeMove(PairImpl(4, 6), PairImpl(4, 7))
    }
    val currentGame: (_, Player.Value, _, _) = parser.makeMove(PairImpl(5,6), PairImpl(4,6))
    currentGame._2 shouldBe Player.Draw


  }

  test("Tests 1 deep clone."){
    var copyParser: ParserProlog = null

    parser.createGame(GameVariant.Brandubh.toString().toLowerCase())
    parser.makeMove(PairImpl(1,4), PairImpl(1,5))
    parser.makeMove(PairImpl(4,5), PairImpl(7,5))
    parser.makeMove(PairImpl(6,4), PairImpl(6,5))
    copyParser = parser.copy()
    parser.makeMove(PairImpl(4,4), PairImpl(4,5))
    parser.makeMove(PairImpl(1,5), PairImpl(3,5))
    parser.makeMove(PairImpl(4,3), PairImpl(1,3))


    assert(!parser.equals(copyParser))
  }

  test("Tests 2 deep clone."){
    var copyParser: ParserProlog = null

    parser.createGame(GameVariant.Brandubh.toString().toLowerCase())
    parser.makeMove(PairImpl(1,4), PairImpl(1,5))
    parser.makeMove(PairImpl(4,5), PairImpl(7,5))
    parser.makeMove(PairImpl(6,4), PairImpl(6,5))
    copyParser = parser.copy()

    assert(parser.equals(copyParser))
  }

  test("Tests 3 deep clone.") {
    var copyParser: ParserProlog = null
    inSequence {
      parser.createGame(GameVariant.Brandubh.toString().toLowerCase())
      copyParser=parser.copy
      parser.makeMove(PairImpl(1,4), PairImpl(1,3))
    }
    assert(parser.showPossibleCells(PairImpl(1,4)).size==0)
    assert(copyParser.showPossibleCells(PairImpl(1,4)).size!=0)
  }

}
