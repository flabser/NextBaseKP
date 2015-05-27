package form.out

import kz.nextbase.script.*
import kz.nextbase.script.actions._Action
import kz.nextbase.script.actions._ActionBar
import kz.nextbase.script.actions._ActionType
import kz.nextbase.script.constants._DocumentModeType
import kz.nextbase.script.coordination._BlockCollection
import kz.nextbase.script.events._FormQueryOpen

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
		def dep = user.getDepartmentID()
		if (dep) {
			publishDepartment("department", dep)
		}
		publishValue("title",getLocalizedWord("Новый исходящий документ", lang))
		publishEmployer("author", ses.getCurrentAppUser().getUserID())
		publishValue("dvn", ses.getCurrentDateAsString())	
	}
	
	
	@Override
	public void doQueryOpen(_Session ses, _Document doc, _WebFormData webFormData, String lang) {
		
		def user = ses.getCurrentAppUser()
		
		def nav = ses.getPage("outline", webFormData)
		publishElement(nav)
		
		def actionBar = new _ActionBar(ses)
		def dep = user.getDepartmentID()
		if (dep) {
			publishDepartment("department", dep)
		}
		if(doc.getValueString("recipient") == user.getUserID() || doc.getValueString("signedby") == user.getUserID()){
			actionBar.addAction(new _Action(getLocalizedWord("Перепоручение",lang),getLocalizedWord("Перепоручение",lang),"compose_task"))
			actionBar.addAction(new _Action(getLocalizedWord("Исполнить",lang),getLocalizedWord("Исполнить",lang),"compose_execution"))
		}
		actionBar.addAction(new _Action(getLocalizedWord("Ознакомить",lang),getLocalizedWord("Ознакомить",lang),"acquaint"))
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
		
		publishValue("title",getLocalizedWord("Исходящий документ", lang) + " ")
		publishEmployer("author",doc.authorID)
		publishValue("vn",doc.getValueString("vn"))
		def dvn = doc.getValueString("dvn") != '' ? doc.getValueString("dvn") : ses.getCurrentDateAsString();
		publishValue("dvn",dvn);
		if (doc.getField("vid")) {
			publishGlossaryValue("vid",doc.getValueGlossary("vid"))
		}
		if (doc.getField("lang")) {
			publishGlossaryValue("lang",doc.getValueGlossary("lang"))
		}
        doc.getValueList("corr").each {println Integer.parseInt(it)}
		if (doc.getField("corr")) {
			publishGlossaryValue("corr", doc.getValueList("corr").collect {Integer.parseInt(it)})
		}
		publishValue("corrstring",doc.getValueString("corrstring"))
		publishValue("addresscorr",doc.getValueString("addresscorr"))
		if (doc.getField("nomentype")) {
			publishGlossaryValue("nomentype", doc.getValueNumber("nomentype"))
		}
		publishEmployer("signedby",doc.getValueString("signedby"))
		if (doc.getField("deliverytype")) {
			publishGlossaryValue("deliverytype",doc.getValueGlossary("deliverytype"))
		}

		publishValue("briefcontent",doc.getValueString("briefcontent"))
		publishValue("contentsource",doc.getValueString("contentsource"))
		publishValue("ctrldate",doc.getValueString("ctrldate"))
		publishValue("vnish",doc.getValueString("vnish"))
		try{
			publishAttachment("rtfcontent","rtfcontent")
		}catch(_Exception e){

		}
		publishValue("remark",doc.getValueString("remark"))
		publishValue("np",doc.getValueString("np"))
		publishValue("np2",doc.getValueString("np2"))
		publishValue("blanknumber",doc.getValueString("blanknumber"))
		publishValue("backvnin",doc.getValueString("backvnin"))
		try{
			def link  = (_CrossLink)doc.getValueObject("link")
			publishValue("link", link)
		}catch(_Exception e){
		
		}
		try{
			def parentdoc = doc.getParentDocument();
			def	blockCollection  = (_BlockCollection)parentdoc.getValueObject("coordination")
			publishValue("coordination", blockCollection)
		}catch(_Exception e){
		
		}
	}
	
}