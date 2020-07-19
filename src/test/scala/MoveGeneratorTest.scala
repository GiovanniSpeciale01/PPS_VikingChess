import actor_ia.MoveGenerator
import model.GameSnapshot.GameSnapshotImpl
import model.{GameVariant, ParserProlog, ParserPrologImpl, Piece, Player, TheoryGame}
import org.scalamock.scalatest.MockFactory
import org.scalatest.{FunSuite, Matchers}
import utils.{BoardGame, Coordinate, Move}

class MoveGeneratorTest  extends FunSuite with MockFactory with Matchers {

  val THEORY: String = TheoryGame.GameRules.toString
  val parserProlog: ParserProlog = ParserPrologImpl(THEORY)

  var variantBrandubh: GameVariant.Val = GameVariant.Brandubh
  var variantHnefatafl: GameVariant.Val = GameVariant.Hnefatafl
  var variantTablut:GameVariant.Val = GameVariant.Tablut
  var variantTawlbwrdd:GameVariant.Val = GameVariant.Tawlbwrdd

  var gameBrandubh: (Player.Val, Player.Val, BoardGame.Board, Int) = _
  var gameHnefatafl: (Player.Val, Player.Val, BoardGame.Board, Int) = _
  var gameTablut: (Player.Val, Player.Val, BoardGame.Board, Int) = _
  var gameTawlbwrdd: (Player.Val, Player.Val, BoardGame.Board, Int) = _

  var snapBrandubh: GameSnapshotImpl = _
  var snapHnefatafl: GameSnapshotImpl = _
  var snapTablut: GameSnapshotImpl = _
  var snapTawlbwrdd: GameSnapshotImpl = _

  test("Test number of possible moves - Brandubh."){
    gameBrandubh = parserProlog.createGame(variantBrandubh.toString().toLowerCase)
    snapBrandubh = GameSnapshotImpl(variantBrandubh, gameBrandubh._1, gameBrandubh._2, gameBrandubh._3, Option.empty, 0, 0)

    assert(MoveGenerator.gamePossibleMoves(snapBrandubh).size == 40)
  }

  test("Test  possible moves - Brandubh."){
    val possibleMove_Brandubh = List( Move(Coordinate(1,4),Coordinate(1,5)), Move(Coordinate(1,4),Coordinate(1,6)), Move(Coordinate(1,4),Coordinate(1,3)), Move(Coordinate(1,4),Coordinate(1,2)), Move(Coordinate(2,4),Coordinate(2,5)), Move(Coordinate(2,4),Coordinate(2,6)), Move(Coordinate(2,4),Coordinate(2,7)), Move(Coordinate(2,4),Coordinate(2,3)),  Move(Coordinate(2,4),Coordinate(2,2)), Move(Coordinate(2,4),Coordinate(2,1)), Move(Coordinate(4,1),Coordinate(3,1)), Move(Coordinate(4,1),Coordinate(2,1)), Move(Coordinate(4,1),Coordinate(5,1)), Move(Coordinate(4,1),Coordinate(6,1)), Move(Coordinate(4,2),Coordinate(3,2)), Move(Coordinate(4,2),Coordinate(2,2)), Move(Coordinate(4,2),Coordinate(1,2)), Move(Coordinate(4,2),Coordinate(5,2)), Move(Coordinate(4,2),Coordinate(6,2)), Move(Coordinate(4,2),Coordinate(7,2)), Move(Coordinate(4,6),Coordinate(3,6)), Move(Coordinate(4,6),Coordinate(2,6)), Move(Coordinate(4,6),Coordinate(1,6)), Move(Coordinate(4,6),Coordinate(5,6)), Move(Coordinate(4,6),Coordinate(6,6)), Move(Coordinate(4,6),Coordinate(7,6)), Move(Coordinate(4,7),Coordinate(3,7)), Move (Coordinate(4,7),Coordinate(2,7)), Move(Coordinate(4,7),Coordinate(5,7)), Move (Coordinate(4,7),Coordinate(6,7)), Move(Coordinate(6,4),Coordinate(6,5)), Move(Coordinate(6,4),Coordinate(6,6)), Move(Coordinate(6,4),Coordinate(6,7)), Move(Coordinate(6,4),Coordinate(6,3)), Move(Coordinate(6,4),Coordinate(6,2)), Move(Coordinate(6,4),Coordinate(6,1)), Move(Coordinate(7,4),Coordinate(7,5)), Move(Coordinate(7,4),Coordinate(7,6)), Move(Coordinate(7,4),Coordinate(7,3)), Move(Coordinate(7,4),Coordinate(7,2)))

    gameBrandubh = parserProlog.createGame(variantBrandubh.toString().toLowerCase)
    snapBrandubh = GameSnapshotImpl(variantBrandubh, gameBrandubh._1, gameBrandubh._2, gameBrandubh._3, Option.empty, 0, 0)

    MoveGenerator.gamePossibleMoves(snapBrandubh).foreach( move => assert(possibleMove_Brandubh.contains(move)))
  }


