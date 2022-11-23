package co.edu.escuelaing
package detectors

import ports.{ForAlerting, ForDetectingChanges}
import protos.trades.TradeProto

object TradeDetector extends ForDetectingChanges[TradeProto] {

  val THRESHOLD = 0.1
  var currentMax = 0.0
  var currentMin = 0.0

  override def detectChanges(trade: TradeProto, forAlerting: ForAlerting): Unit = ???
}

