package form.workdocdept

import kz.nextbase.script._Document
import kz.nextbase.script._Helper
import kz.nextbase.script._Session
import kz.nextbase.script._WebFormData
import kz.nextbase.script.events._FormQuerySave

class QuerySave extends _FormQuerySave {

	@Override
	public void doQuerySave(_Session session, _Document doc,_WebFormData webFormData, String lang) {
		println(webFormData)

		def action = webFormData.getValueSilently('action')


		doc.setForm("workdocdept")
		doc.addStringField("briefcontent", webFormData.getValue("briefcontent"))
		doc.addStringField("ctrldate", webFormData.getValue("ctrldate"))
		doc.addStringField("recipient", webFormData.getValue("recipient"))
		doc.addStringField("signer", webFormData.getValue("signer"))
		doc.addFile("rtfcontent", webFormData)
		doc.setRichText("contentsource", webFormData.getValueSilently("contentsource"))

		def struct = session.getStructure()
		String authorRus = ""
		def author = struct.getEmployer(doc.getValueString("author"))
		if (author){
			authorRus = author.getShortName()
		}
		doc.addReader(webFormData.getValue("recipient"));
		def vt
		Date dDate = new Date()
		def returnURL = session.getURLOfLastPage()
		def db = session.getCurrentDatabase()


		if (doc.getViewNumber()== -1){
			int num = db.getRegNumber("workdocdept")
			String vnAsText = Integer.toString(num)
			doc.addStringField("vn",vnAsText)
			doc.setViewNumber(num)
			returnURL.changeParameter("page", "0");
			localizedMsgBox("Документ № "+ doc.getValueString("vn") +" сохранен")
		}else{
			dDate = doc.getRegDate()
		}
		vt = 'Проект служебной записки между департаментами № ' + doc.getValueString("vn") + ' ' + _Helper.getDateAsStringShort(dDate) + ' ' + author.getShortName() + ' ' + doc.getValueString('briefcontent')

		doc.setViewDate(dDate)
		doc.setViewText(vt)
		doc.addViewText(doc.getValueString('briefcontent'))
		//doc.addViewText(recipient.getEmployersAsText())
		doc.addViewText('')
		setRedirectURL(returnURL)
	}

	def validate(_WebFormData webFormData, String action){

		if (webFormData.getValueSilently("briefcontent") == ""){
			localizedMsgBox("Поле \"Краткое содержание\" не заполнено.")
			return false
		}
		if (webFormData.getValueSilently("recipient") == ""){
			localizedMsgBox("Поле \"Получатель\" не заполнено.")
			return false
		}
	}

	def validateprj(_WebFormData webFormData){
		if (webFormData.getValueSilently("briefcontent") == ""){
			localizedMsgBox("Поле \"Краткое содержание\" не заполнено.")
			return false
		}
		if (webFormData.getValueSilently("recipient") == ""){
			localizedMsgBox("Поле \"Получатель\" не заполнено.")
			return false
		}
		if (webFormData.getValueSilently('briefcontent').length() > 2046){
			localizedMsgBox('Поле \'Краткое содержание\' содержит значение превышающее 2046 символов');
			return false;
		}
		/*if (webFormData.getValueSilently('contentsource').length() > 2046){
			localizedMsgBox('Поле \'Содержание\' содержит значение превышающее 2046 символов');
			return false;
		}*/

	}
		


	
}
