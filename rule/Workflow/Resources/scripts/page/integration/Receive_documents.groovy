package page.integration

import groovy.io.FileType
import kz.nextbase.script._Document
import kz.nextbase.script._Helper
import kz.nextbase.script._Session
import kz.nextbase.script._WebFormData
import kz.nextbase.script.events._DoScript
import org.apache.commons.lang3.StringUtils

import javax.mail.*

class Receive_documents extends _DoScript {
    @Override
    void doProcess(_Session ses, _WebFormData formData, String lang) {
        def db = ses.getCurrentDatabase()

        def trust_email = "4msworkflow@inbox.ru"
        def password = "kf,jhfnjhbz,eleotuj"
        def props = new Properties()

        def server = "imap.mail.ru"
        def port = 993
        props.setProperty("mail.host", server)
        props.setProperty("mail.debug", String.valueOf(true))
        props.setProperty("mail.imap.port", port.toString())
        props.setProperty("mail.imap.ssl.enable", String.valueOf(true))

        def session = Session.getInstance(props)// .getDefaultInstance(props)
        def store = session.getStore("imap")

        store.connect(trust_email, password)

        def folder = store.getFolder("INBOX")
        folder.open(Folder.READ_ONLY)
        folder.messages.each { msg ->
            def save = false
            try {
                def content = msg.content
                if (content instanceof Multipart) {
                    def doc = new _Document(ses.getCurrentDatabase());
                    def attach_map = [:]
                    for (i in 0..(content.count - 1)) {
                        BodyPart bodyPart = content.getBodyPart(i)
                        if (Part.ATTACHMENT.equalsIgnoreCase(bodyPart.disposition)) {
                            InputStream is = bodyPart.getInputStream();
                            if ("document.xml".equalsIgnoreCase(bodyPart.getFileName())) {

                                def someXmlRecords = new XmlParser().parse(is);
                                println someXmlRecords.get("Subject")

                                //проверить на ошибку при отсутствии тега
                                println someXmlRecords.Attachments.AttachmentXmlInfo.size()
                                someXmlRecords.Attachments.AttachmentXmlInfo.each {
                                    attach_map.put(it.MimeFileName.text(), it.FileName.text())
                                }

                                attach_map.each {
                                    println(it.key + " " + it.value)
                                }

                                String current_org = someXmlRecords.To.text()
                                if (current_org) {
                                    def org_col = db.getCollectionOfGlossaries("form = 'corr' and iscurorg = '1' and corrid = '${current_org}'", 1, 1)
                                    if (org_col?.count > 0) {
                                        doc.setForm("IN");
                                        doc.setValueString("in", someXmlRecords.DocumentNumber.text())
                                        doc.setValueString("vn", "")
                                        doc.setValueNumber("vnnumber", 0)
                                        doc.setValueString("briefcontent", someXmlRecords.Subject.text())
                                        doc.setValueString("np", someXmlRecords.SheetCount.text())
                                        doc.setValueString("unid", someXmlRecords.In.text())
                                        doc.setViewText(someXmlRecords.Subject.text())
                                        doc.addViewText(someXmlRecords.Subject.text())

                                        String corrID = someXmlRecords.IdOrg.text()
                                        String corr = ""
                                        if (corrID) {
                                            def corr_col = db.getCollectionOfGlossaries("form = 'corr' and corrid = '${corrID}'", 0, 0)
                                            corr_col?.entries.each {
                                                doc.addGlossaryField("corr", it.document.getDocID())
                                            }
                                        }

                                        //String execFIO = someXmlRecords.ExecutorFIO.text();
                                        String deltypeXML = someXmlRecords.FormSentDictionaryId?.text();
                                        def deltypeglos = ses.getCurrentDatabase().getCollectionOfGlossaries("form = 'deliverytype' and viewtext ~ '${deltypeXML}'", 1, 1)
                                        if (deltypeglos && deltypeglos.count > 0) {
                                            deltypeglos.entries.each {
                                                doc.addGlossaryField("deliverytype", it.document.getDocID())
                                            }
                                        }

                                        String doctypeXML = someXmlRecords.DocumentTypeId?.text()
                                        def doctype = ses.getCurrentDatabase().getCollectionOfGlossaries("form = 'vid' and viewtext ~ '${doctypeXML}'", 1, 1)
                                        if (doctype && doctype.count > 0) {
                                            doctype.entries.each {
                                                doc.addGlossaryField("vid", it.document.getDocID())
                                            }
                                        }

                                        def project = ses.getCurrentDatabase().getGlossaryDocument("form = 'project'", "viewtext ~ 'Вне проекта'")
                                        if (project) {
                                            doc.addGlossaryField("project", project.getDocID())
                                        }

                                        def category = ses.getCurrentDatabase().getGlossaryDocument("form = 'category'", "viewtext ~ 'Общие вопросы'")
                                        if (category) {
                                            doc.addGlossaryField("category", category.getDocID())
                                        }

                                        String din = someXmlRecords.Date.text()
                                        if (din) {
                                            doc.setValueDate("din", _Helper.convertStringToDate(din.replace("T", " ")))
                                        }
                                        doc.setViewDate(new Date())
                                        String execFIO = someXmlRecords.ExecutorFIO.text();
                                        String[] abb = execFIO.split(" ")
                                        abb = abb.collect { it = StringUtils.removeEnd(it, ".") }

                                        def exec = ses.getStructure().getUserByCondition("fullname like '%" + abb.max {
                                            it.length()
                                        } + "%'")
                                        if (exec) {
                                            doc.addValueToList("recipients", exec.getUserID());
                                        }
                                        save = true
                                    }
                                }


                            } else {
                                def f
                                attach_map.each {
                                    println(it.key + "::" + it.value)
                                }
                                println bodyPart.getFileName()
                                println attach_map.containsKey(bodyPart.getFileName())
                                println attach_map.containsValue(bodyPart.getFileName())

                                if (attach_map.containsKey(bodyPart.getFileName())) {
                                    f = new File(attach_map.get(bodyPart.getFileName()));
                                } else {
                                    f = new File(bodyPart.getFileName());
                                }
                                FileOutputStream fos = new FileOutputStream(f);
                                byte[] buf = new byte[4096];
                                int bytesRead;
                                while ((bytesRead = is.read(buf)) != -1) {
                                    fos.write(buf, 0, bytesRead);
                                }
                                fos.close();

                                //только файл может быть и не zip
                                if (f.getAbsolutePath().endsWith("zip")) {
                                    def ant = new AntBuilder()
                                    ant.unzip(src: f, dest: "tmp", overwrite: "true")
                                    //вкладываем zip а не итоговые файлы
                                    File tmpDir = new File("tmp")
                                    tmpDir.mkdir()
                                    tmpDir.eachFile(FileType.FILES) {
                                        doc.addAttachment("rtfcontent", it)
                                    }
                                } else {
                                    doc.addAttachment("rtfcontent", f)
                                }
                            }
                        }
                    }



                    if (save) {
                        doc.setAuthor("wish")
                        doc.addEditor("wish")
                        doc.addEditor("[wish]")
                        doc.addEditor("[supervisor]")
                        doc.addEditor("supervisor")
                        doc.addReader("[wish]")
                        doc.addReader("wish")
                        doc.addReader("observer")
                        doc.addReader("[observer]")
                        doc.save("[supervisor]")
                    }
                }

            } catch (Exception ignore) {
                ignore.printStackTrace()
                println "error processing message, no big deal, moving on"
            }
            println "-----------done------------"
        }

    }
}
