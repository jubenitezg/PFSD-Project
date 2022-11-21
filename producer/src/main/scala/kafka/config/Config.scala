package co.edu.escuelaing
package kafka.config

import java.util.Properties

object Config {

  val TOPIC = "trades"
  val KEY = "websockets"

  val KAFKA_PRODUCER_PROPS: Properties = {
    val props = new Properties()
    val bootstrapServers = sys.env.getOrElse("BOOTSTRAP_SERVERS", "localhost:9093")
    props.put("bootstrap.servers", bootstrapServers)
    props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer")
    props.put("value.serializer", "org.apache.kafka.common.serialization.ByteArraySerializer")
    props
  }

}
