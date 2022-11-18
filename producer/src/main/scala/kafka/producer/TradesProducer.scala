package co.edu.escuelaing
package kafka.producer

import kafka.config.Config

import com.typesafe.scalalogging.Logger
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}

object TradesProducer extends Producer {

  val LOGGER: Logger = Logger("TradesProducer")

  val producer = new KafkaProducer[String, Array[Byte]](Config.kafkaProducerProps)

  def send(topic: String, key: String, value: Array[Byte]): Unit = {
    val record = new ProducerRecord[String, Array[Byte]](topic, key, value)
    producer.send(record)
    LOGGER.info(s"Sent record: $record")
  }
}
