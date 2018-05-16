package persistence

import akka.actor.{ActorSystem, Props}

import scala.io.StdIn

object PersistenceExample extends App {
  import Counter._

  val system = ActorSystem("persistent-example")

  val counter = system.actorOf(Props[Counter], "counter-example")

  println("Running persistence example\nPress ENTER to exit")

  counter ! Command(Increment(3))
  counter ! Command(Increment(5))
  counter ! Command(Decrement(6))

  counter ! "print"

  StdIn.readLine()

  system.terminate()
}
