package page.grantaccess
import kz.nextbase.script._Session
import kz.nextbase.script._WebFormData
import kz.nextbase.script.constants._DocumentType
import kz.nextbase.script.events._DoScript
import kz.nextbase.script.task._GrantedBlockCollection
import kz.nextbase.script.task._GrantedBlock

import java.text.SimpleDateFormat

class DoScript extends _DoScript {
    @Override
    void doProcess(_Session session, _WebFormData formData, String lang) {
		def cdb = ses.getCurrentDatabase();
		def doc = cdb.getDocumentByID(Integer.valueOf(formData.getValue("key")))
		def recipients = formData.get("grantusers");
		recipients.each {
           doc.addReader(it)
        }
        def grantedBlock = new _GrantedBlock(session)
        grantedBlock.addGrantUsers(recipients)
        grantedBlock.setGrantor(session.getCurrentAppUser())
        def grant_collection = new _GrantedBlockCollection(session);
        grant_collection.addBlock(grantedBlock);
        doc.addField("grantblocks", grant_collection)
		doc.save("[supervisor]")
        try {
            if (doc.parentDocID != 0 && doc.parentDocType != _DocumentType.UNKNOWN) {
                def mdoc = doc.getGrandParentDocument()
                def recipientsRus = [];
                def recipientsMail = [];
                if (mdoc) {
                    recipients.each {
                        mdoc.addReader(it)
                        def rec_emp = ses.getStructure().getEmployer(it)
                        recipientsRus.add(rec_emp.getShortName())
                        recipientsMail.add(rec_emp.getEmail())
                    }
                    mdoc.save("[supervisor]")
                }
                def allReaders = mdoc.getReaders()
                mdoc.getDescendants().each {
                    it.addReaders(allReaders)
                    it.save("[supervisor]")
                }

                if (recipientsMail) {
                    String msubject = '[���] ��� ��� ������ �� ������������ � ���������� ' + doc.getValueString("taskvn") + ' �� ' + new SimpleDateFormat("dd.mm.yyyy").format(doc.getValueDate("taskdate")) + ' (' + doc?.getValueString("briefcontent") + ')';

                    String body = '<b><font color="#000080" size="4" face="Default Serif">������ �� ������������</font></b><hr>';
                    body += '<table cellspacing="0" cellpadding="4" border="0" style="padding:10px; font-size:12px; font-family:Arial;">';
                    body += '<tr>';
                    body += '<td style="border-bottom:1px solid #CCC;" valign="top" colspan="2">';
                    body += '��� ������������ ������ �� ������������ � ���������� ' + doc.getValueString("taskvn") + ' �� ' + new SimpleDateFormat("dd.mm.yyyy").format(doc.getValueDate("taskdate")) + '<br>';
                    body += '</td></tr><tr>';
                    body += '<td colspan="2"></td>';
                    body += '</tr><tr>';
                    body += '<td valign="top" width="240px">������� ����������:</td><td valign="top" width="600px">' + doc.getValueString("briefcontent") + '</td>';
                    body += '</tr><tr>';
                    body += '<td valign="top">���������������� ����� �����������:</td><td valign="top">' + formData.get("comment")[0] + '</td>';
                    body += '</tr></table>';
                    body += '<p><font size="2" face="Arial">��� ������ � ���������� ��������� �� <a href="' + doc.getFullURL() + '">������...</a></p></font>';

                    def mailAgent = ses.getMailAgent();
                    boolean wasSent = mailAgent.sendMail(recipientsMail, msubject, body);
                    if (wasSent) {
                        ses.getLogger().normalLogEntry("Message was successfully sent");
                    } else {
                        ses.getLogger().normalLogEntry("Message was not sent");
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


