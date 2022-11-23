package co.edu.escuelaing
package schema

import protos.exchange_rates.ExchangeRatesProto

case class ExchangeRates(disclaimer: String, license: String, timestamp: Long, base: String, rates: Map[String, Double]) {

  def toProto: ExchangeRatesProto = {
    ExchangeRatesProto(
      disclaimer = disclaimer,
      license = license,
      timestamp = timestamp,
      base = base,
      rates = rates
    )
  }

}
