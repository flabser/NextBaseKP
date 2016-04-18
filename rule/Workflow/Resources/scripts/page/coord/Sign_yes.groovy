package page.coord

import kz.nextbase.script._CrossLink
import kz.nextbase.script._Document
import kz.nextbase.script._Session
import kz.nextbase.script._WebFormData
import kz.nextbase.script.coordination._Block
import kz.nextbase.script.coordination._BlockCollection
import kz.nextbase.script.events._DoScript
import kz.nextbase.script.mail._Memo

class Sign_yes extends _DoScript {


	@Override
	public void doProcess(_Session session, _WebFormData formData, String lang) {
		try {
			devPrint(formData)


			def cdb = session.getCurrentDatabase();
			def doc = cdb.getDocumentByID(formData.getNumberValueSilently("key", -1));
			def mailAgent = session.getMailAgent();
			def msngAgent = session.getInstMessengerAgent();
			def recipientsMail = [];
			def recipientsID = [];
			String msubject;
			String subj;
			String body = "";
			String msg = "";

			def struct = session.getStructure()
			def author = struct?.getEmployer(doc.getAuthorID());
			def recipient_col_email;
			def recipient_col_jabber
			if (doc.getDocumentForm() == "officememoprj") {
				//recipient_col_email = ((_EmployerCollection) doc.getValueObject("recipient"))?.getEmployers().collectAll {it.getEmail()};
				//recipient_col_jabber = ((_EmployerCollection) doc.getValueObject("recipient"))?.getEmployers().collectAll {it.getInstMessengerAddr()};
			}

			def doc_blc = (_BlockCollection) doc.getValueObject("coordination")
			def block = (_Block) doc_blc.getSignBlock();
			def coordlist = block.getCurrentCoordinators();
			boolean finalblock = false;

			block.setBlockStatus(_BlockStatusType.COORDINATED);
			def signerlist = block.getCurrentCoordinators();
			for (signer in signerlist) {
				if (signer.getUserID() == session.getCurrentUserID()) {
					String comment = (formData.containsField("comment") ? formData.getValue("comment") : "")
					signer.setDecision(_DecisionType.AGREE, comment != null ? comment : "")
				}
			}
			doc_blc.setCoordStatus(_CoordStatusType.SIGNED)
			doc.replaceViewText(doc_blc.getStatus().name(), 3)


			def _doc = new _Document(cdb);
			def signerCoord = block.getFirstCoordinator();
			if (doc.getDocumentForm() == "officememoprj") {
				_doc.setForm("workdoc")
				_doc.addStringField("vn", doc.getValueString("vn"))
				//_doc.addStringField("author",doc.getValueString("author"))
				_doc.addDateField("dvn", new Date())
				_doc.addStringField("corr", signerCoord?.getUserID())
				_doc.addStringField("signer", signerCoord?.getUserID())
				// _doc.addNumberField("finaldoctype", doc.getValueNumber("finaldoctype"));
				/*  def recipients = (_EmployerCollection) doc.getValueObject("recipient")
				 recipients.getEmployers().each { r ->
				 if (r) {
				 doc.addReader(r.getUserID());
				 }
				 }
				 _doc.addField("recipient", recipients)*/
				int num = cdb.getRegNumber("workdoc");
				_doc.addStringField("vn", num.toString());
				_doc.addNumberField("vnnumber", num);
				_doc.setViewText("Служебная записка № " + _doc.getValueString("vn") + " " + _Helper.getDateAsStringShort(_doc.getValueDate("dvn")) + "  " + session.getStructure()?.getEmployer(doc.getAuthorID())?.getShortName() + " " + doc.getValueString("briefcontent"));
				_doc.addDateField("ctrldate", session.getDatePlusDays(30))
				_doc.setViewNumber(num)
			}
			if (doc.getDocumentForm() == "applicationprj") {
				_doc.setForm("application")
				_doc.addStringField("vn", doc.getValueString("vn"))
				//_doc.addStringField("author",doc.getValueString("author"))
				_doc.addDateField("dvn", new Date())
				_doc.addStringField("corr", signerCoord?.getUserID())
				_doc.addStringField("signer", signerCoord?.getUserID())
				// _doc.addNumberField("finaldoctype", doc.getValueNumber("finaldoctype"));
				/*  def recipients = (_EmployerCollection) doc.getValueObject("recipient")
				 recipients.getEmployers().each { r ->
				 if (r) {
				 doc.addReader(r.getUserID());
				 }
				 }
				 _doc.addField("recipient", recipients)*/
				int num = cdb.getRegNumber("application");
				_doc.addStringField("vn", num.toString());
				_doc.addNumberField("vnnumber", num);
				_doc.setViewText("Заявление № " + _doc.getValueString("vn") + " " + _Helper.getDateAsStringShort(_doc.getValueDate("dvn")) + "  " + session.getStructure()?.getEmployer(doc.getAuthorID())?.getShortName() + " " + doc.getValueString("briefcontent"));
				_doc.addDateField("ctrldate", session.getDatePlusDays(30))
				_doc.setViewNumber(num)
			}
			if (doc.getDocumentForm() == "orderprj") {
				_doc.setForm("order")
				_doc.addStringField("signed", signerCoord?.getUserID())
				_doc.addStringField("prepared", doc.getAuthorID())
				/*def recipients = (_EmployerCollection) doc.getValueObject("recipient")
				 recipients.getEmployers().each { r ->
				 if (r) {
				 _doc.addReader(r.getUserID());
				 }
				 }
				 _doc.addField("recipients", recipients)*/
				_doc.addStringField("in", doc.getValueString("vn"))
				_doc.addDateField("dvn", new Date())
				_doc.addStringField("vn", "")
				_doc.setViewText("Приказ №" + _doc.getValueString("vn") + " " + _Helper.getDateAsStringShort(_doc.getValueDate("dvn")) + "  " + session.getStructure()?.getEmployer(doc.getAuthorID())?.getShortName() + " " + doc.getValueString("briefcontent"));
				_doc.addNumberField("ordtype", doc.getValueNumber("ordtype"))
				_doc.setViewNumber(_doc.getValueString("vn").isNumber() ? _doc.getValueString("vn").toInteger() : 0)
			}

			if (doc.getDocumentForm() == "contractprj") {
				_doc.setForm("contract")
				_doc.addStringField("signedby", signerCoord?.getUserID())
				_doc.addStringField("intexec", doc.getAuthorID())
				_doc.addStringField("initemp", doc.getAuthorID())
				_doc.addStringField("corrstring", doc.getValueString("corrstring"))
				def emp = session.getStructure().getEmployer(doc.getAuthorID()) ;
				_doc.addNumberField("initdivision", emp.getDepartmentID())
				_doc.addStringField("in", doc.getValueString("vn"))
				/*def recipients = (_EmployerCollection) doc.getValueObject("recipient")
				 recipients.getEmployers().each { r ->
				 if (r) {
				 doc.addReader(r.getUserID());
				 }
				 }*/
				_doc.addDateField("dvn", new Date())
				_doc.addStringField("vn", "")
				_doc.setViewText("Договор №" + _doc.getValueString("vn") + " " + _Helper.getDateAsStringShort(_doc.getValueDate("dvn")) + "  " + session.getStructure()?.getEmployer(doc.getAuthorID())?.getShortName() + " " + doc.getValueString("briefcontent"));
				_doc.addNumberField("contracttype", doc.getValueNumber("contracttype"))
				_doc.addNumberField("contractor", doc.getValueNumber("contractor"))
				_doc.setViewNumber(_doc.getValueString("vn").isNumber() ? _doc.getValueString("vn").toInteger() : 0)
			}
			if(doc.getDocumentForm() == "outgoingprj"){
				_doc.setForm("out")
				_doc.addStringField("signedby", signerCoord?.getUserID())
				_doc.addStringField("intexec", doc.getAuthorID())
				_doc.addStringField("in", doc.getValueString("vn"))
				_doc.addStringField("corrstring", doc.getValueString("corrstring"))
				_doc.addStringField("addresscorr", doc.getValueString("addresscorr"))
				_doc.addDateField("din", new Date())
				_doc.addStringField("vn", "")
				_doc.setViewText("Исходящий документ №" + _doc.getValueString("in") + " " + _Helper.getDateAsStringShort(_doc.getValueDate("din")) + "  " + session.getStructure()?.getEmployer(doc.getAuthorID())?.getShortName() + " " + doc.getValueString("briefcontent"));
				_doc.addNumberField("nomentype", doc.getValueNumber("nomentype"))
				_doc.addNumberField("deliverytype", doc.getValueNumber("deliverytype"))
				_doc.addNumberField("vid", 0)
				_doc.addNumberField("corr", doc.getValueNumber("corr"))
				_doc.setViewNumber(_doc.getValueString("in").isNumber() ? _doc.getValueString("in").toInteger() : 0)
				_doc.addReader("[chancellery]");
			}
			doc.getReaders().each {
				_doc.addReader(it.userID);
			}
			doc.copyAttachments(_doc);
			_doc.setRichText("contentsource", doc.getValueString("contentsource"))
			//_doc.addStringField("author", doc.getValueString("author"))
			_doc.setAuthor(doc.authorID)
			_doc.addStringField("briefcontent", doc.getValueString("briefcontent"))
			_doc.addNumberField("projectdocid", doc.getDocID())
			_doc.setParentDoc(doc)
			_doc.setViewDate(new Date())
			_doc.addField("link", new _CrossLink(session, doc))
			_doc.addEditor("[registrator_outgoing]");
			_doc.addEditor(doc.authorID);
			_doc.save("[supervisor]")
			_doc.replaceViewText(signerCoord?.getUserID(), 7)
			_doc.getViewText()
			_doc.save("[supervisor]")
			doc.addField("link", new _CrossLink(session, _doc))
			doc.setValueNumber("regdocid", _doc.getDocID())
			def cBlock = doc_blc.getCurrentBlock()
			if (cBlock) {
				doc.replaceViewText(cBlock.getCurrentCoordinatorsAsText(), 5)
			}
			doc.save("[supervisor]")
			try {
				msg = "Документ \"" + doc.getValueString("briefcontent") + "\" готов к отправке на регистрацию. Пожалуйста, проверьте правильность составления документа и все ли участники согласования ";
				msg += "одобрили документ. \nДля работы с документом перейдите по ссылке " + _doc.getFullURL();
				msngAgent.sendMessage([
					author?.getInstMessengerAddr()
				], msg);
				subj = ' Проект для отправки на регистрацию \"' + doc.getValueString("briefcontent") + '\"';
				body += 'Проект документа подписан и готов к отправке на регистрацию. Проверьте правильность составления документа ';
				body += 'и все ли участники согласования одобрили документ.';
				mailAgent.sendMail([author?.getEmail()], new _Memo(subj, 'Согласование завершено', body, doc, false));

				msg = "Вам новая служебная записка № " + doc.getValueString("vn") + " - " + doc.getValueString("briefcontent") + ".";
				msg += "\nДля работы с документом перейдите по этой ссылке: " + _doc.getFullURL() + "</a>";
				if (!recipient_col_jabber.empty){
					msngAgent.sendMessage(recipient_col_jabber, msg);
				}
				subj = "Уведомление о новой служебной записке";
				body = "Вам новая служебная записка № " + doc.getValueString("vn") + " - " + doc.getValueString("briefcontent") + ".";
				body += "\nДля работы с документом перейдите по этой <a href=\"" + _doc.getFullURL() + "\">ссылке...</a>";
				if (!recipient_col_email.empty){
					mailAgent.sendMail(recipient_col_email, subj, body);
				}

			} catch (Exception e) {
				log(e)
			}

			if (recipient_col_jabber != null){
				msg = "Вам новая служебная записка № " + doc.getValueString("vn") + " - " + doc.getValueString("briefcontent") + ".";
				msg += "\nДля работы с документом перейдите по этой ссылке: " + _doc.getFullURL() + "</a>";
				msngAgent.sendMessage(recipient_col_jabber, msg);
				subj = "Уведомление о новой служебной записке";
				body = "Вам новая служебная записка № " + doc.getValueString("vn") + " - " + doc.getValueString("briefcontent") + ".";
				body += "\nДля работы с документом перейдите по этой <a href=\"" + _doc.getFullURL() + "\">ссылке...</a>";
				mailAgent.sendMail(recipient_col_email, subj, body);
			}

			//def returnURL = session.getURLOfLastPage()
			def returnURL = _doc.getFullURL();
			setRedirectURL(returnURL)
			/*msg = "После исполнения документ № " + doc.getValueString("vn") + " от " + _Helper.getDateAsString(doc.getValueDate("projectdate")) + "\" будет отправлен ";
			 msg += (doc.getDocumentForm() == "workdocprj" ? "получателю." : "на ревизию исполнения.") + "\nДля ознакомления с документом перейдите по <a href=\"" + _doc.getFullURL() + "\">ссылке...</a>";
			 msngAgent.sendMessageAfter([author?.getInstMessengerAddr()], msg);
			 >>>>>>> refs/remotes/origin/master
			 } catch (Exception e) {
			 log(e)
			 }
			 def returnURL = session.getURLOfLastPage()
			 setRedirectURL(returnURL)
			 /*msg = "После исполнения документ № " + doc.getValueString("vn") + " от " + _Helper.getDateAsString(doc.getValueDate("projectdate")) + "\" будет отправлен ";
			 msg += (doc.getDocumentForm() == "workdocprj" ? "получателю." : "на ревизию исполнения.") + "\nДля ознакомления с документом перейдите по <a href=\"" + _doc.getFullURL() + "\">ссылке...</a>";
			 msngAgent.sendMessageAfter([author?.getInstMessengerAddr()], msg);
			 subj = " Исполнено замечание № " + doc.getValueString("vn") + " от " + _Helper.getDateAsString(doc.getValueDate("projectdate"));
			 body = "После исполнения документ №" + doc.getValueString("vn") + " от " + _Helper.getDateAsString(doc.getValueDate("projectdate")) + " будет отправлен ";
			 body += (doc.getDocumentForm() == "workdocprj" ? " получателю. " : " на ревизию исполнения.");
			 mailAgent.sendMailAfter([author?.getEmail()], new _Memo(subj, 'Документ исполнен', body, _doc, false));*/
		} catch (Exception e) {
			e.getStackTrace().each {
				log(it.toString());
			}
		}
	}
}