  test("Test number of possible moves - Hnefatafl."){
    gameHnefatafl = parserProlog.createGame(variantHnefatafl.toString().toLowerCase)
    snapHnefatafl = GameSnapshotImpl(variantHnefatafl, gameHnefatafl._1, gameHnefatafl._2, gameHnefatafl._3, Option.empty, 0, 0)

    assert(MoveGenerator.gamePossibleMoves(snapHnefatafl).size == 116)
  }

  test("Make moves from (1,4) to (4,4) - Hnefatafl"){
    gameHnefatafl = parserProlog.createGame(variantHnefatafl.toString().toLowerCase)
    snapHnefatafl = GameSnapshotImpl(variantHnefatafl, gameHnefatafl._1, gameHnefatafl._2, gameHnefatafl._3, Option.empty, 0, 0)

    val step1 = MoveGenerator.makeMove(snapHnefatafl,Move(Coordinate(1,4), Coordinate(4,4)))
    assert(step1.getBoard.getCell(Coordinate(4,4)).getPiece.equals( Piece.BlackPawn))
  }

  test( "Vertical captured white test - Tablut "){
    gameTablut = parserProlog.createGame(variantTablut.toString().toLowerCase)
    snapTablut = GameSnapshotImpl(variantTablut, gameTablut._1, gameTablut._2, gameTablut._3, Option.empty, 0, 0)

    val step1 = MoveGenerator.makeMove(snapTablut, Move(Coordinate(9,6), Coordinate(6,6)))
    val step2 = MoveGenerator.makeMove(step1, Move(Coordinate(5,3), Coordinate(6,3)))
    val step3 = MoveGenerator.makeMove(step2, Move(Coordinate(1,6), Coordinate(4,6)))

    assert(step3.getBoard.getCell(Coordinate(5,6)).getPiece.equals(Piece.Empty))
    assert(step3.getNumberCapturedWhites == 1)
  }

  test( "Vertical captured black test - Tablut"){
    gameTablut = parserProlog.createGame(variantTablut.toString().toLowerCase)
    snapTablut = GameSnapshotImpl(variantTablut, gameTablut._1, gameTablut._2, gameTablut._3, Option.empty, 0, 0)

    val step1 = MoveGenerator.makeMove(snapTablut, Move(Coordinate(1,6), Coordinate(4,6)))
    val step2 = MoveGenerator.makeMove(step1, Move(Coordinate(3,5), Coordinate(3,6)))


    assert(step2.getBoard.getCell(Coordinate(4,6)).getPiece.equals(Piece.Empty))
    assert(step2.getNumberCapturedBlacks == 1)
  }

  test( "Double captured white test - Tablut"){
    gameTablut = parserProlog.createGame(variantTablut.toString().toLowerCase)
    snapTablut = GameSnapshotImpl(variantTablut, gameTablut._1, gameTablut._2, gameTablut._3, Option.empty, 0, 0)

    val step1 = MoveGenerator.makeMove(snapTablut, Move(Coordinate(9,4), Coordinate(6,4)))
    val step2 = MoveGenerator.makeMove(step1, Move(Coordinate(7,5), Coordinate(7,6)))
    val step3 = MoveGenerator.makeMove(step2, Move(Coordinate(4,9), Coordinate(4,6)))
    val step4 = MoveGenerator.makeMove(step3, Move(Coordinate(5,7), Coordinate(2,7)))
    val step5 = MoveGenerator.makeMove(step4, Move(Coordinate(8,5), Coordinate(8,6)))
    val step6 = MoveGenerator.makeMove(step5, Move(Coordinate(6,5), Coordinate(7,5)))
    val step7 = MoveGenerator.makeMove(step6, Move(Coordinate(6,9), Coordinate(6,6)))

    assert(step7.getBoard.getCell(Coordinate(5,6)).getPiece.equals(Piece.Empty) &&
      step7.getBoard.getCell(Coordinate(7,6)).getPiece.equals(Piece.Empty) &&
      step7.getNumberCapturedWhites == 2)
  }


