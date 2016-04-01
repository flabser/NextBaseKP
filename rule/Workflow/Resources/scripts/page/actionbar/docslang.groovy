package page.actionbar

import java.util.ArrayList;
import java.util.Map;
import kz.nextbase.script.*;
import kz.nextbase.script.actions.*
import kz.nextbase.script.events._DoScript

class docslang extends _DoScript { 

	@Override
	public void doProcess(_Session session, _WebFormData formData, String lang) {
		def actionBar = new _ActionBar(session);
		def user = session.getCurrentAppUser();
			if (user.hasRole(["supervisor","administrator","chancellery"])){
				def newDocAction = new _Action(getLocalizedWord("Новый язык документа",lang), getLocalizedWord("Новый язык документа",lang), "new_glossary")
				newDocAction.setURL("Provider?type=edit&id=docslang&key=")
				actionBar.addAction(newDocAction);
			}
		if (user.hasRole(["administrator"])){
				actionBar.addAction(new _Action(getLocalizedWord("Удалить документ", lang),getLocalizedWord("Удалить документ",lang), _ActionType.DELETE_DOCUMENT));
			}
		setContent(actionBar);
	}
}

