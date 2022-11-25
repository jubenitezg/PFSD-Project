package co.edu.escuelaing
package mail.config

import java.util.Properties

object Config {

  val properties: Properties = {
    val props = new java.util.Properties()
    val sender = sys.env.getOrElse("EMAIL_USER", throw new RuntimeException("EMAIL_USER environment variable is needed"))
    val pass = sys.env.getOrElse("EMAIL_PASS", throw new RuntimeException("EMAIL_PASS environment variable is needed"))
    props.setProperty("mail.smtp.host", "smtp-mail.outlook.com")
    props.setProperty("mail.smtp.starttls.enable", "true")
    props.setProperty("mail.smtp.ssl.protocols", "TLSv1.2")
    props.setProperty("mail.smtp.port", "587")
    props.setProperty("mail.smtp.mail.sender", sender)
    props.setProperty("mail.smtp.user", sender)
    props.setProperty("mail.smtp.pass", pass)
    props.setProperty("mail.smtp.auth", "true")
    props
  }
  val sender: String = properties.get("mail.smtp.mail.sender").toString
  val recipient: String = sys.env.getOrElse("RECIPIENT", "julianbenitez08@hotmail.com")
  val protocol: String = "smtp"
  val user: String = properties.get("mail.smtp.user").toString
  val pass: String = properties.get("mail.smtp.pass").toString


}
