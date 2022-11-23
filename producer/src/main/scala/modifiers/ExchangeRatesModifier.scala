package co.edu.escuelaing
package modifiers

import schema.ExchangeRates

object ExchangeRatesModifier {

  def modifyRandom(exchangeRates: ExchangeRates, randomValueMax: Int): ExchangeRates = {
    val random = scala.util.Random
    val modifiedRates = exchangeRates.rates.map { case (k, v) =>
      k -> (v + random.nextInt(randomValueMax))
    }
    exchangeRates.copy(rates = modifiedRates)
  }

}
