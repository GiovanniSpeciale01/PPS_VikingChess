package utils

trait Pair[A] {

  /**
    * Indicate the first element of the pair.
    */
  def x: A

  /**
    * Indicate the second element of the pair.
    */
  def y: A

  /**
    * Gets the first element of the pair.
    */
  def getX: A

  /**
    * Gets the first element of the pair.
    */
  def getY: A
}

object Pair {

  def apply[A](x: A,y: A): Pair[A] = PairImpl(x,y)

  case class PairImpl[A](pairX: A, pairY: A) extends Pair[A] {

    override def x: A = {
      require(!pairX.equals(null))
      pairX
    }

    override def y: A = {
      require(!pairY.equals(null))
      pairY
    }

    override def getX: A = pairX

    override def getY: A = pairY
  }
}
