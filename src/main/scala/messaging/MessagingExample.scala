package messaging

import akka.actor.{ActorSystem, Props}

import scala.io.StdIn
import scala.language.postfixOps

object MessagingExample extends App {
  val system = ActorSystem("messaging-example")

  val checker = system.actorOf(Props[Checker], "checker")
  val storage = system.actorOf(Props[Storage], "storage")
  val recorder = system.actorOf(Recorder.props(checker, storage), "recorder")

  println("Example started\nPress ENTER to terminate")

  recorder ! Recorder.NewUser(User("Vasiliy", "vasya@example.com"))
  recorder ! Recorder.NewUser(User("Adam", "adam@example.com"))

  StdIn.readLine()

  system.terminate()
}
