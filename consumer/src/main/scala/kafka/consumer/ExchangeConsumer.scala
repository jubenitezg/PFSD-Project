package co.edu.escuelaing
package kafka.consumer

import detectors.ExchangeRatesDetector
import kafka.config.Config.{exchangeRatesDS, ssc}
import mail.SmtpMailer
import protos.exchange_rates.ExchangeRatesProto


object ExchangeConsumer extends Consumer[ExchangeRatesProto] {

  override def consume(): Unit = {

    val exchangeRatesDetector = ExchangeRatesDetector
    exchangeRatesDS
      .map(m => ExchangeRatesProto.parseFrom(m.value))
      .map(er => er.rates.map { case (k, v) => (s"${er.base}-$k", v) })
      .foreachRDD(rdd => {
        exchangeRatesDetector.detectChanges(rdd.collect(), SmtpMailer)
      })

  }
}
