package co.edu.escuelaing
package kafka.consumer

import kafka.config.Config

import org.apache.spark.SparkConf
import org.apache.spark.streaming.kafka010.{ConsumerStrategies, KafkaUtils, LocationStrategies}
import org.apache.spark.streaming.{Seconds, StreamingContext}

object TradesConsumer extends App {


  val conf = new SparkConf().setAppName("TradesConsumer").setMaster("local[*]")
  val ssc = new StreamingContext(conf, Seconds(1))

  // TODO: move to config
  val topic = "trades"

  val messages = KafkaUtils.createDirectStream[String, String](
    ssc,
    LocationStrategies.PreferConsistent,
    ConsumerStrategies.Subscribe[String, String](Set(topic), Config.kafkaConsumerProps)
  )

}
