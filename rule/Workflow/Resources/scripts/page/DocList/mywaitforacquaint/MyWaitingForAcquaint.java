package page.DocList.mywaitforacquaint;

import kz.nextbase.script._Database;
import kz.nextbase.script._Exception;
import kz.nextbase.script._Session;
import kz.nextbase.script._ViewEntryCollection;
import kz.nextbase.script._WebFormData;
import kz.nextbase.script.constants._ReadConditionType;
import kz.nextbase.script.events._DoScript;

public class MyWaitingForAcquaint extends _DoScript {

	@Override
	public void doProcess(_Session session, _WebFormData formData, String lang) {
		// println(formData)
		int page = 1;
		try {
			if (formData.containsField("page") && formData.getValue("page") != null) {
				page = Integer.parseInt(formData.getValue("page"));
			}
		} catch (NumberFormatException | _Exception e) {

		}

		String formula = "grantusers ~ '" + session.getCurrentUserID() + "'";
		_Database db = session.getCurrentDatabase();
		_ViewEntryCollection col = db.getCollectionOfDocuments(formula, page, true, true, false, _ReadConditionType.ONLY_UNREAD);
		setContent(col);
	}
}
