package co.edu.escuelaing
package kafka.consumer

trait Consumer[T] {

  def consume(): Unit

}
