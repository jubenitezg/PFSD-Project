package co.edu.escuelaing
package modifiers

import schema.ExchangeRates

import com.typesafe.scalalogging.Logger

object ExchangeRatesModifier {

  var curr = 0
  var modifier = 15

  val LOGGER: Logger = Logger("ExchangeRatesModifier")

  def modifyFixed(exchangeRates: ExchangeRates): ExchangeRates = {
    LOGGER.info(s"Modifying exchange rates: curr($curr) modifier($modifier)")
    curr += 1
    if (curr % 100 == 0) {
      val modifiedRates = exchangeRates.rates.map { case (k, v) =>
        k -> (v + modifier)
      }
      modifier *= 2
      exchangeRates.copy(rates = modifiedRates)
    } else {
      exchangeRates
    }
  }

}
