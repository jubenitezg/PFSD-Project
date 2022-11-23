package co.edu.escuelaing
package mail.config

import java.util.Properties

object Config {

  val properties: Properties = {
    val props = new java.util.Properties()
    val sender = sys.env.getOrElse("GMAIL_USER", throw new RuntimeException("GMAIL_USER environment variable is needed"))
    val gmailPass = sys.env.getOrElse("GMAIL_APP_PASS", throw new RuntimeException("GMAIL_APP_PASS environment variable is needed"))
    props.setProperty("mail.smtp.host", "smtp.gmail.com")
    props.setProperty("mail.smtp.starttls.enable", "true")
    props.setProperty("mail.smtp.ssl.protocols", "TLSv1.2")
    props.setProperty("mail.smtp.port", "587")
    props.setProperty("mail.smtp.mail.sender", sender)
    props.setProperty("mail.smtp.user", sender)
    props.setProperty("mail.smtp.pass", gmailPass)
    props.setProperty("mail.smtp.auth", "true")
    props
  }
  val sender: String = properties.get("mail.smtp.mail.sender").toString
  val recipient: String = "julian.benitezg99@gmail.com"
  val protocol: String = "smtp"
  val user: String = properties.get("mail.smtp.user").toString
  val pass: String = properties.get("mail.smtp.pass").toString


}
