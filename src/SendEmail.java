import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

class SendEmail {
    public static boolean createAndSendMail(String userName,String msg) throws MessagingException {
        // Step1 setup properties
        Properties mailServerProperties = System.getProperties();
        mailServerProperties.put("mail.smtp.port", "587");
        mailServerProperties.put("mail.smtp.auth", "true");
        mailServerProperties.put("mail.smtp.starttls.enable", "true");

        // Step2 get session
        Session mailSession = Session.getDefaultInstance(mailServerProperties, null);

        // Step2 create the message
        MimeMessage message = new MimeMessage(mailSession);
        message.addRecipient(Message.RecipientType.TO, new InternetAddress("aristodemou.panayiotis@ucy.ac.cy"));
        message.addRecipient(Message.RecipientType.CC, new InternetAddress("mpipon01@ucy.ac.cy"));
        message.addRecipient(Message.RecipientType.BCC, new InternetAddress("andreou.kyprianos@ucy.ac.cy"));
        message.setSubject("The Red Ball Game: Feedback");
        String emailBody = userName + " sent the following feedback about the game:\n\n"+msg;
        message.setContent(emailBody, "text/html");
        // Step3 get session and send email
        Transport transport = mailSession.getTransport("smtp");
        // Enter your  gmail UserID and Password
        transport.connect("smtp.gmail.com", "ece318temp@gmail.com", "Qwerty12345!");
        transport.sendMessage(message, message.getAllRecipients());
        transport.close();
        return true;
    }
}