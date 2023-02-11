package es.david.ejemplo

import cats.effect.{IO, IOApp}

object Main extends IOApp.Simple {
  val run = scalahtt4sServer.run[IO]
}
