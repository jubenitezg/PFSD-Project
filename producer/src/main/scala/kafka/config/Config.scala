package co.edu.escuelaing
package kafka.config

import java.util.Properties

object Config {

  val TOPIC = "trades"

  val kafkaProducerProps: Properties = {
    val props = new Properties()
    val bootstrapServers = sys.env.getOrElse("BOOTSTRAP_SERVERS", "localhost:9092")
    props.put("bootstrap.servers", bootstrapServers)
    props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer")
    props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer")
    props
  }

}
