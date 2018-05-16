package messaging

import akka.actor.Actor
import messaging.Storage.AddUser

object Storage {
  sealed trait StorageMsg

  case class AddUser(user: User) extends  StorageMsg
}

class Storage extends Actor {
  var users = List.empty[User]

  override def receive: PartialFunction[Any, Unit] = {
    case AddUser(user) =>
      println(s"Storage: $user added.")
      users = user :: users
  }
}