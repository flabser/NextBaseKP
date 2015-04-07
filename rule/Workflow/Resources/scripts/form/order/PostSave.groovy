package form.order
import kz.nextbase.script._Document
import kz.nextbase.script._Session
import kz.nextbase.script.events._FormPostSave
import kz.nextbase.script.mail._Memo

class PostSave extends _FormPostSave {

    @Override
    public void doPostSave(_Session ses, _Document doc) {
        //def recipient = ses.getStructure().getEmployer(doc.getValueString("recipient"))
        def recipients = doc.getValueList("recipient");
        def signed = doc.getValueList("signed");
        def prepared = doc.getValueList("prepared");
        def orderexecuters = doc.getValueList("orderexecuters");
        def agreed = doc.getValueList("agreed");
        def xmppmsg = ""
        def recipientsID = []
        doc.addReader(doc.getValueString("recipient"));
        if (doc.getValueString("mailnotification") == '') {

            def mailAgent = ses.getMailAgent();
            def msngAgent = ses.getInstMessengerAgent()

            xmppmsg = "Уведомление о документе на рассмотрение  \n"
            xmppmsg += "Приказ : " + doc.getValueString("briefcontent") + "\n"
            xmppmsg += doc.getFullURL() + "\n"

            def memo = new _Memo("Уведомление о документе на рассмотрение", "Новый приказ", "Приказ", doc, true)
            for (String recipient : recipients) {
            	xmppmsg += "Вы получили данное сообщение как получатель"
                def rec = ses.getStructure().getEmployer(recipient)
                def recipientEmail = ses.getStructure().getEmployer(recipient).getEmail()

                msngAgent.sendMessage([rec.getInstMessengerAddr()], xmppmsg)
                mailAgent.sendMail([recipientEmail], memo)
                def userActivity = ses.getUserActivity();
                userActivity.postActivity(this.getClass().getName(), "Memo has been send to " + recipientEmail)
            }
            for (String signer : signed) {
            	def rec = ses.getStructure().getEmployer(signer)
            	def recipientEmail = ses.getStructure().getEmployer(signer).getEmail()
            			
            	msngAgent.sendMessage([rec.getInstMessengerAddr()], xmppmsg)
            	mailAgent.sendMail([recipientEmail], memo)
            	def userActivity = ses.getUserActivity();
            	userActivity.postActivity(this.getClass().getName(), "Memo has been send to " + recipientEmail)
            }
            for (String prepare : prepared) {
            	def rec = ses.getStructure().getEmployer(prepare)
            	def recipientEmail = ses.getStructure().getEmployer(prepare).getEmail()
				msngAgent.sendMessage([rec.getInstMessengerAddr()], xmppmsg)
           		mailAgent.sendMail([recipientEmail], memo)
            	def userActivity = ses.getUserActivity();
            	userActivity.postActivity(this.getClass().getName(), "Memo has been send to " + recipientEmail)
            }
            for (String exec : orderexecuters) {
            	def rec = ses.getStructure().getEmployer(exec)
            	def recipientEmail = ses.getStructure().getEmployer(exec).getEmail()
            	msngAgent.sendMessage([rec.getInstMessengerAddr()], xmppmsg)
            	mailAgent.sendMail([recipientEmail], memo)
            	def userActivity = ses.getUserActivity();
            	userActivity.postActivity(this.getClass().getName(), "Memo has been send to " + recipientEmail)
            }
            doc.addStringField("mailnotification", "sent")
            doc.save("[supervisor]")
        }
    }
}