  test( "Triple captured white test - Tablut"){
    gameTablut = parserProlog.createGame(variantTablut.toString().toLowerCase)
    snapTablut = GameSnapshotImpl(variantTablut, gameTablut._1, gameTablut._2, gameTablut._3, Option.empty, 0, 0)

    val step1 = MoveGenerator.makeMove(snapTablut, Move(Coordinate(9,4), Coordinate(6,4)))
    val step2 = MoveGenerator.makeMove(step1, Move(Coordinate(7,5), Coordinate(7,6)))
    val step3 = MoveGenerator.makeMove(step2, Move(Coordinate(4,9), Coordinate(4,6)))
    val step4 = MoveGenerator.makeMove(step3, Move(Coordinate(5,7), Coordinate(5,2)))
    val step5 = MoveGenerator.makeMove(step4, Move(Coordinate(8,5), Coordinate(8,6)))
    val step6 = MoveGenerator.makeMove(step5, Move(Coordinate(5,3), Coordinate(4,3)))
    val step7 = MoveGenerator.makeMove(step6, Move(Coordinate(6,9), Coordinate(6,6)))

    assert(step7.getBoard.getCell(Coordinate(5,6)).getPiece.equals(Piece.Empty) &&
      step7.getBoard.getCell(Coordinate(7,6)).getPiece.equals(Piece.Empty) &&
      step7.getBoard.getCell(Coordinate(6,5)).getPiece.equals(Piece.Empty))
    assert(step7.getNumberCapturedWhites == 3)
  }

  test( "Triple captured white test - Tawlbwrdd"){
    gameTawlbwrdd = parserProlog.createGame(variantTawlbwrdd.toString().toLowerCase)
    snapTawlbwrdd = GameSnapshotImpl(variantTawlbwrdd, gameTawlbwrdd._1, gameTawlbwrdd._2, gameTawlbwrdd._3, Option.empty, 0, 0)

    val step1 = MoveGenerator.makeMove(snapTawlbwrdd, Move(Coordinate(6,3), Coordinate(4,3)))
    val step2 = MoveGenerator.makeMove(step1, Move(Coordinate(6,4), Coordinate(4,4)))
    val step3 = MoveGenerator.makeMove(step2, Move(Coordinate(2,7), Coordinate(4,7)))
    val step4 = MoveGenerator.makeMove(step3, Move(Coordinate(6,5), Coordinate(6,3)))
    val step5 = MoveGenerator.makeMove(step4, Move(Coordinate(7,2), Coordinate(6,2)))
    val step6 = MoveGenerator.makeMove(step5, Move(Coordinate(6,3), Coordinate(10,3)))
    val step7 = MoveGenerator.makeMove(step6, Move(Coordinate(6,2), Coordinate(6,5)))
    val step8 = MoveGenerator.makeMove(step7, Move(Coordinate(6,8), Coordinate(5,8)))
    val step9 = MoveGenerator.makeMove(step8, Move(Coordinate(2,5), Coordinate(4,5)))


    assert(step9.getBoard.getCell(Coordinate(4,4)).getPiece.equals(Piece.Empty) &&
      step9.getBoard.getCell(Coordinate(4,6)).getPiece.equals(Piece.Empty) &&
      step9.getBoard.getCell(Coordinate(5,5)).getPiece.equals(Piece.Empty))
    assert(step9.getNumberCapturedWhites == 3)
  }

  test("1. Test white win in (1,1)"){
    gameHnefatafl = parserProlog.createGame(variantHnefatafl.toString().toLowerCase)
    snapHnefatafl = GameSnapshotImpl(variantHnefatafl, gameHnefatafl._1, gameHnefatafl._2, gameHnefatafl._3, Option.empty, 0, 0)

    val step1 = MoveGenerator.makeMove(snapHnefatafl, Move(Coordinate(1,4), Coordinate(1,3)))
    val step2 = MoveGenerator.makeMove(step1, Move(Coordinate(6,6), Coordinate(1,1)))

    assert(step2.getWinner.equals(Player.White))
  }

