package co.edu.escuelaing
package kafka.producer

trait Producer[T] {
  def send(topic: String, key: String, value: T): Unit
}
