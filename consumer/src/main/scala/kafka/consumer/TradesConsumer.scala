package co.edu.escuelaing
package kafka.consumer

import detectors.TradeDetector
import kafka.config.Config.tradesDS
import mail.SmtpMailer
import protos.trades.TradeProto


object TradesConsumer extends Consumer[TradeProto] {

  override def consume(): Unit = {
    val tradesDetector = TradeDetector
    tradesDS
      .map(m => TradeProto.parseFrom(m.value))
      .map(_.data)
      .map(_.groupBy(_.symbol))
      .map(dataFromSymbol => {
        dataFromSymbol.view.mapValues(data => {
          (data.map(_.price).max, data.map(_.price).min)
        }).toMap
      }) // Map(symbol1 -> (maxPrice1, minPrice1), symbol2 -> (maxPrice2, minPrice2), ...)
      .foreachRDD(rdd => {
        tradesDetector.detectChanges(rdd.collect(), SmtpMailer)
      })

  }
}
