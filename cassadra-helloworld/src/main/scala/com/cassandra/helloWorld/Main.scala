package com.cassandra.helloWorld

import com.datastax.driver.core.ConsistencyLevel
import com.outworkers.phantom.ResultSet
import com.outworkers.phantom.connectors.{ContactPoints, RootConnector}
import com.outworkers.phantom.dsl.{Database, KeySpaceDef, Table}
import com.outworkers.phantom.keys.PartitionKey

import scala.concurrent.duration._
import scala.concurrent.{Future}

object Main extends App {

  println("Oe!")

  val hosts = Seq("127.0.0.1")

  val connector = ContactPoints(hosts).withClusterBuilder(_.withPort(9042)).keySpace("firstKeyspace")

  class ClientsDatabase(val keyspaces: KeySpaceDef) extends Database[ClientsDatabase](keyspaces) {

    object ClientByClientID extends NewDatabase with keyspaces.Connector

  }

  // Aqui se crea la BD
  new ClientsDatabase(connector).create(1.minute)

}

case class Client (
  id: Int,
  name: String,
  age: Int
)

abstract class MyDatabase extends Table [MyDatabase, Client] {

  override val tableName = "Clients"

  object id extends IntColumn with PartitionKey{ override lazy val name = "new_ID"}
  object name extends StringColumn
  object age extends IntColumn

}

abstract class NewDatabase extends MyDatabase with RootConnector {

  def getClientByClienteID(clientID: Int): Future[Option[Client]] = {
    select.where(_.id eqs clientID).consistencyLevel_=(ConsistencyLevel.ONE).one()
  }

  def insertCliente(clientID: Int, nameCLient: String, ageClient: Int): Future[ResultSet] = {
    insert.value(_.id, clientID).value(_.name, nameCLient).value(_.age, ageClient).future()
  }

}
