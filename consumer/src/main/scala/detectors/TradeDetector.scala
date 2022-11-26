package co.edu.escuelaing
package detectors

import ports.{ForAlerting, ForDetectingChanges}

import scala.collection.mutable


object TradeDetector extends ForDetectingChanges[Array[Map[String, (Double, Double)]]] {

  var current: mutable.Map[String, (Double, Double)] = mutable.HashMap[String, (Double, Double)]()

  override def detectChanges(trades: Array[Map[String, (Double, Double)]], forAlerting: ForAlerting): Unit = {
    trades.foreach(trade => {
      trade.foreach(pair => {
        val (price, amount) = pair._2
        val (currentMaxPrice, currentMinPrice) = current.getOrElse(pair._1, (0.0, Double.MaxValue))
        if (price > currentMaxPrice) {
          if (currentMaxPrice != 0.0) {
            forAlerting.sendAlert(s"ALERT FOR ${pair._1}", s"MAX PRICE DETECTED: $currentMaxPrice ${pair._1}")
          }
          current(pair._1) = (price, amount)
        }
        if (price < currentMinPrice) {
          if (currentMinPrice != Double.MaxValue) {
            forAlerting.sendAlert(s"ALERT FOR ${pair._1}", s"MIN PRICE DETECTED: $currentMinPrice ${pair._1}")
          }
          current(pair._1) = (price, amount)
        }
      })
    })
  }
}

