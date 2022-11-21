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
    .map(x => Trade.parseFrom(x.value))
    .print()

  //  val lines = messages.map(_.value)
  //  val words = lines.flatMap(_.split(" "))
  //  val wordCounts = words.map(x => (x, 1L))
  //    .reduceByKey(_ + _)
  //  wordCounts.print()
  ssc.start()
  ssc.awaitTermination()

}
