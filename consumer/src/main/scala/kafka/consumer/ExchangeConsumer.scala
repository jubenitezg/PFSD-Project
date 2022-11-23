package co.edu.escuelaing
package kafka.consumer

import kafka.config.Config
import protos.exchange_rates.ExchangeRatesProto

import org.apache.spark.SparkConf
import org.apache.spark.streaming.kafka010.{ConsumerStrategies, KafkaUtils, LocationStrategies}
import org.apache.spark.streaming.{Seconds, StreamingContext}

object ExchangeConsumer extends Consumer[ExchangeRatesProto] with App {

  val conf = new SparkConf().setAppName("ExchangeConsumer").setMaster("local[*]")
  val ssc = new StreamingContext(conf, Seconds(1))

  val messages = KafkaUtils.createDirectStream[String, Array[Byte]](
    ssc,
    LocationStrategies.PreferConsistent,
    ConsumerStrategies.Subscribe[String, Array[Byte]](Set(Config.EXCHANGE_TOPIC), Config.KAFKA_CONSUMER_PROPS)
  )

  messages
    .map(m => ExchangeRatesProto.parseFrom(m.value))
    .print()

  ssc.start()
  ssc.awaitTermination()

  override def process(topic: String, key: String, value: ExchangeRatesProto): Unit = {
    println(s"topic: $topic, key: $key, value: $value")
  }
}
