package co.edu.escuelaing
package mail

import ports.ForAlerting

import java.util.Properties
import javax.mail.{Message, Session}
import javax.mail.internet.{InternetAddress, MimeMessage}


object SmtpMailer extends ForAlerting {

  def properties(): Properties = {
    val props = new Properties()
    props.setProperty("mail.smtp.host", "smtp.gmail.com")
    props.setProperty("mail.smtp.starttls.enable", "true")
    props.setProperty("mail.smtp.ssl.protocols", "TLSv1.2")
    props.setProperty("mail.smtp.port", "587")
    props.setProperty("mail.smtp.mail.sender", "julian.benitezg99@gmail.com")
    props.setProperty("mail.smtp.user", "julian.benitezg99@gmail.com")
    props.setProperty("mail.smtp.pass", sys.env.getOrElse("GMAIL_APP_PASS", ""))
    props.setProperty("mail.smtp.auth", "true")
    props
  }

  override def sendAlert(alert: String): Either[Throwable, Unit] = {
    println(s"Sending alert: $alert")
    val session = Session.getDefaultInstance(properties())
    val message = new MimeMessage(session)
    message.setSubject("ALERT from SCALA")
    message.setText("TEST FROM SCALA")
    message.setFrom(new InternetAddress(properties().get("mail.smtp.mail.sender").toString))
    message.addRecipient(
      Message.RecipientType.TO,
      new InternetAddress("julian.benitezg99@gmail.com")
    )
    val t = session.getTransport("smtp")
    t.connect(properties().get("mail.smtp.user").toString, properties().get("mail.smtp.pass").toString)
    t.sendMessage(message, message.getAllRecipients)
    t.close()
    Right(())
  }

}

