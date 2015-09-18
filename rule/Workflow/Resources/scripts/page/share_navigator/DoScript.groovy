package page.share_navigator

import java.util.Map;
import kz.nextbase.script.*;
import kz.nextbase.script.events._DoScript
import kz.nextbase.script.outline.*;

class DoScript extends _DoScript {

	@Override
	public void doProcess(_Session session, _WebFormData formData, String lang) {
		//println(lang)
		def list = []
		def user = session. getCurrentAppUser()
		def outline = new _Outline("", "", "outline")



		def orgdocs_outline = new _Outline(getLocalizedWord("Документы организации",lang), getLocalizedWord("Документы организации",lang), "orgdocs")
		def workdoc = new _OutlineEntry(getLocalizedWord("СЗ на имя руководителей",lang), getLocalizedWord("СЗ на имя руководителей",lang), "workdoc", "Provider?type=page&id=workdoc&page=0")
        orgdocs_outline.addEntry(workdoc);

		def workdocdept = new _OutlineEntry(getLocalizedWord("СЗ между подразделениями",lang), getLocalizedWord("СЗ между подразделениями",lang), "workdocdept", "Provider?type=page&id=workdocdept&page=0")
        orgdocs_outline.addEntry(workdocdept);

		def application = new _OutlineEntry(getLocalizedWord("Заявления",lang), getLocalizedWord("Заявления",lang), "application", "Provider?type=page&id=application&page=0")
		orgdocs_outline.addEntry(application);

        def indocs = new _OutlineEntry(getLocalizedWord("Входящие",lang), getLocalizedWord("Входящие",lang), "in", "Provider?type=page&id=in&page=0");
        orgdocs_outline.addEntry(indocs);

        def outdocs = new _OutlineEntry(getLocalizedWord("Исходящие",lang), getLocalizedWord("Исходящие",lang), "out", "Provider?type=page&id=out&page=0")
        orgdocs_outline.addEntry(outdocs);

		orgdocs_outline.addEntry(new _OutlineEntry(getLocalizedWord("Обращения граждан",lang), getLocalizedWord("Обращения граждан",lang), "letters", "Provider?type=page&id=letters&page=0"))
		def taskdocs = new _OutlineEntry(getLocalizedWord("Задания",lang), getLocalizedWord("Задания",lang), "task", "Provider?type=page&id=task&page=0");
        orgdocs_outline.addEntry(taskdocs);
		
       def orderdocs = new _OutlineEntry(getLocalizedWord("Приказы",lang), getLocalizedWord("Приказы",lang), "order", "Provider?type=page&id=order&page=0");
		orgdocs_outline.addEntry(addEntryByGlossary(session, orderdocs, "order","ordtype", false));

		def contractdocs = new _OutlineEntry(getLocalizedWord("Договоры",lang), getLocalizedWord("Договоры",lang), "contract", "Provider?type=page&id=contract&page=0");
		orgdocs_outline.addEntry(addEntryByGlossary(session, contractdocs, "contract","contracttype", false));
        if (user.hasRole("reader_sheet") || user.hasRole("registrator_sheet")){
            def sheetdocs = new _OutlineEntry(getLocalizedWord("Табели",lang), getLocalizedWord("Табели",lang), "sheet", "Provider?type=page&id=sheet&page=0");
		    orgdocs_outline.addEntry(sheetdocs);
        }

        outline.addOutline(orgdocs_outline)
		list.add(orgdocs_outline)
		def projects_outline = new _Outline(getLocalizedWord("Проекты организации",lang), getLocalizedWord("Проекты организации",lang), "projects")
		def officememoprj = new _OutlineEntry(getLocalizedWord("СЗ на имя руководителей",lang), getLocalizedWord("СЗ на имя руководителей",lang), "officememoprj", "Provider?type=page&id=officememoprj&page=0")
        projects_outline.addEntry(officememoprj);

		def officememoprjdept = new _OutlineEntry(getLocalizedWord("СЗ между подразделениями",lang), getLocalizedWord("СЗ между подразделениями",lang), "officememoprjdept", "Provider?type=page&id=officememoprjdept&page=0")
        projects_outline.addEntry(officememoprjdept);

        def applicationprj = new _OutlineEntry(getLocalizedWord("Заявления",lang), getLocalizedWord("Заявления",lang), "applicationprj", "Provider?type=page&id=applicationprj&page=0")
        projects_outline.addEntry(applicationprj);

		def outgoingprj = new _OutlineEntry(getLocalizedWord("Исходящие",lang), getLocalizedWord("Исходящие",lang), "outgoingprj", "Provider?type=page&id=outgoingprj&page=0");
        projects_outline.addEntry(outgoingprj);
       
		def orderprj = new _OutlineEntry(getLocalizedWord("Приказы",lang), getLocalizedWord("Приказ",lang), "orderprj", "Provider?type=page&id=orderprj&page=0");
		projects_outline.addEntry(addEntryByGlossary(session, orderprj, "orderprj","ordtype", true));
		
        def contractprj = new _OutlineEntry(getLocalizedWord("Договоры",lang), getLocalizedWord("Договор",lang), "contractprj", "Provider?type=page&id=contractprj&page=0");
		projects_outline.addEntry(addEntryByGlossary(session, contractprj, "contractprj","contracttype", true));

		list.add(projects_outline)
		outline.addOutline(projects_outline)

		if (user.hasRole("chancellery")){
			def docstoreg_outline = new _Outline(getLocalizedWord("На регистрацию",lang), getLocalizedWord("На регистрацию",lang), "docstoreg")
			docstoreg_outline.addEntry(new _OutlineEntry(getLocalizedWord("Исходящие",lang), getLocalizedWord("Исходящие",lang), "outdocreg", "Provider?type=page&id=outdocreg&page=0"))
			docstoreg_outline.addEntry(new _OutlineEntry(getLocalizedWord("Входящие",lang), getLocalizedWord("Входящие",lang), "indocreg", "Provider?type=page&id=indocreg&page=0"))
			docstoreg_outline.addEntry(new _OutlineEntry(getLocalizedWord("Приказы",lang), getLocalizedWord("Приказы",lang), "orderdocreg", "Provider?type=page&id=orderdocreg&page=0"))
			docstoreg_outline.addEntry(new _OutlineEntry(getLocalizedWord("Договоры",lang), getLocalizedWord("Договора",lang), "contractdocreg", "Provider?type=page&id=contractdocreg&page=0"))
			outline.addOutline(docstoreg_outline)
			list.add(docstoreg_outline)
		}

		/*def reports_outline = new _Outline(getLocalizedWord("Отчеты",lang), getLocalizedWord("Отчеты",lang), "reports")
		reports_outline.addEntry(new _OutlineEntry(getLocalizedWord("Задания",lang), getLocalizedWord("Задания",lang), "report_tasks", "Provider?type=page&id=report_tasks&page=0"))
		outline.addOutline(reports_outline)
		list.add(reports_outline)*/
		if (user.hasRole("administrator")){
			def glossary_outline = new _Outline(getLocalizedWord("Справочники",lang), getLocalizedWord("Справочники",lang), "glossary")
			glossary_outline.addEntry(new _OutlineEntry(getLocalizedWord("Тип контроля",lang), getLocalizedWord("Тип контроля",lang), "controltype", "Provider?type=page&id=controltype"))
			glossary_outline.addEntry(new _OutlineEntry(getLocalizedWord("Категория",lang), getLocalizedWord("Категория",lang), "docscat", "Provider?type=page&id=docscat&sortfield=VIEWTEXT1&order=ASC"))
			glossary_outline.addEntry(new _OutlineEntry(getLocalizedWord("Характер вопроса",lang), getLocalizedWord("Характер вопроса",lang), "har", "Provider?type=page&id=har"))
			glossary_outline.addEntry(new _OutlineEntry(getLocalizedWord("Тип документа",lang), getLocalizedWord("Тип документа",lang), "typedoc", "Provider?type=page&id=typedoc&sortfield=VIEWTEXT1&order=ASC"))
			glossary_outline.addEntry(new _OutlineEntry(getLocalizedWord("Вид доставки",lang), getLocalizedWord("Вид доставки",lang), "deliverytype", "Provider?type=page&id=deliverytype&sortfield=VIEWTEXT1&order=ASC"))
			glossary_outline.addEntry(new _OutlineEntry(getLocalizedWord("Категория граждан",lang), getLocalizedWord("Категория граждан",lang), "cat", "Provider?type=page&id=cat"))
			glossary_outline.addEntry(new _OutlineEntry(getLocalizedWord("Номенклатура дел",lang), getLocalizedWord("Номенклатура дел",lang), "nomentypelist", "Provider?type=page&id=nomentypelist&sortfield=VIEWTEXT1&order=ASC"))
			/*glossary_outline.addEntry(new _OutlineEntry(getLocalizedWord("Категория получателей",lang), getLocalizedWord("Категория получателей",lang), "corrcatlist", "Provider?type=page&id=corrcatlist&sortfield=VIEWTEXT1&order=ASC"))
			glossary_outline.addEntry(new _OutlineEntry(getLocalizedWord("Получатели",lang), getLocalizedWord("Получатели",lang), "corrlist", "Provider?type=page&id=corrlist&sortfield=VIEWTEXT1&order=ASC"))*/
			glossary_outline.addEntry(new _OutlineEntry(getLocalizedWord("Адресат",lang), getLocalizedWord("Адресат",lang), "addressee", "Provider?type=page&id=addressee&sortfield=VIEWTEXT1&order=ASC"))
			glossary_outline.addEntry(new _OutlineEntry(getLocalizedWord("Регион/Город",lang), getLocalizedWord("Регион/Город",lang), "city", "Provider?type=page&id=city"))
			glossary_outline.addEntry(new _OutlineEntry(getLocalizedWord("Проекты",lang), getLocalizedWord("Проекты",lang), "projectsprav", "Provider?type=page&id=projectsprav"))
			glossary_outline.addEntry(new _OutlineEntry(getLocalizedWord("Тип конечного документа",lang), getLocalizedWord("Тип конечного документа",lang), "finaldoctype", "Provider?type=page&id=finaldoctype"))
			glossary_outline.addEntry(new _OutlineEntry(getLocalizedWord("Язык документа",lang), getLocalizedWord("Язык документа",lang), "docslang", "Provider?type=page&id=docslang"))
			glossary_outline.addEntry(new _OutlineEntry(getLocalizedWord("Тип приказа",lang), getLocalizedWord("Тип приказа",lang), "ordtype", "Provider?type=page&id=ordtype"))
			glossary_outline.addEntry(new _OutlineEntry(getLocalizedWord("Тип договора",lang), getLocalizedWord("Тип договора",lang), "contracttype", "Provider?type=page&id=contracttype"))
			glossary_outline.addEntry(new _OutlineEntry(getLocalizedWord("Валюта",lang), getLocalizedWord("Валюта",lang), "currency", "Provider?type=page&id=currency"))
			outline.addOutline(glossary_outline)
			list.add(glossary_outline)
		}
		if (user.hasRole("administrator")) {
			def add_outline = new _Outline(getLocalizedWord("Прочее", lang), getLocalizedWord("Прочее", lang), "add")
			add_outline.addEntry(new _OutlineEntry(getLocalizedWord("Корзина", lang), getLocalizedWord("Корзина", lang), "recyclebin", "Provider?type=page&id=recyclebin"))
			outline.addOutline(add_outline)
			list.add(add_outline)
		}
		
		setContent(list)
	}

    def addEntryByGlossary(_Session session, def entry, formid, glossaryform, isproject){

        def projects = session.getCurrentDatabase().getGroupedEntries("$glossaryform#number", 1, 20);
        def cdb = session.getCurrentDatabase();
		def pageid ="docsbyglossary";
		if(isproject){
			pageid = "docsbyglossaryprj"
		} 
      projects.each{
            try{
                int docid = it.getViewText().toDouble().toInteger()
                def name = cdb.getGlossaryDocument(docid)?.getName();
                if(name != null && name != ""){
                    entry.addEntry(new _OutlineEntry(name, name, formid + it.getViewText(), "Provider?type=page&id=$pageid&glossaryform=$glossaryform&glossaryid=$docid&formid=$formid&page=0"));
                }
            }catch(Exception e){
				println(e)
			}
        }

        return entry;
    }
}
