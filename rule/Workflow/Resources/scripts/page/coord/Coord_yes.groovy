package page.coord

import kz.nextbase.script._CrossLink
import kz.nextbase.script._Document
import kz.nextbase.script._Helper
import kz.nextbase.script._Session
import kz.nextbase.script._WebFormData
import kz.nextbase.script.constants._BlockStatusType
import kz.nextbase.script.constants._BlockType
import kz.nextbase.script.constants._CoordStatusType
import kz.nextbase.script.constants._DecisionType
import kz.nextbase.script.coordination._Block
import kz.nextbase.script.coordination._BlockCollection
import kz.nextbase.script.events._DoScript

class Coord_yes extends _DoScript {

    @Override
    public void doProcess(_Session session, _WebFormData formData, String lang) {

        def cdb = session.getCurrentDatabase();
        def mailAgent = session.getMailAgent();
        def msngAgent = session.getInstMessengerAgent();
        def recipientsMail = [];
        def recipientsID = [];
        String msubject = "";
        String body = "";
        String msg = "";
        def doc = cdb.getDocumentByID(formData.getNumberValueSilently("key", -1))
        def struct = session.getStructure()
        def author = struct?.getEmployer(doc.getAuthorID())
        def blocksCollection = (_BlockCollection) doc.getValueObject("coordination")
        def block = (_Block) blocksCollection.getCurrentBlock();
        def coordlist = block.getCurrentCoordinators();
        boolean finalblock = false;
        def doc_blc = (_BlockCollection) doc.getValueObject("coordination")
        def attachid = (formData.containsField("fileid") ? formData.getListOfValues("fileid") : null)
        attachid = attachid?.collect {it as int}
        def returnURL = session.getURLOfLastPage()


        def rejectProject = { _Document document ->
            doc_blc.setCoordStatus(_CoordStatusType.REJECTED);
            document.replaceViewText(blocksCollection.getStatus().name(), 3)
            document.setLastUpdate(new Date());
            document.setValueString("oldversion", "1");
            document.setValueNumber("isrejected", 1)
            document.save("[supervisor]");
            /*Формирование новой версии проекта*/
            document.setNewDoc();
            document.setValueNumber("isrejected", 0);
            doc_blc.setCoordStatus(_CoordStatusType.NEWVERSION);
            int docversion = document.getValueNumber("docversion") + 1;
            document.setValueNumber("docversion", docversion);
            String vn = document.getValueString("vn");
            document.setValueString("vn", vn.contains(",") ? vn.replaceFirst(",.", ",") + docversion.toString() : vn + "," + docversion.toString());
            document.setValueDate("projectdate", new Date());
            document.replaceViewText(' № ' + document.getValueString("vn") + ' ' + _Helper.getDateAsStringShort(document.getValueDate("projectdate")) + ' ' + session.getStructure().getEmployer(document.getValueString('author')).shortName + ' ' + document.getValueString('briefcontent'), 0)
            document.clearEditors();
            document.clearReaders();
            document.addEditor(document.getValueString("author"));
            def blocks = doc_blc.getBlocks();
            blocks.each {
                it.setBlockStatus(_BlockStatusType.AWAITING);
                it.getCoordinators()*.resetCoordinator();
            }
            document.replaceViewText(doc_blc.getStatus().name(), 3);
            document.save("[supervisor]");
        }
        for (coord in coordlist) {
            if (coord.getUserID() && coord.getUserID() == session.getCurrentAppUser().userID) {
                Boolean isComment = formData.containsField("comment")
                String comment = (isComment ? formData.getValue("comment") : "");
                coord.setDecision(_DecisionType.AGREE, comment != null ? comment : "");
                if (attachid) coord.setAttachID(attachid)
                if (block && block.getBlockType() == _BlockType.SERIAL_COORDINATION) {
                    def nextCoord = block.getNextCoordinator(coord);
                    if (nextCoord && nextCoord.getUserID()) {
                        nextCoord.setCurrent(true);
                        doc.addReader(nextCoord.getUserID())
                        def emp = struct.getEmployer(nextCoord.userID)
                        recipientsMail.add(emp.getEmail());
                        recipientsID.add(emp.getInstMessengerAddr())
                    } else {
                        finalblock = true;
                    }
                } else {
                    if (block.getBlockType() == _BlockType.PARALLEL_COORDINATION && coordlist.size <= 1) {
                        finalblock = true;
                    }
                }
                if (finalblock) {
                    block.setBlockStatus(_BlockStatusType.COORDINATED);
                    def nextBlock = doc_blc.getNextBlock(block);
                    if (nextBlock) {
                        if (nextBlock.getBlockType() == _BlockType.PARALLEL_COORDINATION) {
                            nextBlock.setBlockStatus(_BlockStatusType.COORDINATING);
                            def nextcoords = nextBlock.getCoordinators();
                            nextcoords.each { nextcoord ->
                                nextcoord.setCurrent(true);
                                doc.addReader(nextcoord.getUserID())
                                def emp = struct.getEmployer(nextcoord.userID)
                                recipientsMail.add(emp.getEmail())
                                recipientsID.add(emp.getInstMessengerAddr())
                            }
                        } else {
                            if (nextBlock.getBlockType() == _BlockType.SERIAL_COORDINATION) {
                                nextBlock.setBlockStatus(_BlockStatusType.COORDINATING)
                                def nextcoord = nextBlock.getFirstCoordinator()
                                if (nextcoord) {
                                    nextcoord.setCurrent(true);
                                    doc.addReader(nextcoord.getUserID())
                                    def emp = struct.getEmployer(nextcoord.userID)
                                    recipientsMail.add(emp.getEmail())
                                    recipientsID.add(emp.getInstMessengerAddr())
                                }
                            } else {
                                if (nextBlock.getBlockType() == _BlockType.TO_SIGN) {
                                    def decisions = [];
                                    def cusers = doc_blc.getCoordBlocks()*.getCoordinators()
                                    cusers.each {
                                        for (c in it) {
                                            decisions.add(c.getDecisionType())
                                        }
                                    }
                                    if (!decisions.any { it == _DecisionType.DISAGREE }) {
                                        doc_blc.setCoordStatus(_CoordStatusType.COORDINATED)
                                        def signerCoord = doc_blc.getSignBlock()?.getFirstCoordinator();
                                        def signer = session.getStructure().getEmployer(signerCoord.userID)
                                        if (author.getUserID() != signer.getUserID()) {
                                            nextBlock.setBlockStatus(_BlockStatusType.COORDINATING)
                                            msg = "После рассмотрения документа: \"" + doc.getValueString("briefcontent") + "\" он отправлен на подпись к " + signer?.getShortName();
                                            msg += "\nДля работы с документом перейдите по ссылке " + doc.getFullURL();
                                            msngAgent.sendMessageAfter([author?.getInstMessengerAddr()], /*doc.getGrandParentDocument().getValueString("project_name") + ": " +*/ msg);

                                            msg = "Вам документ: \"" + doc.getValueString("briefcontent") + "\" на подпись. \nДля работы с документом перейдите по ссылке " + doc.getFullURL();
                                            msngAgent.sendMessageAfter([signer?.getInstMessengerAddr()], /*doc.getGrandParentDocument().getValueString("project_name") + ": " + */ msg);

                                            msubject = '[СЭД] [Проекты] Отправлен на подпись документ \"' + doc.getValueString("briefcontent") + '\"';
                                            body = '<b><font color="#000080" size="4" face="Default Serif">Документ на подпись</font></b><hr>';
                                            body += '<table cellspacing="0" cellpadding="4" border="0" style="padding:10px; font-size:12px; font-family:Arial;">';
                                            body += '<tr>';
                                            body += '<td style="border-bottom:1px solid #CCC;" valign="top" colspan="2">';
                                            body += 'После рассмотрения документа: \"' + doc.getValueString("briefcontent") + '\" он отправлен на подпись к ';
                                            body += signer?.getShortName() + ' <br>';
                                            body += '</td></tr><tr>';
                                            body += '<td colspan="2"></td>';
                                            body += '</tr></table>';
                                            body += '<p><font size="2" face="Arial">Для работы с документом перейдите по <a href="' + doc.getFullURL() + '">ссылке...</a></p></font>';

                                            mailAgent.sendMailAfter([author?.getEmail()], msubject, body);

                                            msubject = '[СЭД] [Проекты] -> Прошу подписать документ \"' + doc.getValueString("briefcontent") + '\"';
                                            body = '<b><font color="#000080" size="4" face="Default Serif">Документ на подпись</font></b><hr>';
                                            body += '<table cellspacing="0" cellpadding="4" border="0" style="padding:10px; font-size:12px; font-family:Arial;">';
                                            body += '<tr>';
                                            body += '<td style="border-bottom:1px solid #CCC;" valign="top" colspan="2">';
                                            body += 'Вам документ: \"' + doc.getValueString("briefcontent") + '\" на подпись. <br>';
                                            body += '</td></tr><tr>';
                                            body += '<td colspan="2"></td>';
                                            body += '</tr></table>';
                                            body += '<p><font size="2" face="Arial">Для работы с документом перейдите по <a href="' + doc.getFullURL() + '">ссылке...</a></p></font>';

                                            mailAgent.sendMailAfter([signer?.getEmail()], msubject, body);
                                            body = ""
                                            msg = ""
                                            msubject = ""

                                            signerCoord.setCurrent(true);
                                            if (signerCoord.getUserID()) {
                                                doc.addReader(signerCoord.getUserID());
                                            }

                                            doc_blc.setCoordStatus(_CoordStatusType.SIGNING);
                                        } else {
                                            nextBlock.setBlockStatus(_BlockStatusType.COORDINATED);
                                            signerCoord.setDecision(_DecisionType.AGREE, "")
                                            doc_blc.setCoordStatus(_CoordStatusType.SIGNED)
                                            doc.replaceViewText(doc_blc.getStatus().name(), 3)

                                            def _doc = new _Document(cdb);
                                            if (doc.getDocumentForm() == "officememoprjdept") {
                                                _doc.setForm("workdocdept")
                                                _doc.addStringField("vn", doc.getValueString("vn"))
                                                def recipients = doc.getValueList("recipient")
                                                _doc.replaceListField("recipient", recipients as ArrayList);
                                                //_doc.addStringField("recipient", doc.getValueString("recipient"))
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
                                                int num = cdb.getRegNumber("workdocdept");
                                                _doc.addStringField("vn", num.toString());
                                                _doc.addNumberField("vnnumber", num);
                                                _doc.setViewText("Служебная записка между департаментами № " + _doc.getValueString("vn") + " " + _Helper.getDateAsStringShort(_doc.getValueDate("dvn")) + "  " + session.getStructure()?.getEmployer(doc.getAuthorID())?.getShortName() + " " + doc.getValueString("briefcontent"));
                                                _doc.addDateField("ctrldate", session.getDatePlusDays(30))
                                                _doc.setViewNumber(num)
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
                                                //_doc.addEditor("[registrator_outgoing]");
                                                _doc.addEditor(doc.authorID);
                                                _doc.save("[supervisor]")
                                                _doc.replaceViewText(doc.getValueList("recipient").toString(), 7)
                                                _doc.save("[supervisor]")
                                                doc.addField("link", new _CrossLink(session, _doc))
                                                doc.setValueNumber("regdocid", _doc.getDocID())
                                                def cBlock = doc_blc.getCurrentBlock()
                                                if (cBlock) {
                                                    doc.replaceViewText(cBlock.getCurrentCoordinatorsAsText(), 5)
                                                }
                                                doc.save("[supervisor]")
                                                returnURL = _doc.getFullURL();
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
                                                _doc.save("[supervisor]")

                                                doc.addField("link", new _CrossLink(session, _doc))
                                                def cBlock = doc_blc.getCurrentBlock()
                                                if (cBlock) {
                                                    doc.replaceViewText(cBlock.getCurrentCoordinatorsAsText(), 5)
                                                }
                                                doc.save("[supervisor]")
                                            }
                                        }
                                    } else {
                                        rejectProject(doc);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        def cBlock = blocksCollection.getCurrentBlock()
        if (cBlock) {
            doc.replaceViewText(cBlock.getCurrentCoordinatorsAsText(), 5)
        }
        doc.replaceViewText(doc_blc.getStatus().name(), 3);

        println(doc_blc.getStatus().name())

        if (doc_blc.getStatus() == _CoordStatusType.COORDINATING) {
            if (recipientsID) {
                msg = "Вам документ на согласование: \"" + doc.getValueString("briefcontent") + "\" \nДля работы с документом перейдите по ссылке " + doc.getFullURL();
                msngAgent.sendMessage(recipientsID, msg);
            }


            msubject = '[СЭД] [Проекты] -> Прошу согласовать документ \"' + doc.getValueString("briefcontent") + '\"';
            body = '<b><font color="#000080" size="4" face="Default Serif">Документ на согласование</font></b><hr>';
            body += '<table cellspacing="0" cellpadding="4" border="0" style="padding:10px; font-size:12px; font-family:Arial;">';
            body += '<tr>';
            body += '<td style="border-bottom:1px solid #CCC;" valign="top" colspan="2">';
            body += 'Вам документ на согласование \"' + doc.getValueString("briefcontent") + '\"' + '<br>';
            body += '</td></tr><tr>';
            body += '<td colspan="2"></td>';
            body += '</tr></table>';
            body += '<p><font size="2" face="Arial">Для работы с документом перейдите по <a href="' + doc.getFullURL() + '">ссылке...</a></p></font>';

            if (recipientsMail) {
                mailAgent.sendMailAfter(recipientsMail, msubject, body);
            }
        }
        if (doc_blc.getStatus() == _CoordStatusType.COORDINATED || doc_blc.getStatus() == _CoordStatusType.SIGNING) {

            msg = "По документу: \"" + doc.getValueString("briefcontent") + "\" завершено согласование. Для работы с документом перейдите по ссылке " + doc.getFullURL();
            msngAgent.sendMessageAfter([author?.getInstMessengerAddr()], msg);


            msubject = '[СЭД] [Проекты] Согласование документа \"' + doc.getValueString("briefcontent") + '\" завершено.';
            body = '<b><font color="#000080" size="4" face="Default Serif">Завершено согласование</font></b><hr>';
            body += '<table cellspacing="0" cellpadding="4" border="0" style="padding:10px; font-size:12px; font-family:Arial;">';
            body += '<tr>';
            body += '<td style="border-bottom:1px solid #CCC;" valign="top" colspan="2">';
            body += 'По документу \"' + doc.getValueString("briefcontent") + '\" завершено согласование. <br>';
            body += '</td></tr><tr>';
            body += '<td colspan="2"></td>';
            body += '</tr></table>';
            body += '<p><font size="2" face="Arial">Для работы с документом перейдите по <a href="' + doc.getFullURL() + '">ссылке...</a></p></font>';

            mailAgent.sendMailAfter([author?.getEmail()], msubject, body);

        }
        if (doc_blc.getStatus() == "coordinated") {
            msg = "Документ \"" + doc.getValueString("briefcontent") + "\" готов к отправке на подпись. Пожалуйста, проверьте правильность составления документа и все ли участники согласования ";
            msg += "одобрили документ. \nДля работы с документом перейдите по ссылке " + doc.getFullURL();
            msngAgent.sendMessage([author?.getInstMessengerAddr()], doc.getGrandParentDocument().getValueString("project_name") + ": " + msg);

            msubject = '[СЭД] [Проекты] Проект для отправки на подпись \"' + doc.getValueString("briefcontent") + '\"';
            body = '<b><font color="#000080" size="4" face="Default Serif">Согласование завершено</font></b><hr>';
            body += '<table cellspacing="0" cellpadding="4" border="0" style="padding:10px; font-size:12px; font-family:Arial;">';
            body += '<tr>';
            body += '<td style="border-bottom:1px solid #CCC;" valign="top" colspan="2">';
            body += 'Проект документа завершил согласование и готов к отправке на подпись. Проверьте правильность составления документа ';
            body += 'и все ли участники согласования одобрили документ.<br>';
            body += '</td></tr><tr>';
            body += '<td colspan="2"></td>';
            body += '</tr></table>';
            body += '<p><font size="2" face="Arial">Для работы с документом перейдите по <a href="' + doc.getFullURL() + '">ссылке...</a></p></font>';

            mailAgent.sendMailAfter([author?.getEmail()], msubject, body);

        }


        doc.save("[supervisor]");
        setRedirectURL(returnURL)
        println("done")
    }
}