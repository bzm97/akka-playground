package runtime_swap

import akka.actor.{ActorSystem, Props}

import scala.io.StdIn

object RuntimeSwappingExample extends App {
  import UserStorage._

  val system = ActorSystem("swapping-example")

  val userStorage = system.actorOf(Props[UserStorage], "user-storage")

  println("Running Runtime swapping example\nPress ENTER to exit")

  // Wrong order: using Stash trait to send messages for correct state
  userStorage ! Operation(DBOperation.Create, Some(User("Vasiliy", "vasiliy@example.com")))
  userStorage ! Connect
  userStorage ! Disconnect

  StdIn.readLine()

  system.terminate()
}
