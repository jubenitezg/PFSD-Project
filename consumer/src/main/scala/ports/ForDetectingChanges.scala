package co.edu.escuelaing
package ports

trait ForDetectingChanges[T] {

  def detectChanges(value: T, forAlerting: ForAlerting): Unit

}
