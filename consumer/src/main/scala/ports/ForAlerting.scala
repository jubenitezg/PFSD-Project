package co.edu.escuelaing
package ports

trait ForAlerting {

  def sendAlert(alert: String, msg: String): Unit

}
