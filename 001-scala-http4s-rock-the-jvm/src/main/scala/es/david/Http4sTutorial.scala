package es.david

import cats._
import cats.effect._
import cats.implicits._
import org.http4s.circe._
import org.http4s._
import io.circe.generic.auto._
import io.circe.syntax._
import org.http4s.dsl._
import org.http4s.dsl.impl._
import org.http4s.headers._
import org.http4s.implicits._
import org.http4s.server._
import org.http4s.server.blaze.BlazeServerBuilder

import java.time.Year
import java.util.UUID
import scala.collection.mutable
import scala.util.Try

object Http4sTutorial extends IOApp {

  // Peliculas db
  type Actor = String

  case class Movie(id: String, title: String, year: Int, actors: List[Actor], director: String)

  case class Director(firstName: String, lastName: String) {
    override def toString(): String = s"$firstName $lastName"
  }

  case class DirectorDetails(firstName: String, lastName: String, genero: String)

  /*
      - GET todas las peliculas para un director en un año
      - GET todos los actores de una pelicula
      - GET detalles del director
      - POST añadir un nuevo director
  */

  // Dada una Request -> Obtenemos un Response (puede involucrar interactuar con servicios, BBDD)
  // que pueden tener efectos secundarios y queremos mantener la transparencia referencial y lo envolveremos en una F
  // Request -> F[Option[Response]]
  // Http4Routes[F]

  object DirectorQueryParamMatcher extends QueryParamDecoderMatcher[String]("director")

  implicit val yearQueryParamDecoder: QueryParamDecoder[Year] =
    QueryParamDecoder[Int].emap { yearInt =>
      Try(Year.of(yearInt))
        .toEither
        .leftMap { ex =>
          ParseFailure(ex.getMessage, ex.getMessage)
        }
    } //.map(Year.of)

  object YearQueryParamMatcher extends OptionalValidatingQueryParamDecoderMatcher[Year]("year") //OptionalQueryParamDecoderMatcher[Year]("year")


  // GET /movies?director=Zack%20Snyder&year=2021
  def movieRoutes[F[_] : Monad]: HttpRoutes[F] = {
    val dsl = Http4sDsl[F]
    import dsl._

    HttpRoutes.of[F] {
      case GET -> Root / "movies" :? DirectorQueryParamMatcher(director) +& YearQueryParamMatcher(maybeYear) =>
        val moviesByDirector = findMoviesByDirector(director)
        maybeYear match {
          case Some(validatedYear) => validatedYear.fold(
            _ => BadRequest("el año tiene mal el formato"),
            year => {
              val moviesByDirectorAndYear = moviesByDirector.filter(_.year == year.getValue)
              Ok(moviesByDirectorAndYear.asJson)
            }
          )
          case None => Ok(moviesByDirector.asJson)
        }
      case GET -> Root / "movies" / UUIDVar(movieId) / "actors" => findMovieById(movieId).map(_.actors) match {
        case Some(actor) => Ok(actor.asJson)
        case None => NotFound(s"no enonctra la pelicula $movieId en la bbdd")
      }
    }

  }

  def directorRoutes[F[_] : Monad]: HttpRoutes[F] = {
    val dsl = Http4sDsl[F]
    import dsl._

    HttpRoutes.of[F] {
      case GET -> Root / "directors" / DirectorPath(director) => directorDetilsDB.get(director) match {
        case Some(dirDire) => Ok(dirDire.asJson)
        case _ => NotFound(s"No se ha encontrado el $director")
      }
    }
  }

  val directorDetilsDB: mutable.Map[Director, DirectorDetails] = mutable.Map(Director("Zack", "Snyder") -> DirectorDetails("Zack", "Snyder", "hombre"))
  val snjl: Movie = Movie(
    "6bcbca1e-efd3-411d-9f7c-14b872444fce",
    "Zack Snyder's Justice League",
    2021,
    List("Henry Cavill", "Gal Godot", "Ezra Miller", "Ben Affleck", "Ray Fisher", "Jason Momoa"),
    "Zack Snyder"
  )

  val movies: Map[String, Movie] = Map(snjl.id -> snjl)

  // logica negocio
  private def findMovieById(movieId: UUID): Option[Movie] =
    movies.get(movieId.toString)

  private def findMoviesByDirector(director: String): List[Movie] =
    movies.values.filter(_.director == director).toList


  object DirectorPath {
    def unapply(str: String): Option[Director] = {
      Try {
        val tokens = str.split(" ")
        Director(tokens(0), tokens(1))
      }.toOption
    }
  }

  def allRoutes[F[_] : Monad]: HttpRoutes[F] = {
    movieRoutes[F] <+> directorRoutes[F] // cats.syntax.semigroupk._
  }

  def allRoutesComplete[F[_] : Monad]: HttpApp[F] =
    allRoutes[F].orNotFound

  override def run(args: List[String]): IO[ExitCode] = {
    val apis = Router(
      "/api" -> movieRoutes[IO],
      "/api/admin" -> directorRoutes[IO]
    ).orNotFound

    BlazeServerBuilder[IO](runtime.compute)
      .bindHttp(8888, "0.0.0.0")
      .withHttpApp(allRoutesComplete) // alternativa apis
      .resource
      .use(_ => IO.never)
      .as(ExitCode.Success)
  }
}
