package page.mydocs_navi
import kz.nextbase.script._Session
import kz.nextbase.script._WebFormData
import kz.nextbase.script.constants._QueryMacroType
import kz.nextbase.script.constants._ReadConditionType
import kz.nextbase.script.events._DoScript
import kz.nextbase.script.outline._Outline
import kz.nextbase.script.outline._OutlineEntry

class DoScript extends _DoScript {
	
	@Override
	public void doProcess(_Session session, _WebFormData formData, String lang) {
		
		//println(lang)
		
		def db = session.getCurrentDatabase()		
		def outline = new _Outline(getLocalizedWord("Мои документы",lang), getLocalizedWord("Мои документы",lang), "mydocs")
		
		def e1 = new _OutlineEntry(getLocalizedWord("На рассмотрение",lang), getLocalizedWord("На рассмотрение",lang), "toconsider", "Provider?type=page&id=toconsider&page=0")
		def col1 = db.getCollectionOfDocuments("(form='order' or form='workdoc' or form='application' or form='out' or form='IN') and viewtext7 ~ '" + session.getCurrentUserID() + "' and vn != '' and has_response = 0", false, true)
		e1.setValue(col1.getCount())
        e1.unread = col1.unreadCount;
		outline.addEntry(e1)
		
		def e2 = new _OutlineEntry(getLocalizedWord("Поручено мне",lang), getLocalizedWord("Поручено мне",lang), "taskforme", "Provider?type=page&id=taskforme&page=0")
		def col2 = db.getCollectionOfDocuments("form='task' and viewtext4 ~ '" + session.getCurrentUserID() + "' and viewtext3 = '1'", false, true)
		e2.setValue(col2.getCount())
        e2.unread = col2.unreadCount;
		outline.addEntry(e2)
		
		def e3 = new _OutlineEntry(getLocalizedWord("Мои задания",lang), getLocalizedWord("Мои задания",lang), "mytasks", "Provider?type=page&id=mytasks&page=0")
		def col3 = db.getCollectionOfDocuments("form='task' and author='" + session.getCurrentUserID() + "' and viewtext3 = '1'", false, true)
		e3.setValue(col3.getCount())
		e3.unread = col3.unreadCount;
		outline.addEntry(e3)
		
		def e4 = new _OutlineEntry(getLocalizedWord("Исполненные",lang), getLocalizedWord("Исполненные",lang), "completetask", "Provider?type=page&id=completetask&page=0")
		def col4 = db.getCollectionOfDocuments("form='task' and viewtext4 ~ '" + session.getCurrentUserID() + "' and viewtext3 = '0'", false, true)
		e4.setValue(col4.getCount())
		e4.unread = col4.unreadCount;
		outline.addEntry(e4)
		
		def e5 = new _OutlineEntry(getLocalizedWord("Мне на согласование",lang), getLocalizedWord("Мне на согласование",lang), "waitforcoord", "Provider?type=page&id=waitforcoord&page=0")
		def col5 = db.getCollectionOfDocuments("(form='officememoprj' or form='sheet' or form='applicationprj' or form='outgoingprj' or form='orderprj' or form='contractprj') and viewtext5 ~ '" + session.getCurrentUserID() + "' and viewtext3 = 'COORDINATING'", false, true)
		e5.setValue(col5.getCount())
		e5.unread = col5.unreadCount;
		outline.addEntry(e5)
		
		def e6 = new _OutlineEntry(getLocalizedWord("Мне на подпись",lang), getLocalizedWord("Мне на подпись",lang), "waitforsign", "Provider?type=page&id=waitforsign&page=0")
		def col6 = db.getCollectionOfDocuments("(form='officememoprj' or form='applicationprj' or form='outgoingprj' or form='orderprj' or form='contractprj') and viewtext4='" + session.getCurrentUserID() + "' and viewtext3 = 'SIGNING'", false, true)
		e6.setValue(col6.getCount())
		e6.unread = col6.unreadCount;
		outline.addEntry(e6)
		
		def e7 = new _OutlineEntry(getLocalizedWord("На согласовании",lang), getLocalizedWord("На согласовании",lang), "mywaitforcoord", "Provider?type=page&id=mywaitforcoord&page=0")
		def col7 = db.getCollectionOfDocuments("(form='officememoprj' or form='sheet' or form='applicationprj' or form='outgoingprj' or form='orderprj' or form='contractprj') and author = '" + session.getCurrentUserID() + "' and viewtext3 = 'COORDINATING'", false)
		e7.setValue(col7.getCount())
		outline.addEntry(e7)
		
		def e8 = new _OutlineEntry(getLocalizedWord("Ожидают подпись",lang), getLocalizedWord("Ожидают подпись",lang), "mywaitforsign", "Provider?type=page&id=mywaitforsign&page=0")
		def col8 = db.getCollectionOfDocuments("(form='officememoprj' or form='applicationprj' or form='outgoingprj' or form='orderprj' or form='contractprj') and author='" + session.getCurrentUserID() + "' and viewtext3 = 'SIGNING'", false)
		e8.setValue(col8.getCount())
		outline.addEntry(e8)

		def e10 = new _OutlineEntry(getLocalizedWord("На ознакомление",lang), getLocalizedWord("На ознакомление",lang), "waitforacquaint", "Provider?type=page&id=waitforacquaint&page=0")
		def col10 = db.getCollectionOfDocuments("grantor = '" + session.getCurrentUserID() + "'", 0, false, false, false, _ReadConditionType.ONLY_UNREAD, 'grantusers')
		e10.setValue(col10.getCount())
		outline.addEntry(e10)

		def e11 = new _OutlineEntry(getLocalizedWord("Мне на ознакомление",lang), getLocalizedWord("Мне на ознакомление",lang), "mywaitforacquaint", "Provider?type=page&id=mywaitforacquaint&page=0")
		def col11 = db.getCollectionOfDocuments("grantusers ~ '" + session.getCurrentUserID() + "'", 0, false, false, false, _ReadConditionType.ONLY_UNREAD)
		e11.setValue(col11.getCount())
		outline.addEntry(e11)

		def e9 = new _OutlineEntry(getLocalizedWord("Избранные",lang), getLocalizedWord("Избранные",lang), "favdocs", "Provider?type=page&id=favdocs&page=0")
		e9.setValue(db.getCount(_QueryMacroType.FAVOURITES))
		outline.addEntry(e9)
		
		setContent(outline)
	}
	

}