  test("2. Test white win in (1,11)"){
    gameHnefatafl = parserProlog.createGame(variantHnefatafl.toString().toLowerCase)
    snapHnefatafl = GameSnapshotImpl(variantHnefatafl, gameHnefatafl._1, gameHnefatafl._2, gameHnefatafl._3, Option.empty, 0, 0)

    val step1 = MoveGenerator.makeMove(snapHnefatafl, Move(Coordinate(1,4), Coordinate(1,3)))
    val step2 = MoveGenerator.makeMove(step1, Move(Coordinate(6,6), Coordinate(1,11)))

    assert(step2.getWinner.equals(Player.White))
  }

  test("3. Test white win in (11,11)"){
    gameHnefatafl = parserProlog.createGame(variantHnefatafl.toString().toLowerCase)
    snapHnefatafl = GameSnapshotImpl(variantHnefatafl, gameHnefatafl._1, gameHnefatafl._2, gameHnefatafl._3, Option.empty, 0, 0)

    val step1 = MoveGenerator.makeMove(snapHnefatafl, Move(Coordinate(1,4), Coordinate(1,3)))
    val step2 = MoveGenerator.makeMove(step1, Move(Coordinate(6,6), Coordinate(11,11)))

    println(step2.getWinner)
    assert(step2.getWinner.equals(Player.White))
  }

  test("4. Test white win in (11,1)"){
    gameHnefatafl = parserProlog.createGame(variantHnefatafl.toString().toLowerCase)
    snapHnefatafl = GameSnapshotImpl(variantHnefatafl, gameHnefatafl._1, gameHnefatafl._2, gameHnefatafl._3, Option.empty, 0, 0)

    val step1 = MoveGenerator.makeMove(snapHnefatafl, Move(Coordinate(1,4), Coordinate(1,3)))
    val step2 = MoveGenerator.makeMove(step1, Move(Coordinate(6,6), Coordinate(11,1)))

    assert(step2.getWinner.equals(Player.White))
  }


  test("Test board king captured 3 sides and throne"){
    gameHnefatafl = parserProlog.createGame(variantHnefatafl.toString().toLowerCase)
    snapHnefatafl = GameSnapshotImpl(variantHnefatafl, gameHnefatafl._1, gameHnefatafl._2, gameHnefatafl._3, Option.empty, 0, 0)

    val step1 = MoveGenerator.makeMove(snapHnefatafl, Move(Coordinate(4,1), Coordinate(4,2)))
    val step2 = MoveGenerator.makeMove(step1, Move(Coordinate(5,5), Coordinate(2,5)))
    val step3 = MoveGenerator.makeMove(step2, Move(Coordinate(6,2), Coordinate(6,3)))
    val step4 = MoveGenerator.makeMove(step3, Move(Coordinate(6,4), Coordinate(10,4)))
    val step5 = MoveGenerator.makeMove(step4, Move(Coordinate(8,1), Coordinate(8,4)))
    val step6 = MoveGenerator.makeMove(step5, Move(Coordinate(7,5), Coordinate(10,5)))
    val step7 = MoveGenerator.makeMove(step6, Move(Coordinate(6,3), Coordinate(6,4)))
    val step8 = MoveGenerator.makeMove(step7, Move(Coordinate(6,5), Coordinate(3,5)))
    val step9 = MoveGenerator.makeMove(step8, Move(Coordinate(8,4), Coordinate(7,4)))
    val step10 = MoveGenerator.makeMove(step9, Move(Coordinate(6,6), Coordinate(6,5)))
    val step11 = MoveGenerator.makeMove(step10, Move(Coordinate(7,4), Coordinate(7,5)))
    val step12 = MoveGenerator.makeMove(step11, Move(Coordinate(2,5), Coordinate(2,4)))
    val step13 = MoveGenerator.makeMove(step12, Move(Coordinate(5,1), Coordinate(5,5)))

    assert(step13.getWinner.equals(Player.Black))
  }

