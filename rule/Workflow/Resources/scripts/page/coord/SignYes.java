package page.coord;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import kz.flabs.users.Reader;
import kz.nextbase.script._CrossLink;
import kz.nextbase.script._Database;
import kz.nextbase.script._Document;
import kz.nextbase.script._Helper;
import kz.nextbase.script._Session;
import kz.nextbase.script._URL;
import kz.nextbase.script._WebFormData;
import kz.nextbase.script.constants._BlockStatusType;
import kz.nextbase.script.constants._CoordStatusType;
import kz.nextbase.script.constants._DecisionType;
import kz.nextbase.script.coordination._Block;
import kz.nextbase.script.coordination._BlockCollection;
import kz.nextbase.script.coordination._Coordinator;
import kz.nextbase.script.events._DoScript;
import kz.nextbase.script.mail._InstMessengerAgent;
import kz.nextbase.script.mail._MailAgent;
import kz.nextbase.script.mail._Memo;
import kz.nextbase.script.struct._Employer;
import kz.nextbase.script.struct._Structure;

public class SignYes extends _DoScript {

	@Override
	public void doProcess(_Session session, _WebFormData formData, String lang) {
		try {
			// println(formData)

			_Database cdb = session.getCurrentDatabase();
			_Document doc = cdb.getDocumentByID(formData.getNumberValueSilently("key", -1));
			String docForm = doc.getDocumentForm();
			_MailAgent mailAgent = session.getMailAgent();
			_InstMessengerAgent msngAgent = session.getInstMessengerAgent();
			List recipientsMail = new ArrayList();
			List recipientsID = new ArrayList();
			String msubject;
			String subj;
			String body = "";
			String msg = "";

			_Structure struct = session.getStructure();
			_Employer author = struct.getEmployer(doc.getAuthorID());
			List recipient_col_email = null;
			List recipient_col_jabber = null;
			//if(docForm.equals("officememoprj")) {
				// recipient_col_email = ((_EmployerCollection)
				// doc.getValueObject("recipient"))?.getEmployers().collectAll
				// {it.getEmail()};
				// recipient_col_jabber = ((_EmployerCollection)
				// doc.getValueObject("recipient"))?.getEmployers().collectAll
				// {it.getInstMessengerAddr()};
			//}

			_BlockCollection doc_blc = (_BlockCollection) doc.getValueObject("coordination");
			_Block block = doc_blc.getSignBlock();
			ArrayList<_Coordinator> coordlist = block.getCurrentCoordinators();
			boolean finalblock = false;

			block.setBlockStatus(_BlockStatusType.COORDINATED);
			ArrayList<_Coordinator> signerlist = block.getCurrentCoordinators();
			for (_Coordinator signer : signerlist) {
				if (signer.getUserID() == session.getCurrentUserID()) {
					String comment = (formData.containsField("comment") ? formData.getValue("comment") : "");
					signer.setDecision(_DecisionType.AGREE, comment != null ? comment : "");
				}
			}
			doc_blc.setCoordStatus(_CoordStatusType.SIGNED);
			doc.replaceViewText(doc_blc.getStatus().name(), 3);

			_Document _doc = new _Document(cdb);
			_Coordinator signerCoord = block.getFirstCoordinator();
			if (docForm.equals("officememoprj")) {
				_doc.setForm("workdoc");
				_doc.addStringField("vn", doc.getValueString("vn"));
				// _doc.addStringField("author",doc.getValueString("author"))
				_doc.addDateField("dvn", new Date());
				_doc.addStringField("corr", signerCoord.getUserID());
				_doc.addStringField("signer", signerCoord.getUserID());
				// _doc.addNumberField("finaldoctype",
				// doc.getValueNumber("finaldoctype"));
				/*
				 * def recipients = (_EmployerCollection)
				 * doc.getValueObject("recipient")
				 * recipients.getEmployers().each { r -> if (r) {
				 * doc.addReader(r.getUserID()); } } _doc.addField("recipient",
				 * recipients)
				 */
				int num = cdb.getRegNumber("workdoc");
				_doc.addStringField("vn", Integer.toString(num));
				_doc.addNumberField("vnnumber", num);
				_doc.setViewText("Служебная записка № " + _doc.getValueString("vn") + " " + _Helper.getDateAsStringShort(_doc.getValueDate("dvn"))
				        + "  " + session.getStructure().getEmployer(doc.getAuthorID()).getShortName() + " " + doc.getValueString("briefcontent"));
				_doc.addDateField("ctrldate", session.getDatePlusDays(30));
				_doc.setViewNumber(BigDecimal.valueOf(num));
			}
			if (docForm.equals("applicationprj")) {
				_doc.setForm("application");
				_doc.addStringField("vn", doc.getValueString("vn"));
				// _doc.addStringField("author",doc.getValueString("author"))
				_doc.addDateField("dvn", new Date());
				_doc.addStringField("corr", signerCoord.getUserID());
				_doc.addStringField("signer", signerCoord.getUserID());
				// _doc.addNumberField("finaldoctype",
				// doc.getValueNumber("finaldoctype"));
				/*
				 * def recipients = (_EmployerCollection)
				 * doc.getValueObject("recipient")
				 * recipients.getEmployers().each { r -> if (r) {
				 * doc.addReader(r.getUserID()); } } _doc.addField("recipient",
				 * recipients)
				 */
				int num = cdb.getRegNumber("application");
				_doc.addStringField("vn", Integer.toString(num));
				_doc.addNumberField("vnnumber", num);
				_doc.setViewText("Заявление № " + _doc.getValueString("vn") + " " + _Helper.getDateAsStringShort(_doc.getValueDate("dvn")) + "  "
				        + session.getStructure().getEmployer(doc.getAuthorID()).getShortName() + " " + doc.getValueString("briefcontent"));
				_doc.addDateField("ctrldate", session.getDatePlusDays(30));
				_doc.setViewNumber(BigDecimal.valueOf(num));
			}
			if (docForm.equals("orderprj")) {
				_doc.setForm("order");
				_doc.addStringField("signed", signerCoord.getUserID());
				_doc.addStringField("prepared", doc.getAuthorID());
				/*
				 * def recipients = (_EmployerCollection)
				 * doc.getValueObject("recipient")
				 * recipients.getEmployers().each { r -> if (r) {
				 * _doc.addReader(r.getUserID()); } }
				 * _doc.addField("recipients", recipients)
				 */
				_doc.addStringField("in", doc.getValueString("vn"));
				_doc.addDateField("dvn", new Date());
				_doc.addStringField("vn", "");
				_doc.setViewText("Приказ №" + _doc.getValueString("vn") + " " + _Helper.getDateAsStringShort(_doc.getValueDate("dvn")) + "  "
				        + session.getStructure().getEmployer(doc.getAuthorID()).getShortName() + " " + doc.getValueString("briefcontent"));
				_doc.addNumberField("ordtype", doc.getValueNumber("ordtype"));
				try {
					int intVal = Integer.parseInt(_doc.getValueString("vn"));
					_doc.setViewNumber(BigDecimal.valueOf(intVal));
				} catch (NumberFormatException e) {
					_doc.setViewNumber(BigDecimal.valueOf(0));
				}
			}

			if (docForm.equals("contractprj")) {
				_doc.setForm("contract");
				_doc.addStringField("signedby", signerCoord.getUserID());
				_doc.addStringField("intexec", doc.getAuthorID());
				_doc.addStringField("initemp", doc.getAuthorID());
				_doc.addStringField("corrstring", doc.getValueString("corrstring"));
				_Employer emp = session.getStructure().getEmployer(doc.getAuthorID());
				_doc.addNumberField("initdivision", emp.getDepartmentID());
				_doc.addStringField("in", doc.getValueString("vn"));
				/*
				 * def recipients = (_EmployerCollection)
				 * doc.getValueObject("recipient")
				 * recipients.getEmployers().each { r -> if (r) {
				 * doc.addReader(r.getUserID()); } }
				 */
				_doc.addDateField("dvn", new Date());
				_doc.addStringField("vn", "");
				_doc.setViewText("Договор №" + _doc.getValueString("vn") + " " + _Helper.getDateAsStringShort(_doc.getValueDate("dvn")) + "  "
				        + session.getStructure().getEmployer(doc.getAuthorID()).getShortName() + " " + doc.getValueString("briefcontent"));
				_doc.addNumberField("contracttype", doc.getValueNumber("contracttype"));
				_doc.addNumberField("contractor", doc.getValueNumber("contractor"));
				try {
					int intVal = Integer.parseInt(_doc.getValueString("vn"));
					_doc.setViewNumber(BigDecimal.valueOf(intVal));
				} catch (NumberFormatException e) {
					_doc.setViewNumber(BigDecimal.valueOf(0));
				}
			}
			if (docForm.equals("outgoingprj")) {
				_doc.setForm("out");
				_doc.addStringField("signedby", signerCoord.getUserID());
				_doc.addStringField("intexec", doc.getAuthorID());
				_doc.addStringField("in", doc.getValueString("vn"));
				_doc.addStringField("corrstring", doc.getValueString("corrstring"));
				_doc.addStringField("addresscorr", doc.getValueString("addresscorr"));
				_doc.addDateField("din", new Date());
				_doc.addStringField("vn", "");
				_doc.setViewText("Исходящий документ №" + _doc.getValueString("in") + " " + _Helper.getDateAsStringShort(_doc.getValueDate("din"))
				        + "  " + session.getStructure().getEmployer(doc.getAuthorID()).getShortName() + " " + doc.getValueString("briefcontent"));
				_doc.addNumberField("nomentype", doc.getValueNumber("nomentype"));
				_doc.addNumberField("deliverytype", doc.getValueNumber("deliverytype"));
				_doc.addNumberField("vid", 0);
				_doc.addNumberField("corr", doc.getValueNumber("corr"));
				try {
					int intVal = Integer.parseInt(_doc.getValueString("in"));
					_doc.setViewNumber(BigDecimal.valueOf(intVal));
				} catch (NumberFormatException e) {
					_doc.setViewNumber(BigDecimal.valueOf(0));
				}
				_doc.addReader("[chancellery]");
			}
			for (Reader r : doc.getReaders()) {
				_doc.addReader(r.getUserID());
			}
			doc.copyAttachments(_doc);
			_doc.setRichText("contentsource", doc.getValueString("contentsource"));
			// _doc.addStringField("author", doc.getValueString("author"))
			_doc.setAuthor(doc.getAuthorID());
			_doc.addStringField("briefcontent", doc.getValueString("briefcontent"));
			_doc.addNumberField("projectdocid", doc.getDocID());
			_doc.setParentDoc(doc);
			_doc.setViewDate(new Date());
			_doc.addField("link", new _CrossLink(session, doc));
			_doc.addEditor("[registrator_outgoing]");
			_doc.addEditor(doc.getAuthorID());
			_doc.save("[supervisor]");
			_doc.replaceViewText(signerCoord.getUserID(), 7);
			_doc.getViewText();
			_doc.save("[supervisor]");
			doc.addField("link", new _CrossLink(session, _doc));
			doc.setValueNumber("regdocid", _doc.getDocID());
			_Block cBlock = doc_blc.getCurrentBlock();
			if (cBlock != null) {
				doc.replaceViewText(cBlock.getCurrentCoordinatorsAsText(), 5);
			}
			doc.save("[supervisor]");
			try {
				msg = "Документ \"" + doc.getValueString("briefcontent")
				        + "\" готов к отправке на регистрацию. Пожалуйста, проверьте правильность составления документа и все ли участники согласования ";
				msg += "одобрили документ. \nДля работы с документом перейдите по ссылке " + _doc.getFullURL();
				msngAgent.sendMessage(new ArrayList(Arrays.asList(author.getInstMessengerAddr())), msg);
				subj = " Проект для отправки на регистрацию \"" + doc.getValueString("briefcontent") + "\"";
				body += "Проект документа подписан и готов к отправке на регистрацию. Проверьте правильность составления документа ";
				body += "и все ли участники согласования одобрили документ.";
				mailAgent.sendMail(new ArrayList(Arrays.asList(author.getEmail())), new _Memo(subj, "Согласование завершено", body, doc, false));

				if (recipient_col_jabber != null) {
					msg = "Вам новая служебная записка № " + doc.getValueString("vn") + " - " + doc.getValueString("briefcontent") + ".";
					msg += "\nДля работы с документом перейдите по этой ссылке: " + _doc.getFullURL() + "</a>";
					msngAgent.sendMessage(recipient_col_jabber, msg);
				}

				if (recipient_col_email != null) {
					subj = "Уведомление о новой служебной записке";
					body = "Вам новая служебная записка № " + doc.getValueString("vn") + " - " + doc.getValueString("briefcontent") + ".";
					body += "\nДля работы с документом перейдите по этой <a href=\"" + _doc.getFullURL() + "\">ссылке...</a>";
					mailAgent.sendMail(recipient_col_email, subj, body);
				}

			} catch (Exception e) {
				log(e);
			}

			_URL returnURL = session.getURLOfLastPage();
			setRedirectURL(returnURL);
			/*
			 * msg = "После исполнения документ № " + doc.getValueString("vn") +
			 * " от " + _Helper.getDateAsString(doc.getValueDate("projectdate"))
			 * + "\" будет отправлен "; msg += (doc.getDocumentForm() ==
			 * "workdocprj" ? "получателю." : "на ревизию исполнения.") +
			 * "\nДля ознакомления с документом перейдите по <a href=\"" +
			 * _doc.getFullURL() + "\">ссылке...</a>";
			 * msngAgent.sendMessageAfter([author?.getInstMessengerAddr()],
			 * msg); subj = " Исполнено замечание № " + doc.getValueString("vn")
			 * + " от " +
			 * _Helper.getDateAsString(doc.getValueDate("projectdate")); body =
			 * "После исполнения документ №" + doc.getValueString("vn") + " от "
			 * + _Helper.getDateAsString(doc.getValueDate("projectdate")) +
			 * " будет отправлен "; body += (doc.getDocumentForm() ==
			 * "workdocprj" ? " получателю. " : " на ревизию исполнения.");
			 * mailAgent.sendMailAfter([author?.getEmail()], new _Memo(subj,
			 * 'Документ исполнен', body, _doc, false));
			 */
		} catch (Exception e) {
			error(e);
		}
	}
}