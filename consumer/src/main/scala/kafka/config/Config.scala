package co.edu.escuelaing
package kafka.config

object Config {

  val TRADES_TOPIC = "trades"

  val KAFKA_CONSUMER_PROPS: Map[String, Object] = {
    val bootstrapServers = sys.env.getOrElse("BOOTSTRAP_SERVERS", "localhost:9093")
    Map[String, Object](
      "bootstrap.servers" -> bootstrapServers,
      "key.deserializer" -> "org.apache.kafka.common.serialization.StringDeserializer",
      "value.deserializer" -> "org.apache.kafka.common.serialization.ByteArrayDeserializer",
      "group.id" -> "trades-consumer"
    )
  }

}
