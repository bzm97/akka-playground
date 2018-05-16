package fsm

import akka.actor.{ActorSystem, Props}
import fsm.UserStorage.{Connect, DBOperation, Disconnect, Operation}

import scala.io.StdIn

object FsmExample extends App {
  val system = ActorSystem("fsm-example")

  val userStorage = system.actorOf(Props[UserStorage], "user-storage")

  println("Running FSM example\nPress ENTER to exit")

  userStorage ! Connect
  userStorage ! Operation(DBOperation.Create, Some(User("admin", "admin@example.com")))
  userStorage ! Disconnect

  StdIn.readLine()

  system.terminate()
}
