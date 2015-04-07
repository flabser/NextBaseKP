package form.out
import kz.nextbase.script._Document
import kz.nextbase.script._Helper
import kz.nextbase.script._Session
import kz.nextbase.script._WebFormData
import kz.nextbase.script.events._FormQuerySave

class QuerySave extends _FormQuerySave {

	@Override
	public void doQuerySave(_Session session, _Document doc, _WebFormData webFormData, String lang) {
		
		println(webFormData)
		
		boolean v = validate(webFormData)
		if(v == false){
			stopSave()
			return
		}
			
		Date tDate = null
					
		doc.form = "out"			
		doc.addStringField("briefcontent", webFormData.getValue("briefcontent"))
		doc.addStringField("signedby", webFormData.getValue("signedby"))
		doc.addStringField("corrstring", webFormData.getValue("corrstring"))
		doc.addStringField("addresscorr", webFormData.getValue("addresscorr"))

        doc.replaceListField("corr", webFormData.getListOfValuesSilently("corr") as List<String>)

		doc.addNumberField("deliverytype", webFormData.getNumberValueSilently("deliverytype",0))
		doc.addNumberField("vid", webFormData.getNumberValueSilently("vid",0))
		doc.addNumberField("lang", webFormData.getNumberValueSilently("lang",0))
		doc.addNumberField("blanknumber", webFormData.getNumberValueSilently("blanknumber",0))
		doc.addNumberField("np", webFormData.getNumberValueSilently("np",1))
		doc.addNumberField("np2", webFormData.getNumberValueSilently("np2",1))
		doc.setRichText("contentsource", webFormData.getValue("contentsource"))
		doc.addNumberField("nomentype", webFormData.getValue("nomentype"))
		doc.addFile("rtfcontent", webFormData)

        Date dates = new Date()

		def redirectURL = session.getURLOfLastPage()
		
		if (doc.isNewDoc || !doc.getValueString("vn")){
			def db = session.getCurrentDatabase()
            int num = db.getRegNumber("out_" + webFormData.getValueSilently("project"))
			String vnAsText = Integer.toString(num)
			doc.replaceStringField("vn", vnAsText)
			doc.replaceIntField("vnnumber",num)
			doc.replaceDateField("dvn", new Date())
			localizedMsgBox(getLocalizedWord("Документ зарегистрирован под № ",lang) + vnAsText);
			redirectURL.changeParameter("page", "0")
		//	doc.addStringField("mailnotification", "")
			doc.setViewNumber(num)
		}
		doc.setViewText(getLocalizedWord('Исходящий документ',lang) + ' № ' + doc.getValueString('vnnumber') + ' ' + getLocalizedWord('от',lang) + ' ' + dates.format("dd.MM.yyyy HH.mm.ss") + ' ' + session.getStructure().getEmployer(doc.getValueString('author')).shortName + ' : ' + doc.getValueString('briefcontent'))
		doc.addViewText(doc.getValueString('briefcontent'))
		doc.addViewText(webFormData.getValueSilently("corrstring"))
		doc.addViewText('')
		doc.addViewText('')
		doc.addViewText('')
		doc.addViewText('')
		doc.addViewText(doc.getValueString("signedby"))
		doc.setViewDate(_Helper.convertStringToDate(doc.getValueString("dvn")))
		setRedirectURL(redirectURL)
			 
	}
	
	def validate(_WebFormData webFormData){
		
		if (webFormData.getValueSilently("deliverytype") == ""){
			localizedMsgBox("Поле \"Вид доставки\" не заполнено.");
			return false;
		}
		
		if (webFormData.getValueSilently("signedby") == ""){
			localizedMsgBox("Поле \"Кто подписал\" не выбрано.");
			return false;
		}
		
		if (webFormData.getValueSilently("briefcontent") == ""){
			localizedMsgBox("Поле \"Краткое содержание\" не заполнено.");
			return false;
		}
		if (webFormData.getValueSilently('briefcontent').length() > 2046){
			localizedMsgBox('Поле \'Краткое содержание\' содержит значение превышающее 2046 символов');
			return false;
		}
		if (webFormData.getValueSilently("corrstring") == ""){
			localizedMsgBox("Поле \"Получатель\" не заполнено.");
			return false;
		}
		if (webFormData.getValueSilently("addresscorr") == ""){
			localizedMsgBox("Поле \"Адрес получателя\" не заполнено.");
			return false;
		}
		if (webFormData.getValueSilently("nomentype") == ""){
			localizedMsgBox("Поле \"Номенклатура дел\" не выбрано")
			return false
		}
		
		return true;
		
	}
}
