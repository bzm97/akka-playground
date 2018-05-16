package messaging

import akka.actor.Actor
import messaging.Checker.{BadUser, Check, GoodUser}

object Checker {
  sealed trait CheckerMsg

  case class Check(user: User) extends CheckerMsg

  sealed trait CheckerResponse

  case class GoodUser(user: User) extends CheckerResponse
  case class BadUser(user: User) extends CheckerResponse
}

class Checker extends Actor {
  val blacklist = List(
    User("Adam", "adam@example.com")
  )

  override def receive: PartialFunction[Any, Unit] = {
    case Check(user) if blacklist.contains(user) =>
      println(s"Checker: $user is in the blacklist.")
      sender() ! BadUser(user)
    case Check(user) =>
      println(s"Checker: $user is ok.")
      sender() ! GoodUser(user)
  }
}