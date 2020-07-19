package model

import java.io.{BufferedReader, InputStreamReader}
import java.nio.charset.StandardCharsets
import java.util.stream.Collectors

object GameRules {

  val THEORY: String = "/prolog/gameRules.pl"

  def theory(): String = {
    new BufferedReader(new InputStreamReader(getClass.getResourceAsStream(THEORY), StandardCharsets.UTF_8))
      .lines()
      .collect(Collectors.joining("\n"))
  }

}
