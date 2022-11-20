package co.edu.escuelaing
package kafka.producer

import kafka.config.Config

import com.typesafe.scalalogging.Logger
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}

object TradesProducer extends Producer {

  val LOGGER: Logger = Logger("TradesProducer")

  val producer = new KafkaProducer[String, String](Config.KAFKA_PRODUCER_PROPS)

  def send(topic: String, key: String, value: String): Unit = {
    val record = new ProducerRecord[String, String](topic, key, value)
    producer.send(record)
    LOGGER.info(s"Sent record: $record")
  }
}
