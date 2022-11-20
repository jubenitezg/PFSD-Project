package co.edu.escuelaing
package schema

case class Subscription(symbol: String) {
  def toJson: String = {
    s"""{"type":"subscribe","symbol":"$symbol"}"""
  }
}

