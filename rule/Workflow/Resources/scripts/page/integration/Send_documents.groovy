package page.integration
import groovy.xml.MarkupBuilder
import kz.nextbase.script._Helper
import kz.nextbase.script._Session
import kz.nextbase.script._WebFormData
import kz.nextbase.script.events._DoScript

import javax.mail.Message
import javax.mail.Session
import javax.mail.Transport
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeBodyPart
import javax.mail.internet.MimeMessage
import javax.mail.internet.MimeMultipart

class Send_documents extends _DoScript {
    void doProcess(_Session ses, _WebFormData formData, String lang) {
        File tmpdir = new File("tmp/" + _Helper.getRandomValue())
        tmpdir.mkdir()
        File file = new File(tmpdir.path + "/document.xml")
        file.createNewFile()
        file.withWriter { writer ->
            def xml = new MarkupBuilder(writer)
            xml.doubleQuotes = true
            xml.mkp.xmlDeclaration('version': "1.0", 'encoding': "UTF-8")
            xml.DocumentXmlInfo('xmlns:xsd' : "http://www.w3.org/2001/XMLSchema", 'xmlns:xsi' : "http://www.w3.org/2001/XMLSchema-instance") {
                DocumentTypeID('Письмо')
                DocumentNumber('11ИСХ-1466/11')
                Subject('Document')
                SheetCount('1')
                AppendCount('1')
                SignerNameRu('Елшибеков С К')
                ExecutorFIO('Кожабаев Е.О.')
                ExecutorNA('CN=Erbolat Kozhabayev/O=KMGRM')
            }
        }
        def attachments = []
        attachments.add(file)
        File addAttach = new File("C:\\Users\\Mariya\\Desktop\\Люк, я твой папка\\sample_001\\AttachmentContent.zip")
        attachments.add(addAttach)
        sendMessage(attachments)
    }

    void sendMessage(ArrayList<File> attachments) {
        String host = "smtp.mail.ru";
        Properties props = System.getProperties();
        props.put("mail.smtp.auth", true);
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.user", "4msworkflow@inbox.ru");
        props.put("mail.smtp.password", "kf,jhfnjhbz,eleotuj");
        props.put("mail.smtp.port", "25");
        props.put("mail.smtp.ssl.enable", "true");

        Session session = Session.getDefaultInstance(props, null);
        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress("4msworkflow@inbox.ru"));

        InternetAddress toAddress = new InternetAddress("4msworkflow@inbox.ru");

        message.addRecipient(Message.RecipientType.TO, toAddress);

        message.setSubject("Document");
        message.setText("Mail text");

        def textpart = new MimeBodyPart()
        textpart.text = 'This is new message'
        def multi = new MimeMultipart()
        attachments.each {
            def attPart = new MimeBodyPart()
            attPart.attachFile(it)
            multi.addBodyPart(attPart)
        }

        multi.addBodyPart(textpart)
        message.content = multi

        Transport transport = session.getTransport("smtps");

        transport.connect(host, "4msworkflow@inbox.ru", "kf,jhfnjhbz,eleotuj");

        transport.sendMessage(message, message.getAllRecipients());
        transport.close();

    }
}
