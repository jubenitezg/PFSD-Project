package co.edu.escuelaing
package detectors

import ports.{ForAlerting, ForDetectingChanges}

object ExchangeRatesDetector extends ForDetectingChanges[Array[Map[String, Double]]] {

  val DETECT_RATE = "USD-COP"
  var MAX_RATE_DETECTED = 0.0
  var MIN_RATE_DETECTED = 0.0

  override def detectChanges(value: Array[Map[String, Double]], forAlerting: ForAlerting): Unit = {
    val maxValue = value.map(_.get(DETECT_RATE)).max.get
    if (maxValue > MAX_RATE_DETECTED) {
      if (MAX_RATE_DETECTED != 0.0) {
        forAlerting.sendAlert("MAX_RATE_DETECTED", s"MAX_RATE_DETECTED: $MAX_RATE_DETECTED")
      }
      MAX_RATE_DETECTED = maxValue
    } else if (maxValue < MIN_RATE_DETECTED) {
      if (MIN_RATE_DETECTED != 0.0) {
        forAlerting.sendAlert("MIN_RATE_DETECTED", s"MIN_RATE_DETECTED: $MIN_RATE_DETECTED")
      }
      forAlerting.sendAlert(s"New max rate detected for $DETECT_RATE", s"New max rate detected: $maxValue")
    }
  }

}
