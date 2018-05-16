package persistence

import akka.actor.ActorLogging
import akka.persistence.{PersistentActor, SnapshotOffer}

object Counter {
  sealed trait Operation {
    val count: Int
  }

  case class Increment(override val count: Int) extends Operation
  case class Decrement(override val count: Int) extends Operation

  case class Command(operation: Operation)
  case class Event(operation: Operation)

  case class State(count: Int)
}

class Counter extends PersistentActor with ActorLogging {
  import persistence.Counter._

  println("Starting...")

  override def persistenceId: String = "counter-example"

  var state = State(0)

  def updateState(event: Event): Unit = event match {
    case Event(Increment(count)) =>
      state = State(count = state.count + count)
    case Event(Decrement(count)) =>
      state = State(count = state.count - count)
  }

  val receiveRecover: Receive = {
    case event: Event =>
      println(s"Counter receive $event in recovering mode")
      updateState(event)
    case SnapshotOffer(_, snapshot: State) =>
      println(s"Counter receive snapshot with data $snapshot in recovering mode")
      state = snapshot
  }

  val receiveCommand: Receive = {
    case cmd @ Command(operation) =>
      println(s"Counter receive $cmd")
      persist(Event(operation)) { event =>
        updateState(event)
      }

    case "print" =>
      println(s"Counter's current state: $state")
  }
}
