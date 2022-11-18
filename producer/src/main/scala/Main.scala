package co.edu.escuelaing

import finnhub.FinnhubClient


object Main {
  def main(args: Array[String]): Unit = {
    sys.env.get("FINNHUB_TOKEN") match {
      case Some(token) =>
        val finnhubClient = new FinnhubClient(token)
//        println(finnhubClient.symbolLookup("vmware"))
        finnhubClient.webSocket()
      case None => println("No token found")
    }
  }
}