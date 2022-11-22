package co.edu.escuelaing
package kafka.consumer

object ExchangeConsumer extends Consumer[String] {
  override def process(topic: String, key: String, value: String): Unit = {
    println(s"topic: $topic, key: $key, value: $value")
  }
}
