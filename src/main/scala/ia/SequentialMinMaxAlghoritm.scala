package ia
import actor_ia.MoveGenerator
import model._
import utils.Move

trait MiniMax {

  def findBestMove(gameSnapshot: GameSnapshot):Move

}

object MiniMaxImpl {
  def apply(depth: Int): MiniMaxImpl = new MiniMaxImpl(depth)
}


class MiniMaxImpl(depth: Int) extends  MiniMax {

  //var evaluationFunction: EvaluationFunction =  EvaluationFunctionImpl()

  override def findBestMove(gameSnapshot: GameSnapshot):Move = {

    def _findBestMove( bestCoord: Move , bestScore: Int ,gamePossibleMove: List[Move] ):Move = gamePossibleMove match {
      case Nil  => bestCoord
      case h::t =>
                val sonGameSnapshot = MoveGenerator.makeMove(gameSnapshot.getCopy,h)
                val moveScore = pruningAlfaBeta(sonGameSnapshot, depth, Int.MinValue,Int.MaxValue, MaxMin.min, h)
                val (newBestCoord, newBestScore) = if (bestScore > moveScore ) (bestCoord , bestScore) else (h, moveScore)
               _findBestMove( newBestCoord, newBestScore ,t )

    }
    _findBestMove( null, -100, MoveGenerator.gamePossibleMoves(gameSnapshot) )
  }


  def pruningAlfaBeta (sonGameSnapshot: GameSnapshot, depth: Int, alfa: Int, beta: Int, phase: MaxMin.Value, move: Move ): Int =  (depth, phase)  match {
    case (_,_)  if isTerminalNode(sonGameSnapshot) => EvaluationFunction.score(sonGameSnapshot)
    case (0,_)  => EvaluationFunction.score(sonGameSnapshot)
    case (_, MaxMin.Max) => maximizationPhase(sonGameSnapshot, depth, alfa, beta)
    case  _ => minimizationPhase(sonGameSnapshot, depth, alfa, beta)
  }

  def maximizationPhase(sonGameSnapshot: GameSnapshot, depth: Int, alfa: Int, beta: Int):Int = {
    val tempVal:Int = -100
    val gameMoves: List[Move] = MoveGenerator.gamePossibleMoves(sonGameSnapshot)


    def _maximizationPhase(gameMoves: List[Move], tempVal: Int, depth: Int, alfa: Int, beta: Int): Int = gameMoves match {
      case Nil => tempVal
      case _  if beta <= alfa => tempVal
      case h::t =>
            val sonGame: GameSnapshot = MoveGenerator.makeMove(sonGameSnapshot.getCopy, h)
            val newTempVal = math.max(tempVal, pruningAlfaBeta(sonGame, depth-1, alfa, beta, MaxMin.min, h))
            val newAlfa = math.max(alfa, newTempVal)
            //println("Max: " + depth)
            _maximizationPhase(t, newTempVal, depth, newAlfa, beta)

    }

    _maximizationPhase(gameMoves,tempVal,depth,alfa,beta)
  }

  def minimizationPhase(sonGameSnapshot: GameSnapshot, depth: Int, alfa: Int, beta: Int): Int = {
    val tempVal:Int = 100
    val gameMoves: List[Move] = MoveGenerator.gamePossibleMoves(sonGameSnapshot)


    def _minimizationPhase( gameMoves: List[Move], tempVal: Int, depth: Int, alfa: Int, beta: Int): Int = gameMoves match {
      case Nil => tempVal
      case _  if beta <= alfa => tempVal
      case h::t =>
            val sonGame: GameSnapshot = MoveGenerator.makeMove(sonGameSnapshot.getCopy, h)
            val newTempVal = math.min(tempVal, pruningAlfaBeta(sonGame, depth - 1, alfa, beta, MaxMin.Max, h))
            val newBeta = math.min(beta, newTempVal)
            _minimizationPhase( t, newTempVal, depth, alfa, newBeta)

    }

    _minimizationPhase( gameMoves,tempVal,depth,alfa,beta)
  }

  def isTerminalNode(gameSnapshot: GameSnapshot):Boolean = !gameSnapshot.getWinner.equals(Player.None)


}
object TryMinMax extends App{
  val THEORY: String = TheoryGame.GameRules.toString
  val game: ParserProlog = ParserPrologImpl(THEORY)
  val initGame = game.createGame(GameVariant.Hnefatafl.nameVariant.toLowerCase)
  val gameSnapshot = GameSnapshot(GameVariant.Hnefatafl, initGame._1, initGame._2, initGame._3, Option.empty, 0, 0)


  val miniMax: MiniMax = new MiniMaxImpl(3)

  println( miniMax.findBestMove(gameSnapshot))


}