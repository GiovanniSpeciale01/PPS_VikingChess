import alice.tuprolog.{Prolog, SolveInfo, Theory}
import model.GameRules
import org.junit.runner.RunWith
import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class MakingMovesTests extends FunSuite {
  val prolog: Prolog = new Prolog()
  val theory: Theory = new Theory(GameRules.theory())
  var goal: SolveInfo = _
  prolog.setTheory(theory)

  test("Tests if a horizontal pawn capture correctly occurs.") {
    goal = prolog.solve("testHorizCapture.")
    assert(goal.isSuccess)
  }

  test("Tests if a vertical pawn capture correctly occurs.") {
    goal = prolog.solve("testVertCapture.")
    assert(goal.isSuccess)
  }

  test("Tests if a double pawn capture correctly occurs.") {
    goal = prolog.solve("testDoubleCapture.")
    assert(goal.isSuccess)
  }

  test("Tests if a triple pawn capture correctly occurs.") {
    goal = prolog.solve("testTripleCapture.")
    assert(goal.isSuccess)
  }

  test("Tests if white wins moving king to (1,1) corner.") {
    goal = prolog.solve("testWhiteWin.")
    assert(goal.isSuccess)
  }

  test("Tests if white wins moving king to (1,11) corner.") {
    goal = prolog.solve("testWhiteWin1.")
    assert(goal.isSuccess)
  }

  test("Tests if in 11x11 board black wins capturing the king on three sides plus hostile throne.") {
    goal = prolog.solve("testBoard11KingCaptured3SidesAndThrone.")
    assert(goal.isSuccess)
  }

  test("Tests if in 11x11 board king on throne isn't captured on three sides.") {
    goal = prolog.solve("testBoard11KingNotCaptured3Sides.")
    assert(goal.isSuccess)
  }

  test("Tests if in 11x11 board king isn't captured on three sides next on the edge.") {
    goal = prolog.solve("testBoard11KingNotCapturedOnEdge.")
    assert(goal.isSuccess)
  }

  test("Tests if in 11x11 board black wins capturing the king on throne on four sides.") {
    goal = prolog.solve("testBoard11KingOnThroneCapture.")
    assert(goal.isSuccess)
  }

  test("Tests if in 11x11 black wins capturing the king far from throne on four sides.") {
    goal = prolog.solve("testBoard11KingFarFromThroneCapture.")
    assert(goal.isSuccess)
  }

  test("Tests if in 9x9 board black wins capturing the king on throne on four sides.") {
    goal = prolog.solve("testBoard9KingOnThroneCapture4Sides.")
    assert(goal.isSuccess)
  }

  test("Tests if in 7x7 board king on throne isn't captured on three sides.") {
    goal = prolog.solve("testBoard7KingOnThroneNoCapture3Sides.")
    assert(goal.isSuccess)
  }

  test("Tests if in 9x9 board black wins capturing the king far from throne vertically.") {
    goal = prolog.solve("testBoard9KingFarFromThroneVertCapture.")
    assert(goal.isSuccess)
  }

  test("Tests if in 9x9 board black wins capturing the king far from throne horizontally.") {
    goal = prolog.solve("testBoard9KingFarFromThroneHorizCapture.")
    assert(goal.isSuccess)
  }

  test("Tests if in 9x9 board king can sneak between enemy pawns without being captured.") {
    goal = prolog.solve("testBoard9SneakyKingNotCaptured.")
    assert(goal.isSuccess)
  }

  test("Tests if in 11x11 board king can sneak between enemy pawns without  being captured.") {
    goal = prolog.solve("testBoard11SneakyKingNotCaptured.")
    assert(goal.isSuccess)
  }

  test("Tests if in 7x7 board black wins capturing the king on three sides plus hostile throne.") {
    goal = prolog.solve("testBoard7KingNextToThroneCaptured3Sides.")
    assert(goal.isSuccess)
  }

  test("Tests if in 7x7 board black doesn't win capturing the king on two sides plus hostile throne.") {
    goal = prolog.solve("testBoard7KingNextToThroneNotCaptured2Sides.")
    assert(goal.isSuccess)
  }

  test("Tests a draw scenario.") {
    goal = prolog.solve("testBoard7Draw.")
    assert(goal.isSuccess)
  }

  test("Tests if a teleport move in the wrong turn is not legal.") {
    goal = prolog.solve("testIllegalMoveWrongTurnTeleport.")
    assert(!goal.isSuccess)
  }

  test("Tests if a move from empty cell is not legal.") {
    goal = prolog.solve("testIllegalMoveFromEmptyCell.")
    assert(!goal.isSuccess)
  }

  test("Tests if a pawn move to a special cell is not legal.") {
    goal = prolog.solve("testIllegalMoveToSpecialCell.")
    assert(!goal.isSuccess)
  }

  test("Tests if a move on occupied cell is not legal.") {
    goal = prolog.solve("testIllegalMoveOnOccupiedCell.")
    assert(!goal.isSuccess)
  }

  test("Tests if a non orthogonal move is not legal.") {
    goal = prolog.solve("testIllegalMoveNotOrthogonal.")
    assert(!goal.isSuccess)
  }

  test("Tests if an orthogonal move during the right turn is legal.") {
    goal = prolog.solve("testLegalMove.")
    assert(goal.isSuccess)
  }

  test("Tests if an orthogonal move in the wrong turn is not legal.") {
    goal = prolog.solve("testIllegalMoveWrongTurnOrthogonal.")
    assert(!goal.isSuccess)
  }

  test("Tests if moving to the same coordinates is not legal.") {
    goal = prolog.solve("testIllegalMoveOnSameCoord.")
    assert(!goal.isSuccess)
  }
}
