package view.utils

import java.awt.Component

import javax.swing.JPanel

/**
 * Utility object for adding Components to a JPanel in currying style
 */
object JPanelAddAll {

  /**
   * Class adding Components to a JPanel with apply method
   *
   * @param panel
   *         JPanel to which adding components
   */
  case class JPanelAdder(panel: JPanel) {
    def apply(component: Component): JPanelAdder = {
      panel.add(component)
      this
    }
  }

  implicit class ImplicitJPanelAdder(panel: JPanel) {
    implicit def addAll(component: Component): JPanelAdder = JPanelAdder(panel)(component)
  }

}
