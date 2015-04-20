package form.sheet
import kz.nextbase.script.*
import kz.nextbase.script.actions._Action
import kz.nextbase.script.actions._ActionBar
import kz.nextbase.script.actions._ActionType
import kz.nextbase.script.constants._CoordStatusType
import kz.nextbase.script.coordination._BlockCollection
import kz.nextbase.script.events._FormQueryOpen

class QueryOpen extends _FormQueryOpen {

    @Override
    public void doQueryOpen(_Session session, _WebFormData webFormData, String lang) {
        def user = session.getCurrentAppUser()

        def nav = session.getPage("outline", webFormData)
        publishElement(nav)


        def actionBar = new _ActionBar(session)
        actionBar.addAction(new _Action(getLocalizedWord("Отправить",lang),getLocalizedWord("Отправить",lang),"send"))
        actionBar.addAction(new _Action(getLocalizedWord("Закрыть",lang),getLocalizedWord("Закрыть без сохранения",lang),_ActionType.CLOSE))
        publishElement(actionBar)
        def dep = user.getDepartmentID()
        if (dep) {
            publishDepartment("department", dep)
        }
        publishValue("title",getLocalizedWord("Новый табель", lang))
        publishEmployer("author", session.getCurrentAppUser().getUserID())
        publishValue("docversion", "1")
        publishValue("projectauthor", user.getFullName())
        publishValue("projectdate", session.getCurrentDateAsString())
        //def blockCollection = new _BlockCollection(session)
        def blockCollection = session.createBlockCollection()
        publishValue("coordination", blockCollection)

    }


    @Override
    public void doQueryOpen(_Session session, _Document doc, _WebFormData webFormData, String lang) {
        def user = session.getCurrentAppUser()
        def nav = session.getPage("outline", webFormData)
        publishElement(nav)

        def actionBar = new _ActionBar(session)
        def	blockCollection  = (_BlockCollection)doc.getValueObject("coordination")
        def status = blockCollection.getStatus()
        def dep = user.getDepartmentID()
        if (dep) {
            publishDepartment("department", dep)
        }
        if(user.getUserID() == doc.getAuthorID()){
            if (status == _CoordStatusType.DRAFT || status == _CoordStatusType.NEWVERSION){
                actionBar.addAction(new _Action(getLocalizedWord("Отправить",lang),getLocalizedWord("Отправить",lang),"send"))
            }
        }

        if(status == _CoordStatusType.COORDINATING){
            def cblock = blockCollection.getCurrentBlock();
            if (cblock){
                def coordList = cblock.getCurrentCoordinators();
                for (coord in coordList){
                    if (coord.getUserID() == user.getUserID()){
                        actionBar.addAction(new _Action(getLocalizedWord("Принять",lang),getLocalizedWord("Принять",lang),"coord_yes"))
                        actionBar.addAction(new _Action(getLocalizedWord("Отклонить",lang),getLocalizedWord("Отклонить",lang),"coord_no"))
                    }
                }
            }
        }

        if(user.hasRole("supervisor")){
            actionBar.addAction(new _Action(_ActionType.GET_DOCUMENT_ACCESSLIST))
        }
        if(doc.getAuthorID() == user.getUserID()){
            actionBar.addAction(new _Action(getLocalizedWord("Ознакомить",lang),getLocalizedWord("Ознакомить",lang),"acquaint"))
        }
        actionBar.addAction(new _Action(getLocalizedWord("Закрыть",lang),getLocalizedWord("Закрыть без сохранения",lang),_ActionType.CLOSE))
        publishElement(actionBar)


        publishValue("title",getLocalizedWord("Табель", lang) + " № " + doc.getValueString("vn") + " " + getLocalizedWord("от", lang) + " " + doc.getValueString("projectdate"))
        publishEmployer("author",doc.getAuthorID())
        publishValue("vn",doc.getValueString("vn"))
        //publishValue("recipient",doc.getValueObject("recipient"))
        publishValue("projectdate",doc.getValueString("projectdate"))
        publishValue("docversion",doc.getValueNumber("docversion"))
        publishValue("briefcontent",doc.getValueString("briefcontent"))
        publishValue("contentsource",doc.getValueString("contentsource"))
        publishValue("coordination", blockCollection)

        try{
            publishAttachment("rtfcontent","rtfcontent")
        }catch(_Exception e){

        }
    }

}