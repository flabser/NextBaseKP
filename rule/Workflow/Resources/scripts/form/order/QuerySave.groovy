package form.order
import kz.nextbase.script._Document
import kz.nextbase.script._Helper
import kz.nextbase.script._Session
import kz.nextbase.script._WebFormData
import kz.nextbase.script.events._FormQuerySave
import kz.nextbase.script.struct._EmployerCollection

class QuerySave extends _FormQuerySave {

	@Override
	public void doQuerySave(_Session session, _Document doc, _WebFormData webFormData, String lang) {

		println(webFormData)
		
		boolean v = validate(webFormData)
		if(v == false){
			stopSave()
			return
		}

		doc.setForm("order");
		doc.addStringField("sign", webFormData.getValueSilently("sign"))
		doc.addStringField("signedfields", webFormData.getValueSilently("signedfields"))
		doc.setRichText("contentsource", webFormData.getValueSilently("contentsource"))
		doc.addStringField("vn", webFormData.getValueSilently("vn"))
		String dvn = webFormData.getValue("dvn")
		if(dvn != "") doc.addDateField("dvn", _Helper.convertStringToDate(dvn))
		doc.addStringField("outnum", webFormData.getValueSilently("outnum"))
		String outdate = webFormData.getValueSilently("outdate")
		if(outdate != "") doc.addDateField("outdate", _Helper.convertStringToDate(outdate))
		//def recipients = webFormData.getListOfValuesSilently("recipient")
		//doc.replaceListField("recipients", recipients as ArrayList);
		/*def recipient = session.createEmployerCollection(webFormData.getListOfValuesSilently("recipient"))
		doc.addField("recipients",recipient)*/
		def signed = webFormData.getListOfValuesSilently("signed")
		doc.replaceListField("signed", signed as ArrayList);
		def prepared = webFormData.getListOfValuesSilently("prepared");
		doc.replaceListField("prepared", prepared as ArrayList);
		def orderexecuters = webFormData.getListOfValuesSilently("orderexecuters");
		doc.replaceListField("orderexecuters", orderexecuters as ArrayList);
		def agreed = webFormData.getListOfValuesSilently("agreed");
		//doc.replaceListField("agreed", agreed as ArrayList);
		doc.addStringField("author", webFormData.getValue("author"))
		doc.addStringField("briefcontent", webFormData.getValue("briefcontent"))
		
		
		doc.addNumberField("ordtype", webFormData.getNumberValueSilently("ordtype",0))
		doc.addFile("rtfcontent", webFormData)
		doc.addStringField("np", webFormData.getValue("np"))
		doc.addStringField("nc", webFormData.getValue("nc"))

		
		

		/*doc.addReader(webFormData.getListOfValuesSilently("recipient") as HashSet<String>)*/
		doc.addReader(webFormData.getListOfValuesSilently("signed") as HashSet<String>)
		doc.addReader(webFormData.getListOfValuesSilently("prepared") as HashSet<String>)
		doc.addReader(webFormData.getListOfValuesSilently("orderexecuters") as HashSet<String>)
		doc.addReader(webFormData.getListOfValuesSilently("agreed") as HashSet<String>)
		def returnURL = session.getURLOfLastPage()
        Date tDate = new Date()
        if (doc.isNewDoc || !doc.getValueString("vn")){
            def db = session.getCurrentDatabase()
            int num = db.getRegNumber('ord_' + doc.getValueString("ordtype"))
            String vnAsText = Integer.toString(num)
            doc.addStringField("mailnotification", "")
            doc.replaceStringField("vn", vnAsText)
            doc.replaceIntField("vnnumber",num)
            doc.replaceDateField("dvn", new Date())
			localizedMsgBox(getLocalizedWord("Документ зарегистрирован под № ",lang) + vnAsText)
			returnURL.changeParameter("page", "0");
		}

        doc.setViewText(getLocalizedWord('Приказ',lang) +' № '+ doc.getValueString('vnnumber') + '  ' + getLocalizedWord('от',lang) + ' ' + tDate.format("dd.MM.yyyy HH:mm:ss") + ' '+ session.getStructure().getEmployer(doc.getValueString('author')).shortName + ' : '+doc.getValueString('briefcontent'))
        doc.addViewText(doc.getValueString('briefcontent'))
        doc.addViewText(doc.getValueString("vn"))
		doc.addViewText('')
		doc.addViewText('')
		doc.addViewText('')
		doc.addViewText('')
		doc.addViewText(doc.getValueString("signed"))
		doc.setViewNumber(doc.getValueNumber("vnnumber"))
		doc.setViewDate(doc.getValueDate("dvn"))
		setRedirectURL(returnURL)
	}

	def validate(_WebFormData webFormData){

		if (webFormData.getValueSilently("briefcontent") == ""){
			localizedMsgBox("Поле \"Краткое содержание\" не заполнено.")
			return false
		}
		if (webFormData.getValueSilently("contentsource") == ""){
			localizedMsgBox("Поле \"Cодержание\" не заполнено.")
			return false
		}
		if (webFormData.getValueSilently('briefcontent').length() > 2046){
			localizedMsgBox('Поле \'Краткое содержание\' содержит значение превышающее 2046 символов');
			return false;
		}
		if (webFormData.getValueSilently("ordtype") == ""){
			localizedMsgBox("Поле \"Тип приказа\" не указано.")
			return false
		}

		/*if (webFormData.getValueSilently("recipient") == ""){
			localizedMsgBox("Поле \"Кому адресован\" не выбрано.")
			return false
		}*/

		return true
	}
}
