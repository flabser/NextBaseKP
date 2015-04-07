package page.actionbar

import java.util.ArrayList;
import java.util.Map;
import kz.nextbase.script.*;
import kz.nextbase.script.actions.*
import kz.nextbase.script.events._DoScript

class order extends _DoScript { 

	@Override
	public void doProcess(_Session session, _WebFormData formData, String lang) {
		def actionBar = new _ActionBar(session);
		def user = session.getCurrentAppUser();
		if (user.hasRole(["registrator_projects", "administrator"])){
			def newDocAction = new _Action(getLocalizedWord("Новый приказ", lang),getLocalizedWord("Новый приказ", lang),"new_document")
			newDocAction.setURL("Provider?type=edit&element=document&id=order&docid=")
			actionBar.addAction(newDocAction);
		}
		if (user.hasRole(["administrator", "chancellery"])){
			actionBar.addAction(new _Action(getLocalizedWord("Удалить документ", lang),getLocalizedWord("Удалить документ", lang),_ActionType.DELETE_DOCUMENT));
		}
		setContent(actionBar);
	}
}

