package page.actionbar

import java.util.ArrayList;
import java.util.Map;
import kz.nextbase.script.*;
import kz.nextbase.script.actions.*
import kz.nextbase.script.events._DoScript

class applicationprj extends _DoScript { 

	@Override
	public void doProcess(_Session session, _WebFormData formData, String lang) {
		def actionBar = new _ActionBar(session);
		def user = session.getCurrentAppUser();
			if (user.hasRole(["registrator_projects" ,"administrator"])){
				def newDocAction = new _Action(getLocalizedWord("Проект заявления", lang),getLocalizedWord("Проект заявления", lang),"new_document")
				newDocAction.setURL("Provider?type=edit&element=document&id=applicationprj&docid=")
				actionBar.addAction(newDocAction);
			}
		if (user.hasRole(["administrator"])){
			actionBar.addAction(new _Action(getLocalizedWord("Удалить документ", lang),getLocalizedWord("Удалить документ", lang),_ActionType.DELETE_DOCUMENT));
		}
		setContent(actionBar);
	}
}

