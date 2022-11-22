package co.edu.escuelaing
package kafka.consumer

import kafka.config.Config
import protos.trades.Trade

import org.apache.spark.SparkConf
import org.apache.spark.streaming.kafka010.{ConsumerStrategies, KafkaUtils, LocationStrategies}
import org.apache.spark.streaming.{Seconds, StreamingContext}

object TradesConsumer extends App {

  val conf = new SparkConf().setAppName("TradesConsumer").setMaster("local[*]")
  val ssc = new StreamingContext(conf, Seconds(1))


  val messages = KafkaUtils.createDirectStream[String, Array[Byte]](
    ssc,
    LocationStrategies.PreferConsistent,
    ConsumerStrategies.Subscribe[String, Array[Byte]](Set(Config.TRADES_TOPIC), Config.KAFKA_CONSUMER_PROPS)
  )

  messages
    .map(m => Trade.parseFrom(m.value))
    .map(_.data) // List(Data1,Data2, Data3, ...)
    .map(_.groupBy(_.symbol)) // Map(symbol1 -> List(Data1, Data2), symbol2 -> List(Data3, Data4), ...)
    .map(dataFromSymbol => {
      dataFromSymbol.view.mapValues(data => {
        (data.map(_.price).max, data.map(_.price).min)
      }).toMap
      // TODO: send to service to detect if max detected or min detected
    }) // Map(symbol1 -> (maxPrice1, minPrice1), symbol2 -> (maxPrice2, minPrice2), ...)
    .print()

  ssc.start()
  ssc.awaitTermination()

}
