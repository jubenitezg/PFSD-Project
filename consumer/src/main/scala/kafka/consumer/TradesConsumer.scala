package co.edu.escuelaing
package kafka.consumer

import detectors.TradeDetector
import kafka.config.Config
import mail.SmtpMailer
import protos.trades.TradeProto

import org.apache.spark.SparkConf
import org.apache.spark.streaming.kafka010.{ConsumerStrategies, KafkaUtils, LocationStrategies}
import org.apache.spark.streaming.{Seconds, StreamingContext}

object TradesConsumer extends Consumer[TradeProto] {

  override def consume(): Unit = {
    val conf = new SparkConf().setAppName("TradesConsumer").setMaster("local[*]")
    val ssc = new StreamingContext(conf, Seconds(1))

    val tradesDS = KafkaUtils.createDirectStream[String, Array[Byte]](
      ssc,
      LocationStrategies.PreferConsistent,
      ConsumerStrategies.Subscribe[String, Array[Byte]](Set(Config.TRADES_TOPIC), Config.KAFKA_CONSUMER_PROPS)
    )

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

    ssc.start()
    ssc.awaitTermination()
  }
}