  test("Test board king far from throne, not been captured 3 sides"){
    gameTawlbwrdd = parserProlog.createGame(variantTawlbwrdd.toString().toLowerCase)
    snapTawlbwrdd = GameSnapshotImpl(variantTawlbwrdd, gameTawlbwrdd._1, gameTawlbwrdd._2, gameTawlbwrdd._3, Option.empty, 0, 0)

    val step1 = MoveGenerator.makeMove(snapTawlbwrdd, Move(Coordinate(6,3), Coordinate(3,3)))
    val step2 = MoveGenerator.makeMove(step1, Move(Coordinate(6,4), Coordinate(2,4)))
    val step3 = MoveGenerator.makeMove(step2, Move(Coordinate(3,6), Coordinate(3,4)))
    val step4 = MoveGenerator.makeMove(step3, Move(Coordinate(6,5), Coordinate(6,4)))
    val step5 = MoveGenerator.makeMove(step4, Move(Coordinate(5,2), Coordinate(4,2)))
    val step6 = MoveGenerator.makeMove(step5, Move(Coordinate(6,4), Coordinate(4,4)))
    val step7 = MoveGenerator.makeMove(step6, Move(Coordinate(3,3), Coordinate(4,3)))
    val step8 = MoveGenerator.makeMove(step7, Move(Coordinate(6,6), Coordinate(6,2)))
    val step9 = MoveGenerator.makeMove(step8, Move(Coordinate(4,2), Coordinate(5,2)))

    assert(step9.getWinner.equals(Player.None))
  }


  test("Test board 11 king not captured on edge"){
    gameHnefatafl = parserProlog.createGame(variantHnefatafl.toString().toLowerCase)
    snapHnefatafl = GameSnapshotImpl(variantHnefatafl, gameHnefatafl._1, gameHnefatafl._2, gameHnefatafl._3, Option.empty, 0, 0)

    val step1 = MoveGenerator.makeMove(snapHnefatafl, Move(Coordinate(4,1), Coordinate(2,1)))
    val step2 = MoveGenerator.makeMove(step1, Move(Coordinate(5,5), Coordinate(2,5)))
    val step3 = MoveGenerator.makeMove(step2, Move(Coordinate(5,1), Coordinate(3,1)))
    val step4 = MoveGenerator.makeMove(step3, Move(Coordinate(6,4), Coordinate(2,4)))
    val step5 = MoveGenerator.makeMove(step4, Move(Coordinate(6,2), Coordinate(2,2)))
    val step6 = MoveGenerator.makeMove(step5, Move(Coordinate(6,5), Coordinate(3,5)))
    val step7 = MoveGenerator.makeMove(step6, Move(Coordinate(6,1), Coordinate(5,1)))
    val step8 = MoveGenerator.makeMove(step7, Move(Coordinate(6,6), Coordinate(6,1)))
    val step9 = MoveGenerator.makeMove(step8, Move(Coordinate(3,2), Coordinate(6,2)))

    assert(step9.getWinner.equals(Player.None))
  }


  test("Test board 11 king on throne capture "){
    gameHnefatafl = parserProlog.createGame(variantHnefatafl.toString().toLowerCase)
    snapHnefatafl = GameSnapshotImpl(variantHnefatafl, gameHnefatafl._1, gameHnefatafl._2, gameHnefatafl._3, Option.empty, 0, 0)

    val step1 = MoveGenerator.makeMove(snapHnefatafl, Move(Coordinate(1,4), Coordinate(1,3)))
    val step2 = MoveGenerator.makeMove(step1, Move(Coordinate(4,6), Coordinate(4,8)))
    val step3 = MoveGenerator.makeMove(step2, Move(Coordinate(4,1), Coordinate(4,2)))
    val step4 = MoveGenerator.makeMove(step3, Move(Coordinate(5,5), Coordinate(2,5)))
    val step5 = MoveGenerator.makeMove(step4, Move(Coordinate(5,1), Coordinate(3,1)))
    val step6 = MoveGenerator.makeMove(step5, Move(Coordinate(6,4), Coordinate(1,4)))
    val step7 = MoveGenerator.makeMove(step6, Move(Coordinate(2,6), Coordinate(3,6)))
    val step8 = MoveGenerator.makeMove(step7, Move(Coordinate(6,5), Coordinate(3,5)))
    val step9 = MoveGenerator.makeMove(step8, Move(Coordinate(6,2), Coordinate(6,5)))
    val step10 = MoveGenerator.makeMove(step9, Move(Coordinate(5,6), Coordinate(5,1)))
    val step11 = MoveGenerator.makeMove(step10, Move(Coordinate(3,6), Coordinate(5,6)))
    val step12 = MoveGenerator.makeMove(step11, Move(Coordinate(6,8), Coordinate(10,8)))
    val step13 = MoveGenerator.makeMove(step12, Move(Coordinate(6,10), Coordinate(6,9)))
    val step14 = MoveGenerator.makeMove(step13, Move(Coordinate(5,7), Coordinate(2,7)))
    val step15 = MoveGenerator.makeMove(step14, Move(Coordinate(10,6), Coordinate(9,6)))
    val step16 = MoveGenerator.makeMove(step15, Move(Coordinate(6,7), Coordinate(3,7)))
    val step17 = MoveGenerator.makeMove(step16, Move(Coordinate(6,9), Coordinate(6,7)))
    val step18 = MoveGenerator.makeMove(step17, Move(Coordinate(8,6), Coordinate(8,10)))
    val step19 = MoveGenerator.makeMove(step18, Move(Coordinate(8,11), Coordinate(9,11)))
    val step20 = MoveGenerator.makeMove(step19, Move(Coordinate(7,7), Coordinate(7,10)))
    val step21 = MoveGenerator.makeMove(step20, Move(Coordinate(8,1), Coordinate(9,1)))
    val step22 = MoveGenerator.makeMove(step21, Move(Coordinate(7,5), Coordinate(7,2)))
    val step23 = MoveGenerator.makeMove(step22, Move(Coordinate(11,4), Coordinate(10,4)))
    val step24 = MoveGenerator.makeMove(step23, Move(Coordinate(7,6), Coordinate(7,3)))
    val step25 = MoveGenerator.makeMove(step24, Move(Coordinate(9,6), Coordinate(7,6)))

    assert(step25.getWinner.equals(Player.Black) && step25.getBoard.getCell(Coordinate(6,6)).getPiece.equals(Piece.WhiteKing))

  }

