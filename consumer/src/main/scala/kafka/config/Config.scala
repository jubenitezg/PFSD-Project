package co.edu.escuelaing
package kafka.config

import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.spark.SparkConf
import org.apache.spark.streaming.dstream.InputDStream
import org.apache.spark.streaming.kafka010.{ConsumerStrategies, KafkaUtils, LocationStrategies}
import org.apache.spark.streaming.{Seconds, StreamingContext}

object Config {

  val TRADES_TOPIC = "trades"
  val EXCHANGE_TOPIC = "exchange-rates"

  val KAFKA_CONSUMER_PROPS: Map[String, Object] = {
    val bootstrapServers = sys.env.getOrElse("BOOTSTRAP_SERVERS", "localhost:9093")
    Map[String, Object](
      "bootstrap.servers" -> bootstrapServers,
      "key.deserializer" -> "org.apache.kafka.common.serialization.StringDeserializer",
      "value.deserializer" -> "org.apache.kafka.common.serialization.ByteArrayDeserializer",
      "group.id" -> "trades-consumer"
    )
  }

  val conf: SparkConf = new SparkConf().setAppName("Consumer").setMaster("local[*]")
  val ssc = new StreamingContext(conf, Seconds(1))

  val exchangeRatesDS: InputDStream[ConsumerRecord[String, Array[Byte]]] = KafkaUtils.createDirectStream[String, Array[Byte]](
    ssc,
    LocationStrategies.PreferConsistent,
    ConsumerStrategies.Subscribe[String, Array[Byte]](Set(Config.EXCHANGE_TOPIC), Config.KAFKA_CONSUMER_PROPS)
  )

  val tradesDS: InputDStream[ConsumerRecord[String, Array[Byte]]] = KafkaUtils.createDirectStream[String, Array[Byte]](
    ssc,
    LocationStrategies.PreferConsistent,
    ConsumerStrategies.Subscribe[String, Array[Byte]](Set(Config.TRADES_TOPIC), Config.KAFKA_CONSUMER_PROPS)
  )

}
