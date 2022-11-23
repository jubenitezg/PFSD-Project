package co.edu.escuelaing
package schema

case class ExchangeRates(disclaimer: String, license: String, timestamp: Long, base: String, rates: Map[String, Double])
