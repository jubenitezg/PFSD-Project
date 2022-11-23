package co.edu.escuelaing
package mail

import mail.config.Config
import ports.ForAlerting

import com.typesafe.scalalogging.Logger

import javax.mail.internet.{InternetAddress, MimeMessage}
import javax.mail.{Message, Session}


object SmtpMailer extends ForAlerting {

  val LOGGER: Logger = Logger("SmtpMailer")

  override def sendAlert(alert: String, msg: String): Either[Throwable, Unit] = {
    util.Try {
      val session = Session.getDefaultInstance(Config.properties)
      val message = new MimeMessage(session)
      message.setSubject(alert)
      message.setText(msg)
      message.setFrom(new InternetAddress(Config.sender))
      message.addRecipient(
        Message.RecipientType.TO,
        new InternetAddress(Config.recipient)
      )
      val t = session.getTransport(Config.protocol)
      t.connect(Config.user, Config.pass)
      t.sendMessage(message, message.getAllRecipients)
      LOGGER.info(s"Alert sent: $alert")
      t.close()
    }.toEither
  }

}

