package co.edu.escuelaing
package kafka.consumer

trait Consumer[T] {

  def process(topic: String, key: String, value: T): Unit

}
