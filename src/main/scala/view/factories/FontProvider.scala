package view.factories

import java.awt.Font

import view.utils.ResourceLoader

object FontProvider {

  val HNEFATAFL_FONT_PATH = "/font/NorseBold-2Kge.otf"

  val hnefataflFont: Font = ResourceLoader.loadFont(HNEFATAFL_FONT_PATH)

}
