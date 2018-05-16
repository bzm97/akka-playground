package fsm

import akka.actor.{FSM, Stash}
import fsm.UserStorage._

object UserStorage {
  sealed trait State

  case object Connected extends State
  case object Disconnected extends State

  sealed trait Data
  case object EmptyData extends Data

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

class UserStorage extends FSM[UserStorage.State, UserStorage.Data] with Stash {
  startWith(Disconnected, EmptyData)

  when(Disconnected) {
    case Event(Connect, _) =>
      println("UserStorage connected to DB")
      unstashAll()
      goto(Connected) using EmptyData

    case Event(_, _) =>
      stash()
      stay() using EmptyData
  }

  when(Connected) {
    case Event(Disconnect, _) =>
      println("UserStorage disconnected from DB")
      goto(Disconnected) using EmptyData

    case Event(Operation(operation, user), _) =>
      println(s"User storage received $operation with user $user")
      stay() using EmptyData
  }

  initialize()
}
