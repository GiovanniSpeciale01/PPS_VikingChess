package view.utils

import java.awt.GridBagConstraints

import view.factories.GameFactory

/**
 * Utility object handling GridBadConstraints
 */
object GridConstraints {

  private val limits: GridBagConstraints = GameFactory.createBagConstraints
  limits.fill = GridBagConstraints.NONE

  /**
   * Resets a constraints for x grid parameter.
   */
  def resetXConstraints(): Unit = limits.gridx = 0

  /**
   * Sets a constraints for x and y grid parameter.
   */
  def setXYConstraints(x: Int, y: Int): Unit = {limits.gridx = x; limits.gridy = y}

  /**
   * Increments a constraints for x grid parameter.
   */
  def incrementXConstraints(): Unit = limits.gridx += 1

  /**
   * Increments a constraints for x weight parameter.
   */
  def incrementWeightXConstraints(): Unit = limits.weightx += 1

  /**
   * Sets anchor constraints
   *
   * @param constraint
   *         constraints to set as anchor
   */
  def setAnchor(constraint: Int): Unit = limits.anchor = constraint

  /**
   * Returns the GridBagConstraints
   *
   * @return limits
   */
  def getLimits: GridBagConstraints = limits

}
