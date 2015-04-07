package form.order
import kz.nextbase.script._CrossLink
import kz.nextbase.script._Document
import kz.nextbase.script._Exception
import kz.nextbase.script._Session
import kz.nextbase.script._WebFormData
import kz.nextbase.script.actions._Action
import kz.nextbase.script.actions._ActionBar
import kz.nextbase.script.actions._ActionType
import kz.nextbase.script.constants._DocumentModeType
import kz.nextbase.script.coordination._BlockCollection
import kz.nextbase.script.events._FormQueryOpen
import kz.nextbase.script.struct._EmployerCollection

class QueryOpen extends _FormQueryOpen {

	
	@Override
	public void doQueryOpen(_Session ses, _WebFormData webFormData, String lang) {
		def user = ses.getCurrentAppUser()
		
		def nav = ses.getPage("outline", webFormData)
		publishElement(nav)
		
		def actionBar = ses.createActionBar();
		actionBar.addAction(new _Action(getLocalizedWord("Сохранить и закрыть",lang),getLocalizedWord("Сохранить и закрыть",lang),_ActionType.SAVE_AND_CLOSE))
		actionBar.addAction(new _Action(getLocalizedWord("Закрыть",lang),getLocalizedWord("Закрыть без сохранения",lang),_ActionType.CLOSE))
		publishElement(actionBar)

		publishValue("title",getLocalizedWord("Новый приказ от", lang))
		publishEmployer("author", ses.getCurrentAppUser().getUserID())
		publishValue("dvn", ses.getCurrentDateAsString())
		publishValue("ctrldate", ses.getCurrentDateAsString(30))
	}


	@Override
	public void doQueryOpen(_Session ses, _Document doc, _WebFormData webFormData, String lang) {
		def user = ses.getCurrentAppUser()
		
		def nav = ses.getPage("outline", webFormData)
		publishElement(nav)
		
		def actionBar = new _ActionBar(ses)
		def show_compose_actions = false
		/*def recipients = (_EmployerCollection) doc.getValueObject("recipients")
        recipients.getEmployers().each { r ->
             if (r && r.getUserID() == user.getUserID()) {
                   show_compose_actions = true;
             }
        }*/
		if(doc.getValueString("signed") == user.getUserID()){
			show_compose_actions = true;
		}
		if(doc.getAuthorID() == user.getUserID()){
			actionBar.addAction(new _Action(getLocalizedWord("Ознакомить",lang),getLocalizedWord("Ознакомить",lang),"acquaint"))
		}
		if(show_compose_actions){
			actionBar.addAction(new _Action(getLocalizedWord("Резолюция",lang),getLocalizedWord("Резолюция",lang),"compose_task"))
			actionBar.addAction(new _Action(getLocalizedWord("Исполнить",lang),getLocalizedWord("Исполнить",lang),"compose_execution"))
		}
		
		if(doc.getEditMode() == _DocumentModeType.EDIT && user.hasRole("registrator_outgoing")){
			if(doc.getValueString("vn") != ''){
				actionBar.addAction(new _Action(getLocalizedWord("Сохранить и закрыть",lang),getLocalizedWord("Сохранить и закрыть",lang),_ActionType.SAVE_AND_CLOSE))
			}else{
				actionBar.addAction(new _Action(getLocalizedWord("Зарегистрировать документ",lang),getLocalizedWord("Зарегистрировать документ",lang),_ActionType.SAVE_AND_CLOSE))
			}
		}
		if(user.hasRole("supervisor")){
			actionBar.addAction(new _Action(_ActionType.GET_DOCUMENT_ACCESSLIST))
		}
		actionBar.addAction(new _Action(getLocalizedWord("Закрыть",lang),getLocalizedWord("Закрыть без сохранения",lang),_ActionType.CLOSE))
		publishElement(actionBar)
		
		publishValue("title",getLocalizedWord("Приказ", lang) + " ")
		publishEmployer("author",doc.getAuthorID())
		publishValue("vn",doc.getValueString("vn"))
		publishValue("dvn",doc.getValueString("dvn"))
		
		publishValue("outnum",doc.getValueString("outnum"))
		publishValue("outdate",doc.getValueString("outdate"))
		if (doc.getField("ordtype")) {
			publishGlossaryValue("ordtype",doc.getValueGlossary("ordtype"))
		}
		//publishEmployer("recipient", doc.getValueList("recipients"))
		/*publishValue("recipient",doc.getValueObject("recipients"))*/
		publishEmployer("signed", doc.getValueList("signed"))
		publishEmployer("prepared", doc.getValueList("prepared"))
		publishEmployer("orderexecuters", doc.getValueList("orderexecuters"))
		//publishEmployer("agreed", doc.getValueList("agreed"))
		publishValue("briefcontent",doc.getValueString("briefcontent"))
		publishValue("contentsource",doc.getValueString("contentsource"))
		
		try{
			publishAttachment("rtfcontent","rtfcontent")
		}catch(_Exception e){

		}
		try{
			def parentdoc = doc.getParentDocument();
			def	blockCollection  = (_BlockCollection)parentdoc.getValueObject("coordination")
			publishValue("coordination", blockCollection)
		}catch(_Exception e){
		
		}
		publishValue("invbasis",doc.getValueString("invbasis"))
		publishValue("np",doc.getValueString("np"))
		publishValue("nc",doc.getValueString("nc"))
		def link  = (_CrossLink)doc.getValueObject("link")
		publishValue("link", link)

	}
	
	/*private Collection<String> getRecipient(_Document doc){
		def recipients = doc.getValueList("recipient");
		return recipients
	}*/

}