  test("Test board 9 king on throne capture - Tablut"){
    gameTablut = parserProlog.createGame(variantTablut.toString().toLowerCase)
    snapTablut = GameSnapshotImpl(variantTablut, gameTablut._1, gameTablut._2, gameTablut._3, Option.empty, 0, 0)

    val step1 = MoveGenerator.makeMove(snapTablut, Move(Coordinate(1,4), Coordinate(3,4)))
    val step2 = MoveGenerator.makeMove(step1, Move(Coordinate(2,4), Coordinate(4,3)))
    val step3 = MoveGenerator.makeMove(step2, Move(Coordinate(4,1), Coordinate(3,9)))
    val step4 = MoveGenerator.makeMove(step3, Move(Coordinate(5,3), Coordinate(4,5)))
    val step5 = MoveGenerator.makeMove(step4, Move(Coordinate(6,9), Coordinate(7,9)))
    val step6 = MoveGenerator.makeMove(step5, Move(Coordinate(3,5), Coordinate(3,8)))
    val step7 = MoveGenerator.makeMove(step6, Move(Coordinate(5,9), Coordinate(4,9)))
    val step8 = MoveGenerator.makeMove(step7, Move(Coordinate(5,6), Coordinate(3,6)))
    val step9 = MoveGenerator.makeMove(step8, Move(Coordinate(9,6), Coordinate(9,7)))
    val step10 = MoveGenerator.makeMove(step9, Move(Coordinate(4,5), Coordinate(4,7)))
    val step11 = MoveGenerator.makeMove(step10, Move(Coordinate(2,5), Coordinate(4,5)))
    val step12 = MoveGenerator.makeMove(step11, Move(Coordinate(6,5), Coordinate(6,9)))
    assert(step12.getBoard.getCell(Coordinate(7,5)).getPiece.equals(Piece.WhitePawn))

    val step13 = MoveGenerator.makeMove(step12, Move(Coordinate(6,1), Coordinate(6,5)))
    assert(step13.getBoard.getCell(Coordinate(7,5)).getPiece.equals(Piece.Empty))

    assert(step13.getNumberCapturedWhites == 1)

    val step14 = MoveGenerator.makeMove(step13, Move(Coordinate(5,7), Coordinate(8,7)))
    val step15 = MoveGenerator.makeMove(step14, Move(Coordinate(5,8), Coordinate(5,6)))
    val step16 = MoveGenerator.makeMove(step15, Move(Coordinate(4,7), Coordinate(1,7)))
    val step17 = MoveGenerator.makeMove(step16, Move(Coordinate(5,1), Coordinate(5,4)))


    assert(step17.getWinner.equals(Player.Black) && step17.getBoard.getCell(Coordinate(5,5)).getPiece.equals(Piece.WhiteKing))

  }


