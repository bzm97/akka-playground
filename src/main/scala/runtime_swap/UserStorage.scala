package runtime_swap

import akka.actor.{Actor, Stash}
import runtime_swap.UserStorage.{Connect, DBOperation, Disconnect, Operation}

object UserStorage {

  trait DBOperation
  object DBOperation {
    case object Create extends DBOperation
    case object Update extends DBOperation
    case object Read extends DBOperation
    case object Delete extends DBOperation
  }

  sealed trait UserStorageMsg

  case object Connect extends UserStorageMsg
  case object Disconnect extends UserStorageMsg
  case class Operation(dBOperation: DBOperation, user: Option[User]) extends  UserStorageMsg
}

class UserStorage extends Actor with Stash {

  override def receive: Receive = disconnected

  def connected: Actor.Receive = {
    case Disconnect =>
      println("User storage disconnected from DB")
      context.unbecome()
    case Operation(operation, user) =>
      println(s"User storage received $operation to perform on $user")
  }

  def disconnected: Actor.Receive = {
    case Connect =>
      println("User storage connected to DB")
      unstashAll()
      context.become(connected)
    case _ =>
      stash()
  }
}
