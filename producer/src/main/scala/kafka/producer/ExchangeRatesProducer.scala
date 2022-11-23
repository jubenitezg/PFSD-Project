package co.edu.escuelaing
package kafka.producer

import kafka.config.Config

import protos.exchange_rates.ExchangeRatesProto
import com.typesafe.scalalogging.Logger
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}

object ExchangeRatesProducer extends Producer[ExchangeRatesProto] {

  val LOGGER: Logger = Logger("ExchangeRatesProducer")

  val producer = new KafkaProducer[String, Array[Byte]](Config.KAFKA_PRODUCER_PROPS)

  override def send(topic: String, key: String, value: ExchangeRatesProto): Unit = {
    val record = new ProducerRecord[String, Array[Byte]](topic, key, value.toByteArray)
    producer.send(record)
    LOGGER.info(s"Sent record: $record")
  }

}
