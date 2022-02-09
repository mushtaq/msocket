package msocket.example.server

object ServerMain {

  /** Start the server for serving API endpoints */
  def main(args: Array[String]): Unit = {
    val wiring = new ServerWiring

    wiring.rSocketServer.start("0.0.0.0", 7002)
    wiring.exampleServer.start("0.0.0.0", 5002)
  }

}
