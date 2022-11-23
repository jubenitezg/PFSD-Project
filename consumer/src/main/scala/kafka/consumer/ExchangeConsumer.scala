package co.edu.escuelaing
package kafka.consumer

import detectors.ExchangeRatesDetector
import kafka.config.Config
import mail.SmtpMailer
import protos.exchange_rates.ExchangeRatesProto

import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.spark.SparkConf
import org.apache.spark.streaming.dstream.InputDStream
import org.apache.spark.streaming.kafka010.{ConsumerStrategies, KafkaUtils, LocationStrategies}
import org.apache.spark.streaming.{Seconds, StreamingContext}

object ExchangeConsumer extends Consumer[ExchangeRatesProto] {

  override def consume(): Unit = {
    val conf: SparkConf = new SparkConf().setAppName("ExchangeConsumer").setMaster("local[*]")
    val ssc = new StreamingContext(conf, Seconds(1))

    val exchangeRatesDS: InputDStream[ConsumerRecord[String, Array[Byte]]] = KafkaUtils.createDirectStream[String, Array[Byte]](
      ssc,
      LocationStrategies.PreferConsistent,
      ConsumerStrategies.Subscribe[String, Array[Byte]](Set(Config.EXCHANGE_TOPIC), Config.KAFKA_CONSUMER_PROPS)
    )

    val exchangeRatesDetector = ExchangeRatesDetector
    exchangeRatesDS
      .map(m => ExchangeRatesProto.parseFrom(m.value))
      .map(er => er.rates.map { case (k, v) => (s"${er.base}-$k", v) })
      .foreachRDD(rdd => {
        exchangeRatesDetector.detectChanges(rdd.collect(), SmtpMailer)
      })

    ssc.start()
    ssc.awaitTermination()
  }
}