  test("Test board 7 king on throne no captured three sides - Brandubh"){
    gameBrandubh = parserProlog.createGame(variantBrandubh.toString().toLowerCase)
    snapBrandubh = GameSnapshotImpl(variantBrandubh, gameBrandubh._1, gameBrandubh._2, gameBrandubh._3, Option.empty, 0, 0)

    val step1 = MoveGenerator.makeMove(snapBrandubh, Move(Coordinate(1,4), Coordinate(3,4)))
    val step2 = MoveGenerator.makeMove(step1, Move(Coordinate(5,4), Coordinate(5,7)))
    val step3 = MoveGenerator.makeMove(step2, Move(Coordinate(2,4), Coordinate(4,3)))
    val step4 = MoveGenerator.makeMove(step3, Move(Coordinate(4,5), Coordinate(1,5)))
    val step5 = MoveGenerator.makeMove(step4, Move(Coordinate(4,1), Coordinate(4,5)))

    assert(step5.getWinner.equals(Player.None))

  }


  test("Test board 9 king far from throne, vert capture - Tablut"){
    gameTablut = parserProlog.createGame(variantTablut.toString().toLowerCase)
    snapTablut = GameSnapshotImpl(variantTablut, gameTablut._1, gameTablut._2, gameTablut._3, Option.empty, 0, 0)

    val step1 = MoveGenerator.makeMove(snapTablut, Move(Coordinate(1,4), Coordinate(1,3)))
    val step2 = MoveGenerator.makeMove(step1, Move(Coordinate(5,5), Coordinate(2,3)))
    val step3 = MoveGenerator.makeMove(step2, Move(Coordinate(1,5), Coordinate(3,3)))

    assert(step3.getWinner.equals(Player.Black))

  }

  test("Test board 9 king far from throne, horizontal capture - Tablut"){
    gameTablut = parserProlog.createGame(variantTablut.toString().toLowerCase)
    snapTablut = GameSnapshotImpl(variantTablut, gameTablut._1, gameTablut._2, gameTablut._3, Option.empty, 0, 0)

    val step1 = MoveGenerator.makeMove(snapTablut, Move(Coordinate(6,9), Coordinate(8,9)))
    val step2 = MoveGenerator.makeMove(step1, Move(Coordinate(5,5), Coordinate(8,8)))
    val step3 = MoveGenerator.makeMove(step2, Move(Coordinate(8,5), Coordinate(8,7)))

    assert(step3.getWinner.equals(Player.Black))

  }


  test("Test board 9 sneaky king not captured - Tablut"){
    gameTablut = parserProlog.createGame(variantTablut.toString().toLowerCase)
    snapTablut = GameSnapshotImpl(variantTablut, gameTablut._1, gameTablut._2, gameTablut._3, Option.empty, 0, 0)

    val step1 = MoveGenerator.makeMove(snapTablut, Move(Coordinate(6,9), Coordinate(7,9)))
    val step2 = MoveGenerator.makeMove(step1, Move(Coordinate(5,5), Coordinate(6,9)))
    val step3 = MoveGenerator.makeMove(step2, Move(Coordinate(5,8), Coordinate(6,8)))

    assert(step3.getWinner.equals(Player.None))

  }

  test("Test board 11 sneaky king not captured - Tawlbwrdd"){
    gameTawlbwrdd = parserProlog.createGame(variantTawlbwrdd.toString().toLowerCase)
    snapTawlbwrdd = GameSnapshotImpl(variantTawlbwrdd, gameTawlbwrdd._1, gameTawlbwrdd._2, gameTawlbwrdd._3, Option.empty, 0, 0)

    val step1 = MoveGenerator.makeMove(snapTawlbwrdd, Move(Coordinate(6,9), Coordinate(7,9)))
    val step2 = MoveGenerator.makeMove(step1, Move(Coordinate(6,6), Coordinate(6,10)))
    val step3 = MoveGenerator.makeMove(step2, Move(Coordinate(7,9), Coordinate(8,9)))

    assert(step3.getWinner.equals(Player.None))

  }


