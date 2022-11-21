package co.edu.escuelaing


import mail.SmtpMailer

object Main {
  def main(args: Array[String]): Unit = {
    SmtpMailer.sendAlert("test")
  }
}