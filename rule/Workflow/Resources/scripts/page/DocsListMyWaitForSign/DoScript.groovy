package page.DocsListMyWaitForSign

import java.text.SimpleDateFormat
import kz.nextbase.script._Session
import kz.nextbase.script._WebFormData
import kz.nextbase.script.events._DoScript
import nextbase.groovy.*

class DoScript extends _DoScript {
	
	@Override
	public void doProcess(_Session session, _WebFormData formData, String lang) {
		
		//println(formData)

		def page = 1;
		if (formData.containsField("page") && formData.getValue("page")){
			page = Integer.parseInt(formData.getValue("page"))
		}
		

		def formula = "(form='officememoprj' or form='applicationprj' or form='outgoingprj' or form='orderprj' or form='contractprj') and author='" + session.getCurrentUserID() + "' and viewtext3 = 'SIGNING'";
		def db = session.getCurrentDatabase()
		def col = db.getCollectionOfDocuments(formula, page, true, true, new SimpleDateFormat("dd.MM.yyyy"))
		setContent(col)
	}
}