  test("Test board 7 king next to throne captured three sides - Brandubh"){
    gameBrandubh = parserProlog.createGame(variantBrandubh.toString().toLowerCase)
    snapBrandubh = GameSnapshotImpl(variantBrandubh, gameBrandubh._1, gameBrandubh._2, gameBrandubh._3, Option.empty, 0, 0)

    val step1 = MoveGenerator.makeMove(snapBrandubh, Move(Coordinate(1,4), Coordinate(3,3)))
    val step2 = MoveGenerator.makeMove(step1, Move(Coordinate(4,4), Coordinate(4,3)))
    val step3 = MoveGenerator.makeMove(step2, Move(Coordinate(2,4), Coordinate(5,3)))

    assert(step3.getWinner.equals(Player.Black))

  }

  test("Test king not captured on 2 sides next to throne in Brandubh") {
    gameBrandubh = parserProlog.createGame(variantBrandubh.toString().toLowerCase)
    snapBrandubh = GameSnapshotImpl(variantBrandubh, gameBrandubh._1, gameBrandubh._2, gameBrandubh._3, Option.empty, 0, 0)

    val step1 = MoveGenerator.makeMove(snapBrandubh, Move(Coordinate(4,6), Coordinate(1,6)))
    val step2 = MoveGenerator.makeMove(step1, Move(Coordinate(4,5), Coordinate(7,5)))
    val step3 = MoveGenerator.makeMove(step2, Move(Coordinate(6,4), Coordinate(6,5)))
    val step4 = MoveGenerator.makeMove(step3, Move(Coordinate(4,4), Coordinate(4,5)))
    val step5 = MoveGenerator.makeMove(step4, Move(Coordinate(2,4), Coordinate(2,5)))
    val step6 = MoveGenerator.makeMove(step5, Move(Coordinate(7,5), Coordinate(7,6)))
    val step7 = MoveGenerator.makeMove(step6, Move(Coordinate(2,5), Coordinate(3,5)))
    val step8 = MoveGenerator.makeMove(step7, Move(Coordinate(7,6), Coordinate(7,5)))
    val step9 = MoveGenerator.makeMove(step8, Move(Coordinate(6,5), Coordinate(5,5)))

    assert(step9.getWinner.equals(Player.None))
  }

  test("Test board 7 Draw - Brandubh"){
    gameBrandubh = parserProlog.createGame(variantBrandubh.toString().toLowerCase)
    snapBrandubh = GameSnapshotImpl(variantBrandubh, gameBrandubh._1, gameBrandubh._2, gameBrandubh._3, Option.empty, 0, 0)

    val step1 = MoveGenerator.makeMove(snapBrandubh, Move(Coordinate(4,6), Coordinate(1,6)))
    val step2 = MoveGenerator.makeMove(step1, Move(Coordinate(3,4), Coordinate(3,7)))
    val step3 = MoveGenerator.makeMove(step2, Move(Coordinate(2,4), Coordinate(2,7)))
    val step4 = MoveGenerator.makeMove(step3, Move(Coordinate(5,4), Coordinate(5,7)))
    val step5 = MoveGenerator.makeMove(step4, Move(Coordinate(6,4), Coordinate(6,7)))
    val step6 = MoveGenerator.makeMove(step5, Move(Coordinate(4,3), Coordinate(1,3)))
    val step7 = MoveGenerator.makeMove(step6, Move(Coordinate(4,2), Coordinate(1,2)))
    val step8 = MoveGenerator.makeMove(step7, Move(Coordinate(4,5), Coordinate(7,5)))
    val step9 = MoveGenerator.makeMove(step8, Move(Coordinate(1,6), Coordinate(7,6)))
    val step10 = MoveGenerator.makeMove(step9, Move(Coordinate(4,4), Coordinate(4,6)))
    val step11 = MoveGenerator.makeMove(step10, Move(Coordinate(4,7), Coordinate(3,7)))
    val step12 = MoveGenerator.makeMove(step11, Move(Coordinate(4,6), Coordinate(4,7)))
    val step13 = MoveGenerator.makeMove(step12, Move(Coordinate(7,6), Coordinate(5,6)))
    val step14 = MoveGenerator.makeMove(step13, Move(Coordinate(4,7), Coordinate(4,6)))
    val step15 = MoveGenerator.makeMove(step14, Move(Coordinate(6,7), Coordinate(5,7)))
    val step16 = MoveGenerator.makeMove(step15, Move(Coordinate(4,6), Coordinate(4,7)))
    val step17 = MoveGenerator.makeMove(step16, Move(Coordinate(5,6), Coordinate(4,6)))

    assert(step17.getWinner.equals(Player.Draw))

  }

}
