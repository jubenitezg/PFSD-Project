package co.edu.escuelaing
package kafka.producer

trait Producer {
  def send(topic: String, key: String, value: Array[Byte]): Unit
}
