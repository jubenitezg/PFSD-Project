package co.edu.escuelaing
package mail

import mail.config.Config
import ports.ForAlerting

import javax.mail.internet.{InternetAddress, MimeMessage}
import javax.mail.{Message, Session}


object SmtpMailer extends ForAlerting {

  override def sendAlert(alert: String): Either[Throwable, Unit] = {
    println(s"Sending alert: $alert")
    val session = Session.getDefaultInstance(Config.properties)
    val message = new MimeMessage(session)
    message.setSubject("ALERT from SCALA")
    message.setText("TEST FROM SCALA")
    message.setFrom(new InternetAddress(Config.properties.get("mail.smtp.mail.sender").toString))
    message.addRecipient(
      Message.RecipientType.TO,
      new InternetAddress("julian.benitezg99@gmail.com")
    )
    val t = session.getTransport("smtp")
    t.connect(Config.properties.get("mail.smtp.user").toString, Config.properties.get("mail.smtp.pass").toString)
    t.sendMessage(message, message.getAllRecipients)
    t.close()
    Right(())
  }

}

