import alice.tuprolog.{Prolog, SolveInfo, Theory}
import model.GameRules
import org.junit.runner.RunWith
import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class UtilsListTests extends FunSuite {
  val prolog: Prolog = new Prolog()
  val theory: Theory = new Theory(GameRules.theory())
  var goal: SolveInfo = _
  prolog.setTheory(theory)

  test("Adds element in the tail of a list.") {
    goal = prolog.solve("testAddLast.")
    assert(goal.isSuccess)
  }

  test("Returns the size of a list.") {
    goal = prolog.solve("testSize.")
    assert(goal.isSuccess)
  }

  test("Creates a sequence from 4 to 7.") {
    goal = prolog.solve("testSequence.")
    assert(goal.isSuccess)
  }

  test("Create a sequence from 7 to 4; result is empty.") {
    goal = prolog.solve("testSequenceBiggerM.")
    assert(goal.isSuccess)
  }

  test("Compare if two lists are equal.") {
    goal = prolog.solve("testEqualLists.")
    assert(goal.isSuccess)
  }

  test("Compare if two lists are different.") {
    goal = prolog.solve("testDifferentLists.")
    assert(!goal.isSuccess)
  }

  test("Takes the first three elements of a list.") {
    goal = prolog.solve("testTake.")
    assert(goal.isSuccess)
  }

  test("Takes n > list size elements of a list.") {
    goal = prolog.solve("testTakeBiggerN.")
    assert(goal.isSuccess)
  }

  test("Appends 4 lists: [1,2,3],[4,5,6],[7,8,9],[10,11,12].") {
    goal = prolog.solve("testAppend4.")
    assert(goal.isSuccess)
  }

  test("Appends 4 empty lists.") {
    goal = prolog.solve("testAppend4Empty.")
    assert(goal.isSuccess)
  }

  test("Appends 4 lists: [],[4,5,6],[],[10,11,12].") {
    goal = prolog.solve("testAppend4SomeEmpty.")
    assert(goal.isSuccess)
  }

  test("Returns the 4th element from reference list") {
    goal = prolog.solve("testIthElem.")
    assert(goal.isSuccess)
  }
  test("An element not present in a list cannot be returned.") {
    goal = prolog.solve("testOutIthElem.")
    assert(!goal.isSuccess)
  }

  test("Sets elem in 4-th position.") {
    goal = prolog.solve("testSetIthElem.")
    assert(goal.isSuccess)
  }

  test("Cannot set an element to a position greater than the reference list's size.") {
    goal = prolog.solve("testSetOutIthElem.")
    assert(!goal.isSuccess)
  }
}
