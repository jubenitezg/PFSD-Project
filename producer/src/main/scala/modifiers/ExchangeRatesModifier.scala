package co.edu.escuelaing
package modifiers

import schema.ExchangeRates

object ExchangeRatesModifier {

  var curr = 0
  var modifier = 15

  def modifyFixed(exchangeRates: ExchangeRates): ExchangeRates = {
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
