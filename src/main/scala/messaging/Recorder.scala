package messaging

import akka.actor.{Actor, ActorRef, Props}
import akka.pattern.ask
import messaging.Checker.{BadUser, Check, GoodUser}
import messaging.Recorder.NewUser
import messaging.Storage.AddUser

object Recorder {
  sealed trait RecorderMsg

  case class NewUser(user: User) extends RecorderMsg
  def props(checker: ActorRef, storage: ActorRef) = Props(new Recorder(checker, storage))
}

class Recorder(checker: ActorRef, storage: ActorRef) extends Actor {
  import scala.concurrent.ExecutionContext.Implicits.global
  import akka.util.Timeout
  import scala.concurrent.duration._

  implicit val timeout: Timeout = Timeout(5 seconds)

  override def receive: PartialFunction[Any, Unit] = {
    case NewUser(user) =>
      checker ? Check(user) map {
        case GoodUser(`user`) =>
          storage ! AddUser(user)
        case BadUser(`user`) =>
          println(s"Recorder: $user is bad.")
      }
  }
}