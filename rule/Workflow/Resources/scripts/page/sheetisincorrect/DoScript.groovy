package page.sheetisincorrect

import kz.nextbase.script._Session
import kz.nextbase.script._WebFormData
import kz.nextbase.script.events._DoScript

import java.text.SimpleDateFormat

class DoScript extends _DoScript {
    @Override
    void doProcess(_Session session, _WebFormData formData, String lang) {
		def cdb = ses.getCurrentDatabase();
		def doc = cdb.getDocumentByID(Integer.valueOf(formData.getValue("docid")))
		def commenttext = formData.getValue("commenttext");
		def userid = session.getCurrentUserID();

        try {
            doc.setValueString("iscorrect","false")
            doc.setValueString("mark_incorreсt_user",userid)
            doc.setValueString("mark_incorrect_commenttext",commenttext)
            doc.setViewText("false",3)
            doc.save("[supervisor]")
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


