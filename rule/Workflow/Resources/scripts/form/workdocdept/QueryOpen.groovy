package form.workdocdept

import kz.nextbase.script.*
import kz.nextbase.script.actions._Action
import kz.nextbase.script.actions._ActionType
import kz.nextbase.script.constants._DocumentModeType
import kz.nextbase.script.constants._DocumentType
import kz.nextbase.script.coordination._BlockCollection
import kz.nextbase.script.events._FormQueryOpen
import kz.nextbase.script.task._Task

class QueryOpen extends _FormQueryOpen {

	@Override
	public void doQueryOpen(_Session session, _WebFormData webFormData, String lang) {
		def user = session.getCurrentAppUser()

		def nav = session.getPage("outline", webFormData)
		publishElement(nav)

		def actionBar = session.createActionBar();
		actionBar.addAction(new _Action(getLocalizedWord("Сохранить и закрыть",lang),getLocalizedWord("Сохранить и закрыть",lang),_ActionType.SAVE_AND_CLOSE))
		actionBar.addAction(new _Action(getLocalizedWord("Закрыть",lang),getLocalizedWord("Закрыть без сохранения",lang),_ActionType.CLOSE))
		publishElement(actionBar)

		publishValue("title",getLocalizedWord("Новая служебная записка между департаментами", lang))
		publishEmployer("author", user.getUserID())
		publishEmployer("signer", user.getUserID())
		publishValue("dvn", session.getCurrentDateAsString())
	}


	@Override
	public void doQueryOpen(_Session session, _Document doc, _WebFormData webFormData, String lang) {
		def doctitle = "Служебная записка между департаментами"
		if (doc.getField("finaldoctype")) {
			publishGlossaryValue("finaldoctype",doc.getValueNumber("finaldoctype"));
			doctitle = session.getCurrentDatabase().getGlossaryDocument("finaldoctype", "docid="+doc.getValueNumber("finaldoctype").toString()).getViewText();
		}
		publishValue("title",getLocalizedWord(doctitle, lang) +  " № "+ doc.getValueString("vn") +" " + getLocalizedWord("от", lang) + " " + doc.getValueString("dvn"))
		def user = session.getCurrentAppUser()
		
		def nav = session.getPage("outline", webFormData)
		publishElement(nav)
		
		def actionBar =  session.createActionBar();
		if(user.hasRole("supervisor")){
			actionBar.addAction(new _Action(_ActionType.GET_DOCUMENT_ACCESSLIST))
		}
		if(doc.getEditMode() == _DocumentModeType.EDIT){
			actionBar.addAction(new _Action(getLocalizedWord("Сохранить и закрыть",lang),getLocalizedWord("Сохранить и закрыть",lang),_ActionType.SAVE_AND_CLOSE))
		}
		
		
		def show_compose_actions = false
		/*def recipients = (_EmployerCollection)doc.getValueObject("recipient")
		recipients.getEmployers().each { r ->
			if (r && r.getUserID() == user.getUserID()) {
				show_compose_actions = true
			}
		}*/
		if(doc.getValueList("recipient").contains(user.getUserID())){
			show_compose_actions = true;
		}
		 if(show_compose_actions){
			 actionBar.addAction(new _Action(getLocalizedWord("Резолюция",lang),getLocalizedWord("Резолюция",lang),"compose_task"))
			 actionBar.addAction(new _Action(getLocalizedWord("Исполнить",lang),getLocalizedWord("Исполнить",lang),"compose_execution"))
		 }
		 actionBar.addAction(new _Action(getLocalizedWord("Ознакомить",lang),getLocalizedWord("Ознакомить",lang),"acquaint"))
		actionBar.addAction(new _Action(getLocalizedWord("Закрыть",lang),getLocalizedWord("Закрыть без сохранения",lang),_ActionType.CLOSE))
		def grant_story = doc.getValueObject("grantblocks")
		if (grant_story) {
			publishValue("grantblocks", grant_story)
		}

		publishEmployer("author",doc.getAuthorID())
		publishEmployer("signer",doc.getValueString("signer"))
		publishEmployer("recipient",doc.getValueList("recipient"));
		publishValue("vn",doc.getValueString("vn"))
		publishValue("dvn",doc.getValueString("dvn"))
		publishValue("briefcontent",doc.getValueString("briefcontent"))
		publishValue("ctrldate",doc.getValueString("ctrldate"))
		publishValue("contentsource",_Helper.getNormalizedRichText(doc.getValueString("contentsource")))
		def link  = (_CrossLink)doc.getValueObject("link")
		publishValue("link", link)

		try{
			publishAttachment("rtfcontent","rtfcontent")
		}catch(_Exception e){

		}
		publishParentDoc(session,webFormData.getParentDocID())
		 
		 publishElement(actionBar)
	}
	
	/*private Collection<String> getRecipients(_Document doc){
		def recipients = doc.getValueList("recipient");
		return recipients
	}*/
	
	private publishParentDoc(_Session session, int[] complexID){
		def db = session.getCurrentDatabase()
		try{
			def parentDoc = db.getDocumentByComplexID(complexID)
			if (parentDoc.getDocumentType() == _DocumentType.TASK){
				def parentTask = (_Task)parentDoc
				publishValue("parenttasktype", parentTask.getTaskType())
			}
			publishValue("parentviewtext", parentDoc.getViewText())
			publishValue("parenturl", parentDoc.getURL())
		}catch(_Exception e){
			publishValue("parentviewtext", "")
			publishValue("parenturl", "")
		}
	}
}