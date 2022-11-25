package co.edu.escuelaing
package detectors

import ports.{ForAlerting, ForDetectingChanges}

import com.typesafe.scalalogging.Logger

object ExchangeRatesDetector extends ForDetectingChanges[Array[Map[String, Double]]] {

  val DETECT_RATE = "USD-COP"
  var MAX_RATE_DETECTED = 0.0

  val LOGGER = Logger("ExchangeRatesDetector")

  override def detectChanges(value: Array[Map[String, Double]], forAlerting: ForAlerting): Unit = {
    val values = value.map(_.get(DETECT_RATE)).map(_.get)
    val maxValue = if (values.nonEmpty) values.max else 0.0
    if (maxValue > MAX_RATE_DETECTED) {

      if (MAX_RATE_DETECTED != 0.0) {
        forAlerting.sendAlert(s"ALERT FOR $DETECT_RATE", s"MAX RATE DETECTED: $MAX_RATE_DETECTED $DETECT_RATE")
      }
      MAX_RATE_DETECTED = maxValue
    }
  }

}
