package co.edu.escuelaing
package mail

import mail.config.Config
import ports.ForAlerting

import com.typesafe.scalalogging.Logger

import javax.mail.internet.{InternetAddress, MimeMessage}
import javax.mail.{Message, Session}


object SmtpMailer extends ForAlerting {

  val LOGGER: Logger = Logger("SmtpMailer")

  override def sendAlert(alert: String, msg: String): Unit = {
    val session = Session.getInstance(Config.properties)
    session.setDebug(true)
    val message = new MimeMessage(session)
    message.addHeader("Content-Type", "text/plain; charset=UTF-8");
    message.setSubject(alert)
    message.setText(msg)
    message.setFrom(new InternetAddress(Config.sender))
    message.addRecipient(
      Message.RecipientType.TO,
      // For some weird reason, the email address arrives with quotes
      new InternetAddress(Config.recipient.replace("\"", ""))
    )
    val t = session.getTransport(Config.protocol)
    t.connect(Config.user, Config.pass)
    t.sendMessage(message, message.getAllRecipients)
    LOGGER.info(s"ALERT SENT: $alert")
    t.close()
  }